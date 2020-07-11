package org.github.jaraya81;

import lombok.extern.slf4j.Slf4j;
import org.github.jaraya81.bot.TaskCallable;
import org.github.jaraya81.enums.Parameter;
import org.github.jaraya81.exception.BotException;
import org.github.jaraya81.service.DataService;
import org.github.jaraya81.util.YamlUtil;
import org.github.jaraya81.vo.Config;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static org.github.jaraya81.util.ThreadUtil.sleep;

/**
 * Hello world!
 */
@Slf4j
public class App {

    ExecutorService executorService;
    DataService dataService;

    public static void main(String[] args) throws BotException {
        new App().exec(args != null && args.length == 1 ? args[0] : null);
    }

    private void exec(String pathConfig) throws BotException {
        log.info("Iniciando ejecución de robots...");

        log.info("Leyendo configuración...");
        Config config;
        try (InputStream is = new FileInputStream(pathConfig)) {
            config = YamlUtil.get(is, Config.builder().build());
        } catch (IOException e) {
            throw new BotException("Error leyendo archivo de configuración");
        }
        log.info("Hilos a desplegar: " + config.getThreads());
        executorService = Executors.newFixedThreadPool(config.getThreads());
        dataService = new DataService();

        Map<String, Map<String, String>> paramss = new HashMap<>();
        paramss.put("bot", getParams(config));

        log.info("Corriendo...");
        Map<String, TaskCallable> tasks = new HashMap<>();
        Map<String, Future<String>> futures = new HashMap<>();
        do {
            for (Map.Entry<String, Map<String, String>> entry : paramss.entrySet()) {
                if (tasks.get(entry.getKey()) == null) {
                    TaskCallable taskCallable = new TaskCallable(entry.getValue());
                    taskCallable.addDataService(dataService);
                    tasks.put(entry.getKey(), taskCallable);
                    futures.put(entry.getKey(), executorService.submit(tasks.get(entry.getKey())));
                    sleep(5, 10);
                }
                if (futures.get(entry.getKey()) != null && futures.get(entry.getKey()).isDone()) {
                    tasks.remove(entry.getKey());
                    futures.remove(entry.getKey());
                }
            }
            sleep(10, 10);
        } while (Boolean.TRUE);


    }

    private Map<String, String> getParams(Config config) {
        Map<String, String> params = new HashMap<>();
        params.put(Parameter.HEADLESS, config.getHeadless().toString());
        params.put(Parameter.TYPE_BROWSER, config.getTypeBrowser());
        params.put(Parameter.USER, config.getUser());
        params.put(Parameter.PASSWORD, config.getPassword());
        params.put(Parameter.DEVICE_NAME, config.getDeviceName());
        params.put(Parameter.PROXY_ENABLED, config.getProxy().getEnabled().toString());
        params.put(Parameter.PROXY_URL, config.getProxy().getUrl());

        params.put(Parameter.WORKING_PATH, config.getPath());
        return params;
    }
}
