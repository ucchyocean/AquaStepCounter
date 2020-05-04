/*
 * @author     ucchy
 * @license    LGPLv3
 * @copyright  Copyright ucchy 2020
 */
package com.github.ucchyocean.aqua;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.CodeSource;
import java.security.ProtectionDomain;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;

/**
 * Jarファイル操作関連のユーティリティクラス
 * @author ucchy
 */
public class JarUtility {

    /**
     * jarファイルの中に格納されているフォルダを、中のファイルごとまとめてjarファイルの外にコピーするメソッド
     * @param targetFile コピー先のフォルダ
     * @param sourceFilePath コピー元のフォルダ
     */
    public static void copyFolderFromJar(File targetFilePath, String sourceFilePath) {

        if ( !targetFilePath.exists() ) {
            targetFilePath.mkdirs();
        }

        try (JarFile jar = new JarFile(getApplicationPathFile())) {

            Enumeration<JarEntry> entries = jar.entries();

            while ( entries.hasMoreElements() ) {

                JarEntry entry = entries.nextElement();
                if ( !entry.isDirectory() && entry.getName().startsWith(sourceFilePath) ) {

                    File targetFile = new File(targetFilePath,
                            entry.getName().substring(sourceFilePath.length() + 1));
                    if ( !targetFile.getParentFile().exists() ) {
                        targetFile.getParentFile().mkdirs();
                    }

                    try (BufferedReader reader = new BufferedReader(new InputStreamReader(
                                jar.getInputStream(entry), "UTF-8"));
                         BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
                                 new FileOutputStream(targetFile), "UTF-8"))) {

                        String line;
                        while ((line = reader.readLine()) != null) {
                            writer.write(line);
                            writer.newLine();
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<String> getFileList(String sourceFolderPath) {

        ArrayList<String> list = new ArrayList<String>();

        File appPath = getApplicationPathFile();

        if ( appPath.getName().toLowerCase().endsWith(".jar") ||
                appPath.getName().toLowerCase().endsWith(".exe") ) {
            // jarファイル実行

            try (JarFile jar = new JarFile(appPath)) {

                Enumeration<JarEntry> entries = jar.entries();

                while ( entries.hasMoreElements() ) {

                    JarEntry entry = entries.nextElement();
                    if ( !entry.isDirectory() && entry.getName().startsWith(sourceFolderPath) ) {
                        list.add(entry.getName());
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        } else {
            // classファイル実行

            File folder = new File(appPath, sourceFolderPath);

            for ( String s : folder.list() ) {
                if ( sourceFolderPath.endsWith(File.separator) ) {
                    list.add(sourceFolderPath + s);
                } else {
                    list.add(sourceFolderPath + File.separator + s);
                }
            }
        }

        return list;
    }

    public static String getFileAsString(String sourceFilePath) {

        StringBuffer buffer = new StringBuffer();

        File appPath = getApplicationPathFile();

        if ( appPath.getName().toLowerCase().endsWith(".jar") ||
                appPath.getName().toLowerCase().endsWith(".exe") ) {
            // jarファイル実行

            try (JarFile jar = new JarFile(appPath)) {

                ZipEntry entry = jar.getEntry(sourceFilePath);

                try (BufferedReader reader = new BufferedReader(new InputStreamReader(
                        jar.getInputStream(entry), "UTF-8"))) {

                    String line;
                    while ((line = reader.readLine()) != null) {
                        buffer.append(line + "\n");
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

        } else {
            // classファイル実行

            File file = new File(appPath, sourceFilePath);

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(
                    new FileInputStream(file), "UTF-8"))) {

                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line + "\n");
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        return buffer.toString();
    }

    public static File getApplicationPathFile() {
        ProtectionDomain pd = JarUtility.class.getProtectionDomain();
        CodeSource cs = pd.getCodeSource();
        URL location = cs.getLocation();
        try {
            URI uri = location.toURI();
            Path path = Paths.get(uri);
            return path.toFile();
        } catch (URISyntaxException e) {
            // do nothing.
        }
        return null;
    }
}
