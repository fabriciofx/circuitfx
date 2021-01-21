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
import java.awt.*;
import java.util.StringTokenizer;

    class InverterElm extends CircuitElm {
	double slewRate; // V/ns
	public InverterElm(int xx, int yy) {
	    super(xx, yy);
	    noDiagonal = true;
	    slewRate = .5;
	}
	public InverterElm(int xa, int ya, int xb, int yb, int f,
			      StringTokenizer st) {
	    super(xa, ya, xb, yb, f);
	    noDiagonal = true;
	    try {
		slewRate = new Double (st.nextToken()).doubleValue();
	    } catch (Exception e) {
		slewRate = .5;
	    }
	}
	String dump() {
	    return super.dump() + " " + slewRate;
	}
	
	int getDumpType() { return 'I'; }
	void draw(Graphics g) {
	    drawPosts(g);
	    draw2Leads(g);
	    g.setColor(needsHighlight() ? selectColor : lightGrayColor);
	    drawThickPolygon(g, gatePoly);
	    drawThickCircle(g, pcircle.x, pcircle.y, 3);
	    curcount = updateDotCount(current, curcount);
	    drawDots(g, lead2, point2, curcount);
	}
	Polygon gatePoly;
	Point pcircle;
	void setPoints() {
	    super.setPoints();
	    int hs = 16;
	    int ww = 16;
	    if (ww > dn/2)
		ww = (int) (dn/2);
	    lead1 = interpPoint(point1, point2, .5-ww/dn);
	    lead2 = interpPoint(point1, point2, .5+(ww+2)/dn);
	    pcircle = interpPoint(point1, point2, .5+(ww-2)/dn);
	    Point triPoints[] = newPointArray(3);
	    interpPoint2(lead1, lead2, triPoints[0], triPoints[1], 0, hs);
	    triPoints[2] = interpPoint(point1, point2, .5+(ww-5)/dn);
	    gatePoly = createPolygon(triPoints);
	    setBbox(point1, point2, hs);
	}
	int getVoltageSourceCount() { return 1; }
	void stamp() {
	    sim.stampVoltageSource(0, nodes[1], voltSource);
	}
	void doStep() {
	    double v0 = volts[1];
	    double out = volts[0] > 2.5 ? 0 : 5;
	    double maxStep = slewRate * sim.timeStep * 1e9;
	    out = Math.max(Math.min(v0+maxStep, out), v0-maxStep);
	    sim.updateVoltageSource(0, nodes[1], voltSource, out);
	}
	double getVoltageDiff() { return volts[0]; }
	void getInfo(String arr[]) {
	    arr[0] = "inverter";
	    arr[1] = "Vi = " + getVoltageText(volts[0]);
	    arr[2] = "Vo = " + getVoltageText(volts[1]);
	}
	public EditInfo getEditInfo(int n) {
	    if (n == 0)
		return new EditInfo("Slew Rate (V/ns)", slewRate, 0, 0);
	    return null;
	}
	public void setEditValue(int n, EditInfo ei) {
	    slewRate = ei.value;
	}
	// there is no current path through the inverter input, but there
	// is an indirect path through the output to ground.
	boolean getConnection(int n1, int n2) { return false; }
	boolean hasGroundConnection(int n1) {
	    return (n1 == 1);
	}
	int getShortcut() { return '1'; }
    }
