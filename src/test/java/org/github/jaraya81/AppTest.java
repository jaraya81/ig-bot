package org.github.jaraya81;

import lombok.extern.slf4j.Slf4j;
import org.github.jaraya81.util.RandomUtil;
import org.junit.Test;

@Slf4j
public class AppTest 
{
    @Test
    public void random() {
        log.info("" + RandomUtil.get());
        log.info("" + RandomUtil.get());
        log.info("" + RandomUtil.get());
        log.info("" + RandomUtil.get());
        log.info("" + RandomUtil.get());
        log.info("" + RandomUtil.get());
    }
}
