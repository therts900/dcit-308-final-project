package com.ug.smartcampus.model;

public class Location {
    private String id, name, area, locationType;
    private double xCoord, yCoord;

    public Location() {
    }

    public Location(String id, String name, String area, String locationType, double xCoord, double yCoord) {
        this.id = id;
        this.name = name;
        this.area = area;
        this.locationType = locationType;
        this.xCoord = xCoord;
        this.yCoord = yCoord;
    }

    public String getId() {
        return id;
    }

    public void setId(String v) {
        id = v;
    }

    public String getName() {
        return name;
    }

    public void setName(String v) {
        name = v;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String v) {
        area = v;
    }

    public String getLocationType() {
        return locationType;
    }

    public void setLocationType(String v) {
        locationType = v;
    }

    public double getXCoord() {
        return xCoord;
    }

    public void setXCoord(double v) {
        xCoord = v;
    }

    public double getYCoord() {
        return yCoord;
    }

    public void setYCoord(double v) {
        yCoord = v;
    }
}
