package com.ug.smartcampus.experiment.benchmark;

import com.ug.smartcampus.algorithm.graph.Dijkstra;
import com.ug.smartcampus.datastructures.graph.Graph;
import java.util.Locale;
import java.util.Random;

/** Times Dijkstra over connected sparse graphs; its target complexity is O(E + V log V). */
public final class GraphBenchmark {
    private GraphBenchmark() { }

    public static void main(String[] args) {
        int[] sizes = {100, 500, 1_000, 2_000, 5_000};
        System.out.println("vertices,edges,median_ms");
        for (int size : sizes) System.out.printf(Locale.ROOT, "%d,%d,%.3f%n", size, size * 3, medianMillis(size));
    }

    static double medianMillis(int vertices) {
        long[] times = new long[7];
        for (int run = 0; run < times.length; run++) {
            Graph<Integer> graph = connectedGraph(vertices, 3 * vertices, 308L + run);
            long start = System.nanoTime();
            Dijkstra.shortestDistances(graph, 0);
            times[run] = System.nanoTime() - start;
        }
        java.util.Arrays.sort(times);
        return times[times.length / 2] / 1_000_000.0;
    }

    private static Graph<Integer> connectedGraph(int vertices, int edges, long seed) {
        Graph<Integer> graph = new Graph<>();
        Random random = new Random(seed);
        for (int vertex = 0; vertex < vertices; vertex++) graph.addVertex(vertex);
        for (int vertex = 1; vertex < vertices; vertex++) graph.addEdge(vertex - 1, vertex, 1 + random.nextInt(20));
        for (int edge = vertices - 1; edge < edges; edge++) graph.addEdge(random.nextInt(vertices), random.nextInt(vertices), 1 + random.nextInt(20));
        return graph;
    }
}
