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

import java.awt.Choice;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.Label;
import java.awt.LayoutManager;
import java.awt.Scrollbar;

class CircuitLayout implements LayoutManager {
    public CircuitLayout() {
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
        int cw = targetw * 8 / 10;
        int targeth = target.size().height - (insets.top + insets.bottom);
        target.getComponent(0).move(insets.left, insets.top);
        target.getComponent(0).resize(cw, targeth);
        int barwidth = targetw - cw;
        cw += insets.left;
        int i;
        int h = insets.top;
        for (i = 1; i < target.getComponentCount(); i++) {
            Component m = target.getComponent(i);
            if (m.isVisible()) {
                Dimension d = m.getPreferredSize();
                if (m instanceof Scrollbar) {
                    d.width = barwidth;
                }
                if (m instanceof Choice && d.width > barwidth) {
                    d.width = barwidth;
                }
                if (m instanceof Label) {
                    h += d.height / 5;
                    d.width = barwidth;
                }
                m.move(cw, h);
                m.resize(d.width, d.height);
                h += d.height;
            }
        }
    }
}
