package com.ug.smartcampus.service;

import com.ug.smartcampus.algorithm.graph.Dijkstra;import com.ug.smartcampus.datastructures.graph.Graph;import java.util.*;
public class RoutingService<T>{private final Graph<T> graph;public RoutingService(Graph<T>graph){this.graph=graph;}public List<T> route(T from,T to){return Dijkstra.shortestPath(graph,from,to);}public double distance(T from,T to){return Dijkstra.shortestDistances(graph,from).getOrDefault(to,Double.POSITIVE_INFINITY);}}
