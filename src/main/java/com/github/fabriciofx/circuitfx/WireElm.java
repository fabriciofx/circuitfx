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
import java.awt.Graphics;
import java.util.StringTokenizer;

class WireElm extends CircuitElm {
    static final int FLAG_SHOWCURRENT = 1;
    static final int FLAG_SHOWVOLTAGE = 2;

    public WireElm(int xx, int yy) {
        super(xx, yy);
    }

    public WireElm(
        int xa, int ya, int xb, int yb, int f,
        StringTokenizer st
    ) {
        super(xa, ya, xb, yb, f);
    }

    void draw(Graphics g) {
        setVoltageColor(g, volts[0]);
        drawThickLine(g, point1, point2);
        doDots(g);
        setBbox(point1, point2, 3);
        if (mustShowCurrent()) {
            String s = getShortUnitText(Math.abs(getCurrent()), "A");
            drawValues(g, s, 4);
        } else if (mustShowVoltage()) {
            String s = getShortUnitText(volts[0], "V");
            drawValues(g, s, 4);
        }
        drawPosts(g);
    }

    void stamp() {
        sim.stampVoltageSource(nodes[0], nodes[1], voltSource, 0);
    }

    boolean mustShowCurrent() {
        return (flags & FLAG_SHOWCURRENT) != 0;
    }

    boolean mustShowVoltage() {
        return (flags & FLAG_SHOWVOLTAGE) != 0;
    }

    int getVoltageSourceCount() {
        return 1;
    }

    void getInfo(String[] arr) {
        arr[0] = "wire";
        arr[1] = "I = " + getCurrentDText(getCurrent());
        arr[2] = "V = " + getVoltageText(volts[0]);
    }

    int getDumpType() {
        return 'w';
    }

    double getPower() {
        return 0;
    }

    double getVoltageDiff() {
        return volts[0];
    }

    boolean isWire() {
        return true;
    }

    public EditInfo getEditInfo(int n) {
        if (n == 0) {
            EditInfo ei = new EditInfo("", 0, -1, -1);
            ei.checkbox = new Checkbox("Show Current", mustShowCurrent());
            return ei;
        }
        if (n == 1) {
            EditInfo ei = new EditInfo("", 0, -1, -1);
            ei.checkbox = new Checkbox("Show Voltage", mustShowVoltage());
            return ei;
        }
        return null;
    }

    public void setEditValue(int n, EditInfo ei) {
        if (n == 0) {
            if (ei.checkbox.getState()) {
                flags = FLAG_SHOWCURRENT;
            } else {
                flags &= ~FLAG_SHOWCURRENT;
            }
        }
        if (n == 1) {
            if (ei.checkbox.getState()) {
                flags = FLAG_SHOWVOLTAGE;
            } else {
                flags &= ~FLAG_SHOWVOLTAGE;
            }
        }
    }

    int getShortcut() {
        return 'w';
    }
}
