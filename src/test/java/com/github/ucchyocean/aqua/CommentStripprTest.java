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
        boolean withDebugPrint = true;
        boolean withDebugOutput = true;
        matchStripTest("sample.sh", withDebugPrint, withDebugOutput);
        matchStripTest("sample.bat", withDebugPrint, withDebugOutput);
        matchStripTest("sample.yml", withDebugPrint, withDebugOutput);
        matchStripTest("sample.java", withDebugPrint, withDebugOutput);
        matchStripTest("sample.htm", withDebugPrint, withDebugOutput);
        matchStripTest("sample.jsp", withDebugPrint, withDebugOutput);
    }

    private void matchStripTest(String testFileName, boolean withDebugPrint, boolean withDebugOutput) {

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
            }
            if ( withDebugOutput ) {
                File folder = new File("target", "stripped");
                if ( !folder.exists() ) folder.mkdirs();
                try ( BufferedWriter writer = new BufferedWriter(new FileWriter(
                        new File(folder, testFileName)))) {
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
        StringBuilder buffer = new StringBuilder();
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
