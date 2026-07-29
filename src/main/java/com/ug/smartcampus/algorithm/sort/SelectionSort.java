package com.ug.smartcampus.algorithm.sort;

public final class SelectionSort{private SelectionSort(){}public static<T extends Comparable<? super T>>void sort(T[]a){for(int i=0;i<a.length;i++){int m=i;for(int j=i+1;j<a.length;j++)if(a[j].compareTo(a[m])<0)m=j;T x=a[i];a[i]=a[m];a[m]=x;}}public static void sort(int[]a){for(int i=0;i<a.length;i++){int m=i;for(int j=i+1;j<a.length;j++)if(a[j]<a[m])m=j;int x=a[i];a[i]=a[m];a[m]=x;}}}
