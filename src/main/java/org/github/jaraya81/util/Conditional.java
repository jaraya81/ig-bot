package org.github.jaraya81.util;

import org.github.jaraya81.exception.BotException;

import static org.github.jaraya81.exception.Error.throwIt;

public class Conditional {

    public static <T> T elvis(T value, T valueIfNull) {
        return value != null ? value : valueIfNull;
    }

    public static void check(boolean conditional, String reason) throws BotException {
        if (!conditional) {
            throwIt(reason);
        }
    }

}
