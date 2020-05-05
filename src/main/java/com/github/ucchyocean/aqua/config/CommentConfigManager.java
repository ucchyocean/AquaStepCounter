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
 * コメントコンフィグ管理クラス
 * @author ucchy
 */
public class CommentConfigManager {

    private static int MAX_LABEL_LENGTH = 50;
    private static String DEFAULT_CONFIG_FOLDER = "/configs/";

    private ArrayList<CommentConfig> configs = new ArrayList<CommentConfig>();

    /**
     * 指定されたフォルダから、コメントコンフィグをロードする
     * @param folder フォルダ
     * @return ロードされたCommentConfigManager
     */
    public static CommentConfigManager load(File folder) {

        if ( !folder.exists() ) {
            folder.mkdirs();
        }

        CommentConfigManager manager = new CommentConfigManager();

        for ( File file : folder.listFiles() ) {

            if ( !file.getName().endsWith(".properties") ) continue;

            CommentConfig config = CommentConfig.load(file);
            if ( config == null ) continue;

            manager.configs.add(config);
        }

        return manager;
    }

    public static CommentConfigManager loadFromDefaultFiles() {

        CommentConfigManager manager = new CommentConfigManager();

        // TODO jarのフォルダ内にあるファイル一覧を取得する方法を調べる
        for ( String fileName : new String[]{
                "bat.yml", "ccjava.yml", "css.yml", "html.yml", "inf.yml",
                "jsp.yml", "properties.yml", "sh.yml", "xml.yml" } ) {

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
     * 指定された拡張子に対応するコメントコンフィグを返す
     * @param suffix 拡張子
     * @return コメントコンフィグ。対応するコメントコンフィグが見つからない場合はnullが返される。
     */
    public CommentConfig getConfig(String suffix) {

        for ( CommentConfig config : configs ) {
            if ( config.getSupportedSuffixes().contains(suffix) ) return config;
        }
        return null;
    }

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
}
