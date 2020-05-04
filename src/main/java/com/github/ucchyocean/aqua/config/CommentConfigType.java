/*
 * @author     ucchy
 * @license    LGPLv3
 * @copyright  Copyright ucchy 2020
 */
package com.github.ucchyocean.aqua.config;

/**
 * コメント設定のタイプ
 * @author ucchy
 */
public enum CommentConfigType {

    /** 行コメントのみ */
    LINE_COMMENT,

    /** 行コメント（shellタイプ） */
    SHELL,

    /** ブロックコメントのみ */
    BLOCK_COMMENT,

    /** 複合（行コメントとブロックコメント） */
    COMPLEX,

    /** htmlコメント */
    HTML,

    /** jspコメント */
    JSP,
    ;

    /**
     * 文字列からCommentConfigTypeを返す
     * @param src 文字列
     * @return CommentConfigType。一致するものが無い場合はnull
     */
    public static CommentConfigType fromString(String src) {

        for ( CommentConfigType t : CommentConfigType.values() ) {
            if ( t.toString().equals(src) ) return t;
        }
        return null;
    }
}
