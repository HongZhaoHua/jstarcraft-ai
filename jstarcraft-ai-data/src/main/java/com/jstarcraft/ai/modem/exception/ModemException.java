package com.jstarcraft.ai.modem.exception;

/**
 * 调制解调异常
 * 
 * @author Birdy
 */
public class ModemException extends RuntimeException {

    private static final long serialVersionUID = 2749606111423473488L;

    public ModemException() {
        super();
    }

    public ModemException(String message, Throwable exception) {
        super(message, exception);
    }

    public ModemException(String message) {
        super(message);
    }

    public ModemException(Throwable exception) {
        super(exception);
    }

}
