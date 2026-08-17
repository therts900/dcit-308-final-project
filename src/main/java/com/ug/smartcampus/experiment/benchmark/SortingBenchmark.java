package com.ug.smartcampus.experiment.benchmark;

import com.ug.smartcampus.algorithm.sort.MergeSort;
import java.util.Locale;
import java.util.Random;

/** Produces repeatable timing rows for the O(n log n) merge-sort demonstration. */
public final class SortingBenchmark {
    private SortingBenchmark() { }

    public static void main(String[] args) {
        int[] sizes = {1_000, 5_000, 10_000, 50_000, 100_000};
        System.out.println("size,median_ms");
        for (int size : sizes) System.out.printf(Locale.ROOT, "%d,%.3f%n", size, medianMillis(size));
    }

    static double medianMillis(int size) {
        long[] times = new long[7];
        Random random = new Random(308L + size);
        for (int run = 0; run < times.length; run++) {
            int[] values = random.ints(size).toArray();
            long start = System.nanoTime();
            MergeSort.sort(values);
            times[run] = System.nanoTime() - start;
        }
        java.util.Arrays.sort(times);
        return times[times.length / 2] / 1_000_000.0;
    }
}
