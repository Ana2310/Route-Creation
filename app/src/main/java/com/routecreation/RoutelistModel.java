package com.routecreation;

public class RoutelistModel {
    private  String id,startlat,startlon,endlat,endlon,distance,time,name,startaddress,endaddress;



    public RoutelistModel(String id,String startlat, String startlon, String endlat, String endlon, String distance, String time, String name,String startaddress, String endaddress) {
        this.startlat = startlat;
        this.id = id;
        this.startlon = startlon;
        this.endlat = endlat;
        this.endlon = endlon;
        this.distance = distance;
        this.time = time;
        this.name = name;
        this.startaddress = startaddress;
        this.endaddress = endaddress;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStartaddress() {
        return startaddress;
    }

    public void setStartaddress(String startaddress) {
        this.startaddress = startaddress;
    }

    public String getEndaddress() {
        return endaddress;
    }

    public void setEndaddress(String endaddress) {
        this.endaddress = endaddress;
    }

    public String getStartlat() {
        return startlat;
    }

    public void setStartlat(String startlat) {
        this.startlat = startlat;
    }

    public String getStartlon() {
        return startlon;
    }

    public void setStartlon(String startlon) {
        this.startlon = startlon;
    }

    public String getEndlat() {
        return endlat;
    }

    public void setEndlat(String endlat) {
        this.endlat = endlat;
    }

    public String getEndlon() {
        return endlon;
    }

    public void setEndlon(String endlon) {
        this.endlon = endlon;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
