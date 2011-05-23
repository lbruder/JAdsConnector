package org.lb.plc.tpy;

import java.util.*;

public class VariableLocator {
	final Map<String, Variable> variables;
	final List<String> names;

	public VariableLocator(final List<Variable> variables) {
		this.variables = new HashMap<String, Variable>();
		this.names = new ArrayList<String>(variables.size());
		for (final Variable var : variables) {
			this.variables.put(var.name, var);
			this.names.add(var.name);
		}
		Collections.sort(this.names);
	}

	public List<String> getVariableNames() {
		return Collections.unmodifiableList(names);
	}

	public Variable getVariableByName(final String name) throws TpyException {
		final Variable ret = variables.get(name);
		if (ret == null)
			throw new TpyException("Variable not found: '" + name + "'");
		return ret;
	}
}
