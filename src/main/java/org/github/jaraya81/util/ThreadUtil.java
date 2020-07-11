package org.github.jaraya81.util;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ThreadUtil {

    public static void sleep(int seconds) {
        try {
            log.info("sleep :: " + seconds + " seconds");
            Thread.sleep(seconds * 1000);
        } catch (InterruptedException e) {
            log.error("", e);
        }
    }

    public static void sleep(int seconds, int plusRandom) {
        sleep(seconds + (plusRandom * RandomUtil.get()) / RandomUtil.MAX);
    }
}
