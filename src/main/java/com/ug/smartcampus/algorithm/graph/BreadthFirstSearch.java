package com.ug.smartcampus.algorithm.graph;

import com.ug.smartcampus.datastructures.graph.Graph;import java.util.*;public final class BreadthFirstSearch{private BreadthFirstSearch(){}public static<T>List<T> traverse(Graph<T>g,T start){List<T> out=new ArrayList<>();Set<T> seen=new HashSet<>();ArrayDeque<T> q=new ArrayDeque<>();q.add(start);seen.add(start);while(!q.isEmpty()){T v=q.remove();out.add(v);for(T n:g.neighbors(v).keySet())if(seen.add(n))q.add(n);}return out;}public static<T>boolean reachable(Graph<T>g,T from,T to){return traverse(g,from).contains(to);}}
