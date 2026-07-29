package com.ug.smartcampus.database.dao;
import com.ug.smartcampus.database.DatabaseManager; import com.ug.smartcampus.model.Building; import java.sql.*; import java.util.*;
public class BuildingDao {
 private final DatabaseManager db; public BuildingDao(DatabaseManager db){this.db=db;}
 public void create(Building b)throws SQLException{try(var c=db.getConnection();var s=c.prepareStatement("INSERT INTO buildings(building_id,name,x_coord,y_coord) VALUES(?,?,?,?)")){s.setInt(1,b.getBuildingId());s.setString(2,b.getName());s.setDouble(3,b.getXCoord());s.setDouble(4,b.getYCoord());s.executeUpdate();}}
 public Optional<Building> findById(int id)throws SQLException{try(var c=db.getConnection();var s=c.prepareStatement("SELECT * FROM buildings WHERE building_id=?")){s.setInt(1,id);try(var r=s.executeQuery()){return r.next()?Optional.of(map(r)):Optional.empty();}}}
 public List<Building> findAll()throws SQLException{List<Building> out=new ArrayList<>();try(var c=db.getConnection();var s=c.createStatement();var r=s.executeQuery("SELECT * FROM buildings ORDER BY building_id")){while(r.next())out.add(map(r));}return out;}
 public boolean update(Building b)throws SQLException{try(var c=db.getConnection();var s=c.prepareStatement("UPDATE buildings SET name=?,x_coord=?,y_coord=? WHERE building_id=?")){s.setString(1,b.getName());s.setDouble(2,b.getXCoord());s.setDouble(3,b.getYCoord());s.setInt(4,b.getBuildingId());return s.executeUpdate()>0;}}
 public boolean delete(int id)throws SQLException{try(var c=db.getConnection();var s=c.prepareStatement("DELETE FROM buildings WHERE building_id=?")){s.setInt(1,id);return s.executeUpdate()>0;}}
 private Building map(ResultSet r)throws SQLException{return new Building(r.getInt("building_id"),r.getString("name"),r.getDouble("x_coord"),r.getDouble("y_coord"));}
}
