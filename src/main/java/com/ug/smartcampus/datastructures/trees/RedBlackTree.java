package com.ug.smartcampus.datastructures.trees;

import java.util.TreeSet; import java.util.List;
public class RedBlackTree<T extends Comparable<? super T>>{private final TreeSet<T> tree=new TreeSet<>();public boolean insert(T v){return tree.add(v);}public boolean contains(T v){return tree.contains(v);}public boolean remove(T v){return tree.remove(v);}public int size(){return tree.size();}public List<T> inOrder(){return List.copyOf(tree);}}
