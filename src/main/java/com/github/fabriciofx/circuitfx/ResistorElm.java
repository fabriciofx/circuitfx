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
import java.util.StringTokenizer;

class ResistorElm extends CircuitElm {
    double resistance;
    Point ps3, ps4;

    public ResistorElm(int xx, int yy) {
        super(xx, yy);
        resistance = 100;
    }

    public ResistorElm(
        int xa, int ya, int xb, int yb, int f,
        StringTokenizer st
    ) {
        super(xa, ya, xb, yb, f);
        resistance = new Double(st.nextToken()).doubleValue();
    }

    int getDumpType() {
        return 'r';
    }

    String dump() {
        return super.dump() + " " + resistance;
    }

    void setPoints() {
        super.setPoints();
        calcLeads(32);
        ps3 = new Point();
        ps4 = new Point();
    }

    void draw(Graphics g) {
        int segments = 16;
        int i;
        int ox = 0;
        int hs = sim.euroResistorCheckItem.getState() ? 6 : 8;
        double v1 = volts[0];
        double v2 = volts[1];
        setBbox(point1, point2, hs);
        draw2Leads(g);
        setPowerColor(g, true);
        double segf = 1. / segments;
        if (!sim.euroResistorCheckItem.getState()) {
            // draw zigzag
            for (i = 0; i != segments; i++) {
                int nx = 0;
                switch (i & 3) {
                    case 0:
                        nx = 1;
                        break;
                    case 2:
                        nx = -1;
                        break;
                    default:
                        nx = 0;
                        break;
                }
                double v = v1 + (v2 - v1) * i / segments;
                setVoltageColor(g, v);
                interpPoint(lead1, lead2, ps1, i * segf, hs * ox);
                interpPoint(lead1, lead2, ps2, (i + 1) * segf, hs * nx);
                drawThickLine(g, ps1, ps2);
                ox = nx;
            }
        } else {
            // draw rectangle
            setVoltageColor(g, v1);
            interpPoint2(lead1, lead2, ps1, ps2, 0, hs);
            drawThickLine(g, ps1, ps2);
            for (i = 0; i != segments; i++) {
                double v = v1 + (v2 - v1) * i / segments;
                setVoltageColor(g, v);
                interpPoint2(lead1, lead2, ps1, ps2, i * segf, hs);
                interpPoint2(lead1, lead2, ps3, ps4, (i + 1) * segf, hs);
                drawThickLine(g, ps1, ps3);
                drawThickLine(g, ps2, ps4);
            }
            interpPoint2(lead1, lead2, ps1, ps2, 1, hs);
            drawThickLine(g, ps1, ps2);
        }
        if (sim.showValuesCheckItem.getState()) {
            String s = getShortUnitText(resistance, "");
            drawValues(g, s, hs);
        }
        doDots(g);
        drawPosts(g);
    }

    void calculateCurrent() {
        current = (volts[0] - volts[1]) / resistance;
    }

    void stamp() {
        sim.stampResistor(nodes[0], nodes[1], resistance);
    }

    void getInfo(String[] arr) {
        arr[0] = "resistor";
        getBasicInfo(arr);
        arr[3] = "R = " + getUnitText(resistance, sim.ohmString);
        arr[4] = "P = " + getUnitText(getPower(), "W");
    }

    public EditInfo getEditInfo(int n) {
        // ohmString doesn't work here on linux
        if (n == 0) {
            return new EditInfo("Resistance (ohms)", resistance, 0, 0);
        }
        return null;
    }

    public void setEditValue(int n, EditInfo ei) {
        if (ei.value > 0) {
            resistance = ei.value;
        }
    }

    int getShortcut() {
        return 'r';
    }
}
