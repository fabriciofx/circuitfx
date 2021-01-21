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
import java.awt.Graphics;
import java.util.StringTokenizer;

class CC2Elm extends ChipElm {
    double gain;

    public CC2Elm(int xx, int yy) {
        super(xx, yy);
        gain = 1;
    }

    public CC2Elm(int xx, int yy, int g) {
        super(xx, yy);
        gain = g;
    }

    public CC2Elm(
        int xa, int ya, int xb, int yb, int f,
        StringTokenizer st
    ) {
        super(xa, ya, xb, yb, f, st);
        gain = new Double(st.nextToken()).doubleValue();
    }

    String dump() {
        return super.dump() + " " + gain;
    }

    String getChipName() {
        return "CC2";
    }

    void setupPins() {
        sizeX = 2;
        sizeY = 3;
        pins = new ChipElm.Pin[3];
        pins[0] = new ChipElm.Pin(0, SIDE_W, "X");
        pins[0].output = true;
        pins[1] = new ChipElm.Pin(2, SIDE_W, "Y");
        pins[2] = new ChipElm.Pin(1, SIDE_E, "Z");
    }

    void getInfo(String[] arr) {
        arr[0] = (gain == 1) ? "CCII+" : "CCII-";
        arr[1] = "X,Y = " + getVoltageText(volts[0]);
        arr[2] = "Z = " + getVoltageText(volts[2]);
        arr[3] = "I = " + getCurrentText(pins[0].current);
    }

    //boolean nonLinear() { return true; }
    void stamp() {
        // X voltage = Y voltage
        sim.stampVoltageSource(0, nodes[0], pins[0].voltSource);
        sim.stampVCVS(0, nodes[1], 1, pins[0].voltSource);
        // Z current = gain * X current
        sim.stampCCCS(0, nodes[2], pins[0].voltSource, gain);
    }

    void draw(Graphics g) {
        pins[2].current = pins[0].current * gain;
        drawChip(g);
    }

    int getPostCount() {
        return 3;
    }

    int getVoltageSourceCount() {
        return 1;
    }

    int getDumpType() {
        return 179;
    }
}

class CC2NegElm extends CC2Elm {
    public CC2NegElm(int xx, int yy) {
        super(xx, yy, -1);
    }

    Class getDumpClass() {
        return CC2Elm.class;
    }
}
