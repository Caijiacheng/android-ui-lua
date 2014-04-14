package org.snail.robot.engine;

public class ScriptException extends Exception {

	private static final long serialVersionUID = 3527678517900156378L;

    public ScriptException() {
    }

    public ScriptException(String detailMessage) {
        super(detailMessage);
    }

    public ScriptException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }
    
    public ScriptException(Throwable throwable) {
        super(throwable);
    }
}
