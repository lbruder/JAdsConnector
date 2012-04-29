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
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;

import org.lb.plc.Toolbox;

public class Connection {
	private final Socket socket;
	private final InputStream in;
	private final OutputStream out;

	public Connection(InetAddress peer) throws IOException {
		this.socket = new Socket(peer, 48898);
		this.socket.setReuseAddress(true);
		this.socket.setTcpNoDelay(true);
		this.socket.setSoTimeout(1000);
		this.in = socket.getInputStream();
		this.out = socket.getOutputStream();
		try {
			Thread.sleep(100); // Wait for socket to settle. TODO: Needed (cf.
			// continue in readEx...)?
		} catch (InterruptedException ex) {
			// Swallow for now
		}
	}

	public void close() throws IOException {
		socket.close();
	}

	public boolean isClosed() {
		return socket.isClosed();
	}

	public void write(Packet data) throws IOException {
		out.write(data.toBinary());
	}

	public Packet read() throws IOException {
		try {
			final byte[] tcpHeader = new byte[6];
			readExactlyNBytes(tcpHeader, 0, 6);
			final int packetSize = (int) Toolbox.bytesToUint32(tcpHeader, 2);
			if (packetSize > 65535)
				throw new IOException("Invalid AMS packet size");

			final byte[] packet = new byte[6 + packetSize];
			Toolbox.copyIntoByteArray(packet, 0, tcpHeader);
			readExactlyNBytes(packet, 6, packetSize);

			return Packet.valueOf(packet);
		} catch (SocketTimeoutException ex) {
			return null;
		}
	}

	private void readExactlyNBytes(byte[] dest, int offset, int length)
			throws IOException {
		if (length < 1)
			throw new IllegalArgumentException("Less than 1 byte to read");

		int bytesRead = 0;
		while (bytesRead < length) {
			final int leftToRead = length - bytesRead;
			final int thisTimeRead = in.read(dest, offset + bytesRead,
					leftToRead);
			if (thisTimeRead == -1) {
				// TODO: continue; ?
				throw new IOException("Unexpected end of data stream");
			}
			bytesRead += thisTimeRead;
		}

		if (bytesRead != length)
			throw new IOException("Read too much, this should never happen!");
	}
}
