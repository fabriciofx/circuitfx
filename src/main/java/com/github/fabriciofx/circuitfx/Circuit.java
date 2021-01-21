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
// Circuit.java (c) 2005,2008 by Paul Falstad, www.falstad.com
import java.applet.Applet;
import java.awt.Graphics;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

public class Circuit extends Applet implements ComponentListener {
    static CirSim ogf;
    boolean finished = false;
    boolean started = false;

    public static void main(String[] args) {
        ogf = new CirSim(null);
        ogf.init();
    }

    void destroyFrame() {
        if (ogf != null) {
            ogf.dispose();
        }
        ogf = null;
        repaint();
        finished = true;
    }

    public void init() {
        addComponentListener(this);
    }

    public void showFrame() {
        if (finished) {
            repaint();
            return;
        }
        if (ogf == null) {
            started = true;
            ogf = new CirSim(this);
            ogf.init();
        }
        ogf.setVisible(true);
        repaint();
    }

    public void hideFrame() {
        if (finished) {
            return;
        }
        ogf.setVisible(false);
        repaint();
    }

    public void toggleSwitch(int x) {
        ogf.toggleSwitch(x);
    }

    public void paint(Graphics g) {
        String s = "Applet is open in a separate window.";
        if (ogf != null && !ogf.isVisible()) {
            s = "Applet window is hidden.";
        }
        if (!started) {
            s = "Applet is starting.";
        } else if (ogf == null || finished) {
            s = "Applet is finished.";
        } else if (ogf != null && ogf.useFrame) {
            ogf.triggerShow();
        }
        g.drawString(s, 10, 30);
    }

    public void componentHidden(ComponentEvent e) {
    }

    public void componentMoved(ComponentEvent e) {
    }

    public void componentShown(ComponentEvent e) {
        showFrame();
    }

    public void componentResized(ComponentEvent e) {
        if (ogf != null) {
            ogf.componentResized(e);
        }
    }

    public void destroy() {
        if (ogf != null) {
            ogf.dispose();
        }
        ogf = null;
        repaint();
    }
}

