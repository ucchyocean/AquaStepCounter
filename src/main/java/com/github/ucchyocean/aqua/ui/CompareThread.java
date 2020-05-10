/*
 * @author     ucchy
 * @license    LGPLv3
 * @copyright  Copyright ucchy 2020
 */
package com.github.ucchyocean.aqua.ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MessageBox;

import com.github.ucchyocean.aqua.AquaStepCounter;
import com.github.ucchyocean.aqua.FileDiffer;
import com.github.ucchyocean.aqua.FileDifferResult;
import com.github.ucchyocean.aqua.FolderDifferResult;
import com.github.ucchyocean.aqua.FolderFileList;
import com.github.ucchyocean.aqua.Messages;
import com.github.ucchyocean.aqua.config.CommentConfig;
import com.github.ucchyocean.aqua.config.CommentConfigManager;

/**
 * MainWindowのフォルダ比較処理スレッド
 * @author ucchy
 */
public class CompareThread extends Thread {

    private boolean canceled = false;
    private boolean running = false;

    private Display display;
    private MainWindow main;
    private String newFolder;
    private String oldFolder;

    public CompareThread(MainWindow main, String newFolder, String oldFolder) {
        this.display = main.getShell().getDisplay();
        this.main = main;
        this.newFolder = newFolder;
        this.oldFolder = oldFolder;
    }

    public void start(){
        new Thread(this).start();
    }

    public void run(){

        running = true;

        // UI更新処理 ダイアログのコントロール関連を全て無効にする
        display.asyncExec( new Runnable() {
            public void run() {
                main.setControlEnable(false);
                main.setStatus(Messages.MESSAGE_COUNT_RUNNING);
            }
        });

        FolderFileList list = new FolderFileList(oldFolder, newFolder);
        if ( !oldFolder.equals(list.getOldBase()) ) oldFolder = list.getOldBase();
        if ( !newFolder.equals(list.getNewBase()) ) newFolder = list.getNewBase();

        FolderDifferResult results = new FolderDifferResult(oldFolder, newFolder);

        boolean deleteSpaces = AquaStepCounter.getPrefs().isStripWhite();
        boolean deleteComments = AquaStepCounter.getPrefs().isStripComment();

        CommentConfigManager manager = AquaStepCounter.getCommentConfigManager();

        try {
            for ( final String file : list.getList() ) {

                if ( canceled ) {
                    // キャンセルが発生したらInterruptedExceptionを投げて抜ける
                    // あまり良くない方法かもしれない。
                    throw new InterruptedException(Messages.MESSAGE_INTERRUPT);
                }

                CommentConfig config = manager.getConfig(getSuffix(file));
                if ( config == null ) continue;

                // UI更新処理 ステータスバーを更新
                display.asyncExec( new Runnable() {
                    public void run() {
                        main.setStatus(Messages.MESSAGE_COUNT_RUNNING + file);
                    }
                });

                // diffの実行
                FileDiffer differ = new FileDiffer(oldFolder, newFolder, file, config);
                if ( deleteComments ) differ.deleteComments();
                if ( deleteSpaces ) differ.deleteSpaces();

                FileDifferResult result = differ.getDiffData();
                results.addResult(file, result);

                // UI表示用データの作成
                final String[] tableItemData = result.getAllAsStringArray();

                // UI更新処理 1組のファイルの比較結果を追加
                display.asyncExec( new Runnable() {
                    public void run() {
                        main.getTable().addContents(tableItemData);
                    }
                });
            }

            // UI更新処理 列のpack ステータスバーにトータルを表示
            display.asyncExec( new Runnable() {
                public void run() {
                    int[] totalData = results.getTotalData();
                    main.getTable().packColumns();
                    main.setStatus( String.format(
                            "TOTAL : nochanged-> %d  edited-> %d  added-> %d  deleted-> %d",
                            totalData[0], totalData[1], totalData[2], totalData[3] ) );
                }
            });

            main.setLastData(results);

        } catch (InterruptedException e) {

            // UI更新処理 キャンセル処理
            display.asyncExec( new Runnable() {
                public void run() {
                    MessageBox box = new MessageBox(main.getShell(), SWT.ICON_ERROR);
                    box.setText(Messages.TITLE_CANCELED);
                    box.setMessage(Messages.MESSAGE_INTERRUPT);
                    box.open();
                    main.setStatus(Messages.MESSAGE_INTERRUPT);
                }
            });
            main.setLastData(null);

        } catch (Exception e) {

            e.printStackTrace();
            // UI更新処理 エラーメッセージの表示
            display.asyncExec( new Runnable() {
                public void run() {
                    MessageBox box = new MessageBox(main.getShell(), SWT.ICON_ERROR);
                    box.setText(Messages.TITLE_EXCEPTION);
                    box.setMessage(e.toString());
                    box.open();
                    main.setStatus(Messages.MESSAGE_ERROR);
                }
            });
            main.setLastData(null);

        } finally {

            // UI更新処理 ダイアログのコントロール関連を全て有効にする
            display.asyncExec( new Runnable() {
                public void run() {
                    main.setControlEnable(true);
                }
            });

            running = false;
        }
    }

    public void cancel(){
        canceled = true;
    }

    public boolean isRunning() {
        return running;
    }

    private String getSuffix(String filename) {

        int index = filename.lastIndexOf(".");
        if ( index == -1 ) return "";
        return filename.substring(index);
    }
}
