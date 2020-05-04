/*
 * @author     ucchy
 * @license    LGPLv3
 * @copyright  Copyright ucchy 2020
 */
package com.github.ucchyocean.aqua.ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DropTarget;
import org.eclipse.swt.dnd.DropTargetAdapter;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.FileTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.widgets.Composite;

/**
 *
 * @author ucchy
 */
public abstract class DnDComposite extends DropTargetAdapter {

    private Composite composite;
    private boolean multiFileSupport;

    public DnDComposite (Composite parent, boolean multiFileSupport) {

        this.composite = new Composite(parent,SWT.NULL);
        this.multiFileSupport = multiFileSupport;

        DropTarget target = new DropTarget(composite,DND.DROP_DEFAULT|DND.DROP_COPY);
        FileTransfer transfer = FileTransfer.getInstance();
        Transfer[] types = new Transfer[]{transfer};
        target.setTransfer(types);
        target.addDropListener(this);
    }

    /**
     * @return Returns the composite.
     */
    public Composite getComposite() {
        return composite;
    }

    public void dragEnter(DropTargetEvent evt){
        evt.detail = DND.DROP_COPY;
    }

    public void drop(DropTargetEvent evt){
        String[] files = (String[])evt.data;

        if (multiFileSupport) {
            fileDroped(files);
        } else {
            fileDroped(files[0]);
        }
    }

    public abstract void fileDroped(String fileName);
    public abstract void fileDroped(String[] fileName);
}
