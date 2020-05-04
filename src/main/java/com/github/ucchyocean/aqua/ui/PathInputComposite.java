/*
 * @author     ucchy
 * @license    LGPLv3
 * @copyright  Copyright ucchy 2020
 */
package com.github.ucchyocean.aqua.ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import com.github.ucchyocean.aqua.Messages;

/**
 *
 * @author ucchy
 */
public class PathInputComposite extends DnDComposite implements SelectionListener {

    private static final int LABEL_LENGTH = 70;

    private String name;
    private Label label;
    private Text text;
    private Button button;

    public PathInputComposite(Composite parent, String name) {

        super(parent, false);
        this.name = name;
        createContent(parent, name);
    }

    private void createContent(Composite parent, String name) {

        Composite comp = getComposite();
        comp.setLayout( new GridLayout(3, false) );

        label = new Label(comp, SWT.NONE);
        label.setText(name);
        GridData gdata = new GridData(GridData.BEGINNING);
        gdata.widthHint = LABEL_LENGTH;
        label.setLayoutData(gdata);

        text = new Text(comp, SWT.BORDER | SWT.SINGLE);
        text.setLayoutData( new GridData(GridData.FILL_HORIZONTAL) );
        //text.addSelectionListener(this);

        button = new Button(comp, SWT.PUSH);
        button.setText(Messages.MESSAGE_BUTTON_REF);
        button.setLayoutData( new GridData(GridData.END) );
        button.addSelectionListener(this);
    }

    private void pressedButton() {

        DirectoryDialog dialog = new DirectoryDialog( getComposite().getShell() );
        dialog.setFilterPath( text.getText() );
        dialog.setMessage( name + Messages.MESSAGE_DIR_SELECT );
        String dir = dialog.open();
        if (dir != null){
            text.setText(dir);
        }
    }

    public String getText() {
        return text.getText();
    }

    public void setEnable(boolean enable) {
        label.setEnabled(enable);
        text.setEnabled(enable);
        button.setEnabled(enable);
    }

    @Override
    public void fileDroped(String fileName) {
        text.setText(fileName);
    }

    @Override
    public void fileDroped(String[] fileName) {
        // this Composite is single-file-mode. this method is not used.
    }

    /**
     * @see org.eclipse.swt.events.SelectionListener#widgetDefaultSelected(org.eclipse.swt.events.SelectionEvent)
     */
    public void widgetDefaultSelected(SelectionEvent arg0) {
        pressedButton();
    }

    /**
     * @see org.eclipse.swt.events.SelectionListener#widgetSelected(org.eclipse.swt.events.SelectionEvent)
     */
    public void widgetSelected(SelectionEvent arg0) {
        pressedButton();
    }

}
