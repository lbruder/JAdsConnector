package org.lb.plc.ads;

public class GetDeviceInfoRequest extends Payload {
	public GetDeviceInfoRequest() {
	}

	@Override
	public int getCommandId() {
		return 1;
	}

	@Override
	public byte[] toBinary() {
		return new byte[] {};
	}

	public static Payload valueOf(final byte[] data) {
		return new GetDeviceInfoRequest();
	}

	@Override
	public String toString() {
		return "AdsGetDeviceInfoRequest";
	}

	@Override
	public int hashCode() {
		return 31;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof GetDeviceInfoRequest))
			return false;
		return true;
	}
}
