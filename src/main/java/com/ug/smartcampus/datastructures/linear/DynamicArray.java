package com.ug.smartcampus.datastructures.linear;

import java.util.Arrays;
public class DynamicArray<T>{private Object[] data=new Object[10];private int size;public void add(T v){if(size==data.length)data=Arrays.copyOf(data,size*2);data[size++]=v;}@SuppressWarnings("unchecked")public T get(int i){check(i);return (T)data[i];}public T set(int i,T v){check(i);@SuppressWarnings("unchecked") T old=(T)data[i];data[i]=v;return old;}@SuppressWarnings("unchecked")public T remove(int i){check(i);T old=(T)data[i];System.arraycopy(data,i+1,data,i,size-i-1);data[--size]=null;return old;}public int size(){return size;}public boolean isEmpty(){return size==0;}private void check(int i){if(i<0||i>=size)throw new IndexOutOfBoundsException();}}
