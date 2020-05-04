/*
 * @author     ucchy
 * @license    LGPLv3
 * @copyright  Copyright ucchy 2020
 */
package com.github.ucchyocean.aqua.ui;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Vector;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

/**
 *
 * @author ucchy
 */
public class SortTable implements SelectionListener {

    private Composite parent;
    private Table table;
    private TableColumn[] columns;
    private int lastSelectedIndex;
    private Vector<Integer> integerIndex;

    public SortTable (Composite parent, int style, String[] labels) {

        this.parent = parent;
        table = new Table(parent, style);
        table.setLinesVisible(true);
        table.setHeaderVisible(true);

        integerIndex = new Vector<Integer>();

        init(labels);
    }

    private void init(String[] labels) {

        columns = new TableColumn[labels.length];
        for ( int i=0; i<labels.length; i++ ) {
            columns[i] = new TableColumn(table, SWT.LEFT);
            columns[i].setText(labels[i]);
            columns[i].addSelectionListener(this);
        }

        packColumns();
    }

    public void setIntegerIndex(int index) {
        integerIndex.add(index);
    }

    public void addContents(String[] data) {

        TableItem item = new TableItem(table, SWT.NONE);
        item.setText(data);
    }

    public void packColumns() {

        for ( int i=0; i<columns.length; i++ ) {
            columns[i].pack();
        }
    }

    public void removeAllContents() {

        table.removeAll();
    }

    public String[][] getContents() {

        TableItem[] items = table.getItems();
        String[][] output = new String[items.length+1][];
        int col = columns.length;

        output[0] = new String[col];
        for ( int i=0; i<col; i++ ) {
            output[0][i] = columns[i].getText();
        }

        for ( int i=0; i<items.length; i++ ) {
            output[i+1] = new String[col];
            for ( int j=0; j<col; j++ ) {
                output[i+1][j] = items[i].getText(j);
            }
        }

        return output;
    }

    public void setLayoutData(Object layoutData) {
        table.setLayoutData(layoutData);
    }

    public void setItemSelectionListener(SelectionListener listener) {
        table.addSelectionListener(listener);
    }

    /**
     * @see org.eclipse.swt.events.SelectionListener#widgetDefaultSelected(org.eclipse.swt.events.SelectionEvent)
     */
    public void widgetDefaultSelected(SelectionEvent event) {
        // do nothing.
    }

    /**
     * @see org.eclipse.swt.events.SelectionListener#widgetSelected(org.eclipse.swt.events.SelectionEvent)
     */
    public void widgetSelected(SelectionEvent event) {

        int index = -1;
        for (int i=0; i<columns.length; i++) {
            if (event.getSource() == columns[i]) {
                index = i;
            }
        }
        if (index == -1) {
            return;
        }

        final int selectedIndex = index;
        final boolean reverse;
        if ( selectedIndex == lastSelectedIndex ) {
            reverse = true;
            lastSelectedIndex = -1;
        } else {
            reverse = false;
            lastSelectedIndex = selectedIndex;
        }

        TableItem[] items = table.getItems();
        String[][] array = new String[items.length][table.getColumnCount()];
        for ( int i=0; i<array.length; i++ ) {
            for ( int j=0; j<array[i].length; j++ ) {
                array[i][j] = items[i].getText(j);
            }
        }

        Comparator<String[]> comparetor = new Comparator<String[]>(){
            public int compare(String[] o1, String[] o2) {

                if ( integerIndex.contains(selectedIndex) ) {
                    int i1 = Integer.parseInt(o1[selectedIndex]);
                    int i2 = Integer.parseInt(o2[selectedIndex]);
                    if ( reverse ) {
                        return (i1 < i2) ? 1 : ((i1 == i2) ? 0 : -1);
                    } else {
                        return (i1 < i2) ? -1 : ((i1 == i2) ? 0 : 1);
                    }
                }
                if ( reverse ) {
                    return -o1[selectedIndex].compareTo(o2[selectedIndex]);
                } else {
                    return o1[selectedIndex].compareTo(o2[selectedIndex]);
                }
            }
        };
        Arrays.sort(array, comparetor);

        for (int i=0; i<items.length; i++){
            String[] strs = (String[])array[i];
            items[i].setText(strs);
        }
        table.redraw();
    }

    /**
     * @return parent を戻します。
     */
    public Composite getParent() {
        return parent;
    }

}
