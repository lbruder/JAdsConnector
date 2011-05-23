package org.lb.plc.tpy;

public class Type {
	public final String name;
	public final String type;
	public final long bitSize;

	public Type(final String name, final String type, final long bitSize) {
		this.name = name;
		this.type = type;
		this.bitSize = bitSize;
		ensureValid();
	}

	private void ensureValid() {
		if (bitSize <= 0)
			throw new IllegalArgumentException("Invalid bitSize: " + bitSize);
	}
}
