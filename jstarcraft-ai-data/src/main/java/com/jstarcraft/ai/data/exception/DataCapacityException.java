package com.jstarcraft.ai.data.exception;

/**
 * 数据容量异常
 * 
 * @author Birdy
 */
public class DataCapacityException extends DataException {

    private static final long serialVersionUID = -9091488005211313843L;

    public DataCapacityException() {
        super();
    }

    public DataCapacityException(String message, Throwable exception) {
        super(message, exception);
    }

    public DataCapacityException(String message) {
        super(message);
    }

    public DataCapacityException(Throwable exception) {
        super(exception);
    }

}
