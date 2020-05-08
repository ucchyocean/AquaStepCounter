/*
 * @author     ucchy
 * @license    LGPLv3
 * @copyright  Copyright ucchy 2020
 */
package com.github.ucchyocean.aqua;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;

/**
 * 再帰的に同名ファイル探索するためのファイルリスト
 * @author ucchy
 */
public class FolderFileList {

    private String[] list;
    private String oldBase;
    private String newBase;

    /**
     * コンストラクタ
     * @param oldBase 古いファイルが格納されているフォルダ
     * @param newBase 新しいファイルが格納されているフォルダ
     */
    public FolderFileList(String oldBase, String newBase) {
        list = createFileList(oldBase, newBase);
    }

    /**
     * 相対パスのファイルリストを取得する
     * @return ファイルリスト
     */
    public String[] getList() {
        return list;
    }

    /**
     * @return oldBase
     */
    public String getOldBase() {
        return oldBase;
    }

    /**
     * @return newBase
     */
    public String getNewBase() {
        return newBase;
    }

    // ファイルリストを作成する
    private String[] createFileList(String oldBase, String newBase) {

        ArrayList<String> oldVec = createFileListInternal(oldBase, null);
        if ( oldVec.size() == 1 && oldBase.endsWith(oldVec.get(0)) ) {
            // ファイル比較
            oldBase = oldBase.substring(0, oldBase.length() - oldVec.get(0).length() - 1);
        }
        this.oldBase = oldBase;

        ArrayList<String> newVec = createFileListInternal(newBase, null);
        if ( newVec.size() == 1 && newBase.endsWith(newVec.get(0)) ) {
            // ファイル比較
            newBase = newBase.substring(0, newBase.length() - newVec.get(0).length() - 1);
        }
        this.newBase = newBase;

        // 2つのリストをマージ
        for ( int i=0; i<newVec.size(); i++ ) {
            if ( oldVec.contains(newVec.get(i)) ) {
                oldVec.remove(newVec.get(i));
            }
        }
        newVec.addAll(oldVec);

        // 辞書順でソート
        Collections.sort(newVec);

        // 配列に変換
        String[] output = new String[newVec.size()];
        newVec.toArray(output);

        return output;
    }

    // フォルダ内のファイルを再帰的に探索する
    private ArrayList<String> createFileListInternal(String basedir, String filename) {

        ArrayList<String> output = new ArrayList<String>();

        if ( basedir == null ) return output;

        File file;
        if ( filename == null || filename.length() == 0 ) {
            file = new File(basedir);
        } else {
            file = new File(basedir, filename);
        }

        if ( file.isFile() ) {
            if ( filename == null ) {
                output.add(file.getName());
            } else {
                output.add(filename);
            }
            return output;
        }

        if ( file.isDirectory() ) {
            File[] children = file.listFiles();
            if ( children != null ) {
                for ( int i=0; i<children.length; i++ ) {
                    output.addAll( createFileListInternal( basedir,
                            children[i].getAbsolutePath().replace(basedir, "") ) );
                }
            }
        }

        return output;
    }
}
