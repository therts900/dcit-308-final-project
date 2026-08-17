package com.ug.smartcampus;

import com.ug.smartcampus.database.DatabaseManager;
import com.ug.smartcampus.database.SampleDataLoader;
import com.ug.smartcampus.database.dao.RequestDao;
import com.ug.smartcampus.database.dao.ResourceDao;
import com.ug.smartcampus.model.Request;
import com.ug.smartcampus.service.PersistenceService;
import com.ug.smartcampus.service.SchedulingService;
import com.ug.smartcampus.service.NavigationService;
import java.nio.file.Files;
import com.ug.smartcampus.ui.SmartCampusDashboard;
import java.nio.file.Path;

/** Console entry point for initializing sample data and displaying a priority schedule. */
public final class SmartCampusApplication {
    private SmartCampusApplication() { }

    public static void main(String[] args) throws Exception {
        boolean loadSample = hasArgument(args, "--load-sample");
        boolean schedule = hasArgument(args, "--schedule");
        boolean check = hasArgument(args, "--check");
        if (!loadSample && !schedule) {
            if (check) {
                runCheck();
                return;
            }
            SmartCampusDashboard.showDashboard();
            return;
        }
        try (DatabaseManager database = new DatabaseManager()) {
            database.initializeSchema(Path.of("database/schema.sql"));
            if (loadSample) {
                SampleDataLoader.load(database, Path.of("database/data"));
                System.out.println("Sample buildings, resources, and requests loaded.");
            }
            if (schedule) {
                PersistenceService persistence = new PersistenceService(new RequestDao(database), new ResourceDao(database));
                SchedulingService scheduler = new SchedulingService();
                for (Request request : persistence.loadRequests()) scheduler.add(request);
                for (Request request : scheduler.plan()) {
                    System.out.printf("Request %d | priority %d | %s%n", request.getRequestId(), request.getPriority(), request.getDescription());
                }
                System.out.printf("Available resources: %d%n", persistence.loadResources().size());
            }
        }
    }

    private static void runCheck() throws Exception {
        Path data = Path.of("database/data");
        NavigationService navigation = new NavigationService(data);
        var destinations = navigation.destinations();
        var route = navigation.route(destinations.get(0).id(), destinations.get(destinations.size() - 1).id());
        System.out.printf("Navigation: %d nodes, %d edges, %d destinations%n",
                navigation.nodeCount(), navigation.edgeCount(), destinations.size());
        System.out.printf("Route test: %s -> %s, %d nodes, %.2f minutes%n",
                destinations.get(0).name(), destinations.get(destinations.size() - 1).name(),
                route.path().size(), route.travelTimeMinutes());
        System.out.printf("Route distance: %.3f km%n", route.distanceKm());
        Path requests = data.resolve("requests.csv");
        System.out.println("requests.csv: " + (Files.exists(requests) ? "present" : "NOT FOUND"));
    }

    private static boolean hasArgument(String[] args, String value) {
        for (String arg : args) if (value.equals(arg)) return true;
        return false;
    }
}
