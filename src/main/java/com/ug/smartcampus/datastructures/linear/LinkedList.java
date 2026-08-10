package com.ug.smartcampus.datastructures.linear;

import java.util.Iterator; import java.util.NoSuchElementException;
public class LinkedList<T> implements Iterable<T> {
 private static class Node<E>{E value;Node<E> next;Node(E v){value=v;}}
 private Node<T> head,tail; private int size;
 public void add(T v){addLast(v);} public void addFirst(T v){Node<T> n=new Node<>(v);n.next=head;head=n;if(tail==null)tail=n;size++;}
 public void addLast(T v){Node<T> n=new Node<>(v);if(tail==null)head=tail=n;else{tail.next=n;tail=n;}size++;}
 public T removeFirst(){if(head==null)throw new NoSuchElementException();T v=head.value;head=head.next;if(--size==0)tail=null;return v;}
 public boolean remove(T v){Node<T> p=null,c=head;while(c!=null){if(java.util.Objects.equals(c.value,v)){if(p==null)head=c.next;else p.next=c.next;if(c==tail)tail=p;size--;return true;}p=c;c=c.next;}return false;}
 public T get(int i){check(i);Node<T> n=head;while(i-->0)n=n.next;return n.value;} public int size(){return size;} public boolean isEmpty(){return size==0;} public void clear(){head=tail=null;size=0;}
 private void check(int i){if(i<0||i>=size)throw new IndexOutOfBoundsException();}
 public Iterator<T> iterator(){return new Iterator<>(){Node<T> n=head;public boolean hasNext(){return n!=null;}public T next(){if(n==null)throw new NoSuchElementException();T v=n.value;n=n.next;return v;}};}
}
