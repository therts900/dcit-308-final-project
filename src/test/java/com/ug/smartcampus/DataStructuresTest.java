package com.ug.smartcampus;

import com.ug.smartcampus.datastructures.linear.*;
import com.ug.smartcampus.datastructures.nonlinear.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class DataStructuresTest {
    @Test void linearStructuresPreserveTheirOrder() {
        Queue<Integer> queue = new Queue<>(); queue.enqueue(1); queue.enqueue(2);
        assertEquals(1, queue.dequeue()); assertEquals(2, queue.dequeue());
        Stack<Integer> stack = new Stack<>(); stack.push(1); stack.push(2);
        assertEquals(2, stack.pop());
        LinkedList<Integer> list = new LinkedList<>(); list.add(1); list.addFirst(0); list.addLast(2);
        assertEquals(3, list.size()); assertEquals(0, list.get(0)); assertEquals(2, list.get(2));
    }

    @Test void heapReturnsSmallestElementFirst() {
        Heap<Integer> heap = new Heap<>(Integer::compareTo); heap.add(4); heap.add(1); heap.add(3);
        assertEquals(1, heap.poll()); assertEquals(3, heap.poll()); assertEquals(4, heap.poll());
    }

    @Test void disjointSetTracksComponents() {
        DisjointSet<String> set = new DisjointSet<>(); set.union("A", "B");
        assertTrue(set.connected("A", "B")); assertFalse(set.connected("A", "C"));
    }
}
