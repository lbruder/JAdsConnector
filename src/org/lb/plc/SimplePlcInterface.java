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

package org.lb.plc;

import java.io.IOException;
import java.net.InetAddress;
import java.util.Map;

import org.lb.plc.ams.AmsException;
import org.lb.plc.ams.NotificationObserver;
import org.lb.plc.tpy.*;

public class SimplePlcInterface {
	private final SimpleAdsInterface conn;
	private final VariableLocator variableLocator;
	final VariableObserver observer;
	private final NotificationHandleCache notificationhandleCache;

	private class Observer implements NotificationObserver {
		public Observer() {
		}

		@Override
		public void notificationReceived(Map<Long, byte[]> newValues) {
			if (observer == null)
				return;
			if (newValues == null) // Connection closed
				return;
			for (Long handle : newValues.keySet()) {
				final Variable var = notificationhandleCache
						.getVariableByHandle(handle);
				if (var == null)
					continue;
				try {
					Object newValue = var.decode(newValues.get(handle));
					observer.variableChanged(var.name, newValue);
				} catch (Exception ex) {
					// Swallow for now
				}
			}
		}
	}

	public SimplePlcInterface(final InetAddress dest, final String sourceNetId,
			final int sourcePort, final String destNetId, final int destPort,
			final long timeoutInMs, final VariableLocator variableLocator,
			final VariableObserver observer) throws IOException {
		this.variableLocator = variableLocator;
		this.notificationhandleCache = new NotificationHandleCache();
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
		final byte[] data = readByName(name);
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

	public void setVariable(final String name, final long value)
			throws IOException, AmsException, TpyException {
		final Variable var = variableLocator.getVariableByName(name);
		if (!var.isIntegral())
			throw new TpyException("Invalid data type, got <integral>");
		final int byteSize = (int) var.bitSize / 8;
		final byte[] data = Toolbox.integerToBytes(value, byteSize,
				var.isSigned());
		conn.write(var.group, var.offset, data);
	}

	public void setVariable(final String name, final double value)
			throws IOException, AmsException, TpyException {
		final Variable var = variableLocator.getVariableByName(name);
		if (!var.isFloatingPoint())
			throw new TpyException("Invalid data type, got <floatingPoint>");
		final int byteSize = (int) var.bitSize / 8;
		final byte[] data = Toolbox.doubleToBytes(value, byteSize);
		conn.write(var.group, var.offset, data);
	}

	public void setVariable(final String name, final String value)
			throws IOException, AmsException, TpyException {
		final Variable var = variableLocator.getVariableByName(name);
		if (!var.isString())
			throw new TpyException("Invalid data type, got <floatingString>");
		final byte[] data = value.getBytes();
		conn.write(var.group, var.offset, data);
	}

	public void addNotification(final String name, final long checkTimeInMs)
			throws IOException, AmsException, TpyException {
		final Variable var = variableLocator.getVariableByName(name);
		final long notificationHandle = conn.addNotification(var.group,
				var.offset, var.bitSize / 8, checkTimeInMs);
		notificationhandleCache.addNotification(var, notificationHandle);
	}

	public void deleteNotification(final String name) throws IOException,
			AmsException, TpyException {
		final Variable var = variableLocator.getVariableByName(name);
		final long notificationHandle = notificationhandleCache
				.getHandleByVariable(var);
		conn.deleteNotification(notificationHandle);
		notificationhandleCache.deleteNotification(var);
	}
}
