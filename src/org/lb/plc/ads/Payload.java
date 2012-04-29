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
