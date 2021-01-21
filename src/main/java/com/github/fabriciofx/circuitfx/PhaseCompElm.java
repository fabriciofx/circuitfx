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

class PhaseCompElm extends ChipElm {
    boolean ff1, ff2;

    public PhaseCompElm(int xx, int yy) {
        super(xx, yy);
    }

    public PhaseCompElm(
        int xa, int ya, int xb, int yb, int f,
        StringTokenizer st
    ) {
        super(xa, ya, xb, yb, f, st);
    }

    String getChipName() {
        return "phase comparator";
    }

    void setupPins() {
        sizeX = 2;
        sizeY = 2;
        pins = new ChipElm.Pin[3];
        pins[0] = new ChipElm.Pin(0, SIDE_W, "I1");
        pins[1] = new ChipElm.Pin(1, SIDE_W, "I2");
        pins[2] = new ChipElm.Pin(0, SIDE_E, "O");
        pins[2].output = true;
    }

    boolean nonLinear() {
        return true;
    }

    void stamp() {
        int vn = sim.nodeList.size() + pins[2].voltSource;
        sim.stampNonLinear(vn);
        sim.stampNonLinear(0);
        sim.stampNonLinear(nodes[2]);
    }

    void doStep() {
        boolean v1 = volts[0] > 2.5;
        boolean v2 = volts[1] > 2.5;
        if (v1 && !pins[0].value) {
            ff1 = true;
        }
        if (v2 && !pins[1].value) {
            ff2 = true;
        }
        if (ff1 && ff2) {
            ff1 = ff2 = false;
        }
        double out = (ff1) ? 5 : (ff2) ? 0 : -1;
        //System.out.println(out + " " + v1 + " " + v2);
        if (out != -1) {
            sim.stampVoltageSource(0, nodes[2], pins[2].voltSource, out);
        } else {
            // tie current through output pin to 0
            int vn = sim.nodeList.size() + pins[2].voltSource;
            sim.stampMatrix(vn, vn, 1);
        }
        pins[0].value = v1;
        pins[1].value = v2;
    }

    int getPostCount() {
        return 3;
    }

    int getVoltageSourceCount() {
        return 1;
    }

    int getDumpType() {
        return 161;
    }
}

