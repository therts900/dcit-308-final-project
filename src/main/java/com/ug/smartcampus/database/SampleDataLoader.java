package com.ug.smartcampus.database;

import com.ug.smartcampus.database.dao.BuildingDao;
import com.ug.smartcampus.database.dao.CampusResourceDao;
import com.ug.smartcampus.database.dao.LocationDao;
import com.ug.smartcampus.database.dao.RequestDao;
import com.ug.smartcampus.database.dao.RoadDao;
import com.ug.smartcampus.database.dao.ServiceRequestDao;
import com.ug.smartcampus.model.Building;
import com.ug.smartcampus.model.CampusResource;
import com.ug.smartcampus.model.Location;
import com.ug.smartcampus.model.Request;
import com.ug.smartcampus.model.Road;
import com.ug.smartcampus.model.ServiceRequest;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

/** Imports the repository's CSV fixture set into a newly initialized database. */
public final class SampleDataLoader {
    private SampleDataLoader() { }

    public static void load(DatabaseManager database, Path dataDirectory) throws IOException, SQLException {
        BuildingDao buildings = new BuildingDao(database);
        RequestDao requests = new RequestDao(database);
        LocationDao locations = new LocationDao(database);
        RoadDao roads = new RoadDao(database);
        ServiceRequestDao serviceRequests = new ServiceRequestDao(database);
        CampusResourceDao campusResources = new CampusResourceDao(database);

        // Legacy demo fixtures (small hand-written tables, kept for the original UI tabs).
        for (String[] row : rows(dataDirectory.resolve("buildings.csv"))) {
            int id = Integer.parseInt(row[0]);
            if (buildings.findById(id).isEmpty()) buildings.create(new Building(id, row[1], Double.parseDouble(row[2]), Double.parseDouble(row[3])));
        }
        for (String[] row : rows(dataDirectory.resolve("maintenance_requests.csv"))) {
            int id = Integer.parseInt(row[0]);
            if (requests.findById(id).isEmpty()) requests.create(new Request(id, row[1], row[2], Integer.parseInt(row[3]), row[4],
                    Integer.parseInt(row[5]), LocalDateTime.parse(row[6]), row[7]));
        }

        // Real campus data model, collected for the group project.
        // locations.csv: id,name,area,location_type,x_coord,y_coord
        for (String[] row : rows(dataDirectory.resolve("locations.csv"))) {
            String id = row[0];
            if (locations.findById(id).isEmpty()) {
                locations.create(new Location(id, row[1], row[2], row[3], Double.parseDouble(row[4]), Double.parseDouble(row[5])));
            }
        }
        // roads.csv: road_id,from_location_id,to_location_id,distance_km,travel_time_min,condition_weight
        // Must load after locations (FK dependency).
        for (String[] row : rows(dataDirectory.resolve("roads.csv"))) {
            String id = row[0];
            if (roads.findById(id).isEmpty()) {
                roads.create(new Road(id, row[1], row[2], Double.parseDouble(row[3]), Double.parseDouble(row[4]), Double.parseDouble(row[5])));
            }
        }
        // service_requests.csv: request_id,source_location_id,destination_location_id,category,urgency,time_submitted,deadline,status
        // Must load after locations (FK dependency).
        for (String[] row : rows(dataDirectory.resolve("service_requests.csv"))) {
            String id = row[0];
            if (serviceRequests.findById(id).isEmpty()) {
                serviceRequests.create(new ServiceRequest(id, row[1], row[2], row[3], Integer.parseInt(row[4]),
                        LocalDateTime.parse(row[5]), LocalDateTime.parse(row[6]), row[7]));
            }
        }
        // resources.csv: resource_id,resource_type,home_location_id,capacity,availability_status
        // Must load after locations (FK dependency).
        for (String[] row : rows(dataDirectory.resolve("resources.csv"))) {
            String id = row[0];
            if (campusResources.findById(id).isEmpty()) {
                campusResources.create(new CampusResource(id, row[1], row[2], Integer.parseInt(row[3]), row[4]));
            }
        }
    }

    private static List<String[]> rows(Path csv) throws IOException {
        return Files.readAllLines(csv).stream().skip(1).filter(line -> !line.isBlank())
                .map(line -> line.split(",", -1)).toList();
    }
}
