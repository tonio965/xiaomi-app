package com.example.tonio.projektkoncowy.com.example.tonio.entities;

public class DataToDisplayInMain {
    String location;
    float temp;
    float humidity;
    int id;

    public DataToDisplayInMain(String location, float temp, float humidity, int id) {
        this.location = location;
        this.temp = temp;
        this.humidity = humidity;
        this.id=id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public float getTemp() {
        return temp;
    }

    public void setTemp(float temp) {
        this.temp = temp;
    }

    public float getHumidity() {
        return humidity;
    }

    public void setHumidity(float humidity) {
        this.humidity = humidity;
    }
}
