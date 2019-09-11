package com.jstarcraft.ai.data.exception;

/**
 * 数据游标异常
 * 
 * @author Birdy
 */
public class DataCursorException extends DataException {

    private static final long serialVersionUID = -2943490986625047861L;

    public DataCursorException() {
        super();
    }

    public DataCursorException(String message, Throwable exception) {
        super(message, exception);
    }

    public DataCursorException(String message) {
        super(message);
    }

    public DataCursorException(Throwable exception) {
        super(exception);
    }

}
