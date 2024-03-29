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

import java.awt.Checkbox;
import java.awt.Font;
import java.awt.Graphics;
import java.util.StringTokenizer;

class LogicInputElm extends SwitchElm {
    final int FLAG_TERNARY = 1;
    final int FLAG_NUMERIC = 2;
    double hiV, loV;

    public LogicInputElm(int xx, int yy) {
        super(xx, yy, false);
        hiV = 5;
        loV = 0;
    }

    public LogicInputElm(
        int xa, int ya, int xb, int yb, int f,
        StringTokenizer st
    ) {
        super(xa, ya, xb, yb, f, st);
        try {
            hiV = new Double(st.nextToken()).doubleValue();
            loV = new Double(st.nextToken()).doubleValue();
        } catch (Exception e) {
            hiV = 5;
            loV = 0;
        }
        if (isTernary()) {
            posCount = 3;
        }
    }

    boolean isTernary() {
        return (flags & FLAG_TERNARY) != 0;
    }

    boolean isNumeric() {
        return (flags & (FLAG_TERNARY | FLAG_NUMERIC)) != 0;
    }

    int getDumpType() {
        return 'L';
    }

    String dump() {
        return super.dump() + " " + hiV + " " + loV;
    }

    int getPostCount() {
        return 1;
    }

    void setPoints() {
        super.setPoints();
        lead1 = interpPoint(point1, point2, 1 - 12 / dn);
    }

    void draw(Graphics g) {
        Font f = new Font("SansSerif", Font.BOLD, 20);
        g.setFont(f);
        g.setColor(needsHighlight() ? selectColor : whiteColor);
        String s = position == 0 ? "L" : "H";
        if (isNumeric()) {
            s = "" + position;
        }
        setBbox(point1, lead1, 0);
        drawCenteredText(g, s, x2, y2, true);
        setVoltageColor(g, volts[0]);
        drawThickLine(g, point1, lead1);
        updateDotCount();
        drawDots(g, point1, lead1, curcount);
        drawPosts(g);
    }

    void setCurrent(int vs, double c) {
        current = -c;
    }

    void stamp() {
        double v = (position == 0) ? loV : hiV;
        if (isTernary()) {
            v = position * 2.5;
        }
        sim.stampVoltageSource(0, nodes[0], voltSource, v);
    }

    int getVoltageSourceCount() {
        return 1;
    }

    double getVoltageDiff() {
        return volts[0];
    }

    void getInfo(String[] arr) {
        arr[0] = "logic input";
        arr[1] = (position == 0) ? "low" : "high";
        if (isNumeric()) {
            arr[1] = "" + position;
        }
        arr[1] += " (" + getVoltageText(volts[0]) + ")";
        arr[2] = "I = " + getCurrentText(getCurrent());
    }

    boolean hasGroundConnection(int n1) {
        return true;
    }

    public EditInfo getEditInfo(int n) {
        if (n == 0) {
            EditInfo ei = new EditInfo("", 0, 0, 0);
            ei.checkbox = new Checkbox("Momentary Switch", momentary);
            return ei;
        }
        if (n == 1) {
            return new EditInfo("High Voltage", hiV, 10, -10);
        }
        if (n == 2) {
            return new EditInfo("Low Voltage", loV, 10, -10);
        }
        return null;
    }

    public void setEditValue(int n, EditInfo ei) {
        if (n == 0) {
            momentary = ei.checkbox.getState();
        }
        if (n == 1) {
            hiV = ei.value;
        }
        if (n == 2) {
            loV = ei.value;
        }
    }

    int getShortcut() {
        return 'i';
    }
}
