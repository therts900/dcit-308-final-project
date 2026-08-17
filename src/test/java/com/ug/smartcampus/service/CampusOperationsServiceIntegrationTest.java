package com.ug.smartcampus.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.ug.smartcampus.database.DatabaseManager;
import com.ug.smartcampus.database.dao.CampusResourceDao;
import com.ug.smartcampus.database.dao.LocationDao;
import com.ug.smartcampus.database.dao.RoadDao;
import com.ug.smartcampus.database.dao.ServiceRequestDao;
import com.ug.smartcampus.model.CampusResource;
import com.ug.smartcampus.model.Location;
import com.ug.smartcampus.model.Road;
import com.ug.smartcampus.model.ServiceRequest;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class CampusOperationsServiceIntegrationTest {
    @TempDir Path temporaryDirectory;

    @Test
    void schedulesAllocatesAndRoutesRealCampusDomainRecords() throws Exception {
        DatabaseManager database = new DatabaseManager("jdbc:sqlite:" + temporaryDirectory.resolve("campus.db"));
        database.initializeSchema(Path.of("database/schema.sql"));
        LocationDao locations = new LocationDao(database);
        locations.create(new Location("L1", "Library", "Central", "Academic", 0, 0));
        locations.create(new Location("L2", "Engineering", "Central", "Academic", 1, 0));
        locations.create(new Location("L3", "Hall", "Residential", "Residence", 2, 0));
        RoadDao roads = new RoadDao(database);
        roads.create(new Road("R1", "L1", "L2", 0.1, 2, 1));
        roads.create(new Road("R2", "L2", "L3", 0.1, 2, 1));
        new CampusResourceDao(database).create(new CampusResource("M1", "MaintenanceCrew", "L1", 3, "AVAILABLE"));
        ServiceRequestDao requests = new ServiceRequestDao(database);
        requests.create(request("Q1", "L1", "L3", 5, 8));
        requests.create(request("Q2", "L1", "L2", 2, 7));

        CampusOperationsService operations = new CampusOperationsService(database);
        assertEquals(List.of("Q1", "Q2"), operations.prioritySchedule().stream().map(ServiceRequest::getId).toList());
        assertEquals(1, operations.allocate().stream().filter(allocation -> allocation.resource() != null).count());
        CampusOperationsService.Route route = operations.route("L1", "L3");
        assertEquals(List.of("L1", "L2", "L3"), route.path());
        assertEquals(4.0, route.weightedTravelMinutes());
        assertFalse(route.path().isEmpty());
        assertTrue(operations.resources().size() == 1);
    }

    private ServiceRequest request(String id, String source, String destination, int urgency, int hour) {
        LocalDateTime submitted = LocalDateTime.of(2026, 7, 1, hour, 0);
        return new ServiceRequest(id, source, destination, "Maintenance", urgency, submitted, submitted.plusHours(4), "OPEN");
    }
}
