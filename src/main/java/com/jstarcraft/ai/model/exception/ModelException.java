package com.jstarcraft.ai.model.exception;

/**
 * 模型异常
 * 
 * @author Birdy
 */
public class ModelException extends RuntimeException {

	private static final long serialVersionUID = 2749606111423473488L;

	public ModelException() {
		super();
	}

	public ModelException(String message, Throwable exception) {
		super(message, exception);
	}

	public ModelException(String message) {
		super(message);
	}

	public ModelException(Throwable exception) {
		super(exception);
	}

}
