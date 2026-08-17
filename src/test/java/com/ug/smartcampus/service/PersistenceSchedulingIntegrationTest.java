package com.ug.smartcampus.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.ug.smartcampus.database.DatabaseManager;
import com.ug.smartcampus.database.dao.BuildingDao;
import com.ug.smartcampus.database.dao.RequestDao;
import com.ug.smartcampus.database.dao.ResourceDao;
import com.ug.smartcampus.model.Building;
import com.ug.smartcampus.model.Request;
import com.ug.smartcampus.model.Resource;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class PersistenceSchedulingIntegrationTest {
    @TempDir Path temporaryDirectory;

    @Test
    void persistsDataThenSchedulesAndAllocatesByPriorityAndBuilding() throws Exception {
        DatabaseManager database = new DatabaseManager("jdbc:sqlite:" + temporaryDirectory.resolve("campus.db"));
        database.initializeSchema(Path.of("database/schema.sql"));
        new BuildingDao(database).create(new Building(1, "Library", 0, 0));
        new BuildingDao(database).create(new Building(2, "Engineering", 1, 1));
        ResourceDao resourceDao = new ResourceDao(database);
        resourceDao.create(new Resource(11, "Electrical team", "Technician", "Electrical", 2));
        resourceDao.create(new Resource(12, "Plumbing team", "Technician", "Plumbing", 1));
        RequestDao requestDao = new RequestDao(database);
        Request urgent = new Request(101, "Power fault", "Ama", 5, "OPEN", 2, LocalDateTime.of(2026, 7, 1, 8, 0), "Estates");
        Request routine = new Request(102, "Leaking tap", "Kojo", 2, "OPEN", 1, LocalDateTime.of(2026, 7, 1, 7, 0), "Estates");
        requestDao.create(routine);
        requestDao.create(urgent);

        PersistenceService persistence = new PersistenceService(requestDao, resourceDao);
        List<Request> reloadedRequests = persistence.loadRequests();
        List<Resource> reloadedResources = persistence.loadResources();
        SchedulingService scheduler = new SchedulingService();
        reloadedRequests.forEach(scheduler::add);

        assertEquals(List.of(101, 102), scheduler.plan().stream().map(Request::getRequestId).toList());
        Map<Request, Resource> assignments = new ResourceAllocationService().allocate(reloadedRequests, reloadedResources);
        assertEquals(2, assignments.size());
        assertEquals(11, resourceFor(assignments, 101).getResourceId());
        assertEquals(12, resourceFor(assignments, 102).getResourceId());
        assertTrue(requestDao.findById(101).isPresent());
    }

    private Resource resourceFor(Map<Request, Resource> assignments, int requestId) {
        return assignments.entrySet().stream().filter(entry -> entry.getKey().getRequestId() == requestId)
                .findFirst().orElseThrow().getValue();
    }
}
