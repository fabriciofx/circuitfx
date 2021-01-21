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
import java.awt.FileDialog;
import java.awt.Frame;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

class ImportExportFileDialog
    implements ImportExportDialog {
    private static String circuitDump;
    private static String directory = ".";
    CirSim cframe;
    Action type;

    ImportExportFileDialog(CirSim f, Action type) {
        if (directory.equals(".")) {
            File file = new File("circuits");
            if (file.isDirectory()) {
                directory = "circuits";
            }
        }
        this.type = type;
        cframe = f;
    }

    private static String readFile(String path)
        throws IOException {
        FileInputStream stream = null;
        try {
            stream = new FileInputStream(new File(path));
            FileChannel fc = stream.getChannel();
            MappedByteBuffer bb = fc.map(FileChannel.MapMode.READ_ONLY,
                                         0, fc.size()
            );
            return java.nio.charset.StandardCharsets.UTF_8.decode(bb)
                .toString();
        } finally {
            stream.close();
        }
    }

    private static void writeFile(String path)
        throws IOException {
        FileOutputStream stream = null;
        try {
            stream = new FileOutputStream(new File(path));
            FileChannel fc = stream.getChannel();
            ByteBuffer bb = java.nio.charset.StandardCharsets.UTF_8
                .encode(circuitDump);
            fc.write(bb);
        } finally {
            stream.close();
        }
    }

    public String getDump() {
        return circuitDump;
    }

    public void setDump(String dump) {
        circuitDump = dump;
    }

    public void execute() {
        FileDialog fd = new FileDialog(
            new Frame(),
            (type == ImportExportDialog.Action.EXPORT) ? "Save File" :
                "Open File",
            (type == ImportExportDialog.Action.EXPORT) ? FileDialog.SAVE :
                FileDialog.LOAD
        );
        fd.setDirectory(directory);
        fd.setVisible(true);
        String file = fd.getFile();
        String dir = fd.getDirectory();
        if (dir != null) {
            directory = dir;
        }
        if (file == null) {
            return;
        }
        System.err.println(dir + File.separator + file);
        if (type == ImportExportDialog.Action.EXPORT) {
            try {
                writeFile(dir + file);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            try {
                String dump = readFile(dir + file);
                circuitDump = dump;
                cframe.readSetup(circuitDump);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
