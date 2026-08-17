package com.ug.smartcampus.model;

public class CampusResource {
    private String id, resourceType, homeLocationId, availabilityStatus;
    private int capacity;

    public CampusResource() {
    }

    public CampusResource(String id, String resourceType, String homeLocationId, int capacity,
            String availabilityStatus) {
        this.id = id;
        this.resourceType = resourceType;
        this.homeLocationId = homeLocationId;
        this.capacity = capacity;
        this.availabilityStatus = availabilityStatus;
    }

    public String getId() {
        return id;
    }

    public void setId(String v) {
        id = v;
    }

    public String getResourceType() {
        return resourceType;
    }

    public void setResourceType(String v) {
        resourceType = v;
    }

    public String getHomeLocationId() {
        return homeLocationId;
    }

    public void setHomeLocationId(String v) {
        homeLocationId = v;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int v) {
        capacity = v;
    }

    public String getAvailabilityStatus() {
        return availabilityStatus;
    }

    public void setAvailabilityStatus(String v) {
        availabilityStatus = v;
    }
}
