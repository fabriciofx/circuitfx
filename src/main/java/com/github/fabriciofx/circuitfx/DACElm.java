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

class DACElm extends ChipElm {
    public DACElm(int xx, int yy) {
        super(xx, yy);
    }

    public DACElm(
        int xa, int ya, int xb, int yb, int f,
        StringTokenizer st
    ) {
        super(xa, ya, xb, yb, f, st);
    }

    String getChipName() {
        return "DAC";
    }

    boolean needsBits() {
        return true;
    }

    void setupPins() {
        sizeX = 2;
        sizeY = bits > 2 ? bits : 2;
        pins = new ChipElm.Pin[getPostCount()];
        int i;
        for (i = 0; i != bits; i++) {
            pins[i] = new ChipElm.Pin(bits - 1 - i, SIDE_W, "D" + i);
        }
        pins[bits] = new ChipElm.Pin(0, SIDE_E, "O");
        pins[bits].output = true;
        pins[bits + 1] = new ChipElm.Pin(sizeY - 1, SIDE_E, "V+");
        allocNodes();
    }

    void doStep() {
        int ival = 0;
        int i;
        for (i = 0; i != bits; i++) {
            if (volts[i] > 2.5) {
                ival |= 1 << i;
            }
        }
        int ivalmax = (1 << bits) - 1;
        double v = ival * volts[bits + 1] / ivalmax;
        sim.updateVoltageSource(0, nodes[bits], pins[bits].voltSource, v);
    }

    int getVoltageSourceCount() {
        return 1;
    }

    int getPostCount() {
        return bits + 2;
    }

    int getDumpType() {
        return 166;
    }
}

