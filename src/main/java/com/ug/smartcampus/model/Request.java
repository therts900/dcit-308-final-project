package com.ug.smartcampus.model;

import java.time.LocalDateTime;

public class Request {
    private int requestId, priority, buildingId; private String description, requester, status, department; private LocalDateTime requestedTime;
    public Request() { }
    public Request(int id,String description,String requester,int priority,String status,int buildingId,LocalDateTime time,String department){requestId=id;this.description=description;this.requester=requester;this.priority=priority;this.status=status;this.buildingId=buildingId;requestedTime=time;this.department=department;}
    public int getRequestId(){return requestId;} public void setRequestId(int v){requestId=v;}
    public String getDescription(){return description;} public void setDescription(String v){description=v;}
    public String getRequester(){return requester;} public void setRequester(String v){requester=v;}
    public int getPriority(){return priority;} public void setPriority(int v){priority=v;}
    public String getStatus(){return status;} public void setStatus(String v){status=v;}
    public int getBuildingId(){return buildingId;} public void setBuildingId(int v){buildingId=v;}
    public LocalDateTime getRequestedTime(){return requestedTime;} public void setRequestedTime(LocalDateTime v){requestedTime=v;}
    public String getDepartment(){return department;} public void setDepartment(String v){department=v;}
}
