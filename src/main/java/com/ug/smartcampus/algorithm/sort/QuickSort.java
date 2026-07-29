package com.ug.smartcampus.algorithm.sort;

import java.util.*; public final class QuickSort{private QuickSort(){}public static <T extends Comparable<? super T>>void sort(T[] a){sort(a,0,a.length-1);}private static<T extends Comparable<? super T>>void sort(T[]a,int l,int h){if(l>=h)return;int i=l,j=h;T p=a[(l+h)>>>1];while(i<=j){while(a[i].compareTo(p)<0)i++;while(a[j].compareTo(p)>0)j--;if(i<=j){T x=a[i];a[i++]=a[j];a[j--]=x;}}sort(a,l,j);sort(a,i,h);}public static void sort(int[]a){Integer[] b=Arrays.stream(a).boxed().toArray(Integer[]::new);sort(b);for(int i=0;i<a.length;i++)a[i]=b[i];}}
