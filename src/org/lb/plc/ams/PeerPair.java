package org.lb.plc.ams;

import java.util.Arrays;
import org.lb.plc.Toolbox;

public class PeerPair {
	public final NetId destNetId;
	public final int destPort;
	public final NetId srcNetId;
	public final int srcPort;

	public static PeerPair valueOf(final NetId srcNetId, final int srcPort,
			final NetId destNetId, final int destPort) {
		return new PeerPair(srcNetId, srcPort, destNetId, destPort);
	}

	public static PeerPair valueOf(final byte[] data) {
		if (data.length != 16)
			throw new IllegalArgumentException("Invalid data size");
		final NetId dest = NetId.valueOf(Arrays.copyOfRange(data, 0, 6));
		final int destPort = Toolbox.bytesToUint16(data, 6);
		final NetId src = NetId.valueOf(Arrays.copyOfRange(data, 8, 14));
		final int srcPort = Toolbox.bytesToUint16(data, 14);
		return valueOf(src, srcPort, dest, destPort);
	}

	private PeerPair(final NetId srcNetId, final int srcPort,
			final NetId destNetId, final int destPort) {
		this.srcNetId = srcNetId;
		this.srcPort = srcPort;
		this.destNetId = destNetId;
		this.destPort = destPort;
		ensureValid();
	}

	private void ensureValid() {
		ensureValidNetIds();
		ensureValidPort(srcPort);
		ensureValidPort(destPort);
	}

	private void ensureValidNetIds() {
		if (destNetId == null)
			throw new NullPointerException("destNetId == null");
		if (srcNetId == null)
			throw new NullPointerException("srcNetId == null");
		if (srcNetId.equals(destNetId))
			throw new IllegalArgumentException(
					"Source and destination Net IDs must not be equal");
	}

	private void ensureValidPort(final int port) {
		if (port < 0)
			throw new IllegalArgumentException("Invalid port number");
		if (port > 65535)
			throw new IllegalArgumentException("Invalid port number");
	}

	public byte[] toBinary() {
		byte[] ret = new byte[16];
		Toolbox.copyIntoByteArray(ret, 0, destNetId.toBinary());
		Toolbox.copyIntoByteArray(ret, 6, Toolbox.uint16ToBytes(destPort));
		Toolbox.copyIntoByteArray(ret, 8, srcNetId.toBinary());
		Toolbox.copyIntoByteArray(ret, 14, Toolbox.uint16ToBytes(srcPort));
		return ret;
	}

	public boolean isInverseDirection(PeerPair pair2) {
		if (pair2 == null)
			return false;
		if (!pair2.destNetId.equals(srcNetId))
			return false;
		if (!pair2.srcNetId.equals(destNetId))
			return false;
		if (pair2.destPort != srcPort)
			return false;
		if (pair2.srcPort != destPort)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return String.format("{%s:%d -> %s:%d}", srcNetId, srcPort, destNetId,
				destPort);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((destNetId == null) ? 0 : destNetId.hashCode());
		result = prime * result + destPort;
		result = prime * result
				+ ((srcNetId == null) ? 0 : srcNetId.hashCode());
		result = prime * result + srcPort;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof PeerPair))
			return false;
		PeerPair other = (PeerPair) obj;
		if (destNetId == null) {
			if (other.destNetId != null)
				return false;
		} else if (!destNetId.equals(other.destNetId))
			return false;
		if (destPort != other.destPort)
			return false;
		if (srcNetId == null) {
			if (other.srcNetId != null)
				return false;
		} else if (!srcNetId.equals(other.srcNetId))
			return false;
		if (srcPort != other.srcPort)
			return false;
		return true;
	}
}
