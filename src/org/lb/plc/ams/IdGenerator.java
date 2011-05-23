package org.lb.plc.ams;

public class IdGenerator {
	private int nextId = 1;

	public synchronized int getNextId() {
		return nextId++;
	}
}
