/*
 * @author     ucchy
 * @license    LGPLv3
 * @copyright  Copyright ucchy 2020
 */
package com.github.ucchyocean.aqua;

import java.util.Map;

import junit.framework.TestCase;

/**
 *
 * @author ucchy
 */
public class CommandLineParserTest extends TestCase {

    public void testCommandLine() {

        String[] args = new String[] {
                "aaa", "bbb", "cccc", // 無駄な指定
                "-?", // ヘルプ
                "aaaa", // 無駄な指定
                "-ui", // UI表示
                "-report", // レポート表示
                "path to output report file.",
                "-new", // newフォルダ
                "にゅー ふぉるだ",
                "-hello", // 無駄な指定
                "-old", // oldフォルダ（次パラメータが指定されていないケース）
        };

        CommandLineParser parser = new CommandLineParser(args);

        Map<String, String> map = parser.getParams();
        for ( String key : map.keySet() ) {
            System.out.println(key + " -> " + map.get(key));
        }

        assertTrue(parser.hasParam(CommandLineParser.KEY_HELP));
        assertTrue(parser.hasParam(CommandLineParser.KEY_NEW));
        assertTrue(!parser.hasParam(CommandLineParser.KEY_OLD));
        assertTrue(parser.hasParam(CommandLineParser.KEY_REPORT));
        assertTrue(parser.hasParam(CommandLineParser.KEY_UI));

    }
}
