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

class DecadeElm extends ChipElm {
    public DecadeElm(int xx, int yy) {
        super(xx, yy);
    }

    public DecadeElm(
        int xa, int ya, int xb, int yb, int f,
        StringTokenizer st
    ) {
        super(xa, ya, xb, yb, f, st);
    }

    String getChipName() {
        return "decade counter";
    }

    boolean needsBits() {
        return true;
    }

    void setupPins() {
        sizeX = bits > 2 ? bits : 2;
        sizeY = 2;
        pins = new ChipElm.Pin[getPostCount()];
        pins[0] = new ChipElm.Pin(1, SIDE_W, "");
        pins[0].clock = true;
        pins[1] = new ChipElm.Pin(sizeX - 1, SIDE_S, "R");
        pins[1].bubble = true;
        int i;
        for (i = 0; i != bits; i++) {
            int ii = i + 2;
            pins[ii] = new ChipElm.Pin(i, SIDE_N, "Q" + i);
            pins[ii].output = pins[ii].state = true;
        }
        allocNodes();
    }

    int getPostCount() {
        return bits + 2;
    }

    int getVoltageSourceCount() {
        return bits;
    }

    void execute() {
        int i;
        if (pins[0].value && !lastClock) {
            for (i = 0; i != bits; i++) {
                if (pins[i + 2].value) {
                    break;
                }
            }
            if (i < bits) {
                pins[i++ + 2].value = false;
            }
            i %= bits;
            pins[i + 2].value = true;
        }
        if (!pins[1].value) {
            for (i = 1; i != bits; i++) {
                pins[i + 2].value = false;
            }
            pins[2].value = true;
        }
        lastClock = pins[0].value;
    }

    int getDumpType() {
        return 163;
    }
}
