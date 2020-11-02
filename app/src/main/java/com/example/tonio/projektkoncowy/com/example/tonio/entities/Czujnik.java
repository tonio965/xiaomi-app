package com.example.tonio.projektkoncowy.com.example.tonio.entities;

public class Czujnik {
    int id;
    String location;
    String place;
    String macAddress;

    public Czujnik(int id, String location, String place, String macAddress) {
        this.id=id;
        this.location = location;
        this.place = place;
        this.macAddress=macAddress;
    }


    public String getMacAddress() {
        return macAddress;
    }

    public void setMacAddress(String macAddress) {
        this.macAddress = macAddress;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    public String toString(){
        return "czujnik id: "+this.getId()+" location: "+this.getLocation()+" place: "+this.getPlace();
    }
}
