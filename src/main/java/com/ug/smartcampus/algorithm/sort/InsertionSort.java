package com.ug.smartcampus.algorithm.sort;

public final class InsertionSort{private InsertionSort(){}public static<T extends Comparable<? super T>>void sort(T[]a){for(int i=1;i<a.length;i++){T x=a[i];int j=i-1;while(j>=0&&a[j].compareTo(x)>0){a[j+1]=a[j--];}a[j+1]=x;}}public static void sort(int[]a){for(int i=1;i<a.length;i++){int x=a[i],j=i-1;while(j>=0&&a[j]>x)a[j+1]=a[j--];a[j+1]=x;}}}
