package com.ug.smartcampus.database.dao;
import com.ug.smartcampus.database.DatabaseManager; import com.ug.smartcampus.model.Edge; import java.sql.*; import java.util.*;
public class EdgeDao {
 private final DatabaseManager db; public EdgeDao(DatabaseManager db){this.db=db;}
 public void create(Edge x)throws SQLException{try(var c=db.getConnection();var s=c.prepareStatement("INSERT INTO edges(from_building,to_building,distance) VALUES(?,?,?)")){s.setInt(1,x.getFromBuilding());s.setInt(2,x.getToBuilding());s.setDouble(3,x.getDistance());s.executeUpdate();}}
 public Optional<Edge> findById(int from,int to)throws SQLException{try(var c=db.getConnection();var s=c.prepareStatement("SELECT * FROM edges WHERE from_building=? AND to_building=?")){s.setInt(1,from);s.setInt(2,to);try(var r=s.executeQuery()){return r.next()?Optional.of(map(r)):Optional.empty();}}}
 public List<Edge> findAll()throws SQLException{List<Edge> out=new ArrayList<>();try(var c=db.getConnection();var s=c.createStatement();var r=s.executeQuery("SELECT * FROM edges ORDER BY from_building,to_building")){while(r.next())out.add(map(r));}return out;}
 public boolean update(Edge x)throws SQLException{try(var c=db.getConnection();var s=c.prepareStatement("UPDATE edges SET distance=? WHERE from_building=? AND to_building=?")){s.setDouble(1,x.getDistance());s.setInt(2,x.getFromBuilding());s.setInt(3,x.getToBuilding());return s.executeUpdate()>0;}}
 public boolean delete(int from,int to)throws SQLException{try(var c=db.getConnection();var s=c.prepareStatement("DELETE FROM edges WHERE from_building=? AND to_building=?")){s.setInt(1,from);s.setInt(2,to);return s.executeUpdate()>0;}}
 private Edge map(ResultSet r)throws SQLException{return new Edge(r.getInt("from_building"),r.getInt("to_building"),r.getDouble("distance"));}
}
