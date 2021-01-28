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

class TFlipFlopElm extends ChipElm {
    final int FLAG_RESET = 2;
    final int FLAG_SET = 4;
    private boolean last_val;

    public TFlipFlopElm(int xx, int yy) {
        super(xx, yy);
    }

    public TFlipFlopElm(
        int xa, int ya, int xb, int yb, int f,
        StringTokenizer st
    ) {
        super(xa, ya, xb, yb, f, st);
        pins[2].value = !pins[1].value;
    }

    boolean hasReset() {
        return (flags & FLAG_RESET) != 0 || hasSet();
    }

    boolean hasSet() {
        return (flags & FLAG_SET) != 0;
    }

    String getChipName() {
        return "T flip-flop";
    }

    void setupPins() {
        sizeX = 2;
        sizeY = 3;
        pins = new ChipElm.Pin[getPostCount()];
        pins[0] = new ChipElm.Pin(0, SIDE_W, "T");
        pins[1] = new ChipElm.Pin(0, SIDE_E, "Q");
        pins[1].output = pins[1].state = true;
        pins[2] = new ChipElm.Pin(hasSet() ? 1 : 2, SIDE_E, "Q");
        pins[2].output = true;
        pins[2].lineOver = true;
        pins[3] = new ChipElm.Pin(1, SIDE_W, "");
        pins[3].clock = true;
        if (!hasSet()) {
            if (hasReset()) {
                pins[4] = new ChipElm.Pin(2, SIDE_W, "R");
            }
        } else {
            pins[5] = new ChipElm.Pin(2, SIDE_W, "S");
            pins[4] = new ChipElm.Pin(2, SIDE_E, "R");
        }
    }

    int getPostCount() {
        return 4 + (hasReset() ? 1 : 0) + (hasSet() ? 1 : 0);
    }

    int getVoltageSourceCount() {
        return 2;
    }

    void reset() {
        super.reset();
        volts[2] = 5;
        pins[2].value = true;
    }

    void execute() {
        if (pins[3].value && !lastClock) {
            if (pins[0].value) //if T = 1
            {
                pins[1].value = !last_val;
                pins[2].value = !pins[1].value;
                last_val = !last_val;
            }
            //else no change
        }
        if (hasSet() && pins[5].value) {
            pins[1].value = true;
            pins[2].value = false;
        }
        if (hasReset() && pins[4].value) {
            pins[1].value = false;
            pins[2].value = true;
        }
        lastClock = pins[3].value;
    }

    int getDumpType() {
        return 193;
    }

    public EditInfo getEditInfo(int n) {
        if (n == 2) {
            EditInfo ei = new EditInfo("", 0, -1, -1);
            ei.checkbox = new JCheckBox("Reset Pin", hasReset());
            return ei;
        }
        if (n == 3) {
            EditInfo ei = new EditInfo("", 0, -1, -1);
            ei.checkbox = new JCheckBox("Set Pin", hasSet());
            return ei;
        }
        return super.getEditInfo(n);
    }

    public void setEditValue(int n, EditInfo ei) {
        if (n == 2) {
            if (ei.checkbox.isSelected()) {
                flags |= FLAG_RESET;
            } else {
                flags &= ~FLAG_RESET | FLAG_SET;
            }
            setupPins();
            allocNodes();
            setPoints();
        }
        if (n == 3) {
            if (ei.checkbox.isSelected()) {
                flags |= FLAG_SET;
            } else {
                flags &= ~FLAG_SET;
            }
            setupPins();
            allocNodes();
            setPoints();
        }
        super.setEditValue(n, ei);
    }
}
