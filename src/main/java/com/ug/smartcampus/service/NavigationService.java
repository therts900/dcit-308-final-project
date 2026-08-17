package com.ug.smartcampus.service;

import com.ug.smartcampus.algorithm.graph.Dijkstra;
import com.ug.smartcampus.datastructures.graph.Graph;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/** Loads and computes routes over the GeoJSON-derived walking graph. */
public final class NavigationService {
    private final Graph<String> graph = new Graph<>(true);
    private final Map<String, NavigationDestination> destinations = new HashMap<>();
    private final Map<String, double[]> coordinates = new HashMap<>();
    private final Map<String, Double> edgeDistancesKm = new HashMap<>();

    public NavigationService(Path dataDirectory) throws IOException {
        boolean extracted = !Files.exists(dataDirectory.resolve("road_walk_nodes.csv"))
                && Files.exists(dataDirectory.resolve("navigation/navigation_nodes.csv"));
        Path directory = extracted ? dataDirectory.resolve("navigation") : dataDirectory;
        Path nodes = directory.resolve(extracted ? "navigation_nodes.csv" : "road_walk_nodes.csv");
        Path edges = directory.resolve(extracted ? "navigation_edges.csv" : "road_walk_edges.csv");
        Path destinationFile = directory.resolve(extracted ? "navigation_destinations.csv" : "nodes_fixed.csv");
        loadNodes(nodes);
        loadEdges(edges, extracted);
        loadDestinations(destinationFile, extracted);
    }

    public List<NavigationDestination> destinations() {
        return destinations.values().stream().sorted((a, b) -> a.name().compareToIgnoreCase(b.name())).toList();
    }

    public int nodeCount() {
        return graph.vertices().size();
    }

    public int edgeCount() {
        return graph.edges().size();
    }

    public Route route(String fromDestinationId, String toDestinationId) {
        NavigationDestination from = destinations.get(fromDestinationId);
        NavigationDestination to = destinations.get(toDestinationId);
        if (from == null || to == null) return new Route(List.of(), Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY);
        List<String> path = Dijkstra.shortestPath(graph, from.nearestNodeId(), to.nearestNodeId());
        double travelTime = Dijkstra.shortestDistances(graph, from.nearestNodeId())
                .getOrDefault(to.nearestNodeId(), Double.POSITIVE_INFINITY);
        double distance = 0;
        for (int i = 1; i < path.size(); i++) distance += edgeDistancesKm.getOrDefault(path.get(i - 1) + "|" + path.get(i), 0.0);
        return new Route(path, travelTime, distance);
    }

    private void loadNodes(Path path) throws IOException {
        for (Map<String, String> row : rows(path)) {
            String id = row.getOrDefault("node_id", row.get("id"));
            graph.addVertex(id);
            coordinates.put(id, new double[] {Double.parseDouble(row.getOrDefault("longitude", row.get("lon"))),
                    Double.parseDouble(row.getOrDefault("latitude", row.get("lat")))});
        }
    }

    private void loadEdges(Path path, boolean extracted) throws IOException {
        for (Map<String, String> row : rows(path)) {
            if (!extracted || "yes".equalsIgnoreCase(row.get("routable_walk"))) {
                String from = row.getOrDefault("from_node_id", row.get("fromId"));
                String to = row.getOrDefault("to_node_id", row.get("toId"));
                double minutes = extracted ? Double.parseDouble(row.get("travel_time_min"))
                        : Double.parseDouble(row.get("distanceMeters")) / Double.parseDouble(row.get("speedKph")) * 0.06;
                double distanceKm = extracted ? Double.parseDouble(row.get("distance_km"))
                        : Double.parseDouble(row.get("distanceMeters")) / 1000;
                graph.addEdge(from, to, minutes);
                edgeDistancesKm.put(from + "|" + to, distanceKm);
                if (!extracted || "1".equals(row.get("undirected"))) {
                    graph.addEdge(to, from, minutes);
                    edgeDistancesKm.put(to + "|" + from, distanceKm);
                }
            }
        }
    }

    private void loadDestinations(Path path, boolean extracted) throws IOException {
        for (Map<String, String> row : rows(path)) {
            String id = row.getOrDefault("destination_id", row.get("id"));
            String name = row.get("name");
            if (name == null || name.isBlank()) name = "Unnamed " + row.getOrDefault("type", "destination") + " (" + id + ")";
            String nearest = extracted ? row.get("nearest_node_id") : id;
            double snap = extracted ? Double.parseDouble(row.get("snap_distance_km")) : 0;
            if (!graph.vertices().contains(nearest)) {
                double[] point = {Double.parseDouble(row.getOrDefault("longitude", row.get("lon"))),
                        Double.parseDouble(row.getOrDefault("latitude", row.get("lat")))};
                nearest = coordinates.entrySet().stream().min((a, b) -> Double.compare(distance(point, a.getValue()), distance(point, b.getValue())))
                        .map(Map.Entry::getKey).orElse("");
                snap = distance(point, coordinates.get(nearest));
            }
            NavigationDestination destination = new NavigationDestination(id, name,
                    row.getOrDefault("category", row.getOrDefault("type", "poi")), nearest, snap);
            destinations.put(destination.id(), destination);
        }
    }

    private static double distance(double[] a, double[] b) {
        if (b == null) return Double.POSITIVE_INFINITY;
        double lat = Math.toRadians((a[1] + b[1]) / 2);
        double dx = (a[0] - b[0]) * 111.32 * Math.cos(lat);
        double dy = (a[1] - b[1]) * 110.57;
        return Math.sqrt(dx * dx + dy * dy);
    }

    private static List<Map<String, String>> rows(Path path) throws IOException {
        List<String> lines = Files.readAllLines(path);
        if (lines.isEmpty()) return List.of();
        List<String> headers = parseCsv(lines.get(0));
        List<Map<String, String>> result = new ArrayList<>();
        for (String line : lines.subList(1, lines.size())) {
            if (line.isBlank()) continue;
            List<String> values = parseCsv(line);
            Map<String, String> row = new HashMap<>();
            for (int i = 0; i < headers.size(); i++) row.put(headers.get(i), i < values.size() ? values.get(i) : "");
            result.add(row);
        }
        return result;
    }

    private static List<String> parseCsv(String line) {
        List<String> values = new ArrayList<>();
        StringBuilder value = new StringBuilder();
        boolean quoted = false;
        for (int i = 0; i < line.length(); i++) {
            char character = line.charAt(i);
            if (character == '"') {
                if (quoted && i + 1 < line.length() && line.charAt(i + 1) == '"') value.append('"');
                else quoted = !quoted;
            } else if (character == ',' && !quoted) {
                values.add(value.toString());
                value.setLength(0);
            } else value.append(character);
        }
        values.add(value.toString());
        return values;
    }

    public record NavigationDestination(String id, String name, String category, String nearestNodeId,
            double snapDistanceKm) {
        @Override
        public String toString() {
            return name + " (" + category + ")";
        }
    }

    public record Route(List<String> path, double travelTimeMinutes, double distanceKm) { }
}
