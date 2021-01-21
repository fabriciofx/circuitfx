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

    class CurrentElm extends CircuitElm {
	double currentValue;
	public CurrentElm(int xx, int yy) {
	    super(xx, yy);
	    currentValue = .01;
	}
	public CurrentElm(int xa, int ya, int xb, int yb, int f,
		   StringTokenizer st) {
	    super(xa, ya, xb, yb, f);
	    try {
		currentValue = new Double(st.nextToken()).doubleValue();
	    } catch (Exception e) {
		currentValue = .01;
	    }
	}
	String dump() {
	    return super.dump() + " " + currentValue;
	}
	int getDumpType() { return 'i'; }
	
	Polygon arrow;
	Point ashaft1, ashaft2, center;
	void setPoints() {
	    super.setPoints();
	    calcLeads(26);
	    ashaft1 = interpPoint(lead1, lead2, .25);
	    ashaft2 = interpPoint(lead1, lead2, .6);
	    center = interpPoint(lead1, lead2, .5);
	    Point p2 = interpPoint(lead1, lead2, .75);
	    arrow = calcArrow(center, p2, 4, 4);
	}
	void draw(Graphics g) {
	    int cr = 12;
	    draw2Leads(g);
	    setVoltageColor(g, (volts[0]+volts[1])/2);
	    setPowerColor(g, false);
	    
	    drawThickCircle(g, center.x, center.y, cr);
	    drawThickLine(g, ashaft1, ashaft2);

	    g.fillPolygon(arrow);
	    setBbox(point1, point2, cr);
	    doDots(g);
	    if (sim.showValuesCheckItem.getState()) {
		String s = getShortUnitText(currentValue, "A");
		if (dx == 0 || dy == 0)
		    drawValues(g, s, cr);
	    }
	    drawPosts(g);
	}
	void stamp() {
	    current = currentValue;
	    sim.stampCurrentSource(nodes[0], nodes[1], current);
	}
	public EditInfo getEditInfo(int n) {
	    if (n == 0)
		return new EditInfo("Current (A)", currentValue, 0, .1);
	    return null;
	}
	public void setEditValue(int n, EditInfo ei) {
	    currentValue = ei.value;
	}
	void getInfo(String arr[]) {
	    arr[0] = "current source";
	    getBasicInfo(arr);
	}
	double getVoltageDiff() {
	    return volts[1] - volts[0];
	}
    }
