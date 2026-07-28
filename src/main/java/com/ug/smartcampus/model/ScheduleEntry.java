package com.ug.smartcampus.model;

import java.time.LocalDateTime;

public class ScheduleEntry {
    private int scheduleEntryId, requestId, resourceId; private LocalDateTime startTime, endTime;
    public ScheduleEntry() { }
    public ScheduleEntry(int id,int requestId,int resourceId,LocalDateTime start,LocalDateTime end){scheduleEntryId=id;this.requestId=requestId;this.resourceId=resourceId;startTime=start;endTime=end;}
    public int getScheduleEntryId(){return scheduleEntryId;} public void setScheduleEntryId(int v){scheduleEntryId=v;}
    public int getRequestId(){return requestId;} public void setRequestId(int v){requestId=v;}
    public int getResourceId(){return resourceId;} public void setResourceId(int v){resourceId=v;}
    public LocalDateTime getStartTime(){return startTime;} public void setStartTime(LocalDateTime v){startTime=v;}
    public LocalDateTime getEndTime(){return endTime;} public void setEndTime(LocalDateTime v){endTime=v;}
}
