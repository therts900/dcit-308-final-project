package com.ug.smartcampus.database.dao;
import com.ug.smartcampus.database.DatabaseManager; import com.ug.smartcampus.model.CampusResource; import java.sql.*; import java.util.*;
public class CampusResourceDao {
 private final DatabaseManager db; public CampusResourceDao(DatabaseManager db){this.db=db;}
 public void create(CampusResource x)throws SQLException{try(var c=db.getConnection();var s=c.prepareStatement("INSERT INTO campus_resources(id,resource_type,home_location_id,capacity,availability_status) VALUES(?,?,?,?,?)")){s.setString(1,x.getId());s.setString(2,x.getResourceType());s.setString(3,x.getHomeLocationId());s.setInt(4,x.getCapacity());s.setString(5,x.getAvailabilityStatus());s.executeUpdate();}}
 public Optional<CampusResource> findById(String id)throws SQLException{try(var c=db.getConnection();var s=c.prepareStatement("SELECT * FROM campus_resources WHERE id=?")){s.setString(1,id);try(var r=s.executeQuery()){return r.next()?Optional.of(map(r)):Optional.empty();}}}
 public List<CampusResource> findAll()throws SQLException{List<CampusResource> out=new ArrayList<>();try(var c=db.getConnection();var s=c.createStatement();var r=s.executeQuery("SELECT * FROM campus_resources ORDER BY id")){while(r.next())out.add(map(r));}return out;}
 public boolean update(CampusResource x)throws SQLException{try(var c=db.getConnection();var s=c.prepareStatement("UPDATE campus_resources SET resource_type=?,home_location_id=?,capacity=?,availability_status=? WHERE id=?")){s.setString(1,x.getResourceType());s.setString(2,x.getHomeLocationId());s.setInt(3,x.getCapacity());s.setString(4,x.getAvailabilityStatus());s.setString(5,x.getId());return s.executeUpdate()>0;}}
 public boolean delete(String id)throws SQLException{try(var c=db.getConnection();var s=c.prepareStatement("DELETE FROM campus_resources WHERE id=?")){s.setString(1,id);return s.executeUpdate()>0;}}
 private CampusResource map(ResultSet r)throws SQLException{return new CampusResource(r.getString("id"),r.getString("resource_type"),r.getString("home_location_id"),r.getInt("capacity"),r.getString("availability_status"));}
}
