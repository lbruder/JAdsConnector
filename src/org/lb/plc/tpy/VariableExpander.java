// Copyright (c) 2011, Leif Bruder <leifbruder@googlemail.com>
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


import java.util.*;

public final class VariableExpander {
	private final TpyFile tpyFile;
	private List<Variable> variables;

	public VariableExpander(final TpyFile tpyFile) throws TpyException {
		this.tpyFile = tpyFile;
		this.variables = new LinkedList<Variable>();
		populateVariableList();
	}

	private void populateVariableList() throws TpyException {
		for (final Variable var : tpyFile.variables)
			addSubVariables(var);
	}

	private void addSubVariables(final Variable var) throws TpyException {
		if (getByteSizeFromName(var.type) > 0)
			variables.add(var);
		else
			addSubVariablesForComplexType(var, getTypeFromName(var.type));
	}

	private void addSubVariablesForComplexType(final Variable var,
			final Type type) throws TpyException {
		if (type instanceof StructType) {
			for (final StructItem subItem : ((StructType) type).subItems) {
				final String subName = var.name + "." + subItem.name;
				final long subOffset = var.offset + subItem.bitOffset / 8;
				addSubVariables(new Variable(subName, subItem.type,
						var.group, subOffset, subItem.bitSize));
			}
		} else if (type instanceof OneDimensionalArrayType) {
			final OneDimensionalArrayType arrayType = (OneDimensionalArrayType) type;
			final long subTypeBitSizeFromName = 8 * getByteSizeFromName(arrayType.type);
			final long subBitSize = (subTypeBitSizeFromName > 0) ? subTypeBitSizeFromName
					: getTypeFromName(arrayType.type).bitSize;
			final long subByteSize = subBitSize / 8;
			for (long i = arrayType.lowerBound; i <= arrayType.upperBound; ++i) {
				final String subName = var.name + "[" + i + "]";
				final long subOffset = var.offset + i * subByteSize;
				addSubVariables(new Variable(subName, arrayType.type,
						var.group, subOffset, subBitSize));
			}
		} else if (type instanceof TwoDimensionalArrayType) {
			// TODO
		} else {
			variables.add(var); // Enum -> INT
		}
	}

	private Type getTypeFromName(final String name) throws TpyException {
		final Type type = tpyFile.types.get(name);
		if (type == null)
			throw new TpyException("Unknown type: '" + name + "'");
		return type;
	}

	private long getByteSizeFromName(final String name) {
		if (name.equals("BOOL"))
			return 1;
		if (name.equals("BYTE"))
			return 1;
		if (name.equals("USINT"))
			return 1;
		if (name.equals("INT"))
			return 2;
		if (name.equals("UINT"))
			return 2;
		if (name.equals("WORD"))
			return 2;
		if (name.equals("DWORD"))
			return 4;
		if (name.equals("DINT"))
			return 4;
		if (name.equals("UDINT"))
			return 4;
		if (name.equals("REAL"))
			return 4;
		if (name.equals("TIME"))
			return 4;
		if (name.equals("LREAL"))
			return 8;
		if (name.startsWith("STRING(")) {
			final String withClosingParen = name.substring(7);
			final String sizeAsString = withClosingParen.substring(0,
					withClosingParen.length() - 2);
			return Long.valueOf(sizeAsString) + 1;
		}
		return 0;
	}

	public List<Variable> getVariables() {
		return Collections.unmodifiableList(variables);
	}
}
