package com.ug.smartcampus.datastructures.nonlinear;

import java.util.HashMap; import java.util.Map;
public class DisjointSet<T>{private final Map<T,T> parent=new HashMap<>();private final Map<T,Integer> rank=new HashMap<>();public void makeSet(T x){parent.putIfAbsent(x,x);rank.putIfAbsent(x,0);}@SuppressWarnings("unchecked")public T find(T x){makeSet(x);T p=parent.get(x);if(!p.equals(x))parent.put(x,find(p));return parent.get(x);}public boolean union(T a,T b){T x=find(a),y=find(b);if(x.equals(y))return false;if(rank.get(x)<rank.get(y)){parent.put(x,y);}else{parent.put(y,x);if(rank.get(x).equals(rank.get(y)))rank.put(x,rank.get(x)+1);}return true;}public boolean connected(T a,T b){return find(a).equals(find(b));}}
