package com.jstarcraft.ai.data.exception;

/**
 * 数据异常
 * 
 * @author Birdy
 */
public class DataException extends RuntimeException {

    private static final long serialVersionUID = 2030356415654144813L;

    public DataException() {
        super();
    }

    public DataException(String message, Throwable exception) {
        super(message, exception);
    }

    public DataException(String message) {
        super(message);
    }

    public DataException(Throwable exception) {
        super(exception);
    }

}
