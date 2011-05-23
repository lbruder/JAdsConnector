package org.lb.plc.ads;

import org.lb.plc.ams.ErrorCode;

public abstract class Payload {
	public abstract byte[] toBinary();

	public abstract int getCommandId();

	protected Payload() {
	}

	public boolean isError() {
		return getErrorCode().isError();
	}

	public ErrorCode getErrorCode() {
		return ErrorCode.noError();
	}

	public static Payload getRequest(int commandId, final byte[] data) {
		switch (commandId) {
		case 1:
			return GetDeviceInfoRequest.valueOf(data);
		case 2:
			return ReadRequest.valueOf(data);
		case 3:
			return WriteRequest.valueOf(data);
			// case 4:
			// return AdsReadStateRequest.valueOf(data);
			// case 5:
			// return AdsWriteControlRequest.valueOf(data);
		case 6:
			return AddNotificationRequest.valueOf(data);
		case 7:
			return DeleteNotificationRequest.valueOf(data);
		case 8:
			return DeviceNotification.valueOf(data);
		case 9:
			return ReadWriteRequest.valueOf(data);
		default:
			throw new IllegalArgumentException("Invalid command ID: "
					+ commandId);
		}
	}

	public static Payload getResponse(int commandId, final byte[] data) {
		switch (commandId) {
		case 1:
			return GetDeviceInfoResponse.valueOf(data);
		case 2:
			return ReadResponse.valueOf(data);
		case 3:
			return WriteResponse.valueOf(data);
			// case 4:
			// return AdsReadStateResponse.valueOf(data);
			// case 5:
			// return AdsWriteControlResponse.valueOf(data);
		case 6:
			return AddNotificationResponse.valueOf(data);
		case 7:
			return DeleteNotificationResponse.valueOf(data);
		case 9:
			return ReadWriteResponse.valueOf(data);
		default:
			throw new IllegalArgumentException("Invalid command ID: "
					+ commandId);
		}
	}
}
