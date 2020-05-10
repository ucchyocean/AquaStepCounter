/*
 * @author     ucchy
 * @license    LGPLv3
 * @copyright  Copyright ucchy 2020
 */
package com.github.ucchyocean.aqua.config;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * コメント解析設定管理クラス
 * @author ucchy
 */
public class CommentConfigManager {

    private static int MAX_LABEL_LENGTH = 50;
    private static String DEFAULT_CONFIG_FOLDER = "/configs/";

    private HashMap<String, CommentConfig> configs = new HashMap<>();

    /**
     * 指定されたフォルダから、追加のコメント解析設定をロードする。
     * 既にロードされているコメント解析設定と、解析対象拡張子が重複している場合は、後からロードされた方で上書きされる。
     * @param folder フォルダ
     */
    public void loadAdditional(File folder) {

        if ( !folder.exists() ) {
            return;
        }

        File[] children = folder.listFiles();
        if ( children == null ) return;

        for ( File file : children ) {

            if ( !file.getName().endsWith(".yml") && !file.getName().endsWith(".yaml") ) continue;

            CommentConfig config = CommentConfig.load(file);
            if ( config == null ) continue;

            for ( String suf : config.getSupportedSuffixes() ) {
                configs.put(suf, config);
            }
        }
    }

    /**
     * jarファイル内から、デフォルトのコメント解析設定を読み込みする
     * @return デフォルトのコメント解析設定を読み込んだ、コメント解析設定クラス
     */
    public static CommentConfigManager loadFromDefaultFiles() {

        CommentConfigManager manager = new CommentConfigManager();

        // jarファイル内の/configs/list.txtから、デフォルトのファイル一覧を読み込む
        List<String> fileNames = new ArrayList<>();
        try ( BufferedReader reader = new BufferedReader(new InputStreamReader(
                CommentConfigManager.class.getResourceAsStream(
                        DEFAULT_CONFIG_FOLDER + "list.txt") ) ) ) {
            String line;
            while ((line = reader.readLine()) != null) {
                fileNames.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        for ( String fileName : fileNames ) {
            // NOTE: CommentConfigManagerTestのテストケースで失敗する場合は、このリストが不足している可能性がある。

            InputStream is = CommentConfigManager.class.getResourceAsStream(
                    DEFAULT_CONFIG_FOLDER + fileName);

            if ( is != null ) {
                CommentConfig config = CommentConfig.load(is);
                if ( config == null ) continue;

                for ( String suf : config.getSupportedSuffixes() ) {
                    manager.configs.put(suf, config);
                }
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
        return configs.get(suffix);
    }

    /**
     * ロードされているすべてのコメント解析設定の説明文を生成する
     * @return 説明文
     */
    public String getDescription() {

        StringBuffer output = new StringBuffer();

        for ( CommentConfig config : getAllConfig() ) {

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
    public Set<CommentConfig> getAllConfig() {
        return new HashSet<CommentConfig>(configs.values());
    }
}
