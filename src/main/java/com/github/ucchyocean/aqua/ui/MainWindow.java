/*
 * @author     ucchy
 * @license    LGPLv3
 * @copyright  Copyright ucchy 2020
 */
package com.github.ucchyocean.aqua.ui;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

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

import com.github.ucchyocean.aqua.FolderDifferResult;
import com.github.ucchyocean.aqua.Messages;

/**
 * メインウインドウ
 * @author ucchy
 */
public class MainWindow {

    private Shell shell;

    private MainWindowMenu menu;
    private PathInputComposite srcComp;
    private PathInputComposite distComp;
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
        srcComp = new PathInputComposite(parent, Messages.MESSAGE_SOURCE);
        srcComp.getComposite().setLayoutData( new GridData(GridData.FILL_HORIZONTAL) );

        // 比較先
        distComp = new PathInputComposite(parent, Messages.MESSAGE_DISTINATION);
        distComp.getComposite().setLayoutData( new GridData(GridData.FILL_HORIZONTAL) );

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
                SWT.BORDER | SWT.FULL_SELECTION, Messages.TABLE_LABELS.split(";"));
        GridData gdata = new GridData(GridData.FILL_BOTH);
        gdata.heightHint = 500;
        gdata.widthHint = 800;
        table.setLayoutData( gdata );

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

    protected void runCompare() {

        // 全クリア
        table.removeAllContents();

        // これ以降は処理が重いのでスレッドで動作
        thread = new CompareThread(this, srcComp.getText(), distComp.getText() );
        thread.start();
    }

    protected void export() {

        if ( lastData == null ) {
            return;
        }

        FileDialog dialog = new FileDialog( shell, SWT.SAVE );
        String [] exts = Messages.EXPORT_SUFS.split(";");
        String [] filterNames = Messages.EXPORT_DESCS.split(";");
        dialog.setFilterExtensions(exts);
        dialog.setFilterNames(filterNames);
        String file = dialog.open();
        if ( file == null ) {
            return;
        }

        String[] exportData = lastData.getExportData( table.getContents() );
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter( new FileWriter(file) );
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

        } finally {
            try {
                writer.close();
            } catch (IOException e) {
                // do nothing.
            }
        }
    }

    public void setControlEnable(boolean enable) {

        srcComp.setEnable(enable);
        distComp.setEnable(enable);
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
}
