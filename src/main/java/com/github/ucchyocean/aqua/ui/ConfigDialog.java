/*
 * @author     ucchy
 * @license    LGPLv3
 * @copyright  Copyright ucchy 2020
 */
package com.github.ucchyocean.aqua.ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

import com.github.ucchyocean.aqua.AquaStepCounter;
import com.github.ucchyocean.aqua.AquaStepCounterConfig;
import com.github.ucchyocean.aqua.Messages;

/**
 * 設定ダイアログ
 * @author ucchy
 */
public class ConfigDialog {

    private Shell shell;

    public ConfigDialog(Shell parent) {
        shell = new Shell(parent, SWT.APPLICATION_MODAL|SWT.DIALOG_TRIM);
        createContents(shell);
    }

    private Control createContents(Composite parent) {

        shell.setText(Messages.TITLE_PREFS);

        parent.setLayout( new GridLayout(1, false) );

        Button bStripComment = new Button(parent, SWT.CHECK);
        bStripComment.setLayoutData( new GridData(GridData.FILL_HORIZONTAL) );
        bStripComment.setText(Messages.PREFS_NAME_STRIP_COMMENT);
        bStripComment.setSelection(AquaStepCounter.getConfig().isStripComment());

        Button bStripWhite = new Button(parent, SWT.CHECK);
        bStripWhite.setLayoutData( new GridData(GridData.FILL_HORIZONTAL) );
        bStripWhite.setText(Messages.PREFS_NAME_STRIP_WHITE);
        bStripWhite.setSelection(AquaStepCounter.getConfig().isStripWhite());

        // ボタンバー
        Composite buttonComp = new Composite(parent, SWT.NONE);
        buttonComp.setLayoutData( new GridData(GridData.FILL_HORIZONTAL) );
        buttonComp.setLayout( new GridLayout(2, false) );

        Button okButton = new Button(buttonComp, SWT.PUSH);
        okButton.setLayoutData( new GridData(
                GridData.GRAB_HORIZONTAL | GridData.HORIZONTAL_ALIGN_END) );
        okButton.setText("OK");
        okButton.addSelectionListener( new SelectionAdapter() {
            public void widgetSelected(SelectionEvent event) {
                AquaStepCounterConfig config = AquaStepCounter.getConfig();
                config.setStripComment(bStripComment.getSelection());
                config.setStripWhite(bStripWhite.getSelection());
                config.save();
                shell.close();
            }
        });
        shell.setDefaultButton(okButton);

        Button cancelButton = new Button(buttonComp, SWT.PUSH);
        cancelButton.setLayoutData( new GridData(GridData.END) );
        cancelButton.setText("Cancel");
        cancelButton.addSelectionListener( new SelectionAdapter() {
            public void widgetSelected(SelectionEvent event) {
                shell.close();
            }
        });

        shell.pack();

        return parent;
    }

    public void open() {
        shell.open();
    }
}
