/*
 * @author     ucchy
 * @license    LGPLv3
 * @copyright  Copyright ucchy 2020
 */
package com.github.ucchyocean.aqua;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

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
    public static String TABLE_LABELS = "dir;file name;nochanged;edited;added;deleted";

    // 設定関連
    public static String PREFS_NAME_STRIP_COMMENT = "コメントを比較対象としない";
    public static String PREFS_NAME_STRIP_WHITE = "空白、空行を比較対象としない";

    // エクスポートのファイル種類とその説明
    public static String EXPORT_SUFS = "*.csv";
    public static String EXPORT_DESCS = "CSV (カンマ区切り) (*.csv)";


    static {
        try {
            ResourceBundle bundle = ResourceBundle.getBundle("messages");

            SOFTWARE_NAME = bundle.getString("SOFTWARE_NAME");
            SOFTWARE_VERSION = bundle.getString("SOFTWARE_VERSION");
            SOFTWARE_AUTHOR_INFO = bundle.getString("SOFTWARE_AUTHOR_INFO");
            SOFTWARE_DATE_INFO = bundle.getString("SOFTWARE_DATE_INFO");
            MENU_MAIN = bundle.getString("MENU_MAIN");
            MENU_PREFS = bundle.getString("MENU_PREFS");
            MENU_EXIT = bundle.getString("MENU_EXIT");
            MENU_HELP = bundle.getString("MENU_HELP");
            MENU_DESC = bundle.getString("MENU_DESC");
            MENU_HOMEPAGE = bundle.getString("MENU_HOMEPAGE");
            MENU_VERSION = bundle.getString("MENU_VERSION");
            TITLE_VERSION = bundle.getString("TITLE_VERSION");
            TITLE_DESC = bundle.getString("TITLE_DESC");
            TITLE_EXCEPTION = bundle.getString("TITLE_EXCEPTION");
            TITLE_CANCELED = bundle.getString("TITLE_CANCELED");
            TITLE_PREFS = bundle.getString("TITLE_PREFS");
            MESSAGE_MAIN = bundle.getString("MESSAGE_MAIN");
            MESSAGE_SOURCE = bundle.getString("MESSAGE_SOURCE");
            MESSAGE_DISTINATION = bundle.getString("MESSAGE_DISTINATION");
            MESSAGE_RUN = bundle.getString("MESSAGE_RUN");
            MESSAGE_CANCEL = bundle.getString("MESSAGE_CANCEL");
            MESSAGE_CLEAR = bundle.getString("MESSAGE_CLEAR");
            MESSAGE_EXPORT = bundle.getString("MESSAGE_EXPORT");
            MESSAGE_DESC = bundle.getString("MESSAGE_DESC");
            MESSAGE_BUTTON_REF = bundle.getString("MESSAGE_BUTTON_REF");
            MESSAGE_DIR_SELECT = bundle.getString("MESSAGE_DIR_SELECT");
            MESSAGE_COUNT_RUNNING = bundle.getString("MESSAGE_COUNT_RUNNING");
            MESSAGE_ERROR = bundle.getString("MESSAGE_ERROR");
            MESSAGE_INTERRUPT = bundle.getString("MESSAGE_INTERRUPT");
            TABLE_LABELS = bundle.getString("TABLE_LABELS");
            PREFS_NAME_STRIP_COMMENT = bundle.getString("PREFS_NAME_STRIP_COMMENT");
            PREFS_NAME_STRIP_WHITE = bundle.getString("PREFS_NAME_STRIP_WHITE");
            EXPORT_SUFS = bundle.getString("EXPORT_SUFS");
            EXPORT_DESCS = bundle.getString("EXPORT_DESCS");

        } catch (MissingResourceException e) {
            e.printStackTrace();
        }
    }

}
