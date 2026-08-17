package com.ug.smartcampus.algorithm.sort;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

import org.junit.jupiter.api.Test;

class MergeSortTest {
    @Test
    void sortsPrimitiveValuesIncludingDuplicates() {
        int[] values = { 7, -2, 7, 0, 4, -2 };
        MergeSort.sort(values);
        assertArrayEquals(new int[] { -2, -2, 0, 4, 7, 7 }, values);
    }

    @Test
    void sortsComparableValues() {
        String[] values = { "route", "allocate", "schedule" };
        MergeSort.sort(values);
        assertArrayEquals(new String[] { "allocate", "route", "schedule" }, values);
    }
}
