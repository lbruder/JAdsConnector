package org.lb.plc;

import java.io.*;
import java.net.*;

import org.lb.plc.ads.*;
import org.lb.plc.ams.*;

public class SimpleAdsInterface {
	private final SyncConnection conn;
	private final PeerPair peerPair;
	private final IdGenerator idGenerator;
	private final long timeoutInMs;

	public SimpleAdsInterface(final InetAddress dest, final String sourceNetId,
			final int sourcePort, final String destNetId, final int destPort,
			final long timeoutInMs, final NotificationObserver observer)
			throws IOException {
		final Connection lowLevelConn = new Connection(dest);
		this.conn = new SyncConnection(lowLevelConn, observer);
		this.peerPair = PeerPair.valueOf(NetId.valueOf(sourceNetId),
				sourcePort, NetId.valueOf(destNetId), destPort);
		this.timeoutInMs = timeoutInMs;
		this.idGenerator = new IdGenerator();
	}

	public void close() throws IOException {
		conn.close();
	}

	public boolean isClosed() {
		return conn.isClosed();
	}

	private Payload converse(final Payload request) throws IOException,
			AmsException {
		final Packet response = conn.converse(Packet.newRequest(peerPair, 0,
				idGenerator.getNextId(), request), timeoutInMs);
		if (response.isError())
			throw new AmsException(response.getErrorCode());
		final Payload payload = response.getAdsPayload();
		if (payload.isError())
			throw new AmsException(payload.getErrorCode());
		return payload;
	}

	public String getDeviceVersion() throws IOException, AmsException {
		final Payload response = converse(new GetDeviceInfoRequest());
		if (response instanceof GetDeviceInfoResponse)
			return ((GetDeviceInfoResponse) response).toVersionString();
		return response.toString();
	}

	public byte[] read(final long group, final long offset, final long length)
			throws IOException, AmsException {
		final Payload response = converse(new ReadRequest(group, offset, length));
		return ((ReadResponse) response).getPayload();
	}

	public void write(final long group, final long offset, final byte[] data)
			throws IOException, AmsException {
		converse(new WriteRequest(group, offset, data));
	}

	public byte[] readWrite(final long group, final long offset,
			final byte[] dataToWrite, final long readLength)
			throws IOException, AmsException {
		final Payload response = converse(new ReadWriteRequest(group, offset,
				dataToWrite, readLength));
		return ((ReadWriteResponse) response).getPayload();
	}

	public long addNotification(final long group, final long offset,
			final long length, final long checkTimeInMs) throws IOException,
			AmsException {
		final Payload response = converse(new AddNotificationRequest(group,
				offset, length, checkTimeInMs));
		return ((AddNotificationResponse) response).getHandle();
	}

	public void deleteNotification(final long handle) throws IOException,
			AmsException {
		converse(new DeleteNotificationRequest(handle));
	}
}
