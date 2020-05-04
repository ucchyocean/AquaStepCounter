/*
 * @author     ucchy
 * @license    LGPLv3
 * @copyright  Copyright ucchy 2020
 */
package com.github.ucchyocean.aqua.ui;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;

/**
 * アイコン用Imageオブジェクト管理クラス
 * @author ucchy
 */
public class IconLoader {

    public static Image AQUA_DUCK_SMALL;
    public static Image CAR;
    public static Image COMMENT;
    public static Image DOOR_OUT;
    public static Image HELP;
    public static Image WORLD;
    public static Image WRENCH;

    static {
        try {
            AQUA_DUCK_SMALL = loadIcon("/icons/aqua_duck_small.png");
            CAR = loadIcon("/icons/car.png");
            COMMENT = loadIcon("/icons/comment.png");
            DOOR_OUT = loadIcon("/icons/door_out.png");
            HELP = loadIcon("/icons/help.png");
            WORLD = loadIcon("/icons/world.png");
            WRENCH = loadIcon("/icons/wrench.png");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private IconLoader() {
    }

    /**
     * @throws Throwable
     * @see java.lang.Object#finalize()
     */
    @Override
    protected void finalize() throws Throwable {
        disposeIcon(AQUA_DUCK_SMALL);
        disposeIcon(CAR);
        disposeIcon(COMMENT);
        disposeIcon(DOOR_OUT);
        disposeIcon(HELP);
        disposeIcon(WORLD);
        disposeIcon(WRENCH);
        super.finalize();
    }

    // resourcePathで指定されたリソースを取得し、Imageとしてロードします。
    private static Image loadIcon(String resourcePath) throws IOException, FileNotFoundException {

        InputStream is = IconLoader.class.getResourceAsStream(resourcePath);
        if ( is != null ) {
            Image iconImage = new Image(Display.getCurrent(), is);
            is.close();
            return iconImage;
        }
        throw new FileNotFoundException("Not found image file : " + resourcePath);
    }

    // 指定されたiconをdisposeします。
    private static void disposeIcon(Image icon) {
        if ( icon == null ) {
            return;
        }
        icon.dispose();
    }
}
