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
