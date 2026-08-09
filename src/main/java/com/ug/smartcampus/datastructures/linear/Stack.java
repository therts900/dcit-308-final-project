package com.ug.smartcampus.datastructures.linear;

import java.util.ArrayList; import java.util.NoSuchElementException;
public class Stack<T>{private final ArrayList<T> data=new ArrayList<>();public void push(T v){data.add(v);}public T pop(){if(data.isEmpty())throw new NoSuchElementException();return data.remove(data.size()-1);}public T peek(){if(data.isEmpty())throw new NoSuchElementException();return data.get(data.size()-1);}public int size(){return data.size();}public boolean isEmpty(){return data.isEmpty();}}
