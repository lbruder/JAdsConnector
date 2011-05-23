package org.lb.plc.ams;

public class AmsException extends Exception {
	private final ErrorCode errorCode;

	public AmsException(final ErrorCode errorCode) {
		this.errorCode = errorCode;
	}

	public ErrorCode getErrorCode() {
		return this.errorCode;
	}

	@Override
	public String getMessage() {
		return errorCode.toString();
	}
}
