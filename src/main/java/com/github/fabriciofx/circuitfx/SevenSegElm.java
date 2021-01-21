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
import java.awt.Color;
import java.awt.Graphics;
import java.util.StringTokenizer;

class SevenSegElm extends ChipElm {
    Color darkred;

    public SevenSegElm(int xx, int yy) {
        super(xx, yy);
    }

    public SevenSegElm(
        int xa, int ya, int xb, int yb, int f,
        StringTokenizer st
    ) {
        super(xa, ya, xb, yb, f, st);
    }

    String getChipName() {
        return "7-segment driver/display";
    }

    void setupPins() {
        darkred = new Color(30, 0, 0);
        sizeX = 4;
        sizeY = 4;
        pins = new ChipElm.Pin[7];
        pins[0] = new ChipElm.Pin(0, SIDE_W, "a");
        pins[1] = new ChipElm.Pin(1, SIDE_W, "b");
        pins[2] = new ChipElm.Pin(2, SIDE_W, "c");
        pins[3] = new ChipElm.Pin(3, SIDE_W, "d");
        pins[4] = new ChipElm.Pin(1, SIDE_S, "e");
        pins[5] = new ChipElm.Pin(2, SIDE_S, "f");
        pins[6] = new ChipElm.Pin(3, SIDE_S, "g");
    }

    void draw(Graphics g) {
        drawChip(g);
        g.setColor(Color.red);
        int xl = x + cspc * 5;
        int yl = y + cspc;
        setColor(g, 0);
        drawThickLine(g, xl, yl, xl + cspc, yl);
        setColor(g, 1);
        drawThickLine(g, xl + cspc, yl, xl + cspc, yl + cspc);
        setColor(g, 2);
        drawThickLine(g, xl + cspc, yl + cspc, xl + cspc, yl + cspc2);
        setColor(g, 3);
        drawThickLine(g, xl, yl + cspc2, xl + cspc, yl + cspc2);
        setColor(g, 4);
        drawThickLine(g, xl, yl + cspc, xl, yl + cspc2);
        setColor(g, 5);
        drawThickLine(g, xl, yl, xl, yl + cspc);
        setColor(g, 6);
        drawThickLine(g, xl, yl + cspc, xl + cspc, yl + cspc);
    }

    void setColor(Graphics g, int p) {
        g.setColor(pins[p].value ? Color.red :
                       sim.printableCheckItem.getState() ? Color.white :
                           darkred);
    }

    int getPostCount() {
        return 7;
    }

    int getVoltageSourceCount() {
        return 0;
    }

    int getDumpType() {
        return 157;
    }
}
