package com.ug.smartcampus.database.dao;

import com.ug.smartcampus.database.DatabaseManager;
import com.ug.smartcampus.model.Location;
import java.sql.*;
import java.util.*;

public class LocationDao {
    private final DatabaseManager db;

    public LocationDao(DatabaseManager db) {
        this.db = db;
    }

    public void create(Location x) throws SQLException {
        try (var c = db.getConnection();
                var s = c.prepareStatement(
                        "INSERT INTO locations(id,name,area,location_type,x_coord,y_coord) VALUES(?,?,?,?,?,?)")) {
            s.setString(1, x.getId());
            s.setString(2, x.getName());
            s.setString(3, x.getArea());
            s.setString(4, x.getLocationType());
            s.setDouble(5, x.getXCoord());
            s.setDouble(6, x.getYCoord());
            s.executeUpdate();
        }
    }

    public Optional<Location> findById(String id) throws SQLException {
        try (var c = db.getConnection(); var s = c.prepareStatement("SELECT * FROM locations WHERE id=?")) {
            s.setString(1, id);
            try (var r = s.executeQuery()) {
                return r.next() ? Optional.of(map(r)) : Optional.empty();
            }
        }
    }

    public List<Location> findAll() throws SQLException {
        List<Location> out = new ArrayList<>();
        try (var c = db.getConnection();
                var s = c.createStatement();
                var r = s.executeQuery("SELECT * FROM locations ORDER BY id")) {
            while (r.next())
                out.add(map(r));
        }
        return out;
    }

    public boolean update(Location x) throws SQLException {
        try (var c = db.getConnection();
                var s = c.prepareStatement(
                        "UPDATE locations SET name=?,area=?,location_type=?,x_coord=?,y_coord=? WHERE id=?")) {
            s.setString(1, x.getName());
            s.setString(2, x.getArea());
            s.setString(3, x.getLocationType());
            s.setDouble(4, x.getXCoord());
            s.setDouble(5, x.getYCoord());
            s.setString(6, x.getId());
            return s.executeUpdate() > 0;
        }
    }

    public boolean delete(String id) throws SQLException {
        try (var c = db.getConnection(); var s = c.prepareStatement("DELETE FROM locations WHERE id=?")) {
            s.setString(1, id);
            return s.executeUpdate() > 0;
        }
    }

    private Location map(ResultSet r) throws SQLException {
        return new Location(r.getString("id"), r.getString("name"), r.getString("area"), r.getString("location_type"),
                r.getDouble("x_coord"), r.getDouble("y_coord"));
    }
}
