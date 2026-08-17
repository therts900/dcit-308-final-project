package com.ug.smartcampus.database.dao;

import com.ug.smartcampus.database.DatabaseManager;
import com.ug.smartcampus.model.Road;
import java.sql.*;
import java.util.*;

public class RoadDao {
    private final DatabaseManager db;

    public RoadDao(DatabaseManager db) {
        this.db = db;
    }

    public void create(Road x) throws SQLException {
        try (var c = db.getConnection();
                var s = c.prepareStatement(
                        "INSERT INTO roads(id,from_location_id,to_location_id,distance_km,travel_time_min,condition_weight) VALUES(?,?,?,?,?,?)")) {
            s.setString(1, x.getId());
            s.setString(2, x.getFromLocationId());
            s.setString(3, x.getToLocationId());
            s.setDouble(4, x.getDistanceKm());
            s.setDouble(5, x.getTravelTimeMin());
            s.setDouble(6, x.getConditionWeight());
            s.executeUpdate();
        }
    }

    public Optional<Road> findById(String id) throws SQLException {
        try (var c = db.getConnection(); var s = c.prepareStatement("SELECT * FROM roads WHERE id=?")) {
            s.setString(1, id);
            try (var r = s.executeQuery()) {
                return r.next() ? Optional.of(map(r)) : Optional.empty();
            }
        }
    }

    public List<Road> findAll() throws SQLException {
        List<Road> out = new ArrayList<>();
        try (var c = db.getConnection();
                var s = c.createStatement();
                var r = s.executeQuery("SELECT * FROM roads ORDER BY id")) {
            while (r.next())
                out.add(map(r));
        }
        return out;
    }

    public boolean update(Road x) throws SQLException {
        try (var c = db.getConnection();
                var s = c.prepareStatement(
                        "UPDATE roads SET from_location_id=?,to_location_id=?,distance_km=?,travel_time_min=?,condition_weight=? WHERE id=?")) {
            s.setString(1, x.getFromLocationId());
            s.setString(2, x.getToLocationId());
            s.setDouble(3, x.getDistanceKm());
            s.setDouble(4, x.getTravelTimeMin());
            s.setDouble(5, x.getConditionWeight());
            s.setString(6, x.getId());
            return s.executeUpdate() > 0;
        }
    }

    public boolean delete(String id) throws SQLException {
        try (var c = db.getConnection(); var s = c.prepareStatement("DELETE FROM roads WHERE id=?")) {
            s.setString(1, id);
            return s.executeUpdate() > 0;
        }
    }

    private Road map(ResultSet r) throws SQLException {
        return new Road(r.getString("id"), r.getString("from_location_id"), r.getString("to_location_id"),
                r.getDouble("distance_km"), r.getDouble("travel_time_min"), r.getDouble("condition_weight"));
    }
}
