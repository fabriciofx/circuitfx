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
import java.awt.Graphics;
import java.util.StringTokenizer;

class InductorElm extends CircuitElm {
    Inductor ind;
    double inductance;

    public InductorElm(int xx, int yy) {
        super(xx, yy);
        ind = new Inductor(sim);
        inductance = 1;
        ind.setup(inductance, current, flags);
    }

    public InductorElm(
        int xa, int ya, int xb, int yb, int f,
        StringTokenizer st
    ) {
        super(xa, ya, xb, yb, f);
        ind = new Inductor(sim);
        inductance = new Double(st.nextToken()).doubleValue();
        current = new Double(st.nextToken()).doubleValue();
        ind.setup(inductance, current, flags);
    }

    int getDumpType() {
        return 'l';
    }

    String dump() {
        return super.dump() + " " + inductance + " " + current;
    }

    void setPoints() {
        super.setPoints();
        calcLeads(32);
    }

    void draw(Graphics g) {
        double v1 = volts[0];
        double v2 = volts[1];
        int i;
        int hs = 8;
        setBbox(point1, point2, hs);
        draw2Leads(g);
        setPowerColor(g, false);
        drawCoil(g, 8, lead1, lead2, v1, v2);
        if (sim.showValuesCheckItem.getState()) {
            String s = getShortUnitText(inductance, "H");
            drawValues(g, s, hs);
        }
        doDots(g);
        drawPosts(g);
    }

    void reset() {
        current = volts[0] = volts[1] = curcount = 0;
        ind.reset();
    }

    void stamp() {
        ind.stamp(nodes[0], nodes[1]);
    }

    void startIteration() {
        ind.startIteration(volts[0] - volts[1]);
    }

    boolean nonLinear() {
        return ind.nonLinear();
    }

    void calculateCurrent() {
        double voltdiff = volts[0] - volts[1];
        current = ind.calculateCurrent(voltdiff);
    }

    void doStep() {
        double voltdiff = volts[0] - volts[1];
        ind.doStep(voltdiff);
    }

    void getInfo(String[] arr) {
        arr[0] = "inductor";
        getBasicInfo(arr);
        arr[3] = "L = " + getUnitText(inductance, "H");
        arr[4] = "P = " + getUnitText(getPower(), "W");
    }

    public EditInfo getEditInfo(int n) {
        if (n == 0) {
            return new EditInfo("Inductance (H)", inductance, 0, 0);
        }
        if (n == 1) {
            EditInfo ei = new EditInfo("", 0, -1, -1);
            ei.checkbox = new Checkbox(
                "Trapezoidal Approximation",
                ind.isTrapezoidal()
            );
            return ei;
        }
        return null;
    }

    public void setEditValue(int n, EditInfo ei) {
        if (n == 0) {
            inductance = ei.value;
        }
        if (n == 1) {
            if (ei.checkbox.getState()) {
                flags &= ~Inductor.FLAG_BACK_EULER;
            } else {
                flags |= Inductor.FLAG_BACK_EULER;
            }
        }
        ind.setup(inductance, current, flags);
    }
}
