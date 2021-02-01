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

import java.awt.Graphics;
import java.awt.Point;
import java.util.StringTokenizer;
import javax.swing.JCheckBox;

class SwitchElm extends CircuitElm {
    boolean momentary;
    // position 0 == closed, position 1 == open
    int position, posCount;
    Point ps, ps2;

    public SwitchElm(int xx, int yy) {
        super(xx, yy);
        momentary = false;
        position = 0;
        posCount = 2;
    }

    SwitchElm(int xx, int yy, boolean mm) {
        super(xx, yy);
        position = (mm) ? 1 : 0;
        momentary = mm;
        posCount = 2;
    }

    public SwitchElm(
        int xa, int ya, int xb, int yb, int f,
        StringTokenizer st
    ) {
        super(xa, ya, xb, yb, f);
        String str = st.nextToken();
        if (str.compareTo("true") == 0) {
            position = (this instanceof LogicInputElm) ? 0 : 1;
        } else if (str.compareTo("false") == 0) {
            position = (this instanceof LogicInputElm) ? 1 : 0;
        } else {
            position = new Integer(str).intValue();
        }
        momentary = new Boolean(st.nextToken()).booleanValue();
        posCount = 2;
    }

    int getDumpType() {
        return 's';
    }

    String dump() {
        return super.dump() + " " + position + " " + momentary;
    }

    void setPoints() {
        super.setPoints();
        calcLeads(32);
        ps = new Point();
        ps2 = new Point();
    }

    void draw(Graphics g) {
        int openhs = 16;
        int hs1 = (position == 1) ? 0 : 2;
        int hs2 = (position == 1) ? openhs : 2;
        setBbox(point1, point2, openhs);
        draw2Leads(g);
        if (position == 0) {
            doDots(g);
        }
        if (!needsHighlight()) {
            g.setColor(whiteColor);
        }
        interpPoint(lead1, lead2, ps, 0, hs1);
        interpPoint(lead1, lead2, ps2, 1, hs2);
        drawThickLine(g, ps, ps2);
        drawPosts(g);
    }

    void calculateCurrent() {
        if (position == 1) {
            current = 0;
        }
    }

    void stamp() {
        if (position == 0) {
            sim.stampVoltageSource(nodes[0], nodes[1], voltSource, 0);
        }
    }

    int getVoltageSourceCount() {
        return (position == 1) ? 0 : 1;
    }

    void mouseUp() {
        if (momentary) {
            toggle();
        }
    }

    void toggle() {
        position++;
        if (position >= posCount) {
            position = 0;
        }
    }

    void getInfo(String[] arr) {
        arr[0] = (momentary) ? "push switch (SPST)" : "switch (SPST)";
        if (position == 1) {
            arr[1] = "open";
            arr[2] = "Vd = " + getVoltageDText(getVoltageDiff());
        } else {
            arr[1] = "closed";
            arr[2] = "V = " + getVoltageText(volts[0]);
            arr[3] = "I = " + getCurrentDText(getCurrent());
        }
    }

    boolean getConnection(int n1, int n2) {
        return position == 0;
    }

    boolean isWire() {
        return true;
    }

    public EditInfo getEditInfo(int n) {
        if (n == 0) {
            EditInfo ei = new EditInfo("", 0, -1, -1);
            ei.checkbox = new JCheckBox("Momentary Switch", momentary);
            return ei;
        }
        return null;
    }

    public void setEditValue(int n, EditInfo ei) {
        if (n == 0) {
            momentary = ei.checkbox.isSelected();
        }
    }

    int getShortcut() {
        return 's';
    }
}
