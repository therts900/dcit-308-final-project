package com.ug.smartcampus.datastructures.linear;

public class CircularQueue<T>{private final Object[] data;private int head,size;public CircularQueue(int capacity){if(capacity<1)throw new IllegalArgumentException();data=new Object[capacity];}public void enqueue(T v){if(size==data.length)throw new IllegalStateException("Queue full");data[(head+size)%data.length]=v;size++;}@SuppressWarnings("unchecked")public T dequeue(){if(size==0)return null;T v=(T)data[head];data[head]=null;head=(head+1)%data.length;size--;return v;}public int size(){return size;}public boolean isEmpty(){return size==0;}}
