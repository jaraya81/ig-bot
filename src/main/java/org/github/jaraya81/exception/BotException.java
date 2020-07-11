package org.github.jaraya81.exception;

public class BotException extends Exception {

    public BotException(String reason) {
        super(reason);
    }

    public BotException(String reason, Throwable e) {
        super(reason, e);
    }

    public BotException() {
        super();
    }
}
