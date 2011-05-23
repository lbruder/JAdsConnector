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


import java.io.*;
import java.util.*;
import javax.xml.parsers.*;
import org.w3c.dom.*;

public class TpyFile {
	public final Map<String, Type> types;
	public final List<Variable> variables;

	public TpyFile(final String fileName) throws TpyException {
		try {
			final Document doc = parseFile(fileName);
			this.types = getTypes(doc);
			this.variables = getVariables(doc);
		} catch (TpyException ex) {
			throw ex;
		} catch (NumberFormatException ex) {
			throw new TpyException("Invalid numeric value in TPY file: "
					+ ex.getMessage());
		} catch (IllegalArgumentException ex) {
			throw new TpyException("Invalid TPY file: " + ex.getMessage());
		} catch (Exception ex) {
			throw new TpyException("Internal error opening TPY file: "
					+ ex.getMessage());
		}
	}

	private Document parseFile(final String fileName) throws TpyException {
		try {
			return DocumentBuilderFactory.newInstance().newDocumentBuilder()
					.parse(new File(fileName));
		} catch (IOException ex) {
			throw new TpyException("Error reading TPY file");
		} catch (Exception ex) {
			throw new TpyException("Error parsing TPY file");
		}
	}

	private Map<String, Type> getTypes(final Document doc)
			throws TpyException {
		final Map<String, Type> ret = new HashMap<String, Type>();

		final Node root = getSingleChildNodeByName(doc, "PlcProjectInfo");
		final Node dataTypes = getSingleChildNodeByName(root, "DataTypes");
		final List<Node> listOfDataType = getChildNodesByName(dataTypes,
				"DataType");

		for (final Node dataType : listOfDataType) {
			final String name = getTextOfChildNodeByName(dataType, "Name");
			final String bitSize = getTextOfChildNodeByName(dataType, "BitSize");
			List<Node> subItems = getChildNodesByName(dataType, "SubItem");
			List<Node> arrayInfo = getChildNodesByName(dataType, "ArrayInfo");

			if (subItems.size() > 0) {
				ret.put(name, makeStructType(name, bitSize, subItems));
			} else if (arrayInfo.size() > 0) {
				final String type = getTextOfChildNodeByName(dataType, "Type");
				if (arrayInfo.size() == 1)
					ret.put(name, makeOneDimensionalArrayType(name, bitSize,
							type, arrayInfo.get(0)));
				else if (arrayInfo.size() == 2)
					ret.put(name, makeTwoDimensionalArrayType(name, bitSize,
							type, arrayInfo.get(0), arrayInfo.get(1)));
				else
					throw new IllegalArgumentException(
							"Arrays with more than two dimensions not supported");
			} else {
				final String type = getTextOfChildNodeByName(dataType, "Type");
				ret.put(name, new Type(name, type, Long.valueOf(bitSize)));
			}
		}

		return ret;
	}

	private Type makeStructType(final String name, final String bitSize,
			final List<Node> subItems) throws TpyException {
		final List<StructItem> structItems = new LinkedList<StructItem>();
		for (final Node subItem : subItems) {
			final String subName = getTextOfChildNodeByName(subItem, "Name");
			final String subType = getTextOfChildNodeByName(subItem, "Type");
			final String subBitSize = getTextOfChildNodeByName(subItem,
					"BitSize");
			final String subBitOffs = getTextOfChildNodeByName(subItem,
					"BitOffs");
			structItems.add(new StructItem(subName, subType, Long
					.valueOf(subBitSize), Long.valueOf(subBitOffs)));
		}
		return new StructType(name, Long.valueOf(bitSize), structItems);
	}

	private Type makeOneDimensionalArrayType(final String name,
			final String bitSize, final String type, final Node arrayInfo)
			throws TpyException {
		final long lowerBound = getLowerBoundFromNode(arrayInfo);
		final long upperBound = getUpperBoundFromNode(arrayInfo, lowerBound);
		return new OneDimensionalArrayType(name, Long.valueOf(bitSize),
				type, lowerBound, upperBound);
	}

	private Type makeTwoDimensionalArrayType(final String name,
			final String bitSize, final String type, final Node arrayInfo1,
			final Node arrayInfo2) throws TpyException {
		final long lowerBound1 = getLowerBoundFromNode(arrayInfo1);
		final long lowerBound2 = getLowerBoundFromNode(arrayInfo2);
		final long upperBound1 = getUpperBoundFromNode(arrayInfo1, lowerBound1);
		final long upperBound2 = getUpperBoundFromNode(arrayInfo2, lowerBound2);
		return new TwoDimensionalArrayType(name, Long.valueOf(bitSize),
				type, lowerBound1, upperBound1, lowerBound2, upperBound2);
	}

	private long getLowerBoundFromNode(final Node arrayInfo)
			throws TpyException {
		final String lBound = getTextOfChildNodeByName(arrayInfo, "LBound");
		return Long.valueOf(lBound);
	}

	private long getUpperBoundFromNode(final Node arrayInfo,
			final long lowerBound) throws TpyException {
		final String elements = getTextOfChildNodeByName(arrayInfo, "Elements");
		final long numberOfElements = Long.valueOf(elements);
		return lowerBound + numberOfElements - 1;
	}

	private List<Variable> getVariables(final Document doc)
			throws TpyException {
		final List<Variable> ret = new LinkedList<Variable>();

		final Node root = getSingleChildNodeByName(doc, "PlcProjectInfo");
		final Node symbols = getSingleChildNodeByName(root, "Symbols");
		final List<Node> listOfSymbol = getChildNodesByName(symbols, "Symbol");

		for (final Node symbol : listOfSymbol) {
			final String name = getTextOfChildNodeByName(symbol, "Name");
			final String type = getTextOfChildNodeByName(symbol, "Type");
			final String group = getTextOfChildNodeByName(symbol, "IGroup");
			final String offset = getTextOfChildNodeByName(symbol, "IOffset");
			final String bitSize = getTextOfChildNodeByName(symbol, "BitSize");
			ret.add(new Variable(name, type, Long.valueOf(group), Long
					.valueOf(offset), Long.valueOf(bitSize)));
		}

		return ret;
	}

	private Node getSingleChildNodeByName(final Node node, final String name)
			throws TpyException {
		final List<Node> nodesWithThatName = getChildNodesByName(node, name);
		assertExactlyOneNode(nodesWithThatName);
		return nodesWithThatName.get(0);
	}

	private void assertExactlyOneNode(final List<Node> nodes)
			throws TpyException {
		final String nodeName = nodes.get(0).getNodeName();
		if (nodes.size() == 0)
			throw new TpyException("Invalid TPY file: No <" + nodeName
					+ "> node found");
		if (nodes.size() != 1)
			throw new TpyException("Invalid TPY file: Multiple <" + nodeName
					+ "> nodes found");
	}

	private List<Node> getChildNodesByName(final Node node, final String name) {
		final List<Node> ret = new LinkedList<Node>();
		final NodeList children = node.getChildNodes();
		for (int i = 0; i < children.getLength(); ++i)
			if (children.item(i).getNodeName().equals(name))
				ret.add(children.item(i));
		return ret;
	}

	private String getTextOfChildNodeByName(final Node node, final String name)
			throws TpyException {
		return getSingleChildNodeByName(node, name).getTextContent();
	}
}
