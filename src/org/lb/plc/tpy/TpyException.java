package org.lb.plc.tpy;

public class TpyException extends Exception {
	private final String message;

	public TpyException(final String message) {
		this.message = message;
	}

	@Override
	public String getMessage() {
		return message;
	}
}
