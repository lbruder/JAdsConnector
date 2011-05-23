package org.lb.plc.tpy;

public final class StructItem extends Type {
	public final long bitOffset;

	public StructItem(final String name, final String type,
			final long bitSize, final long bitOffset) {
		super(name, type, bitSize);
		this.bitOffset = bitOffset;
		ensureValid();
	}

	private void ensureValid() {
		if (bitOffset < 0)
			throw new IllegalArgumentException("Invalid bit offset: "
					+ bitOffset);
	}
}
