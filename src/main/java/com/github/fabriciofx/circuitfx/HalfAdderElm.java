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
import java.util.StringTokenizer;

class HalfAdderElm extends ChipElm {
    public HalfAdderElm(int xx, int yy) {
        super(xx, yy);
    }

    public HalfAdderElm(
        int xa, int ya, int xb, int yb, int f,
        StringTokenizer st
    ) {
        super(xa, ya, xb, yb, f, st);
    }

    boolean hasReset() {
        return false;
    }

    String getChipName() {
        return "Half Adder";
    }

    void setupPins() {
        sizeX = 2;
        sizeY = 2;
        pins = new ChipElm.Pin[getPostCount()];
        pins[0] = new ChipElm.Pin(0, SIDE_E, "S");
        pins[0].output = true;
        pins[1] = new ChipElm.Pin(1, SIDE_E, "C");
        pins[1].output = true;
        pins[2] = new ChipElm.Pin(0, SIDE_W, "A");
        pins[3] = new ChipElm.Pin(1, SIDE_W, "B");
    }

    int getPostCount() {
        return 4;
    }

    int getVoltageSourceCount() {
        return 2;
    }

    void execute() {
        pins[0].value = pins[2].value ^ pins[3].value;
        pins[1].value = pins[2].value && pins[3].value;
    }

    int getDumpType() {
        return 195;
    }
}
