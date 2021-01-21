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

class RailElm extends VoltageElm {
    public RailElm(int xx, int yy) { super(xx, yy, WF_DC); }
    RailElm(int xx, int yy, int wf) { super(xx, yy, wf); }
    public RailElm(int xa, int ya, int xb, int yb, int f,
		   StringTokenizer st) {
	super(xa, ya, xb, yb, f, st);
    }
    final int FLAG_CLOCK = 1;
    int getDumpType() { return 'R'; }
    int getPostCount() { return 1; }
	
    void setPoints() {
	super.setPoints();
	lead1 = interpPoint(point1, point2, 1-circleSize/dn);
    }
    void draw(Graphics g) {
	setBbox(point1, point2, circleSize);
	setVoltageColor(g, volts[0]);
	drawThickLine(g, point1, lead1);
	boolean clock = waveform == WF_SQUARE && (flags & FLAG_CLOCK) != 0;
	if (waveform == WF_DC || waveform == WF_VAR || clock) {
	    Font f = new Font("SansSerif", 0, 12);
	    g.setFont(f);
	    g.setColor(needsHighlight() ? selectColor : whiteColor);
	    setPowerColor(g, false);
	    double v = getVoltage();
	    String s = getShortUnitText(v, "V");
	    if (Math.abs(v) < 1)
		s = showFormat.format(v) + "V";
	    if (getVoltage() > 0)
		s = "+" + s;
	    if (this instanceof AntennaElm)
		s = "Ant";
	    if (clock)
		s = "CLK";
	    drawCenteredText(g, s, x2, y2, true);
	} else {
	    drawWaveform(g, point2);
	}
	drawPosts(g);
	curcount = updateDotCount(-current, curcount);
	if (sim.dragElm != this)
	    drawDots(g, point1, lead1, curcount);
    }
    double getVoltageDiff() { return volts[0]; }
    void stamp() {
	if (waveform == WF_DC)
	    sim.stampVoltageSource(0, nodes[0], voltSource, getVoltage());
	else
	    sim.stampVoltageSource(0, nodes[0], voltSource);
    }
    void doStep() {
	if (waveform != WF_DC)
	    sim.updateVoltageSource(0, nodes[0], voltSource, getVoltage());
    }
    boolean hasGroundConnection(int n1) { return true; }
    int getShortcut() { return 'V'; }
}
