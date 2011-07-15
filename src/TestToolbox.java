// Copyright (c) 2011, Leif Bruder <leifbruder@googlemail.com>
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

import org.junit.*;
import org.lb.plc.Toolbox;

public class TestToolbox {
	byte[] dest;
	byte[] src;

	@Before
	public void setUp() throws Exception {
		dest = new byte[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 };
		src = new byte[] { 20, 30, 40 };
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testCopyIntoByteArrayFrom0() throws Exception {
		Toolbox.copyIntoByteArray(dest, 0, src);
		Assert.assertArrayEquals(
				new byte[] { 20, 30, 40, 4, 5, 6, 7, 8, 9, 10 }, dest);
	}

	@Test
	public void testCopyIntoByteArrayFrom3() throws Exception {
		Toolbox.copyIntoByteArray(dest, 3, src);
		Assert.assertArrayEquals(
				new byte[] { 1, 2, 3, 20, 30, 40, 7, 8, 9, 10 }, dest);
	}

	@Test
	public void testCopyIntoByteArrayFromMinus() throws Exception {
		try {
			Toolbox.copyIntoByteArray(dest, -2, src);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
		}
	}

	@Test
	public void testCopyIntoByteArrayFrom12() throws Exception {
		Toolbox.copyIntoByteArray(dest, 12, src);
		Assert.assertArrayEquals(new byte[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 },
				dest);
	}

	@Test
	public void testCopyIntoByteArrayFrom8() throws Exception {
		Toolbox.copyIntoByteArray(dest, 8, src);
		Assert.assertArrayEquals(new byte[] { 1, 2, 3, 4, 5, 6, 7, 8, 20, 30 },
				dest);
	}

	@Test
	public void testCopyIntoByteArrayNullArgument() throws Exception {
		try {
			Toolbox.copyIntoByteArray(null, 0, src);
			Assert.fail();
		} catch (NullPointerException ex) {
		}
	}

	@Test
	public void testInt16ToBytesPositive() throws Exception {
		Assert.assertArrayEquals(new byte[] { 33, 3 },
				Toolbox.int16ToBytes(801));
	}

	public void testInt16ToBytesUpperBoundary() throws Exception {
		Assert.assertArrayEquals(new byte[] { (byte) 255, 127 },
				Toolbox.int16ToBytes(32767));
		try {
			Toolbox.int16ToBytes(32768);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
		}
	}

	@Test
	public void testInt16ToBytesNegative() throws Exception {
		Assert.assertArrayEquals(new byte[] { (byte) 254, (byte) 255 },
				Toolbox.int16ToBytes(-2));
	}

	@Test
	public void testInt16ToBytesLowerBoundary() throws Exception {
		Assert.assertArrayEquals(new byte[] { 0, (byte) 128 },
				Toolbox.int16ToBytes(-32768));
		try {
			Toolbox.int16ToBytes(-32769);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
		}
	}

	@Test
	public void testUint16ToBytesNormal() throws Exception {
		Assert.assertArrayEquals(new byte[] { 33, 3 },
				Toolbox.uint16ToBytes(801));
	}

	@Test
	public void testUint16ToBytesUpperBoundary() throws Exception {
		Assert.assertArrayEquals(new byte[] { (byte) 255, (byte) 255 },
				Toolbox.uint16ToBytes(65535));
		try {
			Toolbox.uint16ToBytes(65536);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
		}
	}

	@Test
	public void testUint16ToBytesLowerBoundary() throws Exception {
		Assert.assertArrayEquals(new byte[] { 0, 0 }, Toolbox.uint16ToBytes(0));
		try {
			Toolbox.uint16ToBytes(-1);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
		}
	}

	@Test
	public void testInt32ToBytesPositive() throws Exception {
		Assert.assertArrayEquals(new byte[] { (byte) 210, 2, (byte) 150, 73 },
				Toolbox.int32ToBytes(1234567890));
	}

	public void testInt32ToBytesUpperBoundary() throws Exception {
		Assert.assertArrayEquals(new byte[] { (byte) 255, (byte) 255,
				(byte) 255, 127 }, Toolbox.int32ToBytes(2147483647));
		try {
			Toolbox.int32ToBytes(2147483648L);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
		}
	}

	@Test
	public void testInt32ToBytesNegative() throws Exception {
		Assert.assertArrayEquals(new byte[] { (byte) 254, (byte) 255,
				(byte) 255, (byte) 255 }, Toolbox.int32ToBytes(-2));
	}

	@Test
	public void testInt32ToBytesLowerBoundary() throws Exception {
		Assert.assertArrayEquals(new byte[] { 0, 0, 0, (byte) 128 },
				Toolbox.int32ToBytes(-2147483648));
		try {
			Toolbox.int32ToBytes(-2147483649L);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
		}
	}

	@Test
	public void testUint32ToBytesNormal() throws Exception {
		Assert.assertArrayEquals(new byte[] { (byte) 210, 2, (byte) 150, 73 },
				Toolbox.uint32ToBytes(1234567890));
	}

	@Test
	public void testUint32ToBytesUpperBoundary() throws Exception {
		Assert.assertArrayEquals(new byte[] { (byte) 255, (byte) 255,
				(byte) 255, (byte) 255 }, Toolbox.uint32ToBytes(4294967295L));
		try {
			Toolbox.uint32ToBytes(4294967296L);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
		}
	}

	@Test
	public void testUint32ToBytesLowerBoundary() throws Exception {
		Assert.assertArrayEquals(new byte[] { 0, 0, 0, 0 },
				Toolbox.uint32ToBytes(0));
		try {
			Toolbox.uint32ToBytes(-1);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
		}
	}

	@Test
	public void testToUnsignedNormal() throws Exception {
		Assert.assertEquals(123, Toolbox.toUnsigned((byte) 123));
		Assert.assertEquals(234, Toolbox.toUnsigned((byte) 234));
	}

	@Test
	public void testToUnsignedLowerBoundary() throws Exception {
		Assert.assertEquals(0, Toolbox.toUnsigned((byte) 0));
	}

	@Test
	public void testToUnsignedUpperBoundary() throws Exception {
		Assert.assertEquals(255, Toolbox.toUnsigned((byte) 255));
	}

	@Test
	public void testBytesToUint32TooLittleBytes() throws Exception {
		try {
			Toolbox.bytesToUint32(new byte[] { 1, 2, 3, 4 }, 2);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
		}
	}

	@Test
	public void testBytesToUint32Normal() throws Exception {
		Assert.assertEquals(
				1234567890,
				Toolbox.bytesToUint32(new byte[] { 1, 2, (byte) 210, 2,
						(byte) 150, 73, 3, 4 }, 2));
	}

	@Test
	public void testBytesToUint32LowerBoundary() throws Exception {
		Assert.assertEquals(
				1234567890,
				Toolbox.bytesToUint32(new byte[] { (byte) 210, 2, (byte) 150,
						73, 123, 21 }, 0));
		try {
			Assert.assertEquals(
					1234567890,
					Toolbox.bytesToUint32(new byte[] { (byte) 210, 2,
							(byte) 150, 73, 123, 21 }, -1));
			Assert.fail();
		} catch (Exception ex) {
		}
	}

	@Test
	public void testBytesToUint32UpperBoundary() throws Exception {
		Assert.assertEquals(
				1234567890,
				Toolbox.bytesToUint32(new byte[] { 1, 2, (byte) 210, 2,
						(byte) 150, 73, 123, 21 }, 2));
	}

	@Test
	public void testBytesToUint32MsbSet() throws Exception {
		Assert.assertEquals(
				4294967295L,
				Toolbox.bytesToUint32(new byte[] { (byte) 255, (byte) 255,
						(byte) 255, (byte) 255 }, 0));
	}

	@Test
	public void testBytesToUint16TooLittleBytes() throws Exception {
		try {
			Toolbox.bytesToUint16(new byte[] { 1, 2, 3 }, 2);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
		}
	}

	@Test
	public void testBytesToUint16Normal() throws Exception {
		Assert.assertEquals(801,
				Toolbox.bytesToUint16(new byte[] { 1, 2, 33, 3, 3, 4 }, 2));
	}

	@Test
	public void testBytesToUint16LowerBoundary() throws Exception {
		Assert.assertEquals(801,
				Toolbox.bytesToUint16(new byte[] { 33, 3, 73, 123, 21 }, 0));
		try {
			Assert.assertEquals(801, Toolbox.bytesToUint16(new byte[] { 33, 3,
					73, 123, 21 }, -1));
			Assert.fail();
		} catch (Exception ex) {
		}
	}

	@Test
	public void testBytesToUint16UpperBoundary() throws Exception {
		Assert.assertEquals(801,
				Toolbox.bytesToUint16(new byte[] { 1, 2, 3, 4, 5, 33, 3 }, 5));
	}

	@Test
	public void testBytesToUint16MsbSet() throws Exception {
		Assert.assertEquals(65535,
				Toolbox.bytesToUint16(new byte[] { (byte) 255, (byte) 255 }, 0));
	}

	@Test
	public void testHexdumpEmpty() throws Exception {
		final String expected = "";
		final String actual = Toolbox.hexdump(new byte[] {});
		Assert.assertEquals(expected, actual);
	}

	@Test
	public void testHexdumpSingleValue() throws Exception {
		final String expected = "0A";
		final String actual = Toolbox.hexdump(new byte[] { 0x0a });
		Assert.assertEquals(expected, actual);
	}

	@Test
	public void testHexdumpTwoValues() throws Exception {
		final String expected = "EA 31";
		final String actual = Toolbox.hexdump(new byte[] { (byte) 0xea, 0x31 });
		Assert.assertEquals(expected, actual);
	}

	@Test
	public void testHexdumpThreeValues() throws Exception {
		final String expected = "12 34 56";
		final String actual = Toolbox.hexdump(new byte[] { 0x12, 0x34, 0x56 });
		Assert.assertEquals(expected, actual);
	}

	@Test
	public void testBytesToSint16Positive() throws Exception {
		Assert.assertEquals(32766,
				Toolbox.bytesToSint16(new byte[] { (byte) 254, 127 }, 0));
	}

	@Test
	public void testBytesToSint16PositiveWithOffset() throws Exception {
		Assert.assertEquals(32766, Toolbox.bytesToSint16(new byte[] { 1, 2, 3,
				(byte) 254, 127 }, 3));
	}

	@Test
	public void testBytesToSint16Border() throws Exception {
		Assert.assertEquals(32767,
				Toolbox.bytesToSint16(new byte[] { (byte) 255, 127 }, 0));
	}

	@Test
	public void testBytesToSint16Negative() throws Exception {
		Assert.assertEquals(-32768,
				Toolbox.bytesToSint16(new byte[] { 0, (byte) 128 }, 0));
	}

	@Test
	public void testBytesToSint16InvalidOffset() throws Exception {
		try {
			Toolbox.bytesToSint16(new byte[] { 1, 2, 3, 4, 5 }, -1);
			Assert.fail();
		} catch (Exception ex) {
		}

		try {
			Toolbox.bytesToSint16(new byte[] { 1, 2, 3, 4, 5 }, 4);
			Assert.fail();
		} catch (Exception ex) {
		}

		try {
			Toolbox.bytesToSint16(new byte[] { 1, 2, 3, 4, 5 }, 12);
			Assert.fail();
		} catch (Exception ex) {
		}
	}

	@Test
	public void testBytesToSint32Positive() throws Exception {
		Assert.assertEquals(
				2147483646,
				Toolbox.bytesToSint32(new byte[] { (byte) 254, (byte) 255,
						(byte) 255, 127 }, 0));
	}

	@Test
	public void testBytesToSint32PositiveWithOffset() throws Exception {
		Assert.assertEquals(
				2147483646,
				Toolbox.bytesToSint32(new byte[] { 1, 2, 3, (byte) 254,
						(byte) 255, (byte) 255, 127 }, 3));
	}

	@Test
	public void testBytesToSint32Border() throws Exception {
		Assert.assertEquals(
				2147483647,
				Toolbox.bytesToSint32(new byte[] { (byte) 255, (byte) 255,
						(byte) 255, 127 }, 0));
	}

	@Test
	public void testBytesToSint32Negative() throws Exception {
		Assert.assertEquals(-2147483648,
				Toolbox.bytesToSint32(new byte[] { 0, 0, 0, (byte) 128 }, 0));
	}

	@Test
	public void testBytesToSint32InvalidOffset() throws Exception {
		try {
			Toolbox.bytesToSint32(new byte[] { 1, 2, 3, 4, 5 }, -1);
			Assert.fail();
		} catch (Exception ex) {
		}

		try {
			Toolbox.bytesToSint32(new byte[] { 1, 2, 3, 4, 5 }, 2);
			Assert.fail();
		} catch (Exception ex) {
		}

		try {
			Toolbox.bytesToSint32(new byte[] { 1, 2, 3, 4, 5 }, 12);
			Assert.fail();
		} catch (Exception ex) {
		}
	}

	// TODO

	// public static double bytesToDouble(final byte[] data)
	// public static long bytesToSignedInteger(byte[] data)
	// public static long bytesToUnsignedInteger(byte[] data)
}
