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

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.LayoutManager;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JSlider;

class EditDialogLayout implements LayoutManager {
    public EditDialogLayout() {
    }

    public void addLayoutComponent(String name, Component c) {
    }

    public void removeLayoutComponent(Component c) {
    }

    public Dimension preferredLayoutSize(Container target) {
        return new Dimension(500, 500);
    }

    public Dimension minimumLayoutSize(Container target) {
        return new Dimension(100, 100);
    }

    public void layoutContainer(Container target) {
        Insets insets = target.insets();
        int targetw = target.size().width - insets.left - insets.right;
        int targeth = target.size().height - (insets.top + insets.bottom);
        int i;
        int h = insets.top;
        int pw = 300;
        int x = 0;
        for (i = 0; i < target.getComponentCount(); i++) {
            Component m = target.getComponent(i);
            boolean newline = true;
            if (m.isVisible()) {
                Dimension d = m.getPreferredSize();
                if (pw < d.width) {
                    pw = d.width;
                }
                if (m instanceof JSlider) {
                    h += 10;
                    d.width = targetw - x;
                }
                if (m instanceof JComboBox && d.width > targetw) {
                    d.width = targetw - x;
                }
                if (m instanceof JLabel) {
                    Dimension d2 =
                        target.getComponent(i + 1).getPreferredSize();
                    if (d.height < d2.height) {
                        d.height = d2.height;
                    }
                    h += d.height / 5;
                    newline = false;
                }
                if (m instanceof JButton) {
                    if (x == 0) {
                        h += 20;
                    }
                    if (i != target.getComponentCount() - 1) {
                        newline = false;
                    }
                }
                m.move(insets.left + x, h);
                m.resize(d.width, d.height);
                if (newline) {
                    h += d.height;
                    x = 0;
                } else {
                    x += d.width;
                }
            }
        }
        if (target.size().height < h) {
            target.resize(pw + insets.right, h + insets.bottom);
        }
    }
}

