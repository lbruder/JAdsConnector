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

public class OneDimensionalArrayType extends Type {
	public final long lowerBound;
	public final long upperBound;

	public OneDimensionalArrayType(final String name, final long bitSize,
			final String type, final long lowerBound, final long upperbound) {
		super(name, type, bitSize);
		this.lowerBound = lowerBound;
		this.upperBound = upperbound;
		ensureValid();
	}

	private void ensureValid() {
		if (lowerBound < 0)
			throw new IllegalArgumentException("Invalid lower bound: "
					+ lowerBound);
		if (upperBound < 0)
			throw new IllegalArgumentException("Invalid upper bound: "
					+ upperBound);
		if (lowerBound > upperBound)
			throw new IllegalArgumentException(String.format(
					"Upper bound (%d) must be >= lower bound (%d)", upperBound,
					lowerBound));
	}
}
