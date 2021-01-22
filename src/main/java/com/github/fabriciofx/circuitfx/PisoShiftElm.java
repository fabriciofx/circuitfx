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

/**
 * Contributed by Edward Calver.
 */
class PisoShiftElm extends ChipElm {
    short data = 0;//Lack of unsigned types sucks
    boolean clockstate = false;
    boolean modestate = false;

    public PisoShiftElm(int xx, int yy) {
        super(xx, yy);
    }

    public PisoShiftElm(
        int xa, int ya, int xb, int yb, int f,
        StringTokenizer st
    ) {
        super(xa, ya, xb, yb, f, st);
    }

    boolean hasReset() {
        return false;
    }

    String getChipName() {
        return "PISO shift register";
    }

    void setupPins() {
        sizeX = 10;
        sizeY = 3;
        pins = new ChipElm.Pin[getPostCount()];
        pins[0] = new ChipElm.Pin(1, SIDE_W, "L");
        pins[1] = new ChipElm.Pin(2, SIDE_W, "");
        pins[1].clock = true;
        pins[2] = new ChipElm.Pin(1, SIDE_N, "I7");
        pins[3] = new ChipElm.Pin(2, SIDE_N, "I6");
        pins[4] = new ChipElm.Pin(3, SIDE_N, "I5");
        pins[5] = new ChipElm.Pin(4, SIDE_N, "I4");
        pins[6] = new ChipElm.Pin(5, SIDE_N, "I3");
        pins[7] = new ChipElm.Pin(6, SIDE_N, "I2");
        pins[8] = new ChipElm.Pin(7, SIDE_N, "I1");
        pins[9] = new ChipElm.Pin(8, SIDE_N, "I0");
        pins[10] = new ChipElm.Pin(1, SIDE_E, "Q");
        pins[10].output = true;
    }

    int getPostCount() {
        return 11;
    }

    int getVoltageSourceCount() {
        return 1;
    }

    void execute() {
        if (pins[0].value && !modestate) {
            modestate = true;
            data = 0;
            if (pins[2].value) {
                data += 128;
            }
            if (pins[3].value) {
                data += 64;
            }
            if (pins[4].value) {
                data += 32;
            }
            if (pins[5].value) {
                data += 16;
            }
            if (pins[6].value) {
                data += 8;
            }
            if (pins[7].value) {
                data += 4;
            }
            if (pins[8].value) {
                data += 2;
            }
            if (pins[9].value) {
                data += 1;
            }
        } else if (pins[1].value && !clockstate) {
            clockstate = true;
            if ((data & 1) == 0) {
                pins[10].value = false;
            } else {
                pins[10].value = true;
            }
            data = (byte) (data >>> 1);
        }
        if (!pins[0].value) {
            modestate = false;
        }
        if (!pins[1].value) {
            clockstate = false;
        }
    }

    int getDumpType() {
        return 186;
    }
}
