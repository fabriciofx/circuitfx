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

import java.util.StringTokenizer;
import javax.swing.JCheckBox;

class MonostableElm extends ChipElm {
    //Used to detect rising edge
    private boolean prevInputValue = false;
    private boolean retriggerable = false;
    private boolean triggered = false;
    private double lastRisingEdge = 0;
    private double delay = 0.01;

    public MonostableElm(int xx, int yy) {
        super(xx, yy);
    }

    public MonostableElm(
        int xa, int ya, int xb, int yb, int f,
        StringTokenizer st
    ) {
        super(xa, ya, xb, yb, f, st);
        retriggerable = new Boolean(st.nextToken()).booleanValue();
        delay = new Double(st.nextToken()).doubleValue();
    }

    String getChipName() {
        return "Monostable";
    }

    void setupPins() {
        sizeX = 2;
        sizeY = 2;
        pins = new ChipElm.Pin[getPostCount()];
        pins[0] = new ChipElm.Pin(0, SIDE_W, "");
        pins[0].clock = true;
        pins[1] = new ChipElm.Pin(0, SIDE_E, "Q");
        pins[1].output = true;
        pins[2] = new ChipElm.Pin(1, SIDE_E, "Q");
        pins[2].output = true;
        pins[2].lineOver = true;
    }

    int getPostCount() {
        return 3;
    }

    int getVoltageSourceCount() {
        return 2;
    }

    void execute() {
        if (pins[0].value && prevInputValue != pins[0].value &&
            (retriggerable || !triggered)) {
            lastRisingEdge = sim.t;
            pins[1].value = true;
            pins[2].value = false;
            triggered = true;
        }
        if (triggered && sim.t > lastRisingEdge + delay) {
            pins[1].value = false;
            pins[2].value = true;
            triggered = false;
        }
        prevInputValue = pins[0].value;
    }

    String dump() {
        return super.dump() + " " + retriggerable + " " + delay;
    }

    int getDumpType() {
        return 194;
    }

    public EditInfo getEditInfo(int n) {
        if (n == 2) {
            EditInfo ei = new EditInfo("", 0, -1, -1);
            ei.checkbox = new JCheckBox("Retriggerable", retriggerable);
            return ei;
        }
        if (n == 3) {
            EditInfo ei = new EditInfo("Period (s)", delay, 0.001, 0.1);
            return ei;
        }
        return super.getEditInfo(n);
    }

    public void setEditValue(int n, EditInfo ei) {
        if (n == 2) {
            retriggerable = ei.checkbox.isSelected();
        }
        if (n == 3) {
            delay = ei.value;
        }
        super.setEditValue(n, ei);
    }
}
