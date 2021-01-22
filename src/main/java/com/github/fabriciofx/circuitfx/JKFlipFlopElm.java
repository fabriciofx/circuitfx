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
import java.util.StringTokenizer;

class JKFlipFlopElm extends ChipElm {
    final int FLAG_RESET = 2;

    public JKFlipFlopElm(int xx, int yy) {
        super(xx, yy);
    }

    public JKFlipFlopElm(
        int xa, int ya, int xb, int yb, int f,
        StringTokenizer st
    ) {
        super(xa, ya, xb, yb, f, st);
        pins[4].value = !pins[3].value;
    }

    boolean hasReset() {
        return (flags & FLAG_RESET) != 0;
    }

    String getChipName() {
        return "JK flip-flop";
    }

    void setupPins() {
        sizeX = 2;
        sizeY = 3;
        pins = new ChipElm.Pin[getPostCount()];
        pins[0] = new ChipElm.Pin(0, SIDE_W, "J");
        pins[1] = new ChipElm.Pin(1, SIDE_W, "");
        pins[1].clock = true;
        pins[1].bubble = true;
        pins[2] = new ChipElm.Pin(2, SIDE_W, "K");
        pins[3] = new ChipElm.Pin(0, SIDE_E, "Q");
        pins[3].output = pins[3].state = true;
        pins[4] = new ChipElm.Pin(2, SIDE_E, "Q");
        pins[4].output = true;
        pins[4].lineOver = true;
        if (hasReset()) {
            pins[5] = new ChipElm.Pin(1, SIDE_E, "R");
        }
    }

    int getPostCount() {
        return 5 + (hasReset() ? 1 : 0);
    }

    int getVoltageSourceCount() {
        return 2;
    }

    void execute() {
        if (!pins[1].value && lastClock) {
            boolean q = pins[3].value;
            if (pins[0].value) {
                if (pins[2].value) {
                    q = !q;
                } else {
                    q = true;
                }
            } else if (pins[2].value) {
                q = false;
            }
            pins[3].value = q;
            pins[4].value = !q;
        }
        lastClock = pins[1].value;
        if (hasReset()) {
            if (pins[5].value) {
                pins[3].value = false;
                pins[4].value = true;
            }
        }
    }

    int getDumpType() {
        return 156;
    }

    public EditInfo getEditInfo(int n) {
        if (n == 2) {
            EditInfo ei = new EditInfo("", 0, -1, -1);
            ei.checkbox = new Checkbox("Reset Pin", hasReset());
            return ei;
        }
        return super.getEditInfo(n);
    }

    public void setEditValue(int n, EditInfo ei) {
        if (n == 2) {
            if (ei.checkbox.getState()) {
                flags |= FLAG_RESET;
            } else {
                flags &= ~FLAG_RESET;
            }
            setupPins();
            allocNodes();
            setPoints();
        }
        super.setEditValue(n, ei);
    }
}
