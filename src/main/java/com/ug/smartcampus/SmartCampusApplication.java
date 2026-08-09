package com.ug.smartcampus;

import com.ug.smartcampus.database.DatabaseManager;
import com.ug.smartcampus.database.SampleDataLoader;
import com.ug.smartcampus.database.dao.RequestDao;
import com.ug.smartcampus.database.dao.ResourceDao;
import com.ug.smartcampus.model.Request;
import com.ug.smartcampus.service.PersistenceService;
import com.ug.smartcampus.service.SchedulingService;
import com.ug.smartcampus.ui.SmartCampusDashboard;
import java.nio.file.Path;

/** Console entry point for initializing sample data and displaying a priority schedule. */
public final class SmartCampusApplication {
    private SmartCampusApplication() { }

    public static void main(String[] args) throws Exception {
        boolean loadSample = hasArgument(args, "--load-sample");
        boolean schedule = hasArgument(args, "--schedule");
        if (!loadSample && !schedule) {
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

    private static boolean hasArgument(String[] args, String value) {
        for (String arg : args) if (value.equals(arg)) return true;
        return false;
    }
}
