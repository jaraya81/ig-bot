package org.github.jaraya81.util;

import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import java.io.InputStream;

public class YamlUtil {

    public static <T> T get(InputStream is, T instance) {
        Yaml yaml = new Yaml(new Constructor(instance.getClass()));
        return yaml.load(is);
    }
}
