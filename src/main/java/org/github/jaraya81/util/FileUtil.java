package org.github.jaraya81.util;

import lombok.extern.slf4j.Slf4j;
import org.github.jaraya81.exception.BotException;

import java.io.File;

import static org.github.jaraya81.util.Conditional.check;

@Slf4j
public class FileUtil {
    public static final String FILE_SEPARATOR = "/";

    public static void mkdirs(String path) throws BotException {
        check(path != null && !path.isEmpty(), "path is null or empty");

        File file = new File(path);
        if (file.mkdirs()) {
            log.info("Directory created :: " + file.getAbsolutePath());
        }
    }
}
