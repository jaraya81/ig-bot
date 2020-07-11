package org.github.jaraya81.bot;

import lombok.extern.slf4j.Slf4j;
import org.github.jaraya81.enums.Parameter;
import org.github.jaraya81.service.DataService;

import java.util.Map;
import java.util.concurrent.Callable;

@Slf4j
public class TaskCallable implements Callable<String> {

    private final Map<String, String> params;

    private DataService ds;

    public TaskCallable(Map<String, String> params) {
        super();
        this.params = params;
    }

    public void addDataService(DataService ds) {
        this.ds = ds;
    }

    @Override
    public String call() {
        int veces = 0;
        try {
            IGBot igBot = IGBotImpl.getInstance(params)
                    .addDataService(ds)
                    .init()
                    .open()
                    .login()
                    .follow()
                    .likes()
                    .unfollow()
                    .unlikes()
                    .close()
                    .pause(2, 2)
                    .end();
        } catch (Throwable e) {
            log.error("", e);
        }
        return params.get(Parameter.USER);
    }

}
