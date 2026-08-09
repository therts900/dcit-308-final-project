package com.ug.smartcampus.database;

import com.ug.smartcampus.database.dao.BuildingDao;
import com.ug.smartcampus.database.dao.RequestDao;
import com.ug.smartcampus.database.dao.ResourceDao;
import com.ug.smartcampus.model.Building;
import com.ug.smartcampus.model.Request;
import com.ug.smartcampus.model.Resource;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

/** Imports the repository's small CSV fixture set into a newly initialized database. */
public final class SampleDataLoader {
    private SampleDataLoader() { }

    public static void load(DatabaseManager database, Path dataDirectory) throws IOException, SQLException {
        BuildingDao buildings = new BuildingDao(database);
        ResourceDao resources = new ResourceDao(database);
        RequestDao requests = new RequestDao(database);
        for (String[] row : rows(dataDirectory.resolve("buildings.csv"))) {
            int id = Integer.parseInt(row[0]);
            if (buildings.findById(id).isEmpty()) buildings.create(new Building(id, row[1], Double.parseDouble(row[2]), Double.parseDouble(row[3])));
        }
        for (String[] row : rows(dataDirectory.resolve("resources.csv"))) {
            int id = Integer.parseInt(row[0]);
            if (resources.findById(id).isEmpty()) resources.create(new Resource(id, row[1], row[2], row[3], Integer.parseInt(row[4])));
        }
        for (String[] row : rows(dataDirectory.resolve("maintenance_requests.csv"))) {
            int id = Integer.parseInt(row[0]);
            if (requests.findById(id).isEmpty()) requests.create(new Request(id, row[1], row[2], Integer.parseInt(row[3]), row[4],
                    Integer.parseInt(row[5]), LocalDateTime.parse(row[6]), row[7]));
        }
    }

    private static List<String[]> rows(Path csv) throws IOException {
        return Files.readAllLines(csv).stream().skip(1).filter(line -> !line.isBlank())
                .map(line -> line.split(",", -1)).toList();
    }
}
