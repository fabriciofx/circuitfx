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
package com.github.fabriciofx.circuitfx;

import java.util.StringTokenizer;

/**
 * Contributed by Edward Calver.
 */
class SRAMElm extends ChipElm {
    short[] data = new short[256];//Fuck this lack of unsigned types. That's

    public SRAMElm(int xx, int yy) {
        super(xx, yy);
        short i;
        for (i = 0; i < 256; i++) {
            data[i] = 0;
        }
    }

    public SRAMElm(
        int xa, int ya, int xb, int yb, int f,
        StringTokenizer st
    ) {
        super(xa, ya, xb, yb, f, st);
        short i;
        for (i = 0; i < 256; i++) {
            data[i] = 0;//Initialise data
        }
    }

    String getChipName() {
        return "SRAM";
    }
    // twice as much data as I'd need in C

    void setupPins() {
        sizeX = 4;
        sizeY = 9;
        pins = new ChipElm.Pin[19];
        pins[0] = new ChipElm.Pin(0, SIDE_W, "A7");
        pins[1] = new ChipElm.Pin(1, SIDE_W, "A6");
        pins[2] = new ChipElm.Pin(2, SIDE_W, "A5");
        pins[3] = new ChipElm.Pin(3, SIDE_W, "A4");
        pins[4] = new ChipElm.Pin(4, SIDE_W, "A3");
        pins[5] = new ChipElm.Pin(5, SIDE_W, "A2");
        pins[6] = new ChipElm.Pin(6, SIDE_W, "A1");
        pins[7] = new ChipElm.Pin(7, SIDE_W, "A0");
        pins[8] = new ChipElm.Pin(8, SIDE_W, "R");
        pins[9] = new ChipElm.Pin(8, SIDE_E, "W");
        pins[10] = new ChipElm.Pin(0, SIDE_E, "D7");
        pins[11] = new ChipElm.Pin(1, SIDE_E, "D6");
        pins[12] = new ChipElm.Pin(2, SIDE_E, "D5");
        pins[13] = new ChipElm.Pin(3, SIDE_E, "D4");
        pins[14] = new ChipElm.Pin(4, SIDE_E, "D3");
        pins[15] = new ChipElm.Pin(5, SIDE_E, "D2");
        pins[16] = new ChipElm.Pin(6, SIDE_E, "D1");
        pins[17] = new ChipElm.Pin(7, SIDE_E, "D0");
        pins[10].output = true;
        pins[11].output = true;
        pins[12].output = true;
        pins[13].output = true;
        pins[14].output = true;
        pins[15].output = true;
        pins[16].output = true;
        pins[17].output = true;
    }

    int getPostCount() {
        return 18;
    }

    int getVoltageSourceCount() {
        return 8;
    }

    void execute() {
        short index = 0;
        if (pins[8].value || pins[9].value) {
            if (pins[0].value) {
                index += 128;
            }
            if (pins[1].value) {
                index += 64;
            }
            if (pins[2].value) {
                index += 32;
            }
            if (pins[3].value) {
                index += 16;
            }
            if (pins[4].value) {
                index += 8;
            }
            if (pins[5].value) {
                index += 4;
            }
            if (pins[6].value) {
                index += 2;
            }
            if (pins[7].value) {
                index += 1;
            }
            if (pins[8].value) {
                if ((data[index] & 128) > 0) {
                    pins[10].value = true;
                } else {
                    pins[10].value = false;
                }
                if ((data[index] & 64) > 0) {
                    pins[11].value = true;
                } else {
                    pins[11].value = false;
                }
                if ((data[index] & 32) > 0) {
                    pins[12].value = true;
                } else {
                    pins[12].value = false;
                }
                if ((data[index] & 16) > 0) {
                    pins[13].value = true;
                } else {
                    pins[13].value = false;
                }
                if ((data[index] & 8) > 0) {
                    pins[14].value = true;
                } else {
                    pins[14].value = false;
                }
                if ((data[index] & 4) > 0) {
                    pins[15].value = true;
                } else {
                    pins[15].value = false;
                }
                if ((data[index] & 2) > 0) {
                    pins[16].value = true;
                } else {
                    pins[16].value = false;
                }
                if ((data[index] & 1) > 0) {
                    pins[17].value = true;
                } else {
                    pins[17].value = false;
                }
            } else {
                data[index] = 0;
                if (pins[10].value) {
                    data[index] += 128;
                }
                if (pins[11].value) {
                    data[index] += 64;
                }
                if (pins[12].value) {
                    data[index] += 32;
                }
                if (pins[13].value) {
                    data[index] += 16;
                }
                if (pins[14].value) {
                    data[index] += 8;
                }
                if (pins[15].value) {
                    data[index] += 4;
                }
                if (pins[16].value) {
                    data[index] += 2;
                }
                if (pins[17].value) {
                    data[index] += 1;
                }
            }
        }
    }

    void doStep() {
        int i;
        for (i = 0; i != getPostCount(); i++) {
            ChipElm.Pin p = pins[i];
            if (p.output && pins[9].value) {
                p.value = volts[i] > 2.5;
            }
            if (!p.output) {
                p.value = volts[i] > 2.5;
            }
        }
        execute();
        for (i = 0; i != getPostCount(); i++) {
            ChipElm.Pin p = pins[i];
            if (p.output && !pins[9].value) {
                sim.updateVoltageSource(
                    0,
                    nodes[i],
                    p.voltSource,
                    p.value ? 5 : 0
                );
            }
        }
    }

    int getDumpType() {
        return 204;
    }
}
