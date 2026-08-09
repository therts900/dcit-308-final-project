package com.ug.smartcampus;

import com.ug.smartcampus.algorithm.search.*; import com.ug.smartcampus.algorithm.sort.*;
import org.junit.jupiter.api.Test; import static org.junit.jupiter.api.Assertions.*;

class AlgorithmsTest {
    @Test void sortingAndSearchingWorkTogether() { int[] values={5,1,4,2,3}; QuickSort.sort(values); assertArrayEquals(new int[]{1,2,3,4,5},values); assertEquals(2,BinarySearch.search(values,3)); assertEquals(-1,LinearSearch.search(values,9)); }
}
