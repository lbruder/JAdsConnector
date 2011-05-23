import org.junit.*;
import org.lb.plc.ams.NetId;


public class TestAmsNetId {
	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testToBinary() throws Exception {
		NetId id = NetId.valueOf(1, 2, 3, 4, 5, 6);
		byte actual[] = id.toBinary();
		byte expected[] = new byte[] { 1, 2, 3, 4, 5, 6 };
		Assert.assertArrayEquals(expected, actual);
	}

	@Test
	public void testFromBinary() throws Exception {
		byte asBinary[] = new byte[] { 1, 2, 3, 4, 5, 6 };
		NetId id = NetId.valueOf(asBinary);
		Assert.assertArrayEquals(asBinary, id.toBinary());
	}

	@Test
	public void testToBinaryBigValues() throws Exception {
		NetId id = NetId.valueOf(127, 128, 129, 130, 254, 255);
		byte actual[] = id.toBinary();
		byte expected[] = new byte[] { 127, (byte) 128, (byte) 129, (byte) 130,
				(byte) 254, (byte) 255 };
		Assert.assertArrayEquals(expected, actual);
	}

	@Test
	public void testNegativeNumber() throws Exception {
		try {
			NetId.valueOf(1, 2, -3, 4, 5, 6);
			Assert.fail("Invalid AMS NetId accepted");
		} catch (IllegalArgumentException ex) {
		}
	}

	@Test
	public void testNumberTooLarge() throws Exception {
		try {
			NetId.valueOf(1, 2, 3, 400, 5, 6);
			Assert.fail("Invalid AMS NetId accepted");
		} catch (IllegalArgumentException ex) {
		}
	}

	@Test
	public void testToString() throws Exception {
		NetId id = NetId.valueOf(1, 2, 3, 4, 5, 6);
		Assert.assertEquals("1.2.3.4.5.6", id.toString());
	}

	@Test
	public void testFromString() throws Exception {
		NetId id = NetId.valueOf("1.2.3.4.5.6");
		byte expected[] = new byte[] { 1, 2, 3, 4, 5, 6 };
		byte actual[] = id.toBinary();
		Assert.assertArrayEquals(expected, actual);
	}

	@Test
	public void testFromStringTooFewNumbers() throws Exception {
		try {
			NetId.valueOf("1.2.3.4.5");
			Assert.fail("Invalid AMS NetID accepted");
		} catch (IllegalArgumentException ex) {
		}
	}

	@Test
	public void testFromStringTooManyNumbers() throws Exception {
		try {
			NetId.valueOf("1.2.3.4.5.6.7");
			Assert.fail("Invalid AMS NetID accepted");
		} catch (IllegalArgumentException ex) {
		}
	}

	@Test
	public void testFromStringWrongNumbers() throws Exception {
		try {
			NetId.valueOf("1234.2.3.432.5.-6.7");
			Assert.fail("Invalid AMS NetID accepted");
		} catch (IllegalArgumentException ex) {
		}
	}

	@Test
	public void testFromStringCompletelyWrong() throws Exception {
		try {
			NetId.valueOf("fooBARbaz");
			Assert.fail("Invalid AMS NetID accepted");
		} catch (IllegalArgumentException ex) {
		}
	}

	@Test
	public void testFromStringWithNullParameter() throws Exception {
		try {
			NetId.valueOf((String) null);
			Assert.fail("Invalid AMS NetID accepted");
		} catch (NullPointerException ex) {
		}
	}

	@Test
	public void testFromBinaryWithNullParameter() throws Exception {
		try {
			NetId.valueOf((byte[]) null);
			Assert.fail("Invalid AMS NetID accepted");
		} catch (NullPointerException ex) {
		}
	}

	@Test
	public void testEquals() throws Exception {
		NetId id1 = NetId.valueOf(1, 2, 3, 4, 5, 6);
		NetId id2 = NetId.valueOf("1.2.3.4.5.6");
		Assert.assertEquals(id1, id2);
	}
}
