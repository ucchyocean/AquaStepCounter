/*
 * @author     ucchy
 * @license    LGPLv3
 * @copyright  Copyright ucchy 2020
 */
package com.github.ucchyocean.aqua;

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

    public AquaStepCounter() {

        // 設定のロード
        config = AquaStepCounterConfig.load();

        // コメントコンフィグのロード
        manager = CommentConfigManager.loadFromDefaultFiles();
    }

    public static AquaStepCounter getInstance() {
        return instance;
    }

    public static AquaStepCounterConfig getConfig() {
        return instance.config;
    }

    public static CommentConfigManager getCommentConfigManager() {
        return instance.manager;
    }

    /**
     * エントリポイント
     * @param args
     */
    public static void main(String[] args) {

        MainWindow window = new MainWindow();
        window.open();
    }
}
