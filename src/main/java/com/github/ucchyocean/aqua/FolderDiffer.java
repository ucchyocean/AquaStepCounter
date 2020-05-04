/*
 * @author     ucchy
 * @license    LGPLv3
 * @copyright  Copyright ucchy 2020
 */
package com.github.ucchyocean.aqua;

import java.io.IOException;

import org.apache.commons.jrcs.diff.DifferentiationFailedException;

import com.github.ucchyocean.aqua.config.CommentConfig;
import com.github.ucchyocean.aqua.config.CommentConfigManager;

/**
 * フォルダ間で再帰的なファイル比較を行うクラス
 * @author ucchy
 */
public class FolderDiffer {

    private String oldFolder;
    private String newFolder;
    private boolean deleteSpaces;
    private boolean deleteComments;
    private FolderFileList list;

    /**
     * コンストラクタ
     * @param oldFolder 変更前のファイルがあるフォルダ。nullを指定した場合は新しいファイルのみで比較される（全行が追加行としてカウントされる）
     * @param newFolder 変更後のファイルがあるフォルダ。nullを指定した場合は古いファイルのみで比較される（全行が削除行としてカウントされる）
     * @param deleteSpaces 空白・タブ・空行をカウント対象外とするかどうか
     * @param deleteComments コメントをカウント対象外するかどうか
     */
    public FolderDiffer(String oldFolder, String newFolder, boolean deleteSpaces, boolean deleteComments) {
        this.oldFolder = oldFolder;
        this.newFolder = newFolder;
        this.deleteSpaces = deleteSpaces;
        this.deleteComments = deleteComments;
        list = new FolderFileList(oldFolder, newFolder);
    }

    public FolderDifferResult getDiffData() {

        FolderDifferResult results = new FolderDifferResult(oldFolder, newFolder);

        CommentConfigManager manager = AquaStepCounter.getCommentConfigManager();

        for ( String file : list.getList() ) {

            CommentConfig config = manager.getConfig(getSuffix(file));
            if ( config == null ) continue;

            try {
                FileDiffer differ = new FileDiffer(oldFolder, newFolder, file, config);
                if ( deleteSpaces ) differ.deleteSpaces();
                if ( deleteComments ) differ.deleteComments();

                FileDifferResult result = differ.getDiffData();
                results.addResult(file, result);

            } catch (DifferentiationFailedException | IOException e) {
                e.printStackTrace();
            }
        }

        return results;
    }

    private String getSuffix(String filename) {

        int index = filename.lastIndexOf(".");
        if ( index == -1 ) return "";
        return filename.substring(index);
    }
}
