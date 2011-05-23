package org.lb.plc.ads;

import org.lb.plc.Toolbox;

public class WriteResponse extends Payload {
	private final long errorCode;

	public WriteResponse(final long errorCode) {
		this.errorCode = errorCode;
	}

	public static Payload valueOf(final byte[] data) {
		final long errorCode = Toolbox.bytesToUint32(data, 0);
		return new WriteResponse(errorCode);
	}

	@Override
	public org.lb.plc.ams.ErrorCode getErrorCode() {
		return org.lb.plc.ams.ErrorCode.valueOf(errorCode);
	}

	@Override
	public int getCommandId() {
		return 3;
	}

	@Override
	public byte[] toBinary() {
		return Toolbox.uint32ToBytes(errorCode);
	}

	@Override
	public String toString() {
		return String.format("AdsWriteResponse: Error code %d", errorCode);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (errorCode ^ (errorCode >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof WriteResponse))
			return false;
		WriteResponse other = (WriteResponse) obj;
		if (errorCode != other.errorCode)
			return false;
		return true;
	}
}
