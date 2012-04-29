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

import java.util.*;
import org.lb.plc.Toolbox;

public class DeviceNotification extends Payload {
	// Notification handle -> new value in binary form
	private final Map<Long, byte[]> newValues;

	public Map<Long, byte[]> getNewValues() {
		return new HashMap<Long, byte[]>(newValues);
	}

	public DeviceNotification(final Map<Long, byte[]> newValues) {
		this.newValues = newValues;
		ensureValid();
	}

	private void ensureValid() {
		if (this.newValues == null)
			throw new IllegalArgumentException("newValues must not be null");
	}

	@Override
	public int getCommandId() {
		return 8;
	}

	@Override
	public byte[] toBinary() {
		byte[] samplesAsBinary = getSamplesAsBinary();
		final int dataLength = 20 + samplesAsBinary.length;
		final byte[] ret = new byte[dataLength];
		Toolbox.copyIntoByteArray(ret, 4, Toolbox.uint32ToBytes(1));
		Toolbox.copyIntoByteArray(ret, 8, Toolbox.uint32ToBytes(0));
		Toolbox.copyIntoByteArray(ret, 12, Toolbox.uint32ToBytes(0));
		Toolbox.copyIntoByteArray(ret, 16, Toolbox.uint32ToBytes(newValues
				.size()));
		Toolbox.copyIntoByteArray(ret, 20, samplesAsBinary);
		return ret;
	}

	private byte[] getSamplesAsBinary() {
		int size = 0;
		for (byte[] value : newValues.values()) {
			size += 4 + 4 + value.length; // Handle + Size + Data
		}

		final byte[] ret = new byte[size];
		int offset = 0;
		for (long key : newValues.keySet()) {
			final byte[] value = newValues.get(key);
			Toolbox.copyIntoByteArray(ret, offset, Toolbox.uint32ToBytes(key));
			Toolbox.copyIntoByteArray(ret, offset + 4, Toolbox
					.uint32ToBytes(value.length));
			Toolbox.copyIntoByteArray(ret, offset + 8, value);
			offset += 4 + 4 + value.length;
		}
		return ret;
	}

	public static Payload valueOf(final byte[] data) {
		final Map<Long, byte[]> newValues = new HashMap<Long, byte[]>();

		final long dataLength = Toolbox.bytesToUint32(data, 0);
		if (dataLength != data.length)
			throw new IllegalArgumentException(
					"Invalid data size in AdsDeviceNotification");

		final long numberOfStamps = Toolbox.bytesToUint32(data, 4);
		int offset = 8;
		for (long stampNo = 0; stampNo < numberOfStamps; ++stampNo) {
			offset += 8; // Skip timestamp
			final long numberOfSamples = Toolbox.bytesToUint32(data, offset);
			offset += 4;
			for (long sampleNo = 0; sampleNo < numberOfSamples; ++sampleNo) {
				final long handle = Toolbox.bytesToUint32(data, offset);
				offset += 4;
				final long sampleSize = Toolbox.bytesToUint32(data, offset);
				offset += 4;
				final byte[] sample = Arrays.copyOfRange(data, offset,
						(int) (offset + sampleSize));
				newValues.put(handle, sample);
				offset += sampleSize;
			}
		}
		return new DeviceNotification(newValues);
	}

	@Override
	public String toString() {
		StringBuilder ret = new StringBuilder("AdsDeviceNotification: ");
		boolean first = true;
		for (long key : newValues.keySet()) {
			if (!first)
				ret.append(", ");
			first = false;

			ret.append(key);
			ret.append(" -> ");
			ret.append(Toolbox.hexdump(newValues.get(key)));
		}
		return ret.toString();
	}

	@Override
	public int hashCode() {
		return (newValues == null) ? 0 : newValues.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof DeviceNotification))
			return false;
		DeviceNotification other = (DeviceNotification) obj;
		if (newValues == null) {
			if (other.newValues != null)
				return false;
		} else if (!newValues.equals(other.newValues))
			return false;
		return true;
	}
}
