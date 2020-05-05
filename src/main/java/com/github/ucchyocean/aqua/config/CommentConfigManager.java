/*
 * @author     ucchy
 * @license    LGPLv3
 * @copyright  Copyright ucchy 2020
 */
package com.github.ucchyocean.aqua.config;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * コメント解析設定管理クラス
 * @author ucchy
 */
public class CommentConfigManager {

    private static int MAX_LABEL_LENGTH = 50;
    private static String DEFAULT_CONFIG_FOLDER = "/configs/";

    private ArrayList<CommentConfig> configs = new ArrayList<CommentConfig>();

    /**
     * 指定されたフォルダから、コメント解析設定をロードする
     * @param folder フォルダ
     * @return ロードされたCommentConfigManager
     */
    public static CommentConfigManager load(File folder) {

        if ( !folder.exists() ) {
            folder.mkdirs();
        }

        CommentConfigManager manager = new CommentConfigManager();

        File[] children = folder.listFiles();
        if ( children == null ) return manager;

        for ( File file : children ) {

            if ( !file.getName().endsWith(".properties") ) continue;

            CommentConfig config = CommentConfig.load(file);
            if ( config == null ) continue;

            manager.configs.add(config);
        }

        return manager;
    }

    /**
     * jarファイル内から、デフォルトのコメント解析設定を読み込みする
     * @return デフォルトのコメント解析設定を読み込んだ、コメント解析設定クラス
     */
    public static CommentConfigManager loadFromDefaultFiles() {

        CommentConfigManager manager = new CommentConfigManager();

        for ( String fileName : new String[] {
                "bat.yml", "ccjava.yml", "css.yml", "html.yml", "inf.yml",
                "jsp.yml", "properties.yml", "sh.yml", "xml.yml" } ) {
            // NOTE: CommentConfigManagerTestのテストケースで失敗する場合は、このリストが不足している可能性がある。

            InputStream is = CommentConfigManager.class.getResourceAsStream(
                    DEFAULT_CONFIG_FOLDER + fileName);

            if ( is != null ) {
                CommentConfig config = CommentConfig.load(is);
                if ( config == null ) continue;

                manager.configs.add(config);
            }
        }

        return manager;
    }

    /**
     * 指定された拡張子に対応するコメント解析設定を返す
     * @param suffix 拡張子
     * @return コメント解析設定。対応するコメント解析設定が見つからない場合はnullが返される。
     */
    public CommentConfig getConfig(String suffix) {

        for ( CommentConfig config : configs ) {
            if ( config.getSupportedSuffixes().contains(suffix) ) return config;
        }
        return null;
    }

    /**
     * ロードされているすべてのコメント解析設定の説明文を生成する
     * @return 説明文
     */
    public String getDescription() {

        StringBuffer output = new StringBuffer();

        for ( CommentConfig config : configs ) {

            output.append( config.getDescription() + "\n\t" );
            List<String> sufs = config.getSupportedSuffixes();
            output.append( sufs.get(0) );
            int count = sufs.get(0).length();
            for ( int j=1; j<sufs.size(); j++ ) {
                output.append( ", " + sufs.get(j) );
                count += ( ", " + sufs.get(j) ).length();
                if ( count >= MAX_LABEL_LENGTH ) {
                    output.append(", ...... ");
                    break;
                }
            }
            output.append("\n");
        }

        return output.toString();
    }

    /**
     * ロードされているすべてのコメント解析設定を返す
     * @return すべてのコメント解析設定
     */
    public ArrayList<CommentConfig> getAllConfig() {
        return configs;
    }
}
