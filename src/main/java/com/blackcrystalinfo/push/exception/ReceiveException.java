package com.blackcrystalinfo.push.exception;

public class ReceiveException extends PushException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3707090329055792019L;

	public ReceiveException() {
		super();
	}

	public ReceiveException(String msg) {
		super(msg);
	}

	public ReceiveException(Throwable cause) {
		super(cause);
	}

	public ReceiveException(String msg, Throwable cause) {
		super(msg, cause);
	}
	
	public static void main(String[] args) {
		try {
			throw new ReceiveException(new Exception());
		} catch (ReceiveException e) {
			logger.info("Recoad to Log: ", e);
		}
	}
}
