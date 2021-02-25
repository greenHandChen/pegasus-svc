package com.pegasus.file;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * Created by enHui.Chen on 2021/2/19.
 */
public class TestRandomAccess {
    public static void main(String[] args) throws FileNotFoundException {
        File inFile = new File("C:\\Users\\Administrator\\Desktop\\a.txt");
        File outFile = new File("C:\\Users\\Administrator\\Desktop\\b.txt");

        new Thread(() -> {
            TestRandomAccess.run(inFile, outFile, 0);
        }).start();

        new Thread(() -> {
            TestRandomAccess.run(inFile, outFile, inFile.length() / 2);
        }).start();
    }


    public static void run(File inFile, File outFile, long start) {
        try {
            RandomAccessFile inRaf = new RandomAccessFile(inFile, "rw");
            RandomAccessFile outRaf = new RandomAccessFile(outFile, "rw");

            inRaf.seek(start);
            outRaf.seek(start);

            String s = null;
            while ((s = inRaf.readLine()) != null) {
                outRaf.write(s.getBytes());
            }

            inRaf.close();
            outRaf.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
