package com.ug.smartcampus.model;

public class Building {
    private int buildingId; private String name; private double xCoord; private double yCoord;
    public Building() { }
    public Building(int buildingId, String name, double xCoord, double yCoord) { this.buildingId=buildingId; this.name=name; this.xCoord=xCoord; this.yCoord=yCoord; }
    public int getBuildingId(){return buildingId;} public void setBuildingId(int v){buildingId=v;}
    public String getName(){return name;} public void setName(String v){name=v;}
    public double getXCoord(){return xCoord;} public void setXCoord(double v){xCoord=v;}
    public double getYCoord(){return yCoord;} public void setYCoord(double v){yCoord=v;}
}
