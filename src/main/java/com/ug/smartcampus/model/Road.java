package com.ug.smartcampus.model;

public class Road {
    private String id, fromLocationId, toLocationId;
    private double distanceKm, travelTimeMin, conditionWeight;

    public Road() {
    }

    public Road(String id, String fromLocationId, String toLocationId, double distanceKm, double travelTimeMin,
            double conditionWeight) {
        this.id = id;
        this.fromLocationId = fromLocationId;
        this.toLocationId = toLocationId;
        this.distanceKm = distanceKm;
        this.travelTimeMin = travelTimeMin;
        this.conditionWeight = conditionWeight;
    }

    public String getId() {
        return id;
    }

    public void setId(String v) {
        id = v;
    }

    public String getFromLocationId() {
        return fromLocationId;
    }

    public void setFromLocationId(String v) {
        fromLocationId = v;
    }

    public String getToLocationId() {
        return toLocationId;
    }

    public void setToLocationId(String v) {
        toLocationId = v;
    }

    public double getDistanceKm() {
        return distanceKm;
    }

    public void setDistanceKm(double v) {
        distanceKm = v;
    }

    public double getTravelTimeMin() {
        return travelTimeMin;
    }

    public void setTravelTimeMin(double v) {
        travelTimeMin = v;
    }

    public double getConditionWeight() {
        return conditionWeight;
    }

    public void setConditionWeight(double v) {
        conditionWeight = v;
    }
}
