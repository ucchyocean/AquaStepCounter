/*
 * @author     ucchy
 * @license    LGPLv3
 * @copyright  Copyright ucchy 2020
 */
package com.github.ucchyocean.aqua;

/**
 * StripDifferの結果格納クラス
 * @author ucchy
 */
public class FileDifferResult {

    /** 相対フォルダ */
    protected String dir;

    /** ファイル名 */
    protected String fileName;

    /** 変更されていない行の行数 */
    protected int noChanged;

    /** 変更されている行の行数 */
    protected int edited;

    /** 追加されている行の行数 */
    protected int added;

    /** 削除されている行の行数 */
    protected int deleted;

    /**
     * コンストラクタ
     */
    protected FileDifferResult() {
        this("", "", 0, 0, 0, 0);
    }

    /**
     * コンストラクタ
     * @param noChanged
     * @param edited
     * @param added
     * @param deleted
     */
    protected FileDifferResult(String dir, String fileName,
            int noChanged, int edited, int added, int deleted) {
        this.dir = dir;
        this.fileName = fileName;
        this.noChanged = noChanged;
        this.edited = edited;
        this.added = added;
        this.deleted = deleted;
    }

    public String[] getAllAsStringArray() {
        String[] arr = new String[6];
        arr[0] = dir;
        arr[1] = fileName;
        arr[2] = String.valueOf(noChanged);
        arr[3] = String.valueOf(edited);
        arr[4] = String.valueOf(added);
        arr[5] = String.valueOf(deleted);
        return arr;
    }

    /**
     * @return dir
     */
    public String getDir() {
        return dir;
    }

    /**
     * @return fileName
     */
    public String getFileName() {
        return fileName;
    }

    /**
     * @return noChanged
     */
    public int getNoChanged() {
        return noChanged;
    }

    /**
     * @return edited
     */
    public int getEdited() {
        return edited;
    }

    /**
     * @return added
     */
    public int getAdded() {
        return added;
    }

    /**
     * @return deleted
     */
    public int getDeleted() {
        return deleted;
    }
}
