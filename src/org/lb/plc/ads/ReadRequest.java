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

public class ReadRequest extends Payload {
	private final long group;
	private final long offset;
	private final long length;

	public long getGroup() {
		return group;
	}

	public long getOffset() {
		return offset;
	}

	public long getLength() {
		return length;
	}

	public ReadRequest(final long group, final long offset, final long length) {
		this.group = group;
		this.offset = offset;
		this.length = length;
	}

	@Override
	public int getCommandId() {
		return 2;
	}

	@Override
	public byte[] toBinary() {
		byte[] ret = new byte[12];
		Toolbox.copyIntoByteArray(ret, 0, Toolbox.uint32ToBytes(group));
		Toolbox.copyIntoByteArray(ret, 4, Toolbox.uint32ToBytes(offset));
		Toolbox.copyIntoByteArray(ret, 8, Toolbox.uint32ToBytes(length));
		return ret;
	}

	public static Payload valueOf(final byte[] data) {
		final long group = Toolbox.bytesToUint32(data, 0);
		final long offset = Toolbox.bytesToUint32(data, 4);
		final long length = Toolbox.bytesToUint32(data, 8);
		return new ReadRequest(group, offset, length);
	}

	@Override
	public String toString() {
		return String.format(
				"AdsReadRequest: Group %d, Offset %d, Read %d bytes", group,
				offset, length);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (group ^ (group >>> 32));
		result = prime * result + (int) (length ^ (length >>> 32));
		result = prime * result + (int) (offset ^ (offset >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof ReadRequest))
			return false;
		ReadRequest other = (ReadRequest) obj;
		if (group != other.group)
			return false;
		if (length != other.length)
			return false;
		if (offset != other.offset)
			return false;
		return true;
	}
}
