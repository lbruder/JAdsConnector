package org.lb.plc.ams;

import java.io.*;
import java.util.Map;
import java.util.concurrent.*;

import org.lb.plc.ads.*;

public class SyncConnection {
	private final Connection conn;
	private final BlockingQueue<Packet> dataQueue;
	private final BlockingQueue<Packet> notificationQueue;
	private final Packet nullPacket;

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
