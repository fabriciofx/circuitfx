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

class TimerElm extends ChipElm {
    final int FLAG_RESET = 2;
    final int N_DIS  = 0;
    final int N_TRIG = 1;
    final int N_THRES = 2;
    final int N_VIN = 3;
    final int N_CTL = 4;
    final int N_OUT = 5;
    final int N_RST = 6;
    int getDefaultFlags() { return FLAG_RESET; }
    public TimerElm(int xx, int yy) { super(xx, yy); }
    public TimerElm(int xa, int ya, int xb, int yb, int f,
		    StringTokenizer st) {
	super(xa, ya, xb, yb, f, st);
    }
    String getChipName() { return "555 Timer"; }
    void setupPins() {
	sizeX = 3;
	sizeY = 5;
	pins = new Pin[7];
	pins[N_DIS] = new Pin(1, SIDE_W, "dis");
	pins[N_TRIG] = new Pin(3, SIDE_W, "tr");
	pins[N_TRIG].lineOver = true;
	pins[N_THRES] = new Pin(4, SIDE_W, "th");
	pins[N_VIN] = new Pin(1, SIDE_N, "Vin");
	pins[N_CTL] = new Pin(1, SIDE_S, "ctl");
	pins[N_OUT] = new Pin(2, SIDE_E, "out");
	pins[N_OUT].output = pins[N_OUT].state = true;
	pins[N_RST] = new Pin(1, SIDE_E, "rst");
    }
    boolean nonLinear() { return true; }
    boolean hasReset() { return (flags & FLAG_RESET) != 0; }
    void stamp() {
	// stamp voltage divider to put ctl pin at 2/3 V
	sim.stampResistor(nodes[N_VIN], nodes[N_CTL],  5000);
	sim.stampResistor(nodes[N_CTL], 0,        10000);
	// output pin
	sim.stampVoltageSource(0, nodes[N_OUT], pins[N_OUT].voltSource);
	// discharge pin
	sim.stampNonLinear(nodes[N_DIS]);
    }
    void calculateCurrent() {
	// need current for V, discharge, control; output current is
	// calculated for us, and other pins have no current
	pins[N_VIN].current = (volts[N_CTL]-volts[N_VIN])/5000;
	pins[N_CTL].current = -volts[N_CTL]/10000 - pins[N_VIN].current;
	pins[N_DIS].current = (!out && !setOut) ? -volts[N_DIS]/10 : 0;
    }
    boolean setOut, out;
    void startIteration() {
	out = volts[N_OUT] > volts[N_VIN]/2;
	setOut = false;
	// check comparators
	if (volts[N_CTL]/2 > volts[N_TRIG])
	    setOut = out = true;
	if (volts[N_THRES] > volts[N_CTL] || (hasReset() && volts[N_RST] < .7))
	    out = false;
    }
    void doStep() {
	// if output is low, discharge pin 0.  we use a small
	// resistor because it's easier, and sometimes people tie
	// the discharge pin to the trigger and threshold pins.
	// We check setOut to properly emulate the case where
	// trigger is low and threshold is high.
	if (!out && !setOut)
	    sim.stampResistor(nodes[N_DIS], 0, 10);
	// output
	sim.updateVoltageSource(0, nodes[N_OUT], pins[N_OUT].voltSource,
			    out ? volts[N_VIN] : 0);
    }
    int getPostCount() { return hasReset() ? 7 : 6; }
    int getVoltageSourceCount() { return 1; }
    int getDumpType() { return 165; }
}
    
