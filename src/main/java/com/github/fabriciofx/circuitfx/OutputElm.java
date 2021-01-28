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

import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Point;
import java.util.StringTokenizer;
import javax.swing.JCheckBox;

class OutputElm extends CircuitElm {
    final int FLAG_VALUE = 1;

    public OutputElm(int xx, int yy) {
        super(xx, yy);
    }

    public OutputElm(
        int xa, int ya, int xb, int yb, int f,
        StringTokenizer st
    ) {
        super(xa, ya, xb, yb, f);
    }

    int getDumpType() {
        return 'O';
    }

    int getPostCount() {
        return 1;
    }

    void setPoints() {
        super.setPoints();
        lead1 = new Point();
    }

    void draw(Graphics g) {
        boolean selected = (needsHighlight() || sim.plotYElm == this);
        Font f = new Font("SansSerif", selected ? Font.BOLD : 0, 14);
        g.setFont(f);
        g.setColor(selected ? selectColor : whiteColor);
        String s = (flags & FLAG_VALUE) != 0 ? getVoltageText(volts[0]) : "out";
        FontMetrics fm = g.getFontMetrics();
        if (this == sim.plotXElm) {
            s = "X";
        }
        if (this == sim.plotYElm) {
            s = "Y";
        }
        interpPoint(
            point1,
            point2,
            lead1,
            1 - (fm.stringWidth(s) / 2 + 8) / dn
        );
        setBbox(point1, lead1, 0);
        drawCenteredText(g, s, x2, y2, true);
        setVoltageColor(g, volts[0]);
        if (selected) {
            g.setColor(selectColor);
        }
        drawThickLine(g, point1, lead1);
        drawPosts(g);
    }

    double getVoltageDiff() {
        return volts[0];
    }

    void getInfo(String[] arr) {
        arr[0] = "output";
        arr[1] = "V = " + getVoltageText(volts[0]);
    }

    public EditInfo getEditInfo(int n) {
        if (n == 0) {
            EditInfo ei = new EditInfo("", 0, -1, -1);
            ei.checkbox = new JCheckBox(
                "Show Voltage",
                (flags & FLAG_VALUE) != 0
            );
            return ei;
        }
        return null;
    }

    public void setEditValue(int n, EditInfo ei) {
        if (n == 0) {
            flags = (ei.checkbox.isSelected()) ?
                (flags | FLAG_VALUE) :
                (flags & ~FLAG_VALUE);
        }
    }
}
