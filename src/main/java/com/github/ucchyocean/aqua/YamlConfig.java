/*
 * @author     ucchy
 * @license    LGPLv3
 * @copyright  Copyright ucchy 2020
 */
package com.github.ucchyocean.aqua;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.HashMap;
import java.util.Map;

import org.yaml.snakeyaml.Yaml;

/**
 * Yaml設定読み書きユーティリティクラス
 * @author ucchy
 */
public class YamlConfig {

    private Map<String, Object> map = new HashMap<String, Object>();

    public static YamlConfig load(InputStream stream) throws IOException {

        YamlConfig config = new YamlConfig();
        if ( stream == null ) return config;

        Yaml yaml = new Yaml();

        @SuppressWarnings("unchecked")
        Map<String, Object> map = yaml.loadAs(stream, Map.class);
        if ( map == null ) {
            throw new IOException("Cannot load stream as yaml.");
        }

        config.map = map;
        return config;
    }

    public static YamlConfig load(Reader reader) throws IOException {

        YamlConfig config = new YamlConfig();
        if ( reader == null ) return config;

        Yaml yaml = new Yaml();

        @SuppressWarnings("unchecked")
        Map<String, Object> map = yaml.loadAs(reader, Map.class);
        if ( map == null ) {
            throw new IOException("Cannot load reader as yaml.");
        }

        config.map = map;
        return config;
    }

    public void save(File file) throws IOException {

        Yaml yaml = new Yaml();
        String data = yaml.dumpAsMap(map);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write(data);
        }
    }

    public void set(String key, Object value) {
        map.put(key, value);
    }

    public boolean getBoolean(String key, boolean defaultValue) {
        Object val = map.getOrDefault(key, defaultValue);
        return ( val != null ) ? (boolean)val : null;
    }

    public String getString(String key, String defaultValue) {
        Object val = map.getOrDefault(key, defaultValue);
        return ( val != null ) ? val.toString() : null;
    }

    public String[] getStringArray(String key, String[] defaultValue) {
        Object val = map.getOrDefault(key, defaultValue);
        return ( val != null && val instanceof String[] ) ? (String[])val : null;
    }
}
