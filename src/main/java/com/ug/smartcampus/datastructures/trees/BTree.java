package com.ug.smartcampus.datastructures.trees;

import java.util.TreeMap; import java.util.List;
public class BTree<T extends Comparable<? super T>>{private final TreeMap<T,T> tree=new TreeMap<>();public void insert(T v){tree.put(v,v);}public T search(T v){return tree.get(v);}public boolean contains(T v){return tree.containsKey(v);}public boolean remove(T v){return tree.remove(v)!=null;}public int size(){return tree.size();}public List<T> values(){return List.copyOf(tree.values());}}
