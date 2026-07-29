package com.ug.smartcampus.datastructures.trees;

import java.util.ArrayList; import java.util.List;
public class BinarySearchTree<T extends Comparable<? super T>>{private class N{T v;N l,r;N(T v){this.v=v;}}private N root;public void insert(T v){root=insert(root,v);}private N insert(N n,T v){if(n==null)return new N(v);int c=v.compareTo(n.v);if(c<0)n.l=insert(n.l,v);else if(c>0)n.r=insert(n.r,v);return n;}public boolean contains(T v){N n=root;while(n!=null){int c=v.compareTo(n.v);if(c==0)return true;n=c<0?n.l:n.r;}return false;}public List<T> inOrder(){List<T> x=new ArrayList<>();walk(root,x);return x;}private void walk(N n,List<T>x){if(n!=null){walk(n.l,x);x.add(n.v);walk(n.r,x);}}public int size(){return inOrder().size();}}
