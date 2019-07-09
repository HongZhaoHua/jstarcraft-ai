package com.jstarcraft.ai.search.exception;

/**
 * 检索异常
 * 
 * @author Birdy
 */
public class RetrievalException extends RuntimeException {

    private static final long serialVersionUID = 4344432216739386116L;

    public RetrievalException() {
        super();
    }

    public RetrievalException(String message, Throwable exception) {
        super(message, exception);
    }

    public RetrievalException(String message) {
        super(message);
    }

    public RetrievalException(Throwable exception) {
        super(exception);
    }

}
