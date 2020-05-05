/*
 * @author     ucchy
 * @license    LGPLv3
 * @copyright  Copyright ucchy 2020
 */
package com.github.ucchyocean.aqua;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Locale;

/**
 * メッセージリソースクラス
 * @author ucchy
 */
public class Messages {

    // ソフトウェア情報
    public static String SOFTWARE_NAME = "Aqua Step Counter";
    public static String SOFTWARE_VERSION = "0.1";
    public static String SOFTWARE_AUTHOR_INFO = "ucchy";
    public static String SOFTWARE_DATE_INFO = "2020/05/04";
    public static String SOFTWARE_USAGE_URL =
            "https://github.com/ucchyocean/AquaStepCounter";

    // メニュー項目
    public static String MENU_MAIN = "メニュー(&M)";
    public static String MENU_PREFS = "設定(&C)";
    public static String MENU_EXIT = "終了(&E)";
    public static String MENU_HELP = "ヘルプ(&H)";
    public static String MENU_DESC = "対応済み拡張子一覧を開く(&D)";
    public static String MENU_HOMEPAGE = "ソフトウェアの情報ページを開く(&P)";
    public static String MENU_VERSION = "バージョン情報(&V)";

    // 各種ダイアログのタイトル
    public static String TITLE_VERSION = "バージョン情報";
    public static String TITLE_DESC = "対応済み拡張子一覧";
    public static String TITLE_EXCEPTION = "例外";
    public static String TITLE_CANCELED = "中断";
    public static String TITLE_PREFS = "設定";

    // 各種ダイアログのメッセージ
    public static String MESSAGE_MAIN =
        "比較元と比較先を入力して実行ボタンを押してください。" +
        "ファイル/フォルダをドラックアンドドロップすることもできます。";
    public static String MESSAGE_SOURCE = "比較元(New)";
    public static String MESSAGE_DISTINATION = "比較先(Old)";
    public static String MESSAGE_RUN = "カウント実行";
    public static String MESSAGE_CANCEL = "カウント中断";
    public static String MESSAGE_CLEAR = "表をクリア";
    public static String MESSAGE_EXPORT = "CSVファイルに出力";
    public static String MESSAGE_DESC = "このバージョンに含まれる解析クラスは、以下の通りです。";
    public static String MESSAGE_BUTTON_REF = "フォルダの参照";
    public static String MESSAGE_DIR_SELECT = "に設定するフォルダを選択してください。";
    public static String MESSAGE_COUNT_RUNNING = "ラインカウント処理中......";
    public static String MESSAGE_ERROR = "エラーが発生しました";
    public static String MESSAGE_INTERRUPT = "中断しました";

    // 表領域のラベル
    public static String[] TABLE_LABELS = new String[] {
            "dir", "file name", "nochanged", "edited", "added", "deleted"};

    // 設定関連
    public static String PREFS_NAME_STRIP_COMMENT = "コメントを比較対象としない";
    public static String PREFS_NAME_STRIP_WHITE = "空白、空行を比較対象としない";

    // エクスポートのファイル種類とその説明
    public static String[] EXPORT_SUFS = new String[] {"*.csv"};
    public static String[] EXPORT_DESCS = new String[] {"CSV (カンマ区切り) (*.csv)"};


