package com.ug.smartcampus.algorithm.graph;

import com.ug.smartcampus.datastructures.graph.Graph;import com.ug.smartcampus.datastructures.nonlinear.DisjointSet;import java.util.*;public final class Kruskal{private Kruskal(){}public static<T>List<Graph.Edge<T>> minimumSpanningTree(Graph<T>g){List<Graph.Edge<T>> e=new ArrayList<>(g.edges());e.sort(Comparator.comparingDouble(Graph.Edge::weight));DisjointSet<T>ds=new DisjointSet<>();List<Graph.Edge<T>>out=new ArrayList<>();for(var x:e)if(ds.union(x.from(),x.to()))out.add(x);return out;}}
