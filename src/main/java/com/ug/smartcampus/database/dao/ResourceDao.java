package com.ug.smartcampus.database.dao;
import com.ug.smartcampus.database.DatabaseManager; import com.ug.smartcampus.model.Resource; import java.sql.*; import java.util.*;
public class ResourceDao {
 private final DatabaseManager db; public ResourceDao(DatabaseManager db){this.db=db;}
 public void create(Resource x)throws SQLException{try(var c=db.getConnection();var s=c.prepareStatement("INSERT INTO resources(resource_id,name,type,capabilities,location_building_id) VALUES(?,?,?,?,?)")){s.setInt(1,x.getResourceId());s.setString(2,x.getName());s.setString(3,x.getType());s.setString(4,x.getCapabilities());s.setInt(5,x.getLocationBuildingId());s.executeUpdate();}}
 public Optional<Resource> findById(int id)throws SQLException{try(var c=db.getConnection();var s=c.prepareStatement("SELECT * FROM resources WHERE resource_id=?")){s.setInt(1,id);try(var r=s.executeQuery()){return r.next()?Optional.of(map(r)):Optional.empty();}}}
 public List<Resource> findAll()throws SQLException{List<Resource> out=new ArrayList<>();try(var c=db.getConnection();var s=c.createStatement();var r=s.executeQuery("SELECT * FROM resources ORDER BY resource_id")){while(r.next())out.add(map(r));}return out;}
 public boolean update(Resource x)throws SQLException{try(var c=db.getConnection();var s=c.prepareStatement("UPDATE resources SET name=?,type=?,capabilities=?,location_building_id=? WHERE resource_id=?")){s.setString(1,x.getName());s.setString(2,x.getType());s.setString(3,x.getCapabilities());s.setInt(4,x.getLocationBuildingId());s.setInt(5,x.getResourceId());return s.executeUpdate()>0;}}
 public boolean delete(int id)throws SQLException{try(var c=db.getConnection();var s=c.prepareStatement("DELETE FROM resources WHERE resource_id=?")){s.setInt(1,id);return s.executeUpdate()>0;}}
 private Resource map(ResultSet r)throws SQLException{return new Resource(r.getInt("resource_id"),r.getString("name"),r.getString("type"),r.getString("capabilities"),r.getInt("location_building_id"));}
}
