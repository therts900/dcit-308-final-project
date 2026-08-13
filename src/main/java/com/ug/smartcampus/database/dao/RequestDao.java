package com.ug.smartcampus.database.dao;

import com.ug.smartcampus.database.*;
import com.ug.smartcampus.model.Request;
import java.sql.*;
import java.time.*;
import java.util.*;

public class RequestDao {
    private final DatabaseManager db;

    public RequestDao(DatabaseManager db) {
        this.db = db;
    }

    public void create(Request x) throws SQLException {
        try (var c = db.getConnection();
                var s = c.prepareStatement(
                        "INSERT INTO requests(request_id,description,requester,priority,status,building_id,requested_time,department) VALUES(?,?,?,?,?,?,?,?)")) {
            s.setInt(1, x.getRequestId());
            s.setString(2, x.getDescription());
            s.setString(3, x.getRequester());
            s.setInt(4, x.getPriority());
            s.setString(5, x.getStatus());
            s.setInt(6, x.getBuildingId());
            s.setTimestamp(7, Timestamp.valueOf(x.getRequestedTime()));
            s.setString(8, x.getDepartment());
            s.executeUpdate();
        }
    }

    public Optional<Request> findById(int id) throws SQLException {
        try (var c = db.getConnection(); var s = c.prepareStatement("SELECT * FROM requests WHERE request_id=?")) {
            s.setInt(1, id);
            try (var r = s.executeQuery()) {
                return r.next() ? Optional.of(map(r)) : Optional.empty();
            }
        }
    }

    public List<Request> findAll() throws SQLException {
        List<Request> out = new ArrayList<>();
        try (var c = db.getConnection();
                var s = c.createStatement();
                var r = s.executeQuery("SELECT * FROM requests ORDER BY request_id")) {
            while (r.next())
                out.add(map(r));
        }
        return out;
    }

    public boolean update(Request x) throws SQLException {
        try (var c = db.getConnection();
                var s = c.prepareStatement(
                        "UPDATE requests SET description=?,requester=?,priority=?,status=?,building_id=?,requested_time=?,department=? WHERE request_id=?")) {
            s.setString(1, x.getDescription());
            s.setString(2, x.getRequester());
            s.setInt(3, x.getPriority());
            s.setString(4, x.getStatus());
            s.setInt(5, x.getBuildingId());
            s.setTimestamp(6, Timestamp.valueOf(x.getRequestedTime()));
            s.setString(7, x.getDepartment());
            s.setInt(8, x.getRequestId());
            return s.executeUpdate() > 0;
        }
    }

    public boolean delete(int id) throws SQLException {
        try (var c = db.getConnection(); var s = c.prepareStatement("DELETE FROM requests WHERE request_id=?")) {
            s.setInt(1, id);
            return s.executeUpdate() > 0;
        }
    }

    private Request map(ResultSet r) throws SQLException {
        return new Request(r.getInt("request_id"), r.getString("description"), r.getString("requester"),
                r.getInt("priority"), r.getString("status"), r.getInt("building_id"),
                DatabaseSupport.date(r, "requested_time"), r.getString("department"));
    }
}
