package com.ug.smartcampus.service;

import com.ug.smartcampus.database.DatabaseManager;
import com.ug.smartcampus.database.dao.CampusResourceDao;
import com.ug.smartcampus.database.dao.LocationDao;
import com.ug.smartcampus.database.dao.RoadDao;
import com.ug.smartcampus.database.dao.ServiceRequestDao;
import com.ug.smartcampus.datastructures.graph.Graph;
import com.ug.smartcampus.datastructures.nonlinear.PriorityQueue;
import com.ug.smartcampus.model.CampusResource;
import com.ug.smartcampus.model.Location;
import com.ug.smartcampus.model.Road;
import com.ug.smartcampus.model.ServiceRequest;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Application operations backed by the collected campus dataset.  This keeps the
 * dashboard from mixing the legacy demonstration schema with real campus data.
 */
public final class CampusOperationsService {
    private final DatabaseManager database;

    public CampusOperationsService(DatabaseManager database) {
        this.database = database;
    }

    public List<Location> locations() throws SQLException {
        return new LocationDao(database).findAll();
    }

    public List<Road> roads() throws SQLException {
        return new RoadDao(database).findAll();
    }

    public List<CampusResource> resources() throws SQLException {
        return new CampusResourceDao(database).findAll();
    }

    public List<ServiceRequest> requests() throws SQLException {
        return new ServiceRequestDao(database).findAll();
    }

    /** Plans open work with the project's custom binary-heap priority queue. */
    public List<ServiceRequest> prioritySchedule() throws SQLException {
        PriorityQueue<ServiceRequest> queue = new PriorityQueue<>(Comparator
                .comparingInt(ServiceRequest::getUrgency).reversed()
                .thenComparing(ServiceRequest::getTimeSubmitted));
        for (ServiceRequest request : requests()) {
            if (isActive(request)) {
                queue.offer(request);
            }
        }
        List<ServiceRequest> schedule = new ArrayList<>();
        while (!queue.isEmpty()) {
            schedule.add(queue.dequeue());
        }
        return schedule;
    }

    /** Greedily reserves one available resource per source location and request. */
    public List<Allocation> allocate() throws SQLException {
        Map<String, List<CampusResource>> availableByLocation = new HashMap<>();
        for (CampusResource resource : resources()) {
            if ("AVAILABLE".equalsIgnoreCase(resource.getAvailabilityStatus())) {
                availableByLocation.computeIfAbsent(resource.getHomeLocationId(), unused -> new ArrayList<>()).add(resource);
            }
        }
        List<Allocation> allocations = new ArrayList<>();
        for (ServiceRequest request : prioritySchedule()) {
            List<CampusResource> candidates = availableByLocation.get(request.getSourceLocationId());
            CampusResource resource = candidates == null || candidates.isEmpty() ? null : candidates.remove(0);
            allocations.add(new Allocation(request, resource));
        }
        return allocations;
    }

    public Route route(String from, String to) throws SQLException {
        Graph<String> network = new Graph<>();
        for (Location location : locations()) {
            network.addVertex(location.getId());
        }
        for (Road road : roads()) {
            network.addEdge(road.getFromLocationId(), road.getToLocationId(),
                    road.getTravelTimeMin() * road.getConditionWeight());
        }
        RoutingService<String> routing = new RoutingService<>(network);
        return new Route(routing.route(from, to), routing.distance(from, to));
    }

    private boolean isActive(ServiceRequest request) {
        return !"DONE".equalsIgnoreCase(request.getStatus()) && !"CANCELLED".equalsIgnoreCase(request.getStatus());
    }

    public record Allocation(ServiceRequest request, CampusResource resource) {
    }

    public record Route(List<String> path, double weightedTravelMinutes) {
    }
}
