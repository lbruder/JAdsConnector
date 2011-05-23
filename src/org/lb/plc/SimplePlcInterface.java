package org.lb.plc;

import java.io.IOException;
import java.net.InetAddress;
import java.util.Map;
import org.lb.plc.ams.*;
import org.lb.plc.tpy.*;

public class SimplePlcInterface {
	private final SimpleAdsInterface conn;
	private final VariableLocator variableLocator;
	private final VariableObserver observer;

	private class Observer implements NotificationObserver {
		@Override
		public void notificationReceived(Map<Long, byte[]> newValues) {
			if (observer == null)
				return;
			if (newValues == null) // Connection closed
				return;
			// TODO: Which variable(s) was/were changed?
		}
	}

	public SimplePlcInterface(final InetAddress dest, final String sourceNetId,
			final int sourcePort, final String destNetId, final int destPort,
			final long timeoutInMs, final VariableLocator variableLocator,
			final VariableObserver observer) throws IOException {
		this.variableLocator = variableLocator;
		this.observer = observer;
		this.conn = new SimpleAdsInterface(dest, sourceNetId, sourcePort,
				destNetId, destPort, timeoutInMs, new Observer());
	}

	public void close() throws IOException {
		conn.close();
	}

	public boolean isClosed() {
		return conn.isClosed();
	}

	public String getDeviceVersion() throws IOException, AmsException {
		return conn.getDeviceVersion();
	}

	public long getVariableAsInteger(final String name) throws IOException,
			AmsException, TpyException {
		Variable var = variableLocator.getVariableByName(name);
		byte[] data = readByName(name);
		if (var.isSigned())
			return Toolbox.bytesToSignedInteger(data);
		return Toolbox.bytesToUnsignedInteger(data);
	}

	public double getVariableAsDouble(final String name) throws IOException,
			AmsException, TpyException {
		return Toolbox.bytesToDouble(readByName(name));
	}

	public String getVariableAsString(final String name) throws IOException,
			AmsException, TpyException {
		return String.valueOf(readByName(name));
	}

	private byte[] readByName(final String name) throws IOException,
			AmsException, TpyException {
		final Variable var = variableLocator.getVariableByName(name);
		return conn.read(var.group, var.offset, var.bitSize / 8);
	}

	// public void setVariableAsInteger(final String name, final long value)
	// throws IOException, AmsException {
	// // TODO
	// }
	//
	// public void setVariableAsDouble(final String name, final double value)
	// throws IOException, AmsException {
	// // TODO
	// }
	//
	// public void setVariableAsString(final String name, final String value)
	// throws IOException, AmsException {
	// // TODO
	// }
	//
	// public void addNotification(final String name, final long checkTimeInMs)
	// throws IOException, AmsException {
	// // TODO
	// }
	//
	// public void deleteNotification(final String name) throws IOException,
	// AmsException {
	// // TODO
	// }
}
