package com.ug.smartcampus.datastructures.nonlinear;

import java.util.HashMap; import java.util.Map;
public class HashTable<K,V>{private final Map<K,V> map=new HashMap<>();public V put(K k,V v){return map.put(k,v);}public V get(K k){return map.get(k);}public V remove(K k){return map.remove(k);}public boolean containsKey(K k){return map.containsKey(k);}public int size(){return map.size();}public boolean isEmpty(){return map.isEmpty();}}
