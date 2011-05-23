package org.lb.plc.tpy;

import java.util.List;

public final class StructType extends Type {
	public final List<StructItem> subItems;

	public StructType(final String name, final long bitSize,
			final List<StructItem> subItems) {
		super(name, "STRUCT", bitSize);
		this.subItems = subItems;
	}
}
