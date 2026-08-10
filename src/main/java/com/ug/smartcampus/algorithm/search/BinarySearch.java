package com.ug.smartcampus.algorithm.search;

public final class BinarySearch{private BinarySearch(){}public static<T extends Comparable<? super T>>int search(T[]a,T target){int l=0,h=a.length-1;while(l<=h){int m=(l+h)>>>1;int c=a[m].compareTo(target);if(c==0)return m;if(c<0)l=m+1;else h=m-1;}return -1;}public static int search(int[]a,int t){int l=0,h=a.length-1;while(l<=h){int m=(l+h)>>>1;if(a[m]==t)return m;if(a[m]<t)l=m+1;else h=m-1;}return -1;}}
