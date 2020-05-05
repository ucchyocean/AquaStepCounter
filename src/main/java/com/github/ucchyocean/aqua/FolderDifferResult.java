/*
 * @author     ucchy
 * @license    LGPLv3
 * @copyright  Copyright ucchy 2020
 */
package com.github.ucchyocean.aqua;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Set;

/**
 * FolderDifferの結果格納クラス
 * @author ucchy
 */
public class FolderDifferResult {

    private HashMap<String, FileDifferResult> results;

    private String oldFolder;
    private String newFolder;
    private String date;

    public FolderDifferResult(String oldFolder, String newFolder) {
        results = new HashMap<String, FileDifferResult>();
        this.oldFolder = oldFolder;
        this.newFolder = newFolder;
        this.date = new Date().toString();
    }

    public void addResult(String key, FileDifferResult result) {
        results.put(key, result);
    }

    public Set<String> getKeys() {
        return results.keySet();
    }

    public FileDifferResult getResult(String key) {
        return results.get(key);
    }

    public String[] getExportData(String[][] data) {

        ArrayList<String> temp = new ArrayList<String>();

        temp.add("target directory = " + newFolder);
        temp.add("reference directory = " + oldFolder);
        temp.add("date = " + date);
        temp.add("");
        for ( int i=0; i<data.length; i++ ) {
            String line = new String();
            for ( int j=0; j<Messages.TABLE_LABELS.length; j++ ) {
                line += data[i][j] + ", ";
            }
            temp.add(line);
        }
        temp.add("");
        String lastLine = "all, , ";
        int[] totalData = getTotalData();
        for ( int i=0; i<4; i++ ) {
            lastLine += totalData[i] + ", ";
        }
        temp.add(lastLine);

        String[] output = new String[temp.size()];
        temp.toArray(output);
        return output;
    }

    public int[] getTotalData() {

        int[] data = new int[4];

        for ( FileDifferResult r : results.values() ) {
            data[0] += r.getNoChanged();
            data[1] += r.getEdited();
            data[2] += r.getAdded();
            data[3] += r.getDeleted();
        }

        return data;
    }
}
