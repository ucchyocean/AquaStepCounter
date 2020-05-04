/*
 * @author     ucchy
 * @license    LGPLv3
 * @copyright  Copyright ucchy 2020
 */
package com.github.ucchyocean.aqua.config;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * コメント設定
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

    public void save(File file) {

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {

            Properties prop = new Properties();
            prop.setProperty("name", name);
            prop.setProperty("description", description);
            prop.setProperty("type", type.toString());
            prop.setProperty("supportedSuffixes", String.join(",", supportedSuffixes));
            prop.setProperty("blockCommentStartSimbol", blockCommentStartSimbol);
            prop.setProperty("blockCommentEndSimbol", blockCommentEndSimbol);
            prop.setProperty("lineCommentSimbol", lineCommentSimbol);
            prop.setProperty("extraSimbol", String.join("▲", extraSimbol));

            prop.store(writer, null);

        } catch (IOException e) {
            e.printStackTrace();
            // TODO throwすべきか検討する
        }
    }

    public static CommentConfig load(File file) {

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {

            Properties prop = new Properties();
            prop.load(reader);

            List<String> suffixes = new ArrayList<>();
            for ( String s : prop.getProperty("supportedSuffixes", "").split(",") ) {
                suffixes.add(s);
            }

            return new CommentConfig(
                    prop.getProperty("name", ""),
                    prop.getProperty("description", ""),
                    CommentConfigType.fromString(prop.getProperty("type", "")),
                    suffixes,
                    prop.getProperty("blockCommentStartSimbol", ""),
                    prop.getProperty("blockCommentEndSimbol", ""),
                    prop.getProperty("lineCommentSimbol", ""),
                    prop.getProperty("extraSimbol", "").split("▲")
                    );

        } catch (IOException e) {
            e.printStackTrace();
            // TODO throwすべきか検討する
        }

        return null;
    }

    public static CommentConfig load(String src) {

        try {
            Properties prop = new Properties();
            prop.load(new StringReader(src));

            List<String> suffixes = new ArrayList<>();
            for ( String s : prop.getProperty("supportedSuffixes", "").split(",") ) {
                suffixes.add(s);
            }

            return new CommentConfig(
                    prop.getProperty("name", ""),
                    prop.getProperty("description", ""),
                    CommentConfigType.fromString(prop.getProperty("type", "")),
                    suffixes,
                    prop.getProperty("blockCommentStartSimbol", ""),
                    prop.getProperty("blockCommentEndSimbol", ""),
                    prop.getProperty("lineCommentSimbol", ""),
                    prop.getProperty("extraSimbol", "").split("▲")
                    );

        } catch (IOException e) {
            e.printStackTrace();
            // TODO throwすべきか検討する
        }

        return null;
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
