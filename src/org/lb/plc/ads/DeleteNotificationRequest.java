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
