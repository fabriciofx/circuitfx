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
// stub PhotoResistorElm based on SparkGapElm
// FIXME need to uncomment PhotoResistorElm line from CirSim.java
// FIXME need to add PhotoResistorElm.java to srclist
import java.awt.Graphics;
import java.awt.Label;
import java.awt.Point;
import java.awt.Scrollbar;
import java.util.StringTokenizer;

class PhotoResistorElm extends CircuitElm {
    double minresistance, maxresistance;
    double resistance;
    Scrollbar slider;
    Label label;
    Point ps3, ps4;

    public PhotoResistorElm(int xx, int yy) {
        super(xx, yy);
        maxresistance = 1e9;
        minresistance = 1e3;
        createSlider();
    }

    public PhotoResistorElm(
        int xa, int ya, int xb, int yb, int f,
        StringTokenizer st
    ) {
        super(xa, ya, xb, yb, f);
        minresistance = new Double(st.nextToken()).doubleValue();
        maxresistance = new Double(st.nextToken()).doubleValue();
        createSlider();
    }

    boolean nonLinear() {
        return true;
    }

    int getDumpType() {
        return 190;
    }

    String dump() {
        return super.dump() + " " + minresistance + " " + maxresistance;
    }

    void createSlider() {
        sim.main.add(label = new Label("Light Level", Label.CENTER));
        int value = 50;
        sim.main.add(slider = new Scrollbar(
            Scrollbar.HORIZONTAL,
            value,
            1,
            0,
            101
        ));
        sim.main.validate();
    }

    void setPoints() {
        super.setPoints();
        calcLeads(32);
        ps3 = new Point();
        ps4 = new Point();
    }

    void delete() {
        sim.main.remove(label);
        sim.main.remove(slider);
    }

    void draw(Graphics g) {
        int i;
        double v1 = volts[0];
        double v2 = volts[1];
        setBbox(point1, point2, 6);
        draw2Leads(g);
        // FIXME need to draw properly, see ResistorElm.java
        setPowerColor(g, true);
        doDots(g);
        drawPosts(g);
    }

    void calculateCurrent() {
        double vd = volts[0] - volts[1];
        current = vd / resistance;
    }

    void startIteration() {
        double vd = volts[0] - volts[1];
        // FIXME set resistance as appropriate, using slider.getValue()
        resistance = minresistance;
        //System.out.print(this + " res current set to " + current + "\n");
    }

    void doStep() {
        sim.stampResistor(nodes[0], nodes[1], resistance);
    }

    void stamp() {
        sim.stampNonLinear(nodes[0]);
        sim.stampNonLinear(nodes[1]);
    }

    void getInfo(String[] arr) {
        // FIXME
        arr[0] = "spark gap";
        getBasicInfo(arr);
        arr[3] = "R = " + getUnitText(resistance, sim.ohmString);
        arr[4] = "Ron = " + getUnitText(minresistance, sim.ohmString);
        arr[5] = "Roff = " + getUnitText(maxresistance, sim.ohmString);
    }

    public EditInfo getEditInfo(int n) {
        // ohmString doesn't work here on linux
        if (n == 0) {
            return new EditInfo("Min resistance (ohms)", minresistance, 0, 0);
        }
        if (n == 1) {
            return new EditInfo("Max resistance (ohms)", maxresistance, 0, 0);
        }
        return null;
    }

    public void setEditValue(int n, EditInfo ei) {
        if (ei.value > 0 && n == 0) {
            minresistance = ei.value;
        }
        if (ei.value > 0 && n == 1) {
            maxresistance = ei.value;
        }
    }
}

