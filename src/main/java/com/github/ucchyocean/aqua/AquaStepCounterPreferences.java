/*
 * @author     ucchy
 * @license    LGPLv3
 * @copyright  Copyright ucchy 2020
 */
package com.github.ucchyocean.aqua;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * Aqua Step Counterの設定管理クラス
 * @author ucchy
 */
public class AquaStepCounterPreferences {

    /** コメントを除去するかどうか */
    private boolean stripComment = true;
    /** 空白やタブ文字や連続する改行を除去するかどうか */
    private boolean stripWhite = true;

    private AquaStepCounterPreferences() {
    }

    public static AquaStepCounterPreferences load() {

        AquaStepCounterPreferences config = new AquaStepCounterPreferences();

        // 現在のフォルダに "preferences.yml" がある場合は、その内容をロードして反映する。
        String cd = new File(".").getAbsoluteFile().getParent();
        File confFile = new File(cd, "preferences.yml");
        if ( confFile.exists() ) {
            try (FileReader reader = new FileReader(confFile) ) {
                YamlConfig yaml = YamlConfig.load(reader);
                config.stripComment = yaml.getBoolean("stripComment", config.stripComment);
                config.stripWhite = yaml.getBoolean("stripWhite", config.stripWhite);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return config;
    }

    public void save() {

        // 現在のフォルダに "preferences.yml" として保存する。
        String cd = new File(".").getAbsoluteFile().getParent();
        File confFile = new File(cd, "preferences.yml");

        YamlConfig yaml = new YamlConfig();
        yaml.set("stripComment", stripComment);
        yaml.set("stripWhite", stripWhite);
        try {
            yaml.save(confFile);
        } catch (IOException e) {
            // ignore.
        }
    }

    /**
     * @return stripComment
     */
    public boolean isStripComment() {
        return stripComment;
    }

    /**
     * @param stripComment stripComment
     */
    public void setStripComment(boolean stripComment) {
        this.stripComment = stripComment;
    }

    /**
     * @return stripWhite
     */
    public boolean isStripWhite() {
        return stripWhite;
    }

    /**
     * @param stripWhite stripWhite
     */
    public void setStripWhite(boolean stripWhite) {
        this.stripWhite = stripWhite;
    }
}
