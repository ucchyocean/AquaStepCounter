/*
 * @author     ucchy
 * @license    LGPLv3
 * @copyright  Copyright ucchy 2020
 */
package com.github.ucchyocean.aqua.ui;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import com.github.ucchyocean.aqua.Messages;

/**
 * バージョン情報ダイアログ
 * @author ucchy
 */
public class InformationDialog {

    private Shell shell;

    /**
     * コンストラクタ
     * @param parent 親ダイアログ
     */
    protected InformationDialog(Shell parent) {
        shell = new Shell(parent, SWT.APPLICATION_MODAL | SWT.DIALOG_TRIM);
        createDialogArea(shell);
    }

    /**
     * コンテンツ部分を作成する
     * @param parent
     */
    private void createDialogArea(Composite parent) {

        shell.setText(Messages.TITLE_VERSION);
        shell.setImage(IconLoader.AQUA_DUCK_SMALL);
        shell.setSize(700, 400);

        parent.setLayout(new GridLayout(1, true));

        File file = putInformationToTempFile();

        Browser browser = new Browser(parent, SWT.NONE);
        browser.setUrl(file.getAbsolutePath());
        browser.setLayoutData(new GridData(GridData.FILL_BOTH));

        // ボタンバー
        Composite buttonComp = new Composite(parent, SWT.NONE);
        buttonComp.setLayoutData( new GridData(GridData.FILL_HORIZONTAL) );
        buttonComp.setLayout( new GridLayout(1, false) );

        Button okButton = new Button(buttonComp, SWT.PUSH);
        okButton.setLayoutData( new GridData(
                GridData.GRAB_HORIZONTAL | GridData.HORIZONTAL_ALIGN_END) );
        okButton.setText("OK");
        okButton.addSelectionListener( new SelectionAdapter() {
            public void widgetSelected(SelectionEvent event) {
                shell.close();
            }
        });
        shell.setDefaultButton(okButton);
    }

    /**
     * ダイアログを開く
     */
    protected void open() {
        shell.open();
    }

    /**
     * information.htmをjarから取り出してtempフォルダに一時配置する
     * @return 取り出されたinformation.htm
     */
    private File putInformationToTempFile() {

        // information.htmをjarファイルの中から取り出す
        InputStream inputStream = this.getClass().getResourceAsStream("/information.htm");
        ArrayList<String> contents = new ArrayList<String>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"))) {
            String line;
            while ( (line = reader.readLine()) != null ) {
                contents.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 読み取ったinformation.htmを、フォルダに配置する
        File folder = new File(System.getProperty("java.io.tmpdir"), "asc");
        if ( !folder.exists() ) folder.mkdirs();

        File file = new File(folder, "information.htm");
        try (BufferedWriter writer = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(file), "UTF-8"))) {
            for ( String l : contents ) {
                writer.write(l);
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return file;
    }

    // デバッグ用エントリ
    public static void main(String[] args) {
        InformationDialog dialog = new InformationDialog(null);

        Display display = dialog.shell.getDisplay();
        dialog.shell.open();
        while ( !dialog.shell.isDisposed() ) {
            if ( !display.readAndDispatch() ) {
                display.sleep();
            }
        }
        display.dispose();
    }
}
