/*
 * @author     ucchy
 * @license    LGPLv3
 * @copyright  Copyright ucchy 2020
 */
package com.github.ucchyocean.aqua;

import java.io.File;

import junit.framework.TestCase;

/**
 * FileListのテスト
 * @author ucchy
 */
public class FolderFileListTest extends TestCase {

    public void testFileList() {

        String cd = new File(".").getAbsoluteFile().getParent();
        String oldBase = cd + "\\testdata\\test1\\old";
        String newBase = cd + "\\testdata\\test1\\new";

        // マージされたファイルリストのテスト
        FolderFileList list = new FolderFileList(oldBase, newBase);

        for ( String f : list.getList() ) {
            System.out.println(f);
        }

        assertTrue(list.getList().length == 79);
        assertTrue(isContainString(list.getList(), "HawkEyeBridge.java"));
        assertTrue(isContainString(list.getList(), "PrismBridge.java"));


        // 片方がnullのファイルリストのテスト
        list = new FolderFileList(oldBase, null);

        assertTrue(list.getList().length == 78);
        assertTrue(isContainString(list.getList(), "HawkEyeBridge.java"));
        assertTrue(!isContainString(list.getList(), "PrismBridge.java"));

        list = new FolderFileList(null, newBase);

        assertTrue(list.getList().length == 78);
        assertTrue(!isContainString(list.getList(), "HawkEyeBridge.java"));
        assertTrue(isContainString(list.getList(), "PrismBridge.java"));
    }

    private boolean isContainString(String[] list, String key) {

        for ( String s : list ) {
            if ( s.contains(key) ) return true;
        }
        return false;
    }
}
