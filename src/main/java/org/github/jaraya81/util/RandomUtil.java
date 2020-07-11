package org.github.jaraya81.util;

import java.util.concurrent.ThreadLocalRandom;

public class RandomUtil {

    public static final int MAX = 20;
    public static final int MIN = 0;

    public static int get() {
        return ThreadLocalRandom.current().nextInt(MIN, MAX);
    }
}
