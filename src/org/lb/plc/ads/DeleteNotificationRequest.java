package org.lb.plc.ads;

import org.lb.plc.Toolbox;

public class DeleteNotificationRequest extends Payload {
	private final long handle;

	public long getHandle() {
		return handle;
	}

	public DeleteNotificationRequest(final long handle) {
		this.handle = handle;
	}

	@Override
	public int getCommandId() {
		return 7;
	}

	@Override
	public byte[] toBinary() {
		return Toolbox.uint32ToBytes(handle);
	}

	public static Payload valueOf(final byte[] data) {
		final long handle = Toolbox.bytesToUint32(data, 0);
		return new DeleteNotificationRequest(handle);
	}

	@Override
	public String toString() {
		return String.format("AdsDeleteNotificationRequest: Handle %d", handle);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (handle ^ (handle >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof DeleteNotificationRequest))
			return false;
		DeleteNotificationRequest other = (DeleteNotificationRequest) obj;
		if (handle != other.handle)
			return false;
		return true;
	}
}
