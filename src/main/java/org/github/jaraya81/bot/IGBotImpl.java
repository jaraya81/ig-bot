package org.github.jaraya81.bot;

import lombok.extern.slf4j.Slf4j;
import org.github.jaraya81.exception.BotException;
import org.github.jaraya81.service.DataService;
import org.github.jaraya81.util.ThreadUtil;

import java.util.Map;

@Slf4j
public class IGBotImpl implements IGBot {

    private Map<String, String> params;

    private IGBotImpl(Map<String, String> params) {
        this.params = params;
    }

    public static IGBot getInstance(Map<String, String> params) {
        return new IGBotImpl(params);
    }

    @Override
    public IGBot addDataService(DataService ds) throws BotException {
        log.info("addDataService :: " + ds.toString());
        return this;
    }

    @Override
    public IGBot init() throws BotException {
        log.info("init");
        return this;
    }

    @Override
    public IGBot open() throws BotException {
        log.info("open");
        return this;
    }

    @Override
    public IGBot login() throws BotException {
        log.info("login");
        return this;
    }

    @Override
    public IGBot follow() throws BotException {
        log.info("follow");
        return this;
    }

    @Override
    public IGBot likes() throws BotException {
        log.info("likes");
        return this;
    }

    @Override
    public IGBot unfollow() throws BotException {
        log.info("unfollow");
        return this;
    }

    @Override
    public IGBot unlikes() throws BotException {
        log.info("unlikes");
        return this;
    }

    @Override
    public IGBot close() throws BotException {
        log.info("close");
        return this;
    }

    @Override
    public IGBot end() throws BotException {
        log.info("end");
        return this;
    }

    @Override
    public IGBot pause(int seconds) throws BotException {
        ThreadUtil.sleep(seconds);
        return this;
    }

    @Override
    public IGBot pause(int seconds, int plusRandom) {
        ThreadUtil.sleep(seconds, plusRandom);
        return this;
    }
}
