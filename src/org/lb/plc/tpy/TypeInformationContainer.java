package org.lb.plc.tpy;

import java.util.List;
import java.util.Map;

public interface TypeInformationContainer {
	public Map<String, Type> getTypes();

	public List<Variable> getVariables();
}