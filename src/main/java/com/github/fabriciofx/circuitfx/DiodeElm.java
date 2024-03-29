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

import java.awt.Graphics;
import java.awt.Point;
import java.awt.Polygon;
import java.util.StringTokenizer;

class DiodeElm extends CircuitElm {
    static final int FLAG_FWDROP = 1;
    final double defaultdrop = .805904783;
    final int hs = 8;
    Diode diode;
    double fwdrop, zvoltage;
    Polygon poly;
    Point[] cathode;

    public DiodeElm(int xx, int yy) {
        super(xx, yy);
        diode = new Diode(sim);
        fwdrop = defaultdrop;
        zvoltage = 0;
        setup();
    }

    public DiodeElm(
        int xa, int ya, int xb, int yb, int f,
        StringTokenizer st
    ) {
        super(xa, ya, xb, yb, f);
        diode = new Diode(sim);
        fwdrop = defaultdrop;
        zvoltage = 0;
        if ((f & FLAG_FWDROP) > 0) {
            try {
                fwdrop = new Double(st.nextToken()).doubleValue();
            } catch (Exception e) {
            }
        }
        setup();
    }

    boolean nonLinear() {
        return true;
    }

    void setup() {
        diode.setup(fwdrop, zvoltage);
    }

    int getDumpType() {
        return 'd';
    }

    String dump() {
        flags |= FLAG_FWDROP;
        return super.dump() + " " + fwdrop;
    }

    void setPoints() {
        super.setPoints();
        calcLeads(16);
        cathode = newPointArray(2);
        Point[] pa = newPointArray(2);
        interpPoint2(lead1, lead2, pa[0], pa[1], 0, hs);
        interpPoint2(lead1, lead2, cathode[0], cathode[1], 1, hs);
        poly = createPolygon(pa[0], pa[1], lead2);
    }

    void draw(Graphics g) {
        drawDiode(g);
        doDots(g);
        drawPosts(g);
    }

    void reset() {
        diode.reset();
        volts[0] = volts[1] = curcount = 0;
    }

    void drawDiode(Graphics g) {
        setBbox(point1, point2, hs);
        double v1 = volts[0];
        double v2 = volts[1];
        draw2Leads(g);
        // draw arrow thingy
        setPowerColor(g, true);
        setVoltageColor(g, v1);
        g.fillPolygon(poly);
        // draw thing arrow is pointing to
        setVoltageColor(g, v2);
        drawThickLine(g, cathode[0], cathode[1]);
    }

    void stamp() {
        diode.stamp(nodes[0], nodes[1]);
    }

    void doStep() {
        diode.doStep(volts[0] - volts[1]);
    }

    void calculateCurrent() {
        current = diode.calculateCurrent(volts[0] - volts[1]);
    }

    void getInfo(String[] arr) {
        arr[0] = "diode";
        arr[1] = "I = " + getCurrentText(getCurrent());
        arr[2] = "Vd = " + getVoltageText(getVoltageDiff());
        arr[3] = "P = " + getUnitText(getPower(), "W");
        arr[4] = "Vf = " + getVoltageText(fwdrop);
    }

    public EditInfo getEditInfo(int n) {
        if (n == 0) {
            return new EditInfo("Fwd Voltage @ 1A", fwdrop, 10, 1000);
        }
        return null;
    }

    public void setEditValue(int n, EditInfo ei) {
        fwdrop = ei.value;
        setup();
    }

    int getShortcut() {
        return 'd';
    }
}
