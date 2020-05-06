/*
 * @author     ucchy
 * @license    LGPLv3
 * @copyright  Copyright ucchy 2020
 */
package com.github.ucchyocean.aqua;

import java.util.HashMap;
import java.util.Map;

/**
 * コマンドライン引数解析クラス
 * @author ucchy
 */
public class CommandLineParser {

    private Map<String, String> params = new HashMap<String, String>();
    private String[] args;

    public static final String KEY_HELP = "help";
    public static final String KEY_NEW = "new";
    public static final String KEY_OLD = "old";
    public static final String KEY_UI = "ui";
    public static final String KEY_REPORT = "report";

    public static final String KEY_DC = "deleteComments";
    public static final String KEY_CC = "CountComments";
    public static final String KEY_DW = "deleteWhites";
    public static final String KEY_CW= "CountWhites";

    /**
     * コンストラクタ
     * @param args コマンドライン引数
     */
    public CommandLineParser(String[] args) {
        this.args = args;
        parse();
    }

    private void parse() {

        for ( int index = 0; index < args.length; index++ ) {

            // NOTE: 処理簡略化のため、以降でequalsIgnoreCaseをequalsにする代わりに、最初にtoLowerCaseしておく
            String src = args[index].toLowerCase();

            if (src.equals("-?") || src.equals("-h") || src.equals("-help")) {
                // ヘルプ（-?/-h/-help、次パラメータ無し）
                params.put(KEY_HELP, "true");

            } else if (src.equals("-n") || src.equals("-new")) {
                // newフォルダの指定（-n/-new、次パラメータでフォルダを指定）
                index++;
                if ( index < args.length ) {
                    params.put(KEY_NEW, args[index]);
                }

            } else if (src.equals("-o") || src.equals("-old")) {
                // oldフォルダの指定（-o/-old、次パラメータでフォルダを指定）
                index++;
                if ( index < args.length ) {
                    params.put(KEY_OLD, args[index]);
                }

            } else if (src.equals("-ui")) {
                // UI表示指定（-ui、次パラメータ無し）
                params.put(KEY_UI, "true");

            } else if (src.equals("-dc")) {
                // コメントを削除（-dc、次パラメータ無し）
                params.put(KEY_DC, "true");

            } else if (src.equals("-cc")) {
                // コメントをカウント（-dc、次パラメータ無し）
                params.put(KEY_CC, "true");

            } else if (src.equals("-dw")) {
                // 空白空行を削除（-dw、次パラメータ無し）
                params.put(KEY_DW, "true");

            } else if (src.equals("-cw")) {
                // コメントをカウント（-dc、次パラメータ無し）
                params.put(KEY_CW, "true");

            } else if (src.equals("-r") || src.equals("-report")) {
                // レポート出力先（-r/-report、次パラメータで出力先ファイル名）
                index++;
                if ( index < args.length ) {
                    params.put(KEY_REPORT, args[index]);
                }

            }
        }
    }

    /**
     * 指定されたキーが指定されているかどうかを確認する
     * @param key キー
     * @return 指定されているかどうか
     */
    public boolean hasParam(String key) {
        return params.containsKey(key);
    }

    /**
     * 指定されたキーの値を取得する
     * @param key キー
     * @return 値
     */
    public String get(String key) {
        return params.get(key);
    }

    /**
     * パラメータの個数を取得する
     * @return パラメータの個数
     */
    public int size() {
        return params.size();
    }

    /**
     * すべてのパラメータを返す
     * @return パラメータ
     */
    public Map<String, String> getParams() {
        return params;
    }
}
