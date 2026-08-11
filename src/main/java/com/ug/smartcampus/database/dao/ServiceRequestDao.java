package com.ug.smartcampus.database.dao;

import com.ug.smartcampus.database.DatabaseManager;
import com.ug.smartcampus.model.ServiceRequest;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.*;

public class ServiceRequestDao {
    private final DatabaseManager db;

    public ServiceRequestDao(DatabaseManager db) {
        this.db = db;
    }

    public void create(ServiceRequest x) throws SQLException {
        try (var c = db.getConnection();
                var s = c.prepareStatement(
                        "INSERT INTO service_requests(id,source_location_id,destination_location_id,category,urgency,time_submitted,deadline,status) VALUES(?,?,?,?,?,?,?,?)")) {
            s.setString(1, x.getId());
            s.setString(2, x.getSourceLocationId());
            s.setString(3, x.getDestinationLocationId());
            s.setString(4, x.getCategory());
            s.setInt(5, x.getUrgency());
            s.setString(6, x.getTimeSubmitted().toString());
            s.setString(7, x.getDeadline().toString());
            s.setString(8, x.getStatus());
            s.executeUpdate();
        }
    }

    public Optional<ServiceRequest> findById(String id) throws SQLException {
        try (var c = db.getConnection(); var s = c.prepareStatement("SELECT * FROM service_requests WHERE id=?")) {
            s.setString(1, id);
            try (var r = s.executeQuery()) {
                return r.next() ? Optional.of(map(r)) : Optional.empty();
            }
        }
    }

    public List<ServiceRequest> findAll() throws SQLException {
        List<ServiceRequest> out = new ArrayList<>();
        try (var c = db.getConnection();
                var s = c.createStatement();
                var r = s.executeQuery("SELECT * FROM service_requests ORDER BY id")) {
            while (r.next())
                out.add(map(r));
        }
        return out;
    }

    public boolean update(ServiceRequest x) throws SQLException {
        try (var c = db.getConnection();
                var s = c.prepareStatement(
                        "UPDATE service_requests SET source_location_id=?,destination_location_id=?,category=?,urgency=?,time_submitted=?,deadline=?,status=? WHERE id=?")) {
            s.setString(1, x.getSourceLocationId());
            s.setString(2, x.getDestinationLocationId());
            s.setString(3, x.getCategory());
            s.setInt(4, x.getUrgency());
            s.setString(5, x.getTimeSubmitted().toString());
            s.setString(6, x.getDeadline().toString());
            s.setString(7, x.getStatus());
            s.setString(8, x.getId());
            return s.executeUpdate() > 0;
        }
    }

    public boolean delete(String id) throws SQLException {
        try (var c = db.getConnection(); var s = c.prepareStatement("DELETE FROM service_requests WHERE id=?")) {
            s.setString(1, id);
            return s.executeUpdate() > 0;
        }
    }

    private ServiceRequest map(ResultSet r) throws SQLException {
        return new ServiceRequest(r.getString("id"), r.getString("source_location_id"),
                r.getString("destination_location_id"), r.getString("category"), r.getInt("urgency"),
                LocalDateTime.parse(r.getString("time_submitted")), LocalDateTime.parse(r.getString("deadline")),
                r.getString("status"));
    }
}
