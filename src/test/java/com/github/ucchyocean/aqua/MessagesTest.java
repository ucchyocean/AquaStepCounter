/*
 * @author     ucchy
 * @license    LGPLv3
 * @copyright  Copyright ucchy 2020
 */
package com.github.ucchyocean.aqua;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Locale;

import junit.framework.TestCase;

/**
 *
 * @author ucchy
 */
public class MessagesTest extends TestCase {

    public void testMessages() {
        assertTrue(Messages.TABLE_LABELS != null);
    }

    public void testReadDefaultConfig() {
        InputStream stream = getResourceInputStream();

        assertTrue(stream != null);

        try {
            YamlConfig config = YamlConfig.load(stream);
            String data = config.getRawData();
            System.out.println(data);
            assertTrue(data.length() > 0);

            ArrayList<String> labels = config.getStringArray("TABLE_LABELS", null);
            assertTrue(labels != null);

        } catch (IOException e) {
            e.printStackTrace();
            assertTrue(false);
        }
    }

    private static InputStream getResourceInputStream() {

        String baseName = "messages";
        Locale locale = Locale.getDefault();

        ArrayList<String> candidates = new ArrayList<String>();
        candidates.add(String.format("/%s_%s_%s.yaml", baseName, locale.getLanguage(), locale.getCountry()));
        candidates.add(String.format("/%s_%s_%s.yml", baseName, locale.getLanguage(), locale.getCountry()));
        candidates.add(String.format("/%s_%s.yaml", baseName, locale.getLanguage()));
        candidates.add(String.format("/%s_%s.yml", baseName, locale.getLanguage()));
        candidates.add("/" + baseName + ".yaml");
        candidates.add("/" + baseName + ".yml");

        for ( String name : candidates ) {
            InputStream stream = Messages.class.getResourceAsStream(name);
            if ( stream != null ) return stream;
        }
        return null;
    }
}
