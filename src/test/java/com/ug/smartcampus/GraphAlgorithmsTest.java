package com.ug.smartcampus;

import com.ug.smartcampus.algorithm.graph.*;
import com.ug.smartcampus.datastructures.graph.Graph;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;

class GraphAlgorithmsTest {
    private Graph<String> graph() { Graph<String> g=new Graph<>(); g.addEdge("A","B",2);g.addEdge("B","C",3);g.addEdge("A","C",10);return g; }
    @Test void dijkstraFindsMinimumRoute() { assertEquals(List.of("A","B","C"),Dijkstra.shortestPath(graph(),"A","C")); }
    @Test void traversalsVisitReachableVertices() { Graph<String> g=graph(); assertEquals(3,BreadthFirstSearch.traverse(g,"A").size()); assertTrue(DepthFirstSearch.reachable(g,"A","C")); }
    @Test void kruskalProducesMinimumSpanningTree() { assertEquals(2,Kruskal.minimumSpanningTree(graph()).size()); }
}
