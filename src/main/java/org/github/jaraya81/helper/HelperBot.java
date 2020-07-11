package org.github.jaraya81.helper;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import com.google.common.base.Strings;
import io.github.bonigarcia.wdm.config.DriverManagerType;
import io.github.bonigarcia.wdm.managers.ChromeDriverManager;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.github.jaraya81.exception.BotException;
import org.github.jaraya81.exception.Error;
import org.github.jaraya81.service.DataService;
import org.github.jaraya81.util.FileUtil;
import org.github.jaraya81.util.ThreadUtil;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static org.github.jaraya81.util.Conditional.check;

@Slf4j
public class HelperBot {
    protected int timeout = 60;

    protected WebDriver driver;
    protected boolean headless = false;
    protected boolean render = false;
    protected File workingDir;
    private DataService ds;
    protected String currentUri;

    public void checkParams(Map<String, String> params) throws BotException {
        check(params != null, "params null");
    }

    public void setHeadless(String headless) {
        setHeadless(headless != null ? Boolean.parseBoolean(headless.toLowerCase()) : this.headless);
    }

    public void setHeadless(boolean headless) {
        this.headless = headless;
    }

    public void setBrowserHtmlUnit() {
        render = false;
        driver = new HtmlUnitDriver(BrowserVersion.CHROME, true) {
            @Override
            protected WebClient modifyWebClient(WebClient client) {
                final WebClient webClient = super.modifyWebClient(client);
                webClient.getOptions().setJavaScriptEnabled(true);
                webClient.getOptions().setCssEnabled(true);
                webClient.getOptions().setThrowExceptionOnScriptError(false);
                webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
                webClient.getOptions().setScreenWidth(1366);
                webClient.getOptions().setScreenHeight(768);
                return webClient;
            }
        };
        driver = new HtmlUnitDriver(BrowserVersion.CHROME, true);
    }

    public void setBrowseChrome(File directory, boolean download, boolean profile) throws BotException {
        setChromeDriver();
        render = true;
        Map<String, Object> chromePrefs = new HashMap<>();
        chromePrefs.put("profile.default_content_settings.automatic_downloads.*.setting", 1);
        chromePrefs.put("profile.default_content_setting_values.automatic_downloads.*.setting", 1);
        chromePrefs.put("profile.default_content_settings.popups", 0);
        chromePrefs.put("profile.default_content_setting_values.popups", 0);

        ChromeOptions chromeOptions = new ChromeOptions();
        if (directory != null && download) {
            chromePrefs.put("download.default_directory", directory.getAbsolutePath());
        }
        if (directory != null && profile) {
            String userDataDir = directory.getParent() + "/" + "profile";
            FileUtil.mkdirs(userDataDir);
            log.debug("user-data-dir=" + userDataDir);
            chromeOptions.addArguments("user-data-dir=" + userDataDir);
        }
        chromeOptions.setExperimentalOption("prefs", chromePrefs);

        if (headless) {
            chromeOptions.addArguments("--headless", "--disable-gpu", "window-size=1366,768");
        }

        chromeOptions.addArguments("--lang=en");
        chromeOptions.addArguments("--disable-infobars");
        chromeOptions.addArguments("--disable-dev-shm-usage");

        driver = new ChromeDriver(chromeOptions);
    }

    protected void setChromeDriver() {
        setChromeDriver(null);
    }

    protected void setChromeDriver(String pathChromeDriver) {
        File chromeDriverDir = !Strings.isNullOrEmpty(pathChromeDriver) ? new File(pathChromeDriver) : null;
        if (chromeDriverDir != null && chromeDriverDir.exists()) {
            log.debug("ChromeDriver encontrado en: " + chromeDriverDir.getAbsolutePath());
            System.setProperty("webdriver.chrome.driver", chromeDriverDir.getAbsolutePath());
        } else {
            log.debug("No se encontro chromedriver en el mismo directorio, se intenta con ChromeDriverManager");
            ChromeDriverManager.getInstance(DriverManagerType.CHROME).setup();
        }
    }

    public void setWorkingDir(File workingDir) {
        this.workingDir = workingDir;
    }

    public void setDataService(DataService ds) {
        this.ds = ds;
    }

    public void openWeb(String url) throws BotException {
        Future<String> completableFuture = null;
        completableFuture = getURIAsync(url);
        LocalDateTime ldtExpiration = LocalDateTime.now(ZoneId.systemDefault()).plusSeconds(timeout);
        do {
            LocalDateTime now = LocalDateTime.now(ZoneId.systemDefault());
            log.debug("Quedan " + (ldtExpiration.atZone(ZoneId.systemDefault()).toEpochSecond()
                    - now.atZone(ZoneId.systemDefault()).toEpochSecond()) + " segundos");
            if (ldtExpiration.isBefore(now)) {
                log.debug("Tiempo de carga de pagina expiro: " + url);
                throwIt("TIMEOUT :: " + url);
            } else {
                pause(4);
                log.debug("Cargando pagina: " + url);
            }
        } while (!completableFuture.isDone());
    }

    protected Future<String> getURIAsync(String uri) throws BotException {
        check(!Strings.isNullOrEmpty(uri), "uri is null");
        currentUri = uri;
        log.info("To: " + uri);
        CompletableFuture<String> completableFuture = new CompletableFuture<>();

        Executors.newCachedThreadPool().submit(() -> {
            driver.get(currentUri);
            completableFuture.complete("Nav Complete: " + currentUri);
            return null;
        });

        return completableFuture;
    }

    public void pause(int seconds) throws BotException {
        ThreadUtil.sleep(seconds);
    }

    public void pause(int seconds, int plusRandom) {
        ThreadUtil.sleep(seconds, plusRandom);
    }

    public void close() {
        if (driver != null) {
            driver.close();
            driver.quit();
        }
    }

    public void throwIt(String reason) throws BotException {
        String id = UUID.randomUUID().toString();
        if (driver != null) {
            captureIt("errors" + "/" + id + ".png");
            driver.close();
            driver.quit();
        }
        Error.throwIt(reason + " :: " + id);
    }

    public void captureIt(String path) {
        if (driver != null && render) {
            try {
                File src = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
                FileUtil.mkdirs(new File(path).getParent());
                FileUtils.copyFile(src, new File(path));
            } catch (ClassCastException | IOException | WebDriverException | BotException e) {
                log.error("", e);
            }
        }
    }

    public void throwIt(String reason, Throwable e) throws BotException {
        String id = UUID.randomUUID().toString();
        if (driver != null) {
            captureIt("errors" + "/" + id + ".png");
            driver.close();
            driver.quit();
        }
        Error.throwIt(reason + " :: " + id, e);
    }

    public BotException throwEx(String reason) {
        try {
            throwIt(reason);
        } catch (BotException e) {
            return e;
        }
        return null;
    }
}
