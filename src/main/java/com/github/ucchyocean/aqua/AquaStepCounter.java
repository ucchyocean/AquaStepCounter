/*
 * @author     ucchy
 * @license    LGPLv3
 * @copyright  Copyright ucchy 2020
 */
package com.github.ucchyocean.aqua;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import com.github.ucchyocean.aqua.config.CommentConfigManager;
import com.github.ucchyocean.aqua.ui.MainWindow;

/**
 * Aqua Step Counter
 * @author ucchy
 */
public class AquaStepCounter {

    private static final AquaStepCounter instance = new AquaStepCounter();

    private CommentConfigManager manager;
    private AquaStepCounterConfig config;

    /**
     * コンストラクタ
     */
    public AquaStepCounter() {

        // 設定のロード
        config = AquaStepCounterConfig.load();

        // コメントコンフィグのロード
        manager = CommentConfigManager.loadFromDefaultFiles();
    }

    /**
     * インスタンスを取得する
     * @return インスタンス
     */
    public static AquaStepCounter getInstance() {
        return instance;
    }

    /**
     * コンフィグを取得する
     * @return コンフィグ
     */
    public static AquaStepCounterConfig getConfig() {
        return instance.config;
    }

    /**
     * コメント解析管理クラスを取得する
     * @return コメント解析管理クラス
     */
    public static CommentConfigManager getCommentConfigManager() {
        return instance.manager;
    }

    /**
     * エントリポイント
     * @param args
     */
    public static void main(String[] args) {

        CommandLineParser clp = new CommandLineParser(args);

        if ( clp.hasParam(CommandLineParser.KEY_HELP) ) {
            // ヘルプ表示
            System.out.println(Messages.SOFTWARE_NAME + " - v" + Messages.SOFTWARE_VERSION);
            System.out.println();
            System.out.println("Usage: AquaStepCounter.exe "
                    + "[-?] [-ui] [-new (folder path)] [-old (folder path)] [-r (report file path)]");
            System.out.println();
            return;
        }

        // UI表示モードかコマンドラインモードか
        boolean showUI = clp.size() == 0
                || clp.hasParam(CommandLineParser.KEY_UI)
                || (!clp.hasParam(CommandLineParser.KEY_NEW) && !clp.hasParam(CommandLineParser.KEY_OLD));

        // 設定関連
        boolean configModified = false;
        if ( clp.hasParam(CommandLineParser.KEY_DC) ) {
            getConfig().setStripComment(true);
            configModified = true;
        }
        if ( clp.hasParam(CommandLineParser.KEY_CC) ) {
            getConfig().setStripComment(false);
            configModified = true;
        }
        if ( clp.hasParam(CommandLineParser.KEY_DW) ) {
            getConfig().setStripWhite(true);
            configModified = true;
        }
        if ( clp.hasParam(CommandLineParser.KEY_CW) ) {
            getConfig().setStripWhite(false);
            configModified = true;
        }
        if ( showUI && configModified ) {
            // UI表示モードで、コンフィグが変更されている場合は、保存を行う
            getConfig().save();
        }

        // メインの処理
        if ( showUI ) {
            // UI表示モード

            MainWindow window = new MainWindow();
            boolean runThread = false;
            if ( clp.hasParam(CommandLineParser.KEY_NEW) ) {
                window.setNewFolder(clp.get(CommandLineParser.KEY_NEW));
                runThread = true;
            }
            if ( clp.hasParam(CommandLineParser.KEY_OLD) ) {
                window.setOldFolder(clp.get(CommandLineParser.KEY_OLD));
                runThread = true;
            }
            if ( runThread ) window.runCompare();

            window.open();

        } else {
            // コマンドライン実行モード

            String oldFolder = clp.get(CommandLineParser.KEY_OLD);
            String newFolder = clp.get(CommandLineParser.KEY_NEW);
            FolderDiffer differ = new FolderDiffer(oldFolder, newFolder,
                    getConfig().isStripWhite(), getConfig().isStripComment());
            FolderDifferResult result = differ.getDiffData();
            String[] exportData = result.getExportData();

            if ( clp.hasParam(CommandLineParser.KEY_REPORT) ) {
                // 実行結果をレポートへ出力
                String report = clp.get(CommandLineParser.KEY_REPORT);

                try (BufferedWriter writer = new BufferedWriter( new FileWriter(report) )) {
                    for ( int i=0; i<exportData.length; i++ ) {
                        writer.write(exportData[i]);
                        writer.newLine();
                    }
                    writer.flush();
                } catch (IOException e) {
                    e.printStackTrace();

                    // 終了コード1で終了
                    System.exit(1);
                }

            } else {
                // 実行結果を画面へ出力
                for ( String line : exportData ) {
                    System.out.println(line);
                }
            }
        }
    }
}
