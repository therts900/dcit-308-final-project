package com.ug.smartcampus.model;

import java.time.LocalDateTime;

public class ServiceRequest {
    private String id, sourceLocationId, destinationLocationId, category, status; private int urgency;
    private LocalDateTime timeSubmitted, deadline;
    public ServiceRequest() { }
    public ServiceRequest(String id, String sourceLocationId, String destinationLocationId, String category, int urgency,
                           LocalDateTime timeSubmitted, LocalDateTime deadline, String status) {
        this.id=id; this.sourceLocationId=sourceLocationId; this.destinationLocationId=destinationLocationId;
        this.category=category; this.urgency=urgency; this.timeSubmitted=timeSubmitted; this.deadline=deadline; this.status=status;
    }
    public String getId(){return id;} public void setId(String v){id=v;}
    public String getSourceLocationId(){return sourceLocationId;} public void setSourceLocationId(String v){sourceLocationId=v;}
    public String getDestinationLocationId(){return destinationLocationId;} public void setDestinationLocationId(String v){destinationLocationId=v;}
    public String getCategory(){return category;} public void setCategory(String v){category=v;}
    public int getUrgency(){return urgency;} public void setUrgency(int v){urgency=v;}
    public LocalDateTime getTimeSubmitted(){return timeSubmitted;} public void setTimeSubmitted(LocalDateTime v){timeSubmitted=v;}
    public LocalDateTime getDeadline(){return deadline;} public void setDeadline(LocalDateTime v){deadline=v;}
    public String getStatus(){return status;} public void setStatus(String v){status=v;}
}
