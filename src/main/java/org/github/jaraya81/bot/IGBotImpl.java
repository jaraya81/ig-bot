package org.github.jaraya81.bot;

import lombok.extern.slf4j.Slf4j;
import org.github.jaraya81.enums.Parameter;
import org.github.jaraya81.exception.BotException;
import org.github.jaraya81.helper.HelperBot;
import org.github.jaraya81.service.DataService;
import org.github.jaraya81.util.FileUtil;
import org.github.jaraya81.util.ThreadUtil;

import java.io.File;
import java.util.Map;
import java.util.UUID;

import static org.github.jaraya81.exception.Error.throwIt;

@Slf4j
public class IGBotImpl implements IGBot {

    private static final String IG_URL = "http://instagram.com";
    private File workingDir;
    private HelperBot helperBot = new HelperBot();
    private Map<String, String> params;
    private DataService ds;

    private IGBotImpl(Map<String, String> params) {
        this.params = params;
    }

    public static IGBot getInstance(Map<String, String> params) {
        return new IGBotImpl(params);
    }

    @Override
    public IGBot addDataService(DataService ds) throws BotException {
        this.ds = ds;
        return this;
    }

    @Override
    public IGBot init() throws BotException {
        log.info("init");

        helperBot.checkParams(params);
        workingDir = new File(
                params.get(Parameter.WORKING_PATH) + FileUtil.FILE_SEPARATOR +
                        params.get(Parameter.USER).replaceAll("@", "_") + FileUtil.FILE_SEPARATOR +
                        UUID.randomUUID().toString()
        );
        FileUtil.mkdirs(workingDir.getAbsolutePath());
        helperBot.setHeadless(params.get(Parameter.HEADLESS));
        if (params.get(Parameter.TYPE_BROWSER).contentEquals("HTML_UNIT")) {
            helperBot.setBrowserHtmlUnit();
        } else if (params.get(Parameter.TYPE_BROWSER).contentEquals("CHROME")) {
            helperBot.setBrowseChrome(workingDir, false, false);
        } else {
            throwIt("TYPE_BROWSER not implemented :: " + params.get(Parameter.TYPE_BROWSER));
        }
        helperBot.setWorkingDir(workingDir);
        helperBot.setDataService(ds);
        return this;
    }

    @Override
    public IGBot open() throws BotException {
        helperBot.openWeb(IG_URL);
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
        helperBot.close();
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
