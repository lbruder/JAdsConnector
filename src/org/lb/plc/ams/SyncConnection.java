// Copyright (c) 2011-2012, Leif Bruder <leifbruder@googlemail.com>
// 
// Permission to use, copy, modify, and/or distribute this software for any
// purpose with or without fee is hereby granted, provided that the above
// copyright notice and this permission notice appear in all copies.
// 
// THE SOFTWARE IS PROVIDED "AS IS" AND THE AUTHOR DISCLAIMS ALL WARRANTIES
// WITH REGARD TO THIS SOFTWARE INCLUDING ALL IMPLIED WARRANTIES OF
// MERCHANTABILITY AND FITNESS. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR
// ANY SPECIAL, DIRECT, INDIRECT, OR CONSEQUENTIAL DAMAGES OR ANY DAMAGES
// WHATSOEVER RESULTING FROM LOSS OF USE, DATA OR PROFITS, WHETHER IN AN
// ACTION OF CONTRACT, NEGLIGENCE OR OTHER TORTIOUS ACTION, ARISING OUT OF
// OR IN CONNECTION WITH THE USE OR PERFORMANCE OF THIS SOFTWARE.

package org.lb.plc.ams;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import org.lb.plc.ads.DeviceNotification;
import org.lb.plc.ads.Payload;

public class SyncConnection {
	final Connection conn;
	final BlockingQueue<Packet> dataQueue;
	final BlockingQueue<Packet> notificationQueue;
	final Packet nullPacket;

	public SyncConnection(final Connection conn,
			final NotificationObserver observer) {
		this.conn = conn;
		this.dataQueue = new LinkedBlockingQueue<Packet>();
		this.notificationQueue = new LinkedBlockingQueue<Packet>();

		this.nullPacket = Packet.newRequest(PeerPair.valueOf(new byte[] { 0, 0,
				0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0 }), 0, 0,
				new org.lb.plc.ads.Payload() {
					@Override
					public int getCommandId() {
						return 0;
					}

					@Override
					public byte[] toBinary() {
						return null;
					}

					@Override
					public String toString() {
						return "NullPacket";
					}
				});

		new Thread(new ReaderThread()).start();
		new Thread(new NotificationThread(observer)).start();
	}

	public void close() throws IOException {
		conn.close(); // Causes IOException in readerThread, ending the threads
	}

	public boolean isClosed() {
		return conn.isClosed();
	}

	public synchronized Packet converse(final Packet dataToWrite,
			long timeoutInMs) throws IOException {
		conn.write(dataToWrite);
		try {
			final Packet response = dataQueue.poll(timeoutInMs,
					TimeUnit.MILLISECONDS);
			if (response == null)
				throw new IOException("Timeout");
			if (response == nullPacket)
				throw new IOException("Connection to peer broken");
			if (!response.isResponseTo(dataToWrite))
				throw new IOException("Synchronization error");
			return response;
		} catch (InterruptedException ex) {
			throw new IOException("Interrupted while waiting for response");
		}
	}

	private class ReaderThread implements Runnable {
		public ReaderThread() {
		}

		@Override
		public void run() {
			for (;;) {
				try {
					final Packet packet = conn.read();
					if (packet == null)
						continue;
					if (packet.isNotification())
						notificationQueue.add(packet);
					else
						dataQueue.add(packet);
				} catch (Throwable e) {
					e.printStackTrace(System.err);
					// Escalate error to converse() and stop notification thread
					dataQueue.add(nullPacket);
					notificationQueue.add(nullPacket);
					break;
				}
			}
		}
	}

	private class NotificationThread implements Runnable {
		private final NotificationObserver observer;

		public NotificationThread(final NotificationObserver observer) {
			this.observer = observer;
		}

		@Override
		public void run() {
			for (;;) {
				try {
					final Packet packet = notificationQueue.take();
					if (packet == nullPacket)
						break;
					notify(packet);
				} catch (Throwable e) {
					break;
				}
			}
			notify(null);
		}

		private void notify(final Packet packet) {
			if (observer == null)
				return;

			final Payload payload = packet.getAdsPayload();

			if (!(payload instanceof DeviceNotification))
				return;

			final DeviceNotification notification = (DeviceNotification) payload;
			final Map<Long, byte[]> newValues = notification.getNewValues();
			observer.notificationReceived(newValues);
		}
	}
}
