package com.blackcrystalinfo.push.exception;

public class PushReceiverException extends PushException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3707090329055792019L;

	public PushReceiverException() {
		super();
	}

	public PushReceiverException(String msg) {
		super(msg);
	}

	public PushReceiverException(Throwable cause) {
		super(cause);
	}

	public PushReceiverException(String msg, Throwable cause) {
		super(msg, cause);
	}

}
