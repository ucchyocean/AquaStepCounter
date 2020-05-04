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
        this(0, 0, 0, 0);
    }

    /**
     * コンストラクタ
     * @param noChanged
     * @param edited
     * @param added
     * @param deleted
     */
    protected FileDifferResult(int noChanged, int edited, int added, int deleted) {
        this.noChanged = noChanged;
        this.edited = edited;
        this.added = added;
        this.deleted = deleted;
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
