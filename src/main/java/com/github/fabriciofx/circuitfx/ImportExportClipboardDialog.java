/*
 * The MIT License (MIT)
 *
 * Copyright (C) 2005-2020 Paul Falstad (Circuit Simulator)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included
 * in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NON-INFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
import java.awt.Button;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Event;
import java.awt.Point;
import java.awt.TextArea;
import java.awt.datatransfer.Clipboard;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

class ImportExportClipboardDialog
    extends Dialog
    implements ImportExportDialog, ActionListener {
    CirSim cframe;
    Button importButton, closeButton;
    TextArea text;
    Action type;
    Clipboard clipboard = null;

    ImportExportClipboardDialog(CirSim f, Action type) {
        super(
            f,
            (type == ImportExportDialog.Action.EXPORT) ? "Export" : "Import",
            false
        );
        cframe = f;
        setLayout(new ImportExportDialogLayout());
        add(text = new TextArea("", 10, 60, TextArea.SCROLLBARS_BOTH));
        importButton = new Button("Import");
        this.type = type;
        add(importButton);
        importButton.addActionListener(this);
        add(closeButton = new Button("Close"));
        closeButton.addActionListener(this);
        Point x = cframe.main.getLocationOnScreen();
        resize(400, 300);
        Dimension d = getSize();
        setLocation(
            x.x + (cframe.winSize.width - d.width) / 2,
            x.y + (cframe.winSize.height - d.height) / 2
        );
    }

    public void setDump(String dump) {
        text.setText(dump);
    }

    public void execute() {
        if (type == ImportExportDialog.Action.EXPORT) {
            text.selectAll();
        }
        setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        int i;
        Object src = e.getSource();
        if (src == importButton) {
            cframe.readSetup(text.getText());
        }
        if (src == closeButton) {
            setVisible(false);
        }
    }

    public boolean handleEvent(Event ev) {
        if (ev.id == Event.WINDOW_DESTROY) {
            CirSim.main.requestFocus();
            setVisible(false);
            cframe.impDialog = null;
            return true;
        }
        return super.handleEvent(ev);
    }
}
