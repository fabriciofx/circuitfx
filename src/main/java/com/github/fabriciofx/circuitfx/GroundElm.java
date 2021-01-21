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
import java.util.StringTokenizer;

class GroundElm extends CircuitElm {
    public GroundElm(int xx, int yy) {
        super(xx, yy);
    }

    public GroundElm(
        int xa, int ya, int xb, int yb, int f,
        StringTokenizer st
    ) {
        super(xa, ya, xb, yb, f);
    }

    int getDumpType() {
        return 'g';
    }

    int getPostCount() {
        return 1;
    }

    void draw(Graphics g) {
        setVoltageColor(g, 0);
        drawThickLine(g, point1, point2);
        int i;
        for (i = 0; i != 3; i++) {
            int a = 10 - i * 4;
            int b = i * 5; // -10;
            interpPoint2(point1, point2, ps1, ps2, 1 + b / dn, a);
            drawThickLine(g, ps1, ps2);
        }
        doDots(g);
        interpPoint(point1, point2, ps2, 1 + 11. / dn);
        setBbox(point1, ps2, 11);
        drawPost(g, x, y, nodes[0]);
    }

    void setCurrent(int x, double c) {
        current = -c;
    }

    void stamp() {
        sim.stampVoltageSource(0, nodes[0], voltSource, 0);
    }

    double getVoltageDiff() {
        return 0;
    }

    int getVoltageSourceCount() {
        return 1;
    }

    void getInfo(String[] arr) {
        arr[0] = "ground";
        arr[1] = "I = " + getCurrentText(getCurrent());
    }

    boolean hasGroundConnection(int n1) {
        return true;
    }

    int getShortcut() {
        return 'g';
    }
}
