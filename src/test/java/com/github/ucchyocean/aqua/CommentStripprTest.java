/*
 * @author     ucchy
 * @license    LGPLv3
 * @copyright  Copyright ucchy 2020
 */
package com.github.ucchyocean.aqua;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import com.github.ucchyocean.aqua.config.CommentConfigManager;

import junit.framework.TestCase;

/**
 *
 * @author ucchy
 */
public class CommentStripprTest extends TestCase {

    public void testCommentStrippr() {
        boolean withDebugPrint = false;
        matchStripTest("sample.sh", withDebugPrint);
        matchStripTest("sample.bat", withDebugPrint);
        matchStripTest("sample.yml", withDebugPrint);
        matchStripTest("sample.java", withDebugPrint);
        matchStripTest("sample.jsp", true);
    }

    private void matchStripTest(String testFileName, boolean withDebugPrint) {

        System.out.println("CommentStripprTest : " + testFileName);

        String cd = new File(".").getAbsoluteFile().getParent();
        String orgFileName = cd + "\\testdata\\CommentStripTest\\original\\" + testFileName;
        String resFileName = cd + "\\testdata\\CommentStripTest\\result\\" + testFileName;
        String org = readAllLines(orgFileName);
        String res = readAllLines(resFileName);
        String suf = testFileName.substring(testFileName.indexOf("."));

        CommentConfigManager manager = CommentConfigManager.loadFromDefaultFiles();

        try {
            String stripped = CommentStripper.deleteComments(org, manager.getConfig(suf));

            if ( withDebugPrint ) {
                System.out.println("--- original ---");
                System.out.println(org);
                System.out.println("--- stripped ---");
                System.out.println(stripped);

                try ( BufferedWriter writer = new BufferedWriter(new FileWriter("stripped.txt"))) {
                    writer.write(stripped);
                }
            }

            assertTrue(res.equals(stripped));

        } catch (IOException e) {
            e.printStackTrace();
            assertTrue(false);
        }
    }

    private static String readAllLines(String filename) {
        StringBuffer buffer = new StringBuffer();
        try ( BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return buffer.toString();
    }

    @SuppressWarnings("unused")
    private static boolean matchesArray(ArrayList<String> list1, ArrayList<String> list2) {

        int size = (list1.size() > list2.size()) ? list1.size() : list2.size();
        for ( int i=0; i<size; i++ ) {
            String line1 = (i < list1.size()) ? list1.get(i) : "";
            String line2 = (i < list2.size()) ? list2.get(i) : "";
            if ( !line1.equals(line2) ) return false;
        }
        return true;
    }
}
