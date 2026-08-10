package com.ug.smartcampus.datastructures.linear;

import java.util.ArrayDeque; import java.util.NoSuchElementException;
public class Deque<T>{private final ArrayDeque<T> data=new ArrayDeque<>();public void addFirst(T v){data.addFirst(v);}public void addLast(T v){data.addLast(v);}public T removeFirst(){if(data.isEmpty())throw new NoSuchElementException();return data.removeFirst();}public T removeLast(){if(data.isEmpty())throw new NoSuchElementException();return data.removeLast();}public T peekFirst(){return data.peekFirst();}public T peekLast(){return data.peekLast();}public int size(){return data.size();}public boolean isEmpty(){return data.isEmpty();}}
