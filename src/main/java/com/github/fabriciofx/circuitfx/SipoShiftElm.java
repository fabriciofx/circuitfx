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
class SipoShiftElm extends ChipElm {
    short data = 0;//This has to be a short because there's no unsigned byte
    // and it's screwing with my code
    boolean clockstate = false;

    public SipoShiftElm(int xx, int yy) {
        super(xx, yy);
    }

    public SipoShiftElm(
        int xa, int ya, int xb, int yb, int f,
        StringTokenizer st
    ) {
        super(xa, ya, xb, yb, f, st);
    }

    boolean hasReset() {
        return false;
    }

    String getChipName() {
        return "SIPO shift register";
    }

    void setupPins() {
        sizeX = 9;
        sizeY = 3;
        pins = new ChipElm.Pin[getPostCount()];
        pins[0] = new ChipElm.Pin(1, SIDE_W, "D");
        pins[1] = new ChipElm.Pin(2, SIDE_W, "");
        pins[1].clock = true;
        pins[2] = new ChipElm.Pin(1, SIDE_N, "I7");
        pins[2].output = true;
        pins[3] = new ChipElm.Pin(2, SIDE_N, "I6");
        pins[3].output = true;
        pins[4] = new ChipElm.Pin(3, SIDE_N, "I5");
        pins[4].output = true;
        pins[5] = new ChipElm.Pin(4, SIDE_N, "I4");
        pins[5].output = true;
        pins[6] = new ChipElm.Pin(5, SIDE_N, "I3");
        pins[6].output = true;
        pins[7] = new ChipElm.Pin(6, SIDE_N, "I2");
        pins[7].output = true;
        pins[8] = new ChipElm.Pin(7, SIDE_N, "I1");
        pins[8].output = true;
        pins[9] = new ChipElm.Pin(8, SIDE_N, "I0");
        pins[9].output = true;
    }

    int getPostCount() {
        return 10;
    }

    int getVoltageSourceCount() {
        return 8;
    }

    void execute() {
        if (pins[1].value && !clockstate) {
            clockstate = true;
            data = (short) (data >>> 1);
            if (pins[0].value) {
                data += 128;
            }
            if ((data & 128) > 0) {
                pins[2].value = true;
            } else {
                pins[2].value = false;
            }
            if ((data & 64) > 0) {
                pins[3].value = true;
            } else {
                pins[3].value = false;
            }
            if ((data & 32) > 0) {
                pins[4].value = true;
            } else {
                pins[4].value = false;
            }
            if ((data & 16) > 0) {
                pins[5].value = true;
            } else {
                pins[5].value = false;
            }
            if ((data & 8) > 0) {
                pins[6].value = true;
            } else {
                pins[6].value = false;
            }
            if ((data & 4) > 0) {
                pins[7].value = true;
            } else {
                pins[7].value = false;
            }
            if ((data & 2) > 0) {
                pins[8].value = true;
            } else {
                pins[8].value = false;
            }
            if ((data & 1) > 0) {
                pins[9].value = true;
            } else {
                pins[9].value = false;
            }
        }
        if (!pins[1].value) {
            clockstate = false;
        }
    }

    int getDumpType() {
        return 189;
    }
}
