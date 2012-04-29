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

package org.lb.plc;

public class Toolbox {
	public static void copyIntoByteArray(final byte[] dest,
			final int destStart, final byte[] src) {
		if (dest == null)
			throw new NullPointerException("dest == null");
		if (destStart < 0)
			throw new IllegalArgumentException("Start index must be >= 0");
		int di = 0;
		for (byte value : src) {
			if (destStart + di >= dest.length)
				return;
			dest[destStart + di] = value;
			++di;
		}
	}

	public static byte[] int16ToBytes(final int value) {
		if (value > 32767)
			throw new IllegalArgumentException("Number too large");
		if (value < -32768)
			throw new IllegalArgumentException("Number too small");
		if (value < 0)
			return uint16ToBytes(65536 + value);
		return uint16ToBytes(value);
	}

	public static byte[] uint16ToBytes(final int value) {
		if (value > 65535)
			throw new IllegalArgumentException("Number too large");
		if (value < 0)
			throw new IllegalArgumentException("Number too small");
		return new byte[] { (byte) (value % 256), (byte) (value / 256) };
	}

	public static byte[] int32ToBytes(final long value) {
		if (value > 2147483647)
			throw new IllegalArgumentException("Number too large");
		if (value < -2147483648)
			throw new IllegalArgumentException("Number too small");
		if (value < 0)
			return uint32ToBytes(4294967296L + value);
		return uint32ToBytes(value);
	}

	public static byte[] uint32ToBytes(final long value) {
		if (value > 4294967295L)
			throw new IllegalArgumentException("Number too large");
		if (value < 0)
			throw new IllegalArgumentException("Number too small");

		final byte b1 = (byte) (value % 256);
		long rest = value / 256;
		final byte b2 = (byte) (rest % 256);
		rest /= 256;
		final byte b3 = (byte) (rest % 256);
		rest /= 256;
		final byte b4 = (byte) (rest % 256);

		return new byte[] { b1, b2, b3, b4 };
	}

	public static int toUnsigned(final byte value) {
		if (value >= 0)
			return value;
		return 256 + value;
	}

	public static int bytesToUint16(final byte[] data, final int start) {
		if (data.length - start < 2)
			throw new IllegalArgumentException("Not enough bytes");
		final int b1 = toUnsigned(data[start]);
		final int b2 = toUnsigned(data[start + 1]);
		return b2 * 256 + b1;
	}

	public static long bytesToUint32(final byte[] data, final int start) {
		if (data.length - start < 4)
			throw new IllegalArgumentException("Not enough bytes");
		final long b1 = toUnsigned(data[start]);
		final long b2 = toUnsigned(data[start + 1]);
		final long b3 = toUnsigned(data[start + 2]);
		final long b4 = toUnsigned(data[start + 3]);
		return ((b4 * 256 + b3) * 256 + b2) * 256 + b1;
	}

	public static String hexdump(final byte[] data) {
		final StringBuilder ret = new StringBuilder();
		boolean first = true;

		for (byte current : data) {
			if (!first)
				ret.append(' ');
			first = false;
			ret.append(String.format("%02X", current));
		}

		return ret.toString();
	}

	public static int bytesToSint16(final byte[] data, final int start) {
		if (data.length - start < 2)
			throw new IllegalArgumentException("Not enough bytes");
		final int b1 = toUnsigned(data[start]);
		final int b2 = data[start + 1];
		return b2 * 256 + b1;
	}

	public static long bytesToSint32(final byte[] data, final int start) {
		if (data.length - start < 4)
			throw new IllegalArgumentException("Not enough bytes");
		final long b1 = toUnsigned(data[start]);
		final long b2 = toUnsigned(data[start + 1]);
		final long b3 = toUnsigned(data[start + 2]);
		final long b4 = data[start + 3];
		return ((b4 * 256 + b3) * 256 + b2) * 256 + b1;
	}

	public static double bytesToDouble(final byte[] data) {
		if (data.length == 4)
			return Float.intBitsToFloat((int) bytesToUint32(data, 0));
		return Double.longBitsToDouble(unsignedBytesToLong(data));
	}

	private static long unsignedBytesToLong(final byte[] data) {
		long ret = 0;
		for (int i = data.length - 1; i >= 0; --i)
			ret = ret * 256 + toUnsigned(data[i]);
		return ret;
	}

	public static long bytesToSignedInteger(byte[] data) {
		switch (data.length) {
		case 1:
			return data[0];
		case 2:
			return bytesToSint16(data, 0);
		case 4:
			return bytesToSint32(data, 0);
		default:
			throw new IllegalArgumentException("Invalid number of bytes: "
					+ hexdump(data));
		}
	}

	public static long bytesToUnsignedInteger(byte[] data) {
		switch (data.length) {
		case 1:
			return toUnsigned(data[0]);
		case 2:
			return bytesToUint16(data, 0);
		case 4:
			return bytesToUint32(data, 0);
		default:
			throw new IllegalArgumentException("Invalid number of bytes: "
					+ hexdump(data));
		}
	}

	public static byte[] integerToBytes(long value, int byteSize, boolean signed) {
		switch (byteSize) {
		case 1:
			return new byte[] { (byte) value };
		case 2:
			if (signed)
				return Toolbox.int16ToBytes((int) value);
			return Toolbox.uint16ToBytes((int) value);
		case 4:
			if (signed)
				return Toolbox.int32ToBytes(value);
			return Toolbox.uint32ToBytes(value);
		default:
			throw new IllegalArgumentException("Internal error: Bytesize "
					+ byteSize + "not handled");
		}
	}

	public static byte[] doubleToBytes(double value, int byteSize) {
		switch (byteSize) {
		case 4:
			return Toolbox.floatToBytes((float) value);
		case 8:
			return Toolbox.doubleToBytes(value);
		default:
			throw new IllegalArgumentException("Internal error: Bytesize "
					+ byteSize + "not handled");
		}
	}

	public static byte[] floatToBytes(float value) {
		return Toolbox.uint32ToBytes(Float.floatToIntBits(value));
	}

	public static byte[] doubleToBytes(double value) {
		return Toolbox.uint32ToBytes(Double.doubleToLongBits(value));
	}
}
