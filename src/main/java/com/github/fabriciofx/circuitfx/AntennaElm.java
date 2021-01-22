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

class AntennaElm extends RailElm {
    double fmphase;

    public AntennaElm(int xx, int yy) {
        super(xx, yy, WF_DC);
    }

    public AntennaElm(
        int xa, int ya, int xb, int yb, int f,
        StringTokenizer st
    ) {
        super(xa, ya, xb, yb, f, st);
        waveform = WF_DC;
    }

    void stamp() {
        sim.stampVoltageSource(0, nodes[0], voltSource);
    }

    void doStep() {
        sim.updateVoltageSource(0, nodes[0], voltSource, getVoltage());
    }

    double getVoltage() {
        fmphase += 2 * pi * (2200 + Math.sin(2 * pi * sim.t * 13) * 100) *
            sim.timeStep;
        double fm = 3 * Math.sin(fmphase);
        return Math.sin(2 * pi * sim.t * 3000) * (1.3 + Math.sin(
            2 * pi * sim.t * 12)) * 3 +
            Math.sin(2 * pi * sim.t * 2710) * (1.3 + Math.sin(
                2 * pi * sim.t * 13)) * 3 +
            Math.sin(2 * pi * sim.t * 2433) * (1.3 + Math.sin(
                2 * pi * sim.t * 14)) * 3 + fm;
    }

    int getDumpType() {
        return 'A';
    }

    int getShortcut() {
        return 0;
    }
}
