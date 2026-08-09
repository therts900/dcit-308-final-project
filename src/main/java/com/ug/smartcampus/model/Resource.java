package com.ug.smartcampus.model;

public class Resource {
    private int resourceId, locationBuildingId; private String name, type, capabilities;
    public Resource() { }
    public Resource(int id,String name,String type,String capabilities,int locationBuildingId){resourceId=id;this.name=name;this.type=type;this.capabilities=capabilities;this.locationBuildingId=locationBuildingId;}
    public int getResourceId(){return resourceId;} public void setResourceId(int v){resourceId=v;}
    public String getName(){return name;} public void setName(String v){name=v;}
    public String getType(){return type;} public void setType(String v){type=v;}
    public String getCapabilities(){return capabilities;} public void setCapabilities(String v){capabilities=v;}
    public int getLocationBuildingId(){return locationBuildingId;} public void setLocationBuildingId(int v){locationBuildingId=v;}
}
