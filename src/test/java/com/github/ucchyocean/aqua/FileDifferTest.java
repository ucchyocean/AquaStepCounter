/*
 * @author     ucchy
 * @license    LGPLv3
 * @copyright  Copyright ucchy 2020
 */
package com.github.ucchyocean.aqua;

import java.io.File;
import java.io.IOException;

import org.apache.commons.jrcs.diff.Diff;
import org.apache.commons.jrcs.diff.DifferentiationFailedException;
import org.apache.commons.jrcs.diff.Revision;

import com.github.ucchyocean.aqua.config.CommentConfig;
import com.github.ucchyocean.aqua.config.CommentConfigType;

import junit.framework.TestCase;

/**
 * StripDifferのテスト
 * @author ucchy
 */
public class FileDifferTest extends TestCase {

    public void testDiff() {

        String cd = new File(".").getAbsoluteFile().getParent();
        String oldBase = cd + "\\testdata\\test1\\old\\java\\com\\github\\ucchyocean\\lc\\channel";
        String newBase = cd + "\\testdata\\test1\\new\\java\\com\\github\\ucchyocean\\lc\\channel";
        String fileName = "Channel.java";
        CommentConfig config = new CommentConfig(
                "java", "java comment type", CommentConfigType.COMPLEX,
                null,
                "/*", "*/", "//");

        FileDiffer differ = new FileDiffer(oldBase, newBase, fileName, config);

        try {
            FileDifferResult result = differ.getDiffData();

            System.out.println(String.format("nc=%d, e=%d, a=%d, d=%d",
                    result.getNoChanged(), result.getEdited(), result.getAdded(), result.getDeleted()
                    ));

            assertTrue(result.getEdited() == 4 && result.getAdded() == 2);

            differ.deleteComments();
            differ.deleteSpaces();

            result = differ.getDiffData();

            System.out.println(String.format("nc=%d, e=%d, a=%d, d=%d",
                    result.getNoChanged(), result.getEdited(), result.getAdded(), result.getDeleted()
                    ));

            assertTrue(result.getEdited() == 2 && result.getAdded() == 1);

        } catch (DifferentiationFailedException | IOException e) {
            e.printStackTrace();
            assertTrue(false);
        }
    }

    public void testRowDiff() {

        String cd = new File(".").getAbsoluteFile().getParent();
        String oldBase = cd + "\\testdata\\test1\\old\\java\\com\\github\\ucchyocean\\lc\\channel";
        String newBase = cd + "\\testdata\\test1\\new\\java\\com\\github\\ucchyocean\\lc\\channel";
        String fileName = "Channel.java";
        CommentConfig config = new CommentConfig(
                "java", "java comment type", CommentConfigType.COMPLEX,
                null,
                "/*", "*/", "//");

        FileDiffer differ = new FileDiffer(oldBase, newBase, fileName, config);

        String[] oldCcontents = differ.getOldContent().split("\n");
        String[] newContents = differ.getNewContent().split("\n");

        try {
            Revision rev = Diff.diff(oldCcontents, newContents);
            System.out.println("=== raw diff data ===");
            System.out.println(rev.toString());
            System.out.println("=====================");

            assertTrue(true);

        } catch (DifferentiationFailedException e) {
            e.printStackTrace();
            assertTrue(false);
        }
    }
}
