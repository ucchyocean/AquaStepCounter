/*
 * @author     ucchy
 * @license    LGPLv3
 * @copyright  Copyright ucchy 2020
 */
package com.github.ucchyocean.aqua;

import java.util.ArrayList;

import junit.framework.TestCase;

/**
 * JarUtilityのテスト
 * @author ucchy
 */
public class JarUtilityTest extends TestCase {

    public void testJarFile() {

        ArrayList<String> list = JarUtility.getFileList("config");
        System.out.println("default config files.");
        for ( String l : list ) {
            System.out.println("-- " + l);
        }
        assertTrue(list.size() == 9);
    }
}
