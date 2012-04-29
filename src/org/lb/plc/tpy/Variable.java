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

package org.lb.plc.tpy;

import org.lb.plc.Toolbox;

public class Variable {
	public final String name;
	public final String type;
	public final long group;
	public final long offset;
	public final long bitSize;

	public Variable(final String name, final String type, final long group,
			final long offset, final long bitSize) {
		this.name = name;
		this.type = type;
		this.group = group;
		this.offset = offset;
		this.bitSize = bitSize;
		ensureValid();
	}

	private void ensureValid() {
		if (name == null || name.isEmpty())
			throw new IllegalArgumentException("Invalid name");
		if (type == null || type.isEmpty())
			throw new IllegalArgumentException("Invalid type");
		if (group < 0)
			throw new IllegalArgumentException("Invalid group: " + group);
		if (offset < 0)
			throw new IllegalArgumentException("Invalid offset: " + offset);
		if (bitSize <= 0)
			throw new IllegalArgumentException("Invalid bitSize: " + bitSize);
	}

	public boolean isSigned() {
		if (type.equals("SINT"))
			return true;
		if (type.equals("INT"))
			return true;
		if (type.equals("DINT"))
			return true;
		return false;
	}

	public boolean isIntegral() {
		if (type.equals("BOOL"))
			return true;
		if (type.equals("BYTE"))
			return true;
		if (type.equals("USINT"))
			return true;
		if (type.equals("SINT"))
			return true;
		if (type.equals("INT"))
			return true;
		if (type.equals("UINT"))
			return true;
		if (type.equals("WORD"))
			return true;
		if (type.equals("DWORD"))
			return true;
		if (type.equals("DINT"))
			return true;
		if (type.equals("UDINT"))
			return true;
		if (type.equals("TIME"))
			return true;
		return false;
	}

	public boolean isFloatingPoint() {
		if (type.equals("REAL"))
			return true;
		if (type.equals("LREAL"))
			return true;
		return false;
	}

	public boolean isString() {
		return type.startsWith("STRING");
	}

	@Override
	public String toString() {
		return String.format("{'%s': '%s' Group: %d Offset: %d BitSize: %d}",
				name, type, group, offset, bitSize);
	}

	public Object decode(byte[] bytes) throws TpyException {
		if (isString())
			return new String(bytes);

		if (isFloatingPoint())
			return Toolbox.bytesToDouble(bytes);

		if (isIntegral()) {
			if (isSigned())
				return Toolbox.bytesToSignedInteger(bytes);
			return Toolbox.bytesToUnsignedInteger(bytes);
		}

		throw new TpyException("Unable to decode variable of type " + type);
	}
}
