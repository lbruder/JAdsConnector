package org.lb.plc.ams;

import java.io.*;
import java.net.*;
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
			Thread.sleep(100); // Wait for socket to settle. TODO: Needed (cf. continue in readEx...)?
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