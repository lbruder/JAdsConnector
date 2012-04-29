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

import org.junit.*;
import org.lb.plc.ams.*;

public class TestAmsPeerPair {
	private NetId src;
	private NetId dest;
	private byte[] asBinary;

	@Before
	public void setUp() throws Exception {
		src = NetId.valueOf(1, 2, 3, 4, 5, 6);
		dest = NetId.valueOf(4, 5, 6, 7, 8, 9);
		asBinary = new byte[] { 4, 5, 6, 7, 8, 9, 801 % 256, 801 / 256, 1, 2,
				3, 4, 5, 6, 123 % 256, 123 / 256 };
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testNormalOperation() throws Exception {
		PeerPair pair = PeerPair.valueOf(src, 123, dest, 801);
		Assert.assertEquals(pair.srcNetId, src);
		Assert.assertEquals(pair.destNetId, dest);
		Assert.assertEquals(pair.srcPort, 123);
		Assert.assertEquals(pair.destPort, 801);
	}

	@Test
	public void testFromByteArray() throws Exception {
		PeerPair pair = PeerPair.valueOf(asBinary);
		Assert.assertEquals(pair.srcNetId, src);
		Assert.assertEquals(pair.destNetId, dest);
		Assert.assertEquals(pair.srcPort, 123);
		Assert.assertEquals(pair.destPort, 801);
	}

	@Test
	public void testNullSrc() throws Exception {
		try {
			PeerPair.valueOf(null, 123, dest, 801);
			Assert.fail();
		} catch (NullPointerException ex) {
		}
	}

	@Test
	public void testNullDest() throws Exception {
		try {
			PeerPair.valueOf(src, 123, null, 801);
			Assert.fail();
		} catch (NullPointerException ex) {
		}
	}

	@Test
	public void testIdenticalAmsNetIds() throws Exception {
		try {
			PeerPair.valueOf(src, 123, src, 801);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
		}
	}

	@Test
	public void testPortTooBig() throws Exception {
		try {
			PeerPair.valueOf(src, 65536, dest, 801);
			PeerPair.valueOf(src, 123, dest, 65536);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
		}
	}

	@Test
	public void testPortNegative() throws Exception {
		try {
			PeerPair.valueOf(src, -1, dest, 801);
			PeerPair.valueOf(src, 123, dest, -1);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
		}
	}

	@Test
	public void testToString() throws Exception {
		PeerPair pair = PeerPair.valueOf(src, 123, dest, 801);
		String actual = pair.toString();
		Assert.assertEquals("{1.2.3.4.5.6:123 -> 4.5.6.7.8.9:801}", actual);
	}

	@Test
	public void testEquals() throws Exception {
		PeerPair pair1 = PeerPair.valueOf(src, 123, dest, 801);
		PeerPair pair2 = PeerPair.valueOf(src, 123, dest, 801);
		Assert.assertEquals(pair1, pair2);
	}

	@Test
	public void testToBinary() throws Exception {
		PeerPair pair = PeerPair.valueOf(src, 123, dest, 801);
		Assert.assertArrayEquals(asBinary, pair.toBinary());
	}

	@Test
	public void testIsInverseDirection() throws Exception {
		PeerPair pair1 = PeerPair.valueOf(src, 123, dest, 801);
		PeerPair pair2 = PeerPair.valueOf(dest, 801, src, 123);
		Assert.assertTrue(pair1.isInverseDirection(pair2));
		Assert.assertTrue(pair2.isInverseDirection(pair1));
		Assert.assertFalse(pair1.isInverseDirection(null));
		Assert.assertFalse(pair2.isInverseDirection(null));
	}
}
