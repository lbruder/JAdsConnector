package org.lb.plc.ads;

import org.lb.plc.Toolbox;

public class AddNotificationResponse extends Payload {
	private final long errorCode;
	private final long handle;

	public long getHandle() {
		return handle;
	}

	public AddNotificationResponse(final long errorCode, final long handle) {
		this.errorCode = errorCode;
		this.handle = handle;
	}

	@Override
	public org.lb.plc.ams.ErrorCode getErrorCode() {
		return org.lb.plc.ams.ErrorCode.valueOf(errorCode);
	}

	@Override
	public int getCommandId() {
		return 6;
	}

	@Override
	public byte[] toBinary() {
		final byte[] ret = new byte[8];
		Toolbox.copyIntoByteArray(ret, 0, Toolbox.uint32ToBytes(errorCode));
		Toolbox.copyIntoByteArray(ret, 4, Toolbox.uint32ToBytes(handle));
		return ret;
	}

	public static Payload valueOf(final byte[] data) {
		final long errorCode = Toolbox.bytesToUint32(data, 0);
		final long handle = Toolbox.bytesToUint32(data, 4);
		return new AddNotificationResponse(errorCode, handle);
	}

	@Override
	public String toString() {
		return String.format(
				"AdsAddNotificationResponse: Error code %d, Handle %d",
				errorCode, handle);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (errorCode ^ (errorCode >>> 32));
		result = prime * result + (int) (handle ^ (handle >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof AddNotificationResponse))
			return false;
		AddNotificationResponse other = (AddNotificationResponse) obj;
		if (errorCode != other.errorCode)
			return false;
		if (handle != other.handle)
			return false;
		return true;
	}
}
