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

class ADCElm extends ChipElm {
    public ADCElm(int xx, int yy) {
        super(xx, yy);
    }

    public ADCElm(
        int xa, int ya, int xb, int yb, int f,
        StringTokenizer st
    ) {
        super(xa, ya, xb, yb, f, st);
    }

    String getChipName() {
        return "ADC";
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
            pins[i] = new ChipElm.Pin(bits - 1 - i, SIDE_E, "D" + i);
            pins[i].output = true;
        }
        pins[bits] = new ChipElm.Pin(0, SIDE_W, "In");
        pins[bits + 1] = new ChipElm.Pin(sizeY - 1, SIDE_W, "V+");
        allocNodes();
    }

    void execute() {
        int imax = (1 << bits) - 1;
        // if we round, the half-flash doesn't work
        double val = imax * volts[bits] / volts[bits + 1]; // + .5;
        int ival = (int) val;
        ival = min(imax, max(0, ival));
        int i;
        for (i = 0; i != bits; i++) {
            pins[i].value = ((ival & (1 << i)) != 0);
        }
    }

    int getVoltageSourceCount() {
        return bits;
    }

    int getPostCount() {
        return bits + 2;
    }

    int getDumpType() {
        return 167;
    }
}

