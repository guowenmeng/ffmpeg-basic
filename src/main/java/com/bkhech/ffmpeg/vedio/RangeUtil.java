package com.bkhech.ffmpeg.vedio;

import java.util.ArrayList;
import java.util.List;

public class RangeUtil {

    public static List<Range> createRangesByFixedThread(int thread, int size) {
        ArrayList<Range> ranges = new ArrayList<>();

        int step = size/thread;
        for (int i = 0; i < thread; i++) {
            if (i == thread - 1) {
                ranges.add(new Range(i * step, size));
            } else {
                ranges.add(new Range(i * step, (i + 1) * step));
            }
        }
        return ranges;
    }

}
