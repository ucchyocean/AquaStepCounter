/*
 * @author     ucchy
 * @license    LGPLv3
 * @copyright  Copyright ucchy 2020
 */
package com.github.ucchyocean.aqua.config;

import java.io.File;
import java.util.ArrayList;

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
                configs.add(CommentConfig.load(file));
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
}
