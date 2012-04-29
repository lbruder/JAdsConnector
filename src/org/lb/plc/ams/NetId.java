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

package org.lb.plc.ams;

import java.util.Arrays;

import org.lb.plc.Toolbox;

public class NetId {
	private final int[] data;

	public static NetId valueOf(final String value) {
		final String[] values = value.split("\\.");
		if (values.length != 6)
			throw new IllegalArgumentException(
					"Invalid textual AMS NetID representation: '" + value + "'");
		final int[] intValues = new int[6];
		for (int i = 0; i < 6; ++i)
			intValues[i] = Integer.valueOf(values[i]);
		return NetId.valueOf(intValues[0], intValues[1], intValues[2],
				intValues[3], intValues[4], intValues[5]);
	}

	public static NetId valueOf(final int b1, final int b2, final int b3,
			final int b4, final int b5, final int b6) {
		return new NetId(b1, b2, b3, b4, b5, b6);
	}

	public static NetId valueOf(final byte[] data) {
		if (data.length != 6)
			throw new IllegalArgumentException("Invalid data size");
		final int[] values = new int[6];
		for (int i = 0; i < 6; ++i)
			values[i] = Toolbox.toUnsigned(data[i]);
		return valueOf(values[0], values[1], values[2], values[3], values[4],
				values[5]);
	}

	private NetId(final int b1, final int b2, final int b3, final int b4,
			final int b5, final int b6) {
		ensureValueFitsByteSize(b1);
		ensureValueFitsByteSize(b2);
		ensureValueFitsByteSize(b3);
		ensureValueFitsByteSize(b4);
		ensureValueFitsByteSize(b5);
		ensureValueFitsByteSize(b6);
		data = new int[] { b1, b2, b3, b4, b5, b6 };
	}

	private static void ensureValueFitsByteSize(final int value) {
		if (value < 0)
			throw new IllegalArgumentException("Value too small, min = 0");
		if (value > 255)
			throw new IllegalArgumentException("Value too large, max = 255");
	}

	public byte[] toBinary() {
		byte[] ret = new byte[6];
		for (int i = 0; i < 6; ++i)
			ret[i] = (byte) data[i];
		return ret;
	}

	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder();
		for (int i = 0; i < 6; ++i) {
			if (i > 0)
				builder.append('.');
			builder.append(data[i]);
		}
		return builder.toString();
	}

	@Override
	public int hashCode() {
		return Arrays.hashCode(data);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof NetId))
			return false;
		NetId other = (NetId) obj;
		if (!Arrays.equals(data, other.data))
			return false;
		return true;
	}
}
