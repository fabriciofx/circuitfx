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
package com.github.fabriciofx.circuitfx;

import java.awt.Dimension;
import java.awt.Event;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JTextArea;

class ImportDialog extends JDialog implements ActionListener {
    CirSim cframe;
    JButton importButton, closeButton;
    JTextArea text;
    boolean isURL;

    ImportDialog(CirSim f, String str, boolean url) {
        super(f, (str.length() > 0) ? "Export" : "Import", false);
        isURL = url;
        cframe = f;
        setLayout(new ImportDialogLayout());
        add(text = new JTextArea(null, str, 10, 60));
        importButton = new JButton("Import");
        if (!isURL) {
            add(importButton);
        }
        importButton.addActionListener(this);
        add(closeButton = new JButton("Close"));
        closeButton.addActionListener(this);
        Point x = cframe.main.getLocationOnScreen();
        resize(400, 300);
        Dimension d = getSize();
        setLocation(
            x.x + (cframe.winSize.width - d.width) / 2,
            x.y + (cframe.winSize.height - d.height) / 2
        );
        show();
        if (str.length() > 0) {
            text.selectAll();
        }
    }

    public void actionPerformed(ActionEvent e) {
        int i;
        Object src = e.getSource();
        if (src == importButton) {
            cframe.readSetup(text.getText());
            setVisible(false);
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

