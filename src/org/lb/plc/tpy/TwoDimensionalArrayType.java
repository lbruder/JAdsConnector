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

public class TwoDimensionalArrayType extends Type {
	public final long lowerBoundDimension1;
	public final long upperBoundDimension1;
	public final long lowerBoundDimension2;
	public final long upperBoundDimension2;

	public TwoDimensionalArrayType(final String name, final long bitSize,
			final String type, final long lowerBoundDimension1,
			final long upperboundDimension1, final long lowerBoundDimension2,
			final long upperboundDimension2) {
		super(name, type, bitSize);
		this.lowerBoundDimension1 = lowerBoundDimension1;
		this.upperBoundDimension1 = upperboundDimension1;
		this.lowerBoundDimension2 = lowerBoundDimension2;
		this.upperBoundDimension2 = upperboundDimension2;
		ensureValid();
	}

	private void ensureValid() {
		if (lowerBoundDimension1 < 0)
			throw new IllegalArgumentException(
					"Invalid lower bound in first dimension: "
							+ lowerBoundDimension1);
		if (upperBoundDimension1 < 0)
			throw new IllegalArgumentException(
					"Invalid upper bound in first dimension: "
							+ upperBoundDimension1);
		if (lowerBoundDimension1 > upperBoundDimension1)
			throw new IllegalArgumentException(String.format(
					"Upper bound (%d) must be >= lower "
							+ "bound (%d) in first dimension",
					upperBoundDimension1, lowerBoundDimension1));
		if (lowerBoundDimension2 < 0)
			throw new IllegalArgumentException(
					"Invalid lower bound in second dimension: "
							+ lowerBoundDimension2);
		if (upperBoundDimension2 < 0)
			throw new IllegalArgumentException(
					"Invalid upper bound in second dimension: "
							+ upperBoundDimension2);
		if (lowerBoundDimension2 > upperBoundDimension2)
			throw new IllegalArgumentException(String.format(
					"Upper bound (%d) must be >= lower "
							+ "bound (%d) in second dimension",
					upperBoundDimension2, lowerBoundDimension2));
	}
}
