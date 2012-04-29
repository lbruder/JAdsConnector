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

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.lb.plc.ads.GetDeviceInfoRequest;
import org.lb.plc.ads.Payload;
import org.lb.plc.ads.ReadWriteRequest;
import org.lb.plc.ams.NetId;
import org.lb.plc.ams.Packet;
import org.lb.plc.ams.PeerPair;

public class TestAmsPacket {
	private NetId srcNetId;
	private NetId destNetId;
	private PeerPair peers;

	@Before
	public void setUp() throws Exception {
		srcNetId = NetId.valueOf("1.2.3.4.5.6");
		destNetId = NetId.valueOf("128.129.130.131.132.133");
		peers = PeerPair.valueOf(srcNetId, 128, destNetId, 801);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testToBinary() throws Exception {
		Packet packet = Packet.newRequest(peers, 0, 123, new Payload() {
			@Override
			public int getCommandId() {
				return 42;
			}

			@Override
			public byte[] toBinary() {
				return new byte[] { 64, 65, 66, 67 };
			}
		});
		byte[] actual = packet.toBinary();
		byte[] expected = new byte[] { 0, 0, 36, 0, 0, 0, (byte) 128,
				(byte) 129, (byte) 130, (byte) 131, (byte) 132, (byte) 133, 33,
				3, 1, 2, 3, 4, 5, 6, (byte) 128, 0, 42, 0, 4, 0, 4, 0, 0, 0, 0,
				0, 0, 0, 123, 0, 0, 0, 64, 65, 66, 67 };
		Assert.assertArrayEquals(expected, actual);
	}

	@Test
	public void testNullPeers() throws Exception {
		try {
			Packet.newRequest(null, 0, 123, new GetDeviceInfoRequest());
			Assert.fail();
		} catch (NullPointerException ex) {
		}
	}

	@Test
	public void testNullPayload() throws Exception {
		try {
			Packet.newRequest(peers, 0, 123, null);
			Assert.fail();
		} catch (NullPointerException ex) {
		}
	}

	@Test
	public void testToString() throws Exception {
		Packet packet = Packet.newRequest(peers, 0, 123, new ReadWriteRequest(
				1000, 2000, new byte[] { 1, 2, 3, 4, 5 }, 123));
		String expected = "AmsPacket: "
				+ "{1.2.3.4.5.6:128 -> 128.129.130.131.132.133:801}, "
				+ "ErrorCode: 0, Id: 123, Payload: AdsReadWriteRequest: "
				+ "Group 1000, Offset 2000, Read 123 bytes, "
				+ "Write: [1, 2, 3, 4, 5]";
		String actual = packet.toString();
		Assert.assertEquals(expected, actual);
	}

	@Test
	public void testBinaryRoundtrip() throws Exception {
		Packet packet = Packet.newRequest(peers, 0, 123, new ReadWriteRequest(
				1000, 2000, new byte[] { 1, 2, 3, 4, 5 }, 123));
		byte[] asBinary = packet.toBinary();
		Packet newPacket = Packet.valueOf(asBinary);
		Assert.assertEquals(packet, newPacket);
	}

	// TODO: FromBinary
}
