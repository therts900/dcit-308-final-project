package com.ug.smartcampus.database.dao;
import com.ug.smartcampus.database.*; import com.ug.smartcampus.model.ScheduleEntry; import java.sql.*; import java.util.*;
public class ScheduleEntryDao {
 private final DatabaseManager db; public ScheduleEntryDao(DatabaseManager db){this.db=db;}
 public void create(ScheduleEntry x)throws SQLException{try(var c=db.getConnection();var s=c.prepareStatement("INSERT INTO schedule_entries(schedule_entry_id,request_id,resource_id,start_time,end_time) VALUES(?,?,?,?,?)")){s.setInt(1,x.getScheduleEntryId());s.setInt(2,x.getRequestId());s.setInt(3,x.getResourceId());s.setTimestamp(4,Timestamp.valueOf(x.getStartTime()));s.setTimestamp(5,Timestamp.valueOf(x.getEndTime()));s.executeUpdate();}}
 public Optional<ScheduleEntry> findById(int id)throws SQLException{try(var c=db.getConnection();var s=c.prepareStatement("SELECT * FROM schedule_entries WHERE schedule_entry_id=?")){s.setInt(1,id);try(var r=s.executeQuery()){return r.next()?Optional.of(map(r)):Optional.empty();}}}
 public List<ScheduleEntry> findAll()throws SQLException{List<ScheduleEntry> out=new ArrayList<>();try(var c=db.getConnection();var s=c.createStatement();var r=s.executeQuery("SELECT * FROM schedule_entries ORDER BY schedule_entry_id")){while(r.next())out.add(map(r));}return out;}
 public boolean update(ScheduleEntry x)throws SQLException{try(var c=db.getConnection();var s=c.prepareStatement("UPDATE schedule_entries SET request_id=?,resource_id=?,start_time=?,end_time=? WHERE schedule_entry_id=?")){s.setInt(1,x.getRequestId());s.setInt(2,x.getResourceId());s.setTimestamp(3,Timestamp.valueOf(x.getStartTime()));s.setTimestamp(4,Timestamp.valueOf(x.getEndTime()));s.setInt(5,x.getScheduleEntryId());return s.executeUpdate()>0;}}
 public boolean delete(int id)throws SQLException{try(var c=db.getConnection();var s=c.prepareStatement("DELETE FROM schedule_entries WHERE schedule_entry_id=?")){s.setInt(1,id);return s.executeUpdate()>0;}}
 private ScheduleEntry map(ResultSet r)throws SQLException{return new ScheduleEntry(r.getInt("schedule_entry_id"),r.getInt("request_id"),r.getInt("resource_id"),DatabaseSupport.date(r,"start_time"),DatabaseSupport.date(r,"end_time"));}
}
