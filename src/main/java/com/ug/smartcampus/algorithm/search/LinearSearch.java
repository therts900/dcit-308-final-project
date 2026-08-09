package com.ug.smartcampus.algorithm.search;

import java.util.Objects;public final class LinearSearch{private LinearSearch(){}public static<T>int search(T[]a,T t){for(int i=0;i<a.length;i++)if(Objects.equals(a[i],t))return i;return -1;}public static int search(int[]a,int t){for(int i=0;i<a.length;i++)if(a[i]==t)return i;return -1;}}
