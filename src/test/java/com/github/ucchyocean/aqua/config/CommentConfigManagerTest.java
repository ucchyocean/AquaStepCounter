/*
 * @author     ucchy
 * @license    LGPLv3
 * @copyright  Copyright ucchy 2020
 */
package com.github.ucchyocean.aqua.config;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

/**
 * CommentConfigManagerのテスト
 * @author ucchy
 */
public class CommentConfigManagerTest extends TestCase {

    public void testList() {
        // すべてのデフォルトコメント解析クラスが、CommentConfigManagerでロードされているかどうか確認する

        ArrayList<CommentConfig> configs = new ArrayList<>();
        File[] files = (new File("src\\main\\resources\\configs")).listFiles();
        if ( files != null ) {
            for ( File file : files ) {
                if ( file.getName().endsWith(".yml") ) configs.add(CommentConfig.load(file));
            }
        }

        CommentConfigManager manager = CommentConfigManager.loadFromDefaultFiles();

        for ( CommentConfig c : configs ) {
            System.out.println(c.getName() + " - " + c.getDescription());
            boolean find = false;
            for ( CommentConfig d : manager.getAllConfig() ) {
                if ( c.getName().equals(d.getName()) ) {
                    find = true;
                    break;
                }
            }
            assertTrue(find);
        }
        assertTrue(true);
    }

    public void testAdditionalFolderLoad() {
        // CommentConfigManagerに追加のコメント解析設定を読み込みさせるテスト

        CommentConfigManager manager = CommentConfigManager.loadFromDefaultFiles();

        // 偽のコメント解析設定を作成して保存
        File tempFolder = new File("target", "temp");
        if ( !tempFolder.exists() ) tempFolder.mkdirs();
        List<String> sufs = new ArrayList<>();
        sufs.add(".bat");
        sufs.add(".cpp");
        sufs.add(".java");
        sufs.add(".xxx");
        CommentConfig conf = new CommentConfig("nise", "nise no config",
                CommentConfigType.COMPLEX, sufs, "hoge", "hage", "hige");
        conf.save(new File(tempFolder, "nise.yml"));

        // フォルダから追加のコメント解析設定を読み込み
        manager.loadAdditional(tempFolder);


        for ( String s : sufs ) {
            assertTrue(manager.getConfig(s) != null);
            assertTrue(manager.getConfig(s).getName().equals("nise"));
        }
    }
}
