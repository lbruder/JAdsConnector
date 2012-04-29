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

import java.util.*;

import org.junit.*;
import org.lb.plc.tpy.*;

public class TestVariableExpander {
	private VariableExpander expander;

	@Before
	public void setUp() throws Exception {
		TypeInformationContainer fakeTypeContainer = new TypeInformationContainer() {
			private Map<String, Type> types = null;
			private List<Variable> variables = null;

			@Override
			public Map<String, Type> getTypes() {
				if (types == null) {
					types = new HashMap<String, Type>();
				}
				return types;
			}

			@Override
			public List<Variable> getVariables() {
				if (variables == null) {
					variables = new ArrayList<Variable>();
					variables.add(new Variable("string80Var", "STRING(80)",
							1234, 4711, 81 * 8));
				}
				return variables;
			}
		};
		this.expander = new VariableExpander(fakeTypeContainer);
	}

	@After
	public void tearDown() throws Exception {
	}

	private Variable getVariableByName(final String name) throws Exception {
		for (Variable var : expander.getVariables())
			if (var.name.equals(name))
				return var;
		throw new Exception("Variable not found");
	}

	@Test
	public void testByteSize() throws Exception {
		long expected = 81 * 8;
		long actual = getVariableByName("string80Var").bitSize;
		Assert.assertEquals(expected, actual);
	}
}
