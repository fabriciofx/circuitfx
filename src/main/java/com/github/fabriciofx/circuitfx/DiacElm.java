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
import java.awt.Point;
import java.util.StringTokenizer;

/**
 * Stub implementation of DiacElm, based on SparkGapElm
 *
 */
// @todo: need to add DiacElm.java to srclist.
// @todo: need to uncomment DiacElm line from CirSim.
class DiacElm extends CircuitElm {
    double onresistance, offresistance, breakdown, holdcurrent;
    boolean state;
    Point ps3, ps4;

    public DiacElm(int xx, int yy) {
        super(xx, yy);
        // @todo: need to adjust defaults to make sense for diac
        offresistance = 1e9;
        onresistance = 1e3;
        breakdown = 1e3;
        holdcurrent = 0.001;
        state = false;
    }

    public DiacElm(
        int xa, int ya, int xb, int yb, int f,
        StringTokenizer st
    ) {
        super(xa, ya, xb, yb, f);
        onresistance = new Double(st.nextToken()).doubleValue();
        offresistance = new Double(st.nextToken()).doubleValue();
        breakdown = new Double(st.nextToken()).doubleValue();
        holdcurrent = new Double(st.nextToken()).doubleValue();
    }

    boolean nonLinear() {
        return true;
    }

    int getDumpType() {
        return 203;
    }

    String dump() {
        return super.dump() + " " + onresistance + " " + offresistance + " "
            + breakdown + " " + holdcurrent;
    }

    void setPoints() {
        super.setPoints();
        calcLeads(32);
        ps3 = new Point();
        ps4 = new Point();
    }

    void draw(Graphics g) {
        // @todo: need to draw Diac
        int i;
        double v1 = volts[0];
        double v2 = volts[1];
        setBbox(point1, point2, 6);
        draw2Leads(g);
        setPowerColor(g, true);
        doDots(g);
        drawPosts(g);
    }

    void calculateCurrent() {
        double vd = volts[0] - volts[1];
        if (state) {
            current = vd / onresistance;
        } else {
            current = vd / offresistance;
        }
    }

    void startIteration() {
        double vd = volts[0] - volts[1];
        if (Math.abs(current) < holdcurrent) {
            state = false;
        }
        if (Math.abs(vd) > breakdown) {
            state = true;
        }
    }

    void doStep() {
        if (state) {
            sim.stampResistor(nodes[0], nodes[1], onresistance);
        } else {
            sim.stampResistor(nodes[0], nodes[1], offresistance);
        }
    }

    void stamp() {
        sim.stampNonLinear(nodes[0]);
        sim.stampNonLinear(nodes[1]);
    }

    void getInfo(String[] arr) {
        arr[0] = "spark gap";
        getBasicInfo(arr);
        arr[3] = state ? "on" : "off";
        arr[4] = "Ron = " + getUnitText(onresistance, sim.ohmString);
        arr[5] = "Roff = " + getUnitText(offresistance, sim.ohmString);
        arr[6] = "Vbrkdn = " + getUnitText(breakdown, "V");
        arr[7] = "Ihold = " + getUnitText(holdcurrent, "A");
    }

    public EditInfo getEditInfo(int n) {
        if (n == 0) {
            return new EditInfo("On resistance (ohms)", onresistance, 0, 0);
        }
        if (n == 1) {
            return new EditInfo("Off resistance (ohms)", offresistance, 0, 0);
        }
        if (n == 2) {
            return new EditInfo("Breakdown voltage (volts)", breakdown, 0, 0);
        }
        if (n == 3) {
            return new EditInfo("Hold current (amps)", holdcurrent, 0, 0);
        }
        return null;
    }

    public void setEditValue(int n, EditInfo ei) {
        if (ei.value > 0 && n == 0) {
            onresistance = ei.value;
        }
        if (ei.value > 0 && n == 1) {
            offresistance = ei.value;
        }
        if (ei.value > 0 && n == 2) {
            breakdown = ei.value;
        }
        if (ei.value > 0 && n == 3) {
            holdcurrent = ei.value;
        }
    }
}

