package com.ug.smartcampus.algorithm.sort;

/** Stable top-down merge sort with O(n log n) worst-case running time. */
public final class MergeSort {
    private MergeSort() {
    }

    public static <T extends Comparable<? super T>> void sort(T[] values) {
        @SuppressWarnings("unchecked")
        T[] work = (T[]) new Comparable<?>[values.length];
        sort(values, work, 0, values.length);
    }

    private static <T extends Comparable<? super T>> void sort(T[] values, T[] work, int low, int high) {
        if (high - low < 2) {
            return;
        }
        int middle = (low + high) >>> 1;
        sort(values, work, low, middle);
        sort(values, work, middle, high);
        merge(values, work, low, middle, high);
    }

    private static <T extends Comparable<? super T>> void merge(T[] values, T[] work, int low, int middle, int high) {
        int left = low;
        int right = middle;
        int target = low;
        while (left < middle && right < high) {
            work[target++] = values[left].compareTo(values[right]) <= 0 ? values[left++] : values[right++];
        }
        while (left < middle) {
            work[target++] = values[left++];
        }
        while (right < high) {
            work[target++] = values[right++];
        }
        for (int index = low; index < high; index++) {
            values[index] = work[index];
        }
    }

    public static void sort(int[] values) {
        int[] work = new int[values.length];
        sort(values, work, 0, values.length);
    }

    private static void sort(int[] values, int[] work, int low, int high) {
        if (high - low < 2) {
            return;
        }
        int middle = (low + high) >>> 1;
        sort(values, work, low, middle);
        sort(values, work, middle, high);
        int left = low;
        int right = middle;
        int target = low;
        while (left < middle && right < high) {
            work[target++] = values[left] <= values[right] ? values[left++] : values[right++];
        }
        while (left < middle) {
            work[target++] = values[left++];
        }
        while (right < high) {
            work[target++] = values[right++];
        }
        for (int index = low; index < high; index++) {
            values[index] = work[index];
        }
    }
}
