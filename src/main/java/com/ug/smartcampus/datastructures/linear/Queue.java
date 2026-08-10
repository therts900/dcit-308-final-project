package com.ug.smartcampus.datastructures.linear;

import java.util.ArrayDeque; import java.util.NoSuchElementException;
public class Queue<T>{private final ArrayDeque<T> data=new ArrayDeque<>();public void enqueue(T v){data.addLast(v);}public void offer(T v){enqueue(v);}public T dequeue(){if(data.isEmpty())throw new NoSuchElementException();return data.removeFirst();}public T poll(){return data.pollFirst();}public T peek(){if(data.isEmpty())throw new NoSuchElementException();return data.peekFirst();}public int size(){return data.size();}public boolean isEmpty(){return data.isEmpty();}}
