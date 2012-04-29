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

import java.util.Arrays;
import org.lb.plc.Toolbox;

public class ReadWriteRequest extends Payload {
	private final long group;
	private final long offset;
	private final byte[] payload;
	private final long readLength;

	public long getGroup() {
		return group;
	}

	public long getOffset() {
		return offset;
	}

	public byte[] getPayload() {
		return payload;
	}

	public long getReadLength() {
		return readLength;
	}

	public ReadWriteRequest(final long group, final long offset,
			final byte[] payload, final long readLength) {
		this.group = group;
		this.offset = offset;
		this.payload = payload;
		this.readLength = readLength;
	}

	@Override
	public int getCommandId() {
		return 9;
	}

	@Override
	public byte[] toBinary() {
		final int dataLength = 16 + payload.length;
		byte[] ret = new byte[dataLength];
		Toolbox.copyIntoByteArray(ret, 0, Toolbox.uint32ToBytes(group));
		Toolbox.copyIntoByteArray(ret, 4, Toolbox.uint32ToBytes(offset));
		Toolbox.copyIntoByteArray(ret, 8, Toolbox.uint32ToBytes(readLength));
		Toolbox.copyIntoByteArray(ret, 12, Toolbox
				.uint32ToBytes(payload.length));
		Toolbox.copyIntoByteArray(ret, 16, payload);
		return ret;
	}

	public static Payload valueOf(final byte[] data) {
		final long group = Toolbox.bytesToUint32(data, 0);
		final long offset = Toolbox.bytesToUint32(data, 4);
		final long readLength = Toolbox.bytesToUint32(data, 8);
		final byte[] payload = Arrays.copyOfRange(data, 16, data.length);
		return new ReadWriteRequest(group, offset, payload, readLength);
	}

	@Override
	public String toString() {
		return String.format("AdsReadWriteRequest: Group %d, Offset %d, "
				+ "Read %d bytes, Write: %s", group, offset, readLength, Arrays
				.toString(payload));
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (group ^ (group >>> 32));
		result = prime * result + (int) (offset ^ (offset >>> 32));
		result = prime * result + Arrays.hashCode(payload);
		result = prime * result + (int) (readLength ^ (readLength >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof ReadWriteRequest))
			return false;
		ReadWriteRequest other = (ReadWriteRequest) obj;
		if (group != other.group)
			return false;
		if (offset != other.offset)
			return false;
		if (!Arrays.equals(payload, other.payload))
			return false;
		if (readLength != other.readLength)
			return false;
		return true;
	}
}
