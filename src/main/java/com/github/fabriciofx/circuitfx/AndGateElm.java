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

    class AndGateElm extends GateElm {
	public AndGateElm(int xx, int yy) { super(xx, yy); }
	public AndGateElm(int xa, int ya, int xb, int yb, int f,
			  StringTokenizer st) {
	    super(xa, ya, xb, yb, f, st);
	}
	void setPoints() {
	    super.setPoints();
	    
	    // 0=topleft, 1-10 = top curve, 11 = right, 12-21=bottom curve,
	    // 22 = bottom left
	    Point triPoints[] = newPointArray(23);
	    interpPoint2(lead1, lead2, triPoints[0], triPoints[22], 0, hs2);
	    int i;
	    for (i = 0; i != 10; i++) {
		double a = i*.1;
		double b = Math.sqrt(1-a*a);
		interpPoint2(lead1, lead2,
			     triPoints[i+1], triPoints[21-i],
			     .5+a/2, b*hs2);
	    }
	    triPoints[11] = new Point(lead2);
	    if (isInverting()) {
		pcircle = interpPoint(point1, point2, .5+(ww+4)/dn);
		lead2 = interpPoint(point1, point2, .5+(ww+8)/dn);
	    }
	    gatePoly = createPolygon(triPoints);
	}
	String getGateName() { return "AND gate"; }
	boolean calcFunction() {
	    int i;
	    boolean f = true;
	    for (i = 0; i != inputCount; i++)
		f &= getInput(i);
	    return f;
	}
	int getDumpType() { return 150; }
	int getShortcut() { return '2'; }
    }