    static {
        InputStream stream = getResourceInputStream();
        if ( stream != null ) {
            try {
                YamlConfig config = YamlConfig.load(stream);

                SOFTWARE_NAME = config.getString("SOFTWARE_NAME", SOFTWARE_NAME);
                SOFTWARE_VERSION = config.getString("SOFTWARE_VERSION", SOFTWARE_VERSION);
                SOFTWARE_AUTHOR_INFO = config.getString("SOFTWARE_AUTHOR_INFO", SOFTWARE_AUTHOR_INFO);
                SOFTWARE_DATE_INFO = config.getString("SOFTWARE_DATE_INFO", SOFTWARE_DATE_INFO);
                MENU_MAIN = config.getString("MENU_MAIN", MENU_MAIN);
                MENU_PREFS = config.getString("MENU_PREFS", MENU_PREFS);
                MENU_EXIT = config.getString("MENU_EXIT", MENU_EXIT);
                MENU_HELP = config.getString("MENU_HELP", MENU_HELP);
                MENU_DESC = config.getString("MENU_DESC", MENU_DESC);
                MENU_HOMEPAGE = config.getString("MENU_HOMEPAGE", MENU_HOMEPAGE);
                MENU_VERSION = config.getString("MENU_VERSION", MENU_VERSION);
                TITLE_VERSION = config.getString("TITLE_VERSION", TITLE_VERSION);
                TITLE_DESC = config.getString("TITLE_DESC", TITLE_DESC);
                TITLE_EXCEPTION = config.getString("TITLE_EXCEPTION", TITLE_EXCEPTION);
                TITLE_CANCELED = config.getString("TITLE_CANCELED", TITLE_CANCELED);
                TITLE_PREFS = config.getString("TITLE_PREFS", TITLE_PREFS);
                MESSAGE_MAIN = config.getString("MESSAGE_MAIN", MESSAGE_MAIN);
                MESSAGE_SOURCE = config.getString("MESSAGE_SOURCE", MESSAGE_SOURCE);
                MESSAGE_DISTINATION = config.getString("MESSAGE_DISTINATION", MESSAGE_DISTINATION);
                MESSAGE_RUN = config.getString("MESSAGE_RUN", MESSAGE_RUN);
                MESSAGE_CANCEL = config.getString("MESSAGE_CANCEL", MESSAGE_CANCEL);
                MESSAGE_CLEAR = config.getString("MESSAGE_CLEAR", MESSAGE_CLEAR);
                MESSAGE_EXPORT = config.getString("MESSAGE_EXPORT", MESSAGE_EXPORT);
                MESSAGE_DESC = config.getString("MESSAGE_DESC", MESSAGE_DESC);
                MESSAGE_BUTTON_REF = config.getString("MESSAGE_BUTTON_REF", MESSAGE_BUTTON_REF);
                MESSAGE_DIR_SELECT = config.getString("MESSAGE_DIR_SELECT", MESSAGE_DIR_SELECT);
                MESSAGE_COUNT_RUNNING = config.getString("MESSAGE_COUNT_RUNNING", MESSAGE_COUNT_RUNNING);
                MESSAGE_ERROR = config.getString("MESSAGE_ERROR", MESSAGE_ERROR);
                MESSAGE_INTERRUPT = config.getString("MESSAGE_INTERRUPT", MESSAGE_INTERRUPT);
                TABLE_LABELS = config.getStringArray("TABLE_LABELS", TABLE_LABELS);
                PREFS_NAME_STRIP_COMMENT = config.getString("PREFS_NAME_STRIP_COMMENT", PREFS_NAME_STRIP_COMMENT);
                PREFS_NAME_STRIP_WHITE = config.getString("PREFS_NAME_STRIP_WHITE", PREFS_NAME_STRIP_WHITE);
                EXPORT_SUFS = config.getStringArray("EXPORT_SUFS", EXPORT_SUFS);
                EXPORT_DESCS = config.getStringArray("EXPORT_DESCS", EXPORT_DESCS);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static InputStream getResourceInputStream() {

        String baseName = "messages";
        Locale locale = Locale.getDefault();

        ArrayList<String> candidates = new ArrayList<String>();
        candidates.add(String.format("%s_%s_%s.yaml", baseName, locale.getLanguage(), locale.getCountry()));
        candidates.add(String.format("%s_%s_%s.yml", baseName, locale.getLanguage(), locale.getCountry()));
        candidates.add(String.format("%s_%s.yaml", baseName, locale.getLanguage()));
        candidates.add(String.format("%s_%s.yml", baseName, locale.getLanguage()));
        candidates.add(baseName + ".yaml");
        candidates.add(baseName + ".yml");

        for ( String name : candidates ) {
            InputStream stream = Messages.class.getResourceAsStream(name);
            if ( stream != null ) return stream;
        }
        return null;
    }
}
