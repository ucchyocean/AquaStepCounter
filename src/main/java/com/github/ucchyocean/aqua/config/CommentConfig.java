/*
 * @author     ucchy
 * @license    LGPLv3
 * @copyright  Copyright ucchy 2020
 */
package com.github.ucchyocean.aqua.config;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import com.github.ucchyocean.aqua.YamlConfig;

/**
 * コメント解析設定クラス
 * @author ucchy
 */
public class CommentConfig {

    /** 名前 */
    private String name;
    /** 説明文 */
    private String description;
    /** タイプ */
    private CommentConfigType type;
    /** 対象となるソースコードの拡張子 */
    private List<String> supportedSuffixes;
    /** ブロックコメントの始端 */
    private String blockCommentStartSimbol;
    /** ブロックコメントの終端 */
    private String blockCommentEndSimbol;
    /** 行コメントの始端 */
    private String lineCommentSimbol;
    /** エクストラ */
    private String[] extraSimbol;

    /**
     * コンストラクタ
     * @param name 名前
     * @param description 説明文
     * @param supportedSuffixes 対象となるソースコードの拡張子
     * @param blockCommentStartSimbol ブロックコメントの始端
     * @param blockCommentEndSimbol ブロックコメントの終端
     * @param lineCommentSimbol 行コメントの始端
     * @param extraSimbol エクストラ
     */
    public CommentConfig(String name, String description, CommentConfigType type,
            List<String> supportedSuffixes,
            String blockCommentStartSimbol, String blockCommentEndSimbol, String lineCommentSimbol,
            String ... extraSimbol) {
        this.name = name;
        this.description = description;
        this.type = type;
        this.supportedSuffixes = supportedSuffixes;
        this.blockCommentStartSimbol = blockCommentStartSimbol;
        this.blockCommentEndSimbol = blockCommentEndSimbol;
        this.lineCommentSimbol = lineCommentSimbol;
        this.extraSimbol = extraSimbol;
    }

    /**
     * 指定されたファイルに保存する
     * @param file 保存先
     */
    public void save(File file) {

        YamlConfig config = new YamlConfig();
        config.set("name", name);
        config.set("description", description);
        config.set("type", type.toString());
        config.set("supportedSuffixes", supportedSuffixes);
        config.set("blockCommentStartSimbol", blockCommentStartSimbol);
        config.set("blockCommentEndSimbol", blockCommentEndSimbol);
        config.set("lineCommentSimbol", lineCommentSimbol);
        config.set("extraSimbol", extraSimbol);
        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 指定されたファイルからロードする
     * @param file 読み込み元
     * @return 読み込まれたコメント解析クラス
     */
    public static CommentConfig load(File file) {

        try (FileReader reader = new FileReader(file)) {
            YamlConfig yaml = YamlConfig.load(reader);
            return makeCommentConfigFromYaml(yaml);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 指定されたInputStreamからロードする
     * @param stream 読み込み元
     * @return 読み込まれたコメント解析クラス
     */
    public static CommentConfig load(InputStream stream) {

        try {
            YamlConfig yaml = YamlConfig.load(stream);
            return makeCommentConfigFromYaml(yaml);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    // 指定されたYamlConfigからCommentConfigを生成する
    private static CommentConfig makeCommentConfigFromYaml(YamlConfig yaml) {
        return new CommentConfig(
                yaml.getString("name", ""),
                yaml.getString("description", ""),
                CommentConfigType.fromString(yaml.getString("type", "")),
                yaml.getStringArray("supportedSuffixes", new ArrayList<String>()),
                yaml.getString("blockCommentStartSimbol", "/*"),
                yaml.getString("blockCommentEndSimbol", "*/"),
                yaml.getString("lineCommentSimbol", "//"),
                convertStringList(yaml.getStringArray("extraSimbol", null))
                );
    }

    // ArrayList<String> を String[] に変換する
    private static String[] convertStringList(ArrayList<String> list) {
        String[] dest = new String[list.size()];
        list.toArray(dest);
        return dest;
    }

    /**
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * @return description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @return type
     */
    public CommentConfigType getType() {
        return type;
    }

    /**
     * @return supportedSuffixes
     */
    public List<String> getSupportedSuffixes() {
        return supportedSuffixes;
    }

    /**
     * @return blockCommentStartSimbol
     */
    public String getBlockCommentStartSimbol() {
        return blockCommentStartSimbol;
    }

    /**
     * @return blockCommentEndSimbol
     */
    public String getBlockCommentEndSimbol() {
        return blockCommentEndSimbol;
    }

    /**
     * @return lineCommentSimbol
     */
    public String getLineCommentSimbol() {
        return lineCommentSimbol;
    }

    /**
     * @return extraSimbol
     */
    public String[] getExtraSimbol() {
        return extraSimbol;
    }

}
