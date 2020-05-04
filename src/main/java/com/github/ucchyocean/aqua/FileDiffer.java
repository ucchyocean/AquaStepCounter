/*
 * @author     ucchy
 * @license    LGPLv3
 * @copyright  Copyright ucchy 2020
 */
package com.github.ucchyocean.aqua;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.apache.commons.jrcs.diff.AddDelta;
import org.apache.commons.jrcs.diff.ChangeDelta;
import org.apache.commons.jrcs.diff.DeleteDelta;
import org.apache.commons.jrcs.diff.Delta;
import org.apache.commons.jrcs.diff.Diff;
import org.apache.commons.jrcs.diff.DifferentiationFailedException;
import org.apache.commons.jrcs.diff.Revision;

import com.github.ucchyocean.aqua.config.CommentConfig;

/**
 * ファイル間の比較を行うクラス
 * @author ucchy
 */
public class FileDiffer {

    // 変更前のファイルの内容
    private String oldContent;
    // 変更後のファイルの内容
    private String newContent;
    // コメントの設定
    private CommentConfig config;

    /**
     * コンストラクタ
     * @param oldbase 変更前のファイルがあるフォルダ。nullを指定した場合は新しいファイルのみで比較される（全行が追加行としてカウントされる）
     * @param newbase 変更後のファイルがあるフォルダ。nullを指定した場合は古いファイルのみで比較される（全行が削除行としてカウントされる）
     * @param filename 比較するファイル名
     * @param config 比較に使用する設定
     */
    public FileDiffer(String oldbase, String newbase, String filename, CommentConfig config) {
        this.config = config;
        this.oldContent = getContent(new File(oldbase, filename));
        this.newContent = getContent(new File(newbase, filename));
    }

    public void deleteComments() throws IOException {

        if ( oldContent != null ) {
            oldContent = CommentStripper.deleteComments(oldContent, config);
        }
        if ( newContent != null ) {
            newContent = CommentStripper.deleteComments(newContent, config);
        }
    }

    public void deleteSpaces() {

        if ( oldContent != null ) {
            oldContent = deleteSpacesInternal(oldContent);
        }
        if ( newContent != null ) {
            newContent = deleteSpacesInternal(newContent);
        }
    }

    private String deleteSpacesInternal(String input) {

        String output = new String(input);

        // 全てのスペースとタブを削除
        output = output.replaceAll(" ", "");
        output = output.replaceAll("\t", "");

        // 全ての空行を削除
        while ( output.indexOf("\n\n") != -1 ) {
            output = output.replaceAll("\n\n", "\n");
        }
        if ( output.startsWith("\n") ) {
            output = output.substring(1);
        }

        return output;
    }

    private String getContent(File file) {

        if ( ! file.exists() ) {
            return null;
        }
        StringBuffer temp = new StringBuffer();
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(file));
            String line;
            while ( ( line = reader.readLine() ) != null ) {
                temp.append( line.trim() + "\n" );
            }
        } catch (FileNotFoundException e) {
            return null;
        } catch (IOException e) {
            return null;
        } finally {
            if ( reader != null ) {
                try {
                    reader.close();
                } catch (IOException e) {
                    // do nothing.
                }
            }
        }
        return temp.toString();
    }

    public FileDifferResult getDiffData() throws IOException, DifferentiationFailedException {

        FileDifferResult result = new FileDifferResult();

        if ( oldContent == null ) {
            if ( newContent == null ) {
                // 古いファイルも新しいファイルも存在しない
                return result;
            }

            // 古いファイルが存在しない場合は、新しいファイルの行数を追加行数としてカウントする
            String[] newContentTemp = newContent.split("\n");
            result.added = newContentTemp.length;
            return result;
        }

        String[] oldContentTemp = oldContent.split("\n");

        if ( newContent == null ) {
            // 新しいファイルが存在しない場合は、古いファイルの行数を削除行数としてカウントする
            result.deleted = oldContentTemp.length;
            return result;
        }

        String[] newContentTemp = newContent.split("\n");

        // diffを実行
        Revision rev = Diff.diff(oldContentTemp, newContentTemp);
        // TODO revのnullチェック必要？

        // 新しいファイルの行数を初期値として設定する
        result.noChanged = newContentTemp.length;

        for ( int i=0; i<rev.size(); i++ ) {
            Delta d = rev.getDelta(i);
            int original = d.getOriginal().size();
            int revised = d.getRevised().size();

            if ( d instanceof ChangeDelta ) {
                // 変更
                if ( original > revised ) {
                    result.noChanged -= revised;
                    result.edited += revised;
                    result.deleted += original - revised;
                } else {
                    result.noChanged -= revised;
                    result.edited += original;
                    result.added += revised - original;
                }
            } else if ( d instanceof AddDelta ) {
                // 追加
                result.noChanged -= revised;
                result.added += revised;
            } else if ( d instanceof DeleteDelta ) {
                // 削除
                result.deleted += original;
            } else {
                // 内部エラー
                throw new IOException("unknown delta type. : " + d.getClass() + " \"" + d.toString() + "\"");
            }
        }


        return result;
    }

    /**
     * @return newContent
     */
    public String getNewContent() {
        return newContent;
    }

    /**
     * @return oldContent
     */
    public String getOldContent() {
        return oldContent;
    }
}
