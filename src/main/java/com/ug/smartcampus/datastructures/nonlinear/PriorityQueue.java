package com.ug.smartcampus.datastructures.nonlinear;

import java.util.Comparator;
public class PriorityQueue<T>{private final Heap<T> heap;public PriorityQueue(Comparator<? super T> c){heap=new Heap<>(c);}public void enqueue(T v){heap.add(v);}public void offer(T v){enqueue(v);}public T dequeue(){return heap.poll();}public T peek(){return heap.peek();}public int size(){return heap.size();}public boolean isEmpty(){return heap.isEmpty();}}
