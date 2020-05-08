/*
 * @author     ucchy
 * @license    LGPLv3
 * @copyright  Copyright ucchy 2020
 */
package com.github.ucchyocean.aqua.ui;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TableItem;

import com.github.ucchyocean.aqua.FileDifferResult;
import com.github.ucchyocean.aqua.FolderDifferResult;
import com.github.ucchyocean.aqua.Messages;

/**
 * メインウインドウ
 * @author ucchy
 */
public class MainWindow {

    private Shell shell;

    private MainWindowMenu menu;
    private PathInputComposite newFolderComp;
    private PathInputComposite oldFolderComp;
    private SortTable table;
    private Button runButton;
    private Button clearButton;
    private Button exportButton;
    private Label statusBar;

    private FolderDifferResult lastData;
    private CompareThread thread;

    public MainWindow() {

        Display display = new Display();
        shell = new Shell(display);

        createContents(shell);

        menu = new MainWindowMenu(this);
    }

    private Control createContents(Composite parent) {

        shell.setText(Messages.SOFTWARE_NAME + " - ver." + Messages.SOFTWARE_VERSION);
        shell.setImage(IconLoader.AQUA_DUCK_SMALL);

        parent.setLayout( new GridLayout(1, false) );

        // メッセージ
        Label message = new Label(parent, SWT.NONE);
        message.setLayoutData( new GridData(GridData.FILL_HORIZONTAL) );
        message.setText(Messages.MESSAGE_MAIN);

        // 比較元
        newFolderComp = new PathInputComposite(parent, Messages.MESSAGE_NEWFOLDER);
        newFolderComp.getComposite().setLayoutData( new GridData(GridData.FILL_HORIZONTAL) );

        // 比較先
        oldFolderComp = new PathInputComposite(parent, Messages.MESSAGE_OLDFOLDER);
        oldFolderComp.getComposite().setLayoutData( new GridData(GridData.FILL_HORIZONTAL) );

        // ボタンバー
        Composite buttonComp = new Composite(parent, SWT.NONE);
        buttonComp.setLayoutData( new GridData(GridData.FILL_HORIZONTAL) );
        buttonComp.setLayout( new GridLayout(3, false) );

        runButton = new Button(buttonComp, SWT.PUSH);
        runButton.setLayoutData( new GridData(
                GridData.GRAB_HORIZONTAL | GridData.HORIZONTAL_ALIGN_END) );
        runButton.setText(Messages.MESSAGE_RUN);
        runButton.addSelectionListener( new SelectionAdapter() {
            public void widgetSelected(SelectionEvent event) {
                if ( thread == null || !thread.isRunning() ) {
                    runCompare();
                } else {
                    thread.cancel();
                    setControlEnable(true);
                    setStatus(Messages.MESSAGE_INTERRUPT);
                }
            }
        });
        shell.setDefaultButton(runButton);

        clearButton = new Button(buttonComp, SWT.PUSH);
        clearButton.setLayoutData( new GridData(GridData.END) );
        clearButton.setText(Messages.MESSAGE_CLEAR);
        clearButton.addSelectionListener( new SelectionAdapter() {
            public void widgetSelected(SelectionEvent event) {
                table.removeAllContents();
            }
        });

        exportButton = new Button(buttonComp, SWT.PUSH);
        exportButton.setLayoutData( new GridData(GridData.END) );
        exportButton.setText(Messages.MESSAGE_EXPORT);
        exportButton.addSelectionListener( new SelectionAdapter() {
            public void widgetSelected(SelectionEvent event) {
                export();
            }
        });

        // テーブル
        table = new SortTable(parent,
                SWT.BORDER | SWT.FULL_SELECTION, convertStringList(Messages.TABLE_LABELS));
        GridData gdata = new GridData(GridData.FILL_BOTH);
        gdata.heightHint = 500;
        gdata.widthHint = 800;
        table.setLayoutData( gdata );
        table.setItemSelectionListener( new SelectionAdapter() {
            public void widgetDefaultSelected(SelectionEvent e) {
                onSelectedTableItem(e);
            }
        });

        // 3～6列目は整数値としてソートするように指示する。残りは文字列（辞書式）ソート。
        table.setIntegerIndex(2);
        table.setIntegerIndex(3);
        table.setIntegerIndex(4);
        table.setIntegerIndex(5);

        // ステータスバー
        statusBar = new Label(shell, SWT.NULL);
        statusBar.setLayoutData( new GridData(GridData.FILL_HORIZONTAL) );

        return parent;
    }

    public void runCompare() {

        // 全クリア
        table.removeAllContents();

        // これ以降は処理が重いのでスレッドで動作
        thread = new CompareThread(this, newFolderComp.getText(), oldFolderComp.getText() );
        thread.start();
    }

    protected void export() {

        if ( lastData == null ) {
            return;
        }

        FileDialog dialog = new FileDialog( shell, SWT.SAVE );
        String[] exts = convertStringList(Messages.EXPORT_SUFS);
        String[] filterNames = convertStringList(Messages.EXPORT_DESCS);
        dialog.setFilterExtensions(exts);
        dialog.setFilterNames(filterNames);
        String file = dialog.open();
        if ( file == null ) {
            return;
        }

        String[] exportData = lastData.getExportData( table.getContents() );
        try (BufferedWriter writer = new BufferedWriter( new FileWriter(file) )) {
            for ( int i=0; i<exportData.length; i++ ) {
                writer.write(exportData[i]);
                writer.newLine();
            }
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
            MessageBox box = new MessageBox(shell, SWT.ICON_ERROR);
            box.setText(Messages.TITLE_EXCEPTION);
            box.setMessage(e.toString());
            box.open();
        }
    }

    public void setControlEnable(boolean enable) {

        newFolderComp.setEnable(enable);
        oldFolderComp.setEnable(enable);
        clearButton.setEnabled(enable);
        exportButton.setEnabled(enable);
        menu.setEnable(enable);

        if ( enable ) {
            runButton.setText(Messages.MESSAGE_RUN);
        } else {
            runButton.setText(Messages.MESSAGE_CANCEL);
        }
    }

    protected Shell getShell() {
        return shell;
    }

    protected SortTable getTable() {
        return table;
    }

    protected void setLastData(FolderDifferResult data) {
        lastData = data;
    }

    protected void setStatus(String status) {
        statusBar.setText(status);
    }

    public void open() {

        Display display = shell.getDisplay();
        shell.open();
        while ( !shell.isDisposed() ) {
            if ( !display.readAndDispatch() ) {
                display.sleep();
            }
        }
        display.dispose();
    }

    protected void close() {
        shell.close();
    }

    private String[] convertStringList(ArrayList<String> list) {
        String[] dest = new String[list.size()];
        list.toArray(dest);
        return dest;
    }

    public void setOldFolder(String oldFolder) {
        oldFolderComp.setText(oldFolder);
    }

    public void setNewFolder(String newFolder) {
        newFolderComp.setText(newFolder);
    }

    private void onSelectedTableItem(SelectionEvent event) {
        if (lastData == null) return;

        // 選択した行の実行結果を取得
        TableItem item = (TableItem)event.item;
        String key = item.getText(0) + File.separator + item.getText(1);
        FileDifferResult res = lastData.getResult(key);

        if ( res != null && res.getRev() != null ) {
            MessageBox box = new MessageBox(shell, SWT.NONE);
            box.setMessage(res.getRev().toString());
            box.open();
        }
    }
}
