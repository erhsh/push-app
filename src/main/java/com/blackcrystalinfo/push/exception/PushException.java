package com.blackcrystalinfo.push.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PushException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7885108278329726759L;

	protected static final Logger logger = LoggerFactory
			.getLogger(PushException.class);

	public PushException() {
		super();
	}

	public PushException(String msg) {
		super(msg);
	}

	public PushException(Throwable cause) {
		super(cause);
	}

	public PushException(String msg, Throwable cause) {
		super(msg, cause);
	}
}
