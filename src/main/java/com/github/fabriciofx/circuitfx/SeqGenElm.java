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

/**
 * Contributed by Edward Calver.
 */
class SeqGenElm extends ChipElm {
    short data = 0;
    byte position = 0;
    boolean oneshot = false;
    double lastchangetime = 0;
    boolean clockstate = false;

    public SeqGenElm(int xx, int yy) {
        super(xx, yy);
    }

    public SeqGenElm(
        int xa, int ya, int xb, int yb, int f,
        StringTokenizer st
    ) {
        super(xa, ya, xb, yb, f, st);
        data = (short) (new Integer(st.nextToken()).intValue());
        if (st.hasMoreTokens()) {
            oneshot = new Boolean(st.nextToken()).booleanValue();
            position = 8;
        }
    }

    boolean hasReset() {
        return false;
    }

    String getChipName() {
        return "Sequence generator";
    }

    void setupPins() {
        sizeX = 2;
        sizeY = 2;
        pins = new ChipElm.Pin[getPostCount()];
        pins[0] = new ChipElm.Pin(0, SIDE_W, "");
        pins[0].clock = true;
        pins[1] = new ChipElm.Pin(1, SIDE_E, "Q");
        pins[1].output = true;
    }

    int getPostCount() {
        return 2;
    }

    int getVoltageSourceCount() {
        return 1;
    }

    void GetNextBit() {
        if (((data >>> position) & 1) != 0) {
            pins[1].value = true;
        } else {
            pins[1].value = false;
        }
        position++;
    }

    void execute() {
        if (oneshot) {
            if (sim.t - lastchangetime > 0.005) {
                if (position <= 8) {
                    GetNextBit();
                }
                lastchangetime = sim.t;
            }
        }
        if (pins[0].value && !clockstate) {
            clockstate = true;
            if (oneshot) {
                position = 0;
            } else {
                GetNextBit();
                if (position >= 8) {
                    position = 0;
                }
            }
        }
        if (!pins[0].value) {
            clockstate = false;
        }
    }

    int getDumpType() {
        return 188;
    }

    String dump() {
        return super.dump() + " " + data + " " + oneshot;
    }

    public EditInfo getEditInfo(int n) {
        //My code
        if (n == 0) {
            EditInfo ei = new EditInfo("", 0, -1, -1);
            ei.checkbox = new Checkbox("Bit 0 set", (data & 1) != 0);
            return ei;
        }
        if (n == 1) {
            EditInfo ei = new EditInfo("", 0, -1, -1);
            ei.checkbox = new Checkbox("Bit 1 set", (data & 2) != 0);
            return ei;
        }
        if (n == 2) {
            EditInfo ei = new EditInfo("", 0, -1, -1);
            ei.checkbox = new Checkbox("Bit 2 set", (data & 4) != 0);
            return ei;
        }
        if (n == 3) {
            EditInfo ei = new EditInfo("", 0, -1, -1);
            ei.checkbox = new Checkbox("Bit 3 set", (data & 8) != 0);
            return ei;
        }
        if (n == 4) {
            EditInfo ei = new EditInfo("", 0, -1, -1);
            ei.checkbox = new Checkbox("Bit 4 set", (data & 16) != 0);
            return ei;
        }
        if (n == 5) {
            EditInfo ei = new EditInfo("", 0, -1, -1);
            ei.checkbox = new Checkbox("Bit 5 set", (data & 32) != 0);
            return ei;
        }
        if (n == 6) {
            EditInfo ei = new EditInfo("", 0, -1, -1);
            ei.checkbox = new Checkbox("Bit 6 set", (data & 64) != 0);
            return ei;
        }
        if (n == 7) {
            EditInfo ei = new EditInfo("", 0, -1, -1);
            ei.checkbox = new Checkbox("Bit 7 set", (data & 128) != 0);
            return ei;
        }
        if (n == 8) {
            EditInfo ei = new EditInfo("", 0, -1, -1);
            ei.checkbox = new Checkbox("One shot", oneshot);
            return ei;
        }
        return null;
    }

    public void setEditValue(int n, EditInfo ei) {
        if (n == 0) {
            if (ei.checkbox.getState()) {
                data |= 1;
            } else {
                data &= ~1;
            }
            setPoints();
        }
        if (n == 1) {
            if (ei.checkbox.getState()) {
                data |= 2;
            } else {
                data &= ~2;
            }
            setPoints();
        }
        if (n == 2) {
            if (ei.checkbox.getState()) {
                data |= 4;
            } else {
                data &= ~4;
            }
            setPoints();
        }
        if (n == 3) {
            if (ei.checkbox.getState()) {
                data |= 8;
            } else {
                data &= ~8;
            }
            setPoints();
        }
        if (n == 4) {
            if (ei.checkbox.getState()) {
                data |= 16;
            } else {
                data &= ~16;
            }
            setPoints();
        }
        if (n == 5) {
            if (ei.checkbox.getState()) {
                data |= 32;
            } else {
                data &= ~32;
            }
            setPoints();
        }
        if (n == 6) {
            if (ei.checkbox.getState()) {
                data |= 64;
            } else {
                data &= ~64;
            }
            setPoints();
        }
        if (n == 7) {
            if (ei.checkbox.getState()) {
                data |= 128;
            } else {
                data &= ~128;
            }
            setPoints();
        }
        if (n == 8) {
            if (ei.checkbox.getState()) {
                oneshot = true;
                position = 8;
            } else {
                position = 0;
                oneshot = false;
            }
        }
    }
}
