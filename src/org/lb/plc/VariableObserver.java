package org.lb.plc;

public interface VariableObserver {
	void variableChanged(final String variable, final Object newValue);
}
