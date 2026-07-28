package com.ug.smartcampus.model;

public class Edge {
    private int fromBuilding, toBuilding; private double distance;
    public Edge() { } public Edge(int from,int to,double distance){fromBuilding=from;toBuilding=to;this.distance=distance;}
    public int getFromBuilding(){return fromBuilding;} public void setFromBuilding(int v){fromBuilding=v;}
    public int getToBuilding(){return toBuilding;} public void setToBuilding(int v){toBuilding=v;}
    public double getDistance(){return distance;} public void setDistance(double v){distance=v;}
}
