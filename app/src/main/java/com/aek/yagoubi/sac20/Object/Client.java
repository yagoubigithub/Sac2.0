package com.aek.yagoubi.sac20.Object;

public class Client {

    private int id;
    private String name;
    private String tele;
    private String lat;
    private String lon;


    public Client(int id,String name, String tele, String lat, String lon) {
        this.id  =id;
        this.name = name;
        this.tele = tele;
        this.lat = lat;
        this.lon = lon;

    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTele(String tele) {
        this.tele = tele;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getTele() {
        return tele;
    }

    public String getLat() {
        return lat;
    }

    public String getLon() {
        return lon;
    }


}
