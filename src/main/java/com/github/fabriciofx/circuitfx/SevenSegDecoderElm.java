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

class SevenSegDecoderElm extends ChipElm {
    private static final boolean[][] symbols = {
        { true, true, true, true, true, true, false },//0
        { false, true, true, false, false, false, false },//1
        { true, true, false, true, true, false, true },//2
        { true, true, true, true, false, false, true },//3
        { false, true, true, false, false, true, true },//4
        { true, false, true, true, false, true, true },//5
        { true, false, true, true, true, true, true },//6
        { true, true, true, false, false, false, false },//7
        { true, true, true, true, true, true, true },//8
        { true, true, true, false, false, true, true },//9
        { true, true, true, false, true, true, true },//A
        { false, false, true, true, true, true, true },//B
        { true, false, false, true, true, true, false },//C
        { false, true, true, true, true, false, true },//D
        { true, false, false, true, true, true, true },//E
        { true, false, false, false, true, true, true },//F
    };

    public SevenSegDecoderElm(int xx, int yy) {
        super(xx, yy);
    }

    public SevenSegDecoderElm(
        int xa, int ya, int xb, int yb, int f,
        StringTokenizer st
    ) {
        super(xa, ya, xb, yb, f, st);
    }

    boolean hasReset() {
        return false;
    }

    String getChipName() {
        return "Seven Segment LED Decoder";
    }

    void setupPins() {
        sizeX = 3;
        sizeY = 7;
        pins = new ChipElm.Pin[getPostCount()];
        pins[7] = new ChipElm.Pin(0, SIDE_W, "I3");
        pins[8] = new ChipElm.Pin(1, SIDE_W, "I2");
        pins[9] = new ChipElm.Pin(2, SIDE_W, "I1");
        pins[10] = new ChipElm.Pin(3, SIDE_W, "I0");
        pins[0] = new ChipElm.Pin(0, SIDE_E, "a");
        pins[0].output = true;
        pins[1] = new ChipElm.Pin(1, SIDE_E, "b");
        pins[1].output = true;
        pins[2] = new ChipElm.Pin(2, SIDE_E, "c");
        pins[2].output = true;
        pins[3] = new ChipElm.Pin(3, SIDE_E, "d");
        pins[3].output = true;
        pins[4] = new ChipElm.Pin(4, SIDE_E, "e");
        pins[4].output = true;
        pins[5] = new ChipElm.Pin(5, SIDE_E, "f");
        pins[5].output = true;
        pins[6] = new ChipElm.Pin(6, SIDE_E, "g");
        pins[6].output = true;
    }

    int getPostCount() {
        return 11;
    }

    int getVoltageSourceCount() {
        return 7;
    }

    void execute() {
        int input = 0;
        if (pins[7].value) {
            input += 8;
        }
        if (pins[8].value) {
            input += 4;
        }
        if (pins[9].value) {
            input += 2;
        }
        if (pins[10].value) {
            input += 1;
        }
        for (int i = 0; i < 7; i++) {
            pins[i].value = symbols[input][i];
        }
    }

    int getDumpType() {
        return 197;
    }
}
