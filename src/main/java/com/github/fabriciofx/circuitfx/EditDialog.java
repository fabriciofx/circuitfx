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

import java.awt.Button;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Event;
import java.awt.Label;
import java.awt.Point;
import java.awt.Scrollbar;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.DecimalFormat;
import java.text.NumberFormat;

interface Editable {
    EditInfo getEditInfo(int n);
    void setEditValue(int n, EditInfo ei);
}

class EditDialog extends Dialog
    implements AdjustmentListener, ActionListener, ItemListener {
    final int barmax = 1000;
    Editable elm;
    CirSim cframe;
    Button applyButton, okButton;
    EditInfo[] einfos;
    int einfocount;
    NumberFormat noCommaFormat;

    EditDialog(Editable ce, CirSim f) {
        super(f, "Edit Component", false);
        cframe = f;
        elm = ce;
        setLayout(new EditDialogLayout());
        einfos = new EditInfo[10];
        noCommaFormat = DecimalFormat.getInstance();
        noCommaFormat.setMaximumFractionDigits(10);
        noCommaFormat.setGroupingUsed(false);
        int i;
        for (i = 0; ; i++) {
            einfos[i] = elm.getEditInfo(i);
            if (einfos[i] == null) {
                break;
            }
            EditInfo ei = einfos[i];
            add(new Label(ei.name));
            if (ei.choice != null) {
                add(ei.choice);
                ei.choice.addItemListener(this);
            } else if (ei.checkbox != null) {
                add(ei.checkbox);
                ei.checkbox.addItemListener(this);
            } else {
                add(ei.textf =
                        new TextField(unitString(ei), 10));
                if (ei.text != null) {
                    ei.textf.setText(ei.text);
                }
                ei.textf.addActionListener(this);
                if (ei.text == null) {
                    add(ei.bar = new Scrollbar(Scrollbar.HORIZONTAL,
                                               50, 10, 0, barmax + 2
                    ));
                    setBar(ei);
                    ei.bar.addAdjustmentListener(this);
                }
            }
        }
        einfocount = i;
        add(applyButton = new Button("Apply"));
        applyButton.addActionListener(this);
        add(okButton = new Button("OK"));
        okButton.addActionListener(this);
        Point x = cframe.main.getLocationOnScreen();
        Dimension d = getSize();
        setLocation(
            x.x + (cframe.winSize.width - d.width) / 2,
            x.y + (cframe.winSize.height - d.height) / 2
        );
        addWindowListener(new WindowAdapter() {
                              public void windowClosing(WindowEvent we) {
                                  closeDialog();
                              }
                          }
        );
    }

    String unitString(EditInfo ei) {
        double v = ei.value;
        double va = Math.abs(v);
        if (ei.dimensionless) {
            return noCommaFormat.format(v);
        }
        if (v == 0) {
            return "0";
        }
        if (va < 1e-9) {
            return noCommaFormat.format(v * 1e12) + "p";
        }
        if (va < 1e-6) {
            return noCommaFormat.format(v * 1e9) + "n";
        }
        if (va < 1e-3) {
            return noCommaFormat.format(v * 1e6) + "u";
        }
        if (va < 1 && !ei.forceLargeM) {
            return noCommaFormat.format(v * 1e3) + "m";
        }
        if (va < 1e3) {
            return noCommaFormat.format(v);
        }
        if (va < 1e6) {
            return noCommaFormat.format(v * 1e-3) + "k";
        }
        if (va < 1e9) {
            return noCommaFormat.format(v * 1e-6) + "M";
        }
        return noCommaFormat.format(v * 1e-9) + "G";
    }

    double parseUnits(EditInfo ei) throws java.text.ParseException {
        String s = ei.textf.getText();
        s = s.trim();
        int len = s.length();
        char uc = s.charAt(len - 1);
        double mult = 1;
        switch (uc) {
            case 'p':
            case 'P':
                mult = 1e-12;
                break;
            case 'n':
            case 'N':
                mult = 1e-9;
                break;
            case 'u':
            case 'U':
                mult = 1e-6;
                break;
            // for ohm values, we assume mega for lowercase m, otherwise milli
            case 'm':
                mult = (ei.forceLargeM) ? 1e6 : 1e-3;
                break;
            case 'k':
            case 'K':
                mult = 1e3;
                break;
            case 'M':
                mult = 1e6;
                break;
            case 'G':
            case 'g':
                mult = 1e9;
                break;
        }
        if (mult != 1) {
            s = s.substring(0, len - 1).trim();
        }
        return noCommaFormat.parse(s).doubleValue() * mult;
    }

    void apply() {
        int i;
        for (i = 0; i != einfocount; i++) {
            EditInfo ei = einfos[i];
            if (ei.textf == null) {
                continue;
            }
            if (ei.text == null) {
                try {
                    double d = parseUnits(ei);
                    ei.value = d;
                } catch (Exception ex) { /* ignored */ }
            }
            elm.setEditValue(i, ei);
            if (ei.text == null) {
                setBar(ei);
            }
        }
        cframe.needAnalyze();
    }

    public void actionPerformed(ActionEvent e) {
        int i;
        Object src = e.getSource();
        for (i = 0; i != einfocount; i++) {
            EditInfo ei = einfos[i];
            if (src == ei.textf) {
                if (ei.text == null) {
                    try {
                        double d = parseUnits(ei);
                        ei.value = d;
                    } catch (Exception ex) { /* ignored */ }
                }
                elm.setEditValue(i, ei);
                if (ei.text == null) {
                    setBar(ei);
                }
                cframe.needAnalyze();
            }
        }
        if (e.getSource() == okButton) {
            apply();
            closeDialog();
        }
        if (e.getSource() == applyButton) {
            apply();
        }
    }

    public void adjustmentValueChanged(AdjustmentEvent e) {
        Object src = e.getSource();
        int i;
        for (i = 0; i != einfocount; i++) {
            EditInfo ei = einfos[i];
            if (ei.bar == src) {
                double v = ei.bar.getValue() / 1000.;
                if (v < 0) {
                    v = 0;
                }
                if (v > 1) {
                    v = 1;
                }
                ei.value = (ei.maxval - ei.minval) * v + ei.minval;
                ei.value = Math.round(ei.value / ei.minval) * ei.minval;
                elm.setEditValue(i, ei);
                ei.textf.setText(unitString(ei));
                cframe.needAnalyze();
            }
        }
    }

    public void itemStateChanged(ItemEvent e) {
        Object src = e.getItemSelectable();
        int i;
        boolean changed = false;
        for (i = 0; i != einfocount; i++) {
            EditInfo ei = einfos[i];
            if (ei.choice == src || ei.checkbox == src) {
                elm.setEditValue(i, ei);
                if (ei.newDialog) {
                    changed = true;
                }
                cframe.needAnalyze();
            }
        }
        if (changed) {
            setVisible(false);
            cframe.editDialog = new EditDialog(elm, cframe);
            cframe.editDialog.show();
        }
    }

    public boolean handleEvent(Event ev) {
        if (ev.id == Event.WINDOW_DESTROY) {
            closeDialog();
            return true;
        }
        return super.handleEvent(ev);
    }

    void setBar(EditInfo ei) {
        int x = (int) (barmax * (ei.value - ei.minval) /
            (ei.maxval - ei.minval));
        ei.bar.setValue(x);
    }

    protected void closeDialog() {
        cframe.main.requestFocus();
        setVisible(false);
        cframe.editDialog = null;
    }
}

