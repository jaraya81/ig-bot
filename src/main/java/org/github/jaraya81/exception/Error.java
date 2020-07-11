package org.github.jaraya81.exception;

public class Error {

    public static void throwIt() throws BotException {
        throw new BotException();
    }

    public static void throwIt(String reason) throws BotException {
        throw new BotException(reason);
    }

    public static void throwIt(String reason, Throwable e) throws BotException {
        throw new BotException(reason, e);
    }

    public static BotException throwEx(String reason) {
        try {
            throwIt(reason);
        } catch (BotException e) {
            return e;
        }
        return null;
    }
}
