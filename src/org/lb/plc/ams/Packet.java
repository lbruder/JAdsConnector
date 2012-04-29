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
import org.lb.plc.ads.Payload;

public class Packet {
	private final PeerPair peerPair;
	private final int command;
	private final int flags;
	private final long errorCode;
	private final long id;
	private final Payload payload;

	private Packet(final PeerPair peerPair, final int command, final int flags,
			final long errorCode, final long id, final Payload payload) {
		this.peerPair = peerPair;
		this.command = command;
		this.flags = flags;
		this.payload = payload;
		this.errorCode = errorCode;
		this.id = id;
		ensureValid();
		System.out.println(this.toString());
	}

	private void ensureValid() {
		if (peerPair == null)
			throw new NullPointerException("peerPair == null");
		if (payload == null)
			throw new NullPointerException("payload == null");
		if (flags != 4 && flags != 5)
			throw new IllegalArgumentException("Invalid AMS State Flags");
	}

	public static Packet newRequest(final PeerPair peerPair,
			final long errorCode, final long id, final Payload payload) {
		return new Packet(peerPair, payload.getCommandId(), 4, errorCode, id,
				payload);
	}

	public static Packet newResponse(final PeerPair peerPair,
			final long errorCode, final long id, final Payload payload) {
		return new Packet(peerPair, payload.getCommandId(), 5, errorCode, id,
				payload);
	}

	public static Packet valueOf(final byte[] data) {
		final PeerPair peerPair = PeerPair.valueOf(Arrays.copyOfRange(data, 6,
				22));
		final int commandId = Toolbox.bytesToUint16(data, 22);
		final int flags = Toolbox.bytesToUint16(data, 24);
		final int errorCode = Toolbox.bytesToUint16(data, 30);
		final long id = Toolbox.bytesToUint32(data, 34);
		if (isRequest(flags)) {
			return newRequest(peerPair, errorCode, id, Payload.getRequest(
					commandId, Arrays.copyOfRange(data, 38, data.length)));
		} else if (isResponse(flags)) {
			return newResponse(peerPair, errorCode, id, Payload.getResponse(
					commandId, Arrays.copyOfRange(data, 38, data.length)));
		} else
			throw new IllegalArgumentException("Illegal AMS State Flags");
	}

	private static boolean isRequest(final int flags) {
		return flags == 4;
	}

	public boolean isRequest() {
		return isRequest(flags);
	}

	private static boolean isResponse(final int flags) {
		return flags == 5;
	}

	public boolean isResponse() {
		return isResponse(flags);
	}

	public boolean isNotification() {
		return command == 8;
	}

	public ErrorCode getErrorCode() {
		return ErrorCode.valueOf(errorCode);
	}

	public boolean isError() {
		return getErrorCode().isError();
	}

	public boolean isResponseTo(final Packet packet) {
		return packet != null && packet.isRequest() && isResponse()
				&& packet.id == id
				&& peerPair.isInverseDirection(packet.peerPair);
	}

	public byte[] toBinary() {
		byte[] payloadData = payload.toBinary();
		byte[] ret = new byte[6 + 32 + payloadData.length];
		Toolbox.copyIntoByteArray(ret, 2, Toolbox
				.uint32ToBytes(32 + payloadData.length));
		Toolbox.copyIntoByteArray(ret, 6, peerPair.toBinary());
		Toolbox.copyIntoByteArray(ret, 22, Toolbox.uint16ToBytes(command));
		Toolbox.copyIntoByteArray(ret, 24, Toolbox.uint16ToBytes(flags));
		Toolbox.copyIntoByteArray(ret, 26, Toolbox
				.uint32ToBytes(payloadData.length));
		Toolbox.copyIntoByteArray(ret, 30, Toolbox.uint32ToBytes(errorCode));
		Toolbox.copyIntoByteArray(ret, 34, Toolbox.uint32ToBytes(id));
		Toolbox.copyIntoByteArray(ret, 38, payloadData);
		return ret;
	}

	public Payload getAdsPayload() {
		return payload;
	}

	@Override
	public String toString() {
		return String.format(
				"AmsPacket: %s, ErrorCode: %d, Id: %d, Payload: %s", peerPair
						.toString(), errorCode, id, payload.toString());
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + command;
		result = prime * result + flags;
		result = prime * result + (int) (errorCode ^ (errorCode >>> 32));
		result = prime * result + (int) (id ^ (id >>> 32));
		result = prime * result + ((payload == null) ? 0 : payload.hashCode());
		result = prime * result
				+ ((peerPair == null) ? 0 : peerPair.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof Packet))
			return false;
		Packet other = (Packet) obj;
		if (command != other.command)
			return false;
		if (flags != other.flags)
			return false;
		if (errorCode != other.errorCode)
			return false;
		if (id != other.id)
			return false;
		if (payload == null) {
			if (other.payload != null)
				return false;
		} else if (!payload.equals(other.payload))
			return false;
		if (peerPair == null) {
			if (other.peerPair != null)
				return false;
		} else if (!peerPair.equals(other.peerPair))
			return false;
		return true;
	}
}
