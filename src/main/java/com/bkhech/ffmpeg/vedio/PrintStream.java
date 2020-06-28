package com.bkhech.ffmpeg.vedio;

public class PrintStream extends Thread {
    java.io.InputStream __is;

    public PrintStream(java.io.InputStream is) {
        __is = is;
    }

    @Override
    public void run() {
        try {
            while (this != null) {
                int _ch = __is.read();
                if (_ch == -1) {
                    break;
                } else {
//                    System.out.print((char) _ch);
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
