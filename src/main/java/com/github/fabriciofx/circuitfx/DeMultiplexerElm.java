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
import java.util.StringTokenizer;
// contributed by Edward Calver

class DeMultiplexerElm extends ChipElm {
    public DeMultiplexerElm(int xx, int yy) {
        super(xx, yy);
    }

    public DeMultiplexerElm(
        int xa, int ya, int xb, int yb, int f,
        StringTokenizer st
    ) {
        super(xa, ya, xb, yb, f, st);
    }

    boolean hasReset() {
        return false;
    }

    String getChipName() {
        return "Multiplexer";
    }

    void setupPins() {
        sizeX = 3;
        sizeY = 5;
        pins = new ChipElm.Pin[getPostCount()];
        pins[0] = new ChipElm.Pin(0, SIDE_E, "Q0");
        pins[0].output = true;
        pins[1] = new ChipElm.Pin(1, SIDE_E, "Q1");
        pins[1].output = true;
        pins[2] = new ChipElm.Pin(2, SIDE_E, "Q2");
        pins[2].output = true;
        pins[3] = new ChipElm.Pin(3, SIDE_E, "Q3");
        pins[3].output = true;
        pins[4] = new ChipElm.Pin(0, SIDE_S, "S0");
        pins[5] = new ChipElm.Pin(1, SIDE_S, "S1");
        pins[6] = new ChipElm.Pin(0, SIDE_W, "Q");
    }

    int getPostCount() {
        return 7;
    }

    int getVoltageSourceCount() {
        return 4;
    }

    void execute() {
        int selectedvalue = 0;
        if (pins[4].value) {
            selectedvalue++;
        }
        if (pins[5].value) {
            selectedvalue += 2;
        }
        for (int i = 0; i < 4; i++) {
            pins[i].value = false;
        }
        pins[selectedvalue].value = pins[6].value;
    }

    int getDumpType() {
        return 185;
    }
}
