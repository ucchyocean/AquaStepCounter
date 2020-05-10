/*
 * @author     ucchy
 * @license    LGPLv3
 * @copyright  Copyright ucchy 2020
 */
package com.github.ucchyocean.aqua.ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.program.Program;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.MessageBox;

import com.github.ucchyocean.aqua.AquaStepCounter;
import com.github.ucchyocean.aqua.Messages;

/**
 *
 * @author ucchy
 */
public class MainWindowMenu {

    private MainWindow main;
    private Menu bar;

    public MainWindowMenu(MainWindow main) {

        this.main = main;

        bar = new Menu(main.getShell(), SWT.BAR);
        main.getShell().setMenuBar(bar);

        createMainMenu(bar);
        createHelpMenu(bar);
    }

    private void createMainMenu(Menu bar) {

        MenuItem mainMenu = new MenuItem(bar, SWT.CASCADE);
        mainMenu.setText(Messages.MENU_MAIN);
        mainMenu.setImage(IconLoader.CAR);

        Menu menu = new Menu(mainMenu);
        mainMenu.setMenu(menu);

        MenuItem itemPrefs = new MenuItem(menu, SWT.PUSH);
        itemPrefs.setText(Messages.MENU_PREFS);
        itemPrefs.setImage(IconLoader.WRENCH);
        itemPrefs.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
                new PreferencesDialog(main.getShell()).open();
            }
        });

        new MenuItem(menu, SWT.SEPARATOR);

        MenuItem itemExit = new MenuItem(menu, SWT.PUSH);
        itemExit.setText(Messages.MENU_EXIT);
        itemExit.setImage(IconLoader.DOOR_OUT);
        itemExit.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
                main.close();
            }
        });
    }

    private void createHelpMenu(Menu bar) {

        MenuItem helpMenu = new MenuItem(bar, SWT.CASCADE);
        helpMenu.setText(Messages.MENU_HELP);
        helpMenu.setImage(IconLoader.HELP);

        Menu menu = new Menu(helpMenu);
        helpMenu.setMenu(menu);

        MenuItem itemDesc = new MenuItem(menu, SWT.PUSH);
        itemDesc.setText(Messages.MENU_DESC);
        itemDesc.setImage(IconLoader.COMMENT);
        itemDesc.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
                String message = new String( Messages.MESSAGE_DESC + "\n--------------------\n\n"
                        + AquaStepCounter.getCommentConfigManager().getDescription() );
                MessageBox box = new MessageBox(main.getShell(), SWT.NULL);
                box.setText(Messages.TITLE_DESC);
                box.setMessage(message);
                box.open();
            }
        });

        MenuItem itemHomepage = new MenuItem(menu, SWT.PUSH);
        itemHomepage.setText(Messages.MENU_HOMEPAGE);
        itemHomepage.setImage(IconLoader.WORLD);
        itemHomepage.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
                Program program = Program.findProgram("htm");
                if ( program != null ) {
                    program.execute(Messages.SOFTWARE_USAGE_URL);
                }
            }
        });

        MenuItem itemVersion = new MenuItem(menu, SWT.PUSH);
        itemVersion.setText(Messages.MENU_VERSION);
        itemVersion.setImage(IconLoader.HELP);
        itemVersion.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
                new InformationDialog(main.getShell()).open();
            }
        });
    }

    protected void setEnable(boolean enable) {
        bar.setEnabled(enable);
    }
}
