/*
 * @author     ucchy
 * @license    LGPLv3
 * @copyright  Copyright ucchy 2020
 */
package com.github.ucchyocean.aqua;

/**
 * メッセージリソースクラス
 * @author ucchy
 */
public class Messages {

    public static final String SOFTWARE_NAME = "Aqua Step Counter";
    public static final String SOFTWARE_VERSION = "v1.0";
    public static final String SOFTWARE_AUTHOR_INFO = "ucchy";
    public static final String SOFTWARE_DATE_INFO = "2020/05/02";

    // メニュー項目
    public static final String MENU_MAIN = "メニュー(&M)";
    public static final String MENU_PREFS = "設定(&C)";
    public static final String MENU_EXIT = "終了(&E)";
    public static final String MENU_HELP = "ヘルプ(&H)";
    public static final String MENU_DESC = "対応済み拡張子一覧を開く(&D)";
    public static final String MENU_HOMEPAGE = "ソフトウェアの情報ページを開く(&P)";
    public static final String MENU_VERSION = "バージョン情報(&V)";

    // 各種ダイアログのタイトル
    public static final String TITLE_VERSION = "バージョン情報";
    public static final String TITLE_DESC = "対応済み拡張子一覧";
    public static final String TITLE_EXCEPTION = "例外";
    public static final String TITLE_CANCELED = "中断";
    public static final String TITLE_PREFS = "設定";

    // 各種ダイアログのメッセージ
    public static final String MESSAGE_MAIN =
        "比較元と比較先を入力して実行ボタンを押してください。" +
        "ファイル/フォルダをドラックアンドドロップすることもできます。";
    public static final String MESSAGE_SOURCE = "比較元(New)";
    public static final String MESSAGE_DISTINATION = "比較先(Old)";
    public static final String MESSAGE_RUN = "カウント実行";
    public static final String MESSAGE_CANCEL = "カウント中断";
    public static final String MESSAGE_CLEAR = "表をクリア";
    public static final String MESSAGE_EXPORT = "CSVファイルに出力";
    public static final String MESSAGE_DESC = "このバージョンに含まれる解析クラスは、以下の通りです。";
    public static final String MESSAGE_BUTTON_REF = "フォルダの参照";
    public static final String MESSAGE_DIR_SELECT = "に設定するフォルダを選択してください。";
    public static final String MESSAGE_COUNT_RUNNING = "ラインカウント処理中......";
    public static final String MESSAGE_ERROR = "エラーが発生しました";
    public static final String MESSAGE_INTERRUPT = "中断しました";

    // 表領域のラベル
    public static final String TABLE_LABELS = "dir;file name;nochanged;edited;added;deleted";

    // 設定関連
    public static final String PREFS_NAME_STRIP_COMMENT = "コメントを比較対象としない";
    public static final String PREFS_NAME_STRIP_WHITE = "空白、空行を比較対象としない";

    // エクスポートのファイル種類とその説明
    public static final String EXPORT_SUFS = "*.csv";
    public static final String EXPORT_DESCS = "CSV (カンマ区切り) (*.csv)";

    ;

    private String msg;

    private Messages(String msg) {
        this.msg = msg;
    }

    @Override
    public String toString() {
        return msg;
    }
}
