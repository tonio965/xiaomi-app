package com.example.tonio.projektkoncowy.com.example.tonio.entities;

import java.util.Date;

public class Odczyt {

//db.execSQL("create table "+ TABLE_NAME2 +" (ID INTEGER PRIMARY KEY AUTOINCREMENT, location TEXT)");
//db.execSQL("create table "+ TABLE_NAME1 +" (ID INTEGER PRIMARY KEY AUTOINCREMENT, date INTEGER, value FLOAT,sensorId INTEGER)");
    int id;
    String data;
    float value;
    int sensorId;
    float temperature;

    public Odczyt(int id, String data, float value, int sensorId, float temperature) {
        this.id = id;
        this.data = data;
        this.value = value;
        this.sensorId = sensorId;
        this.temperature=temperature;
    }

    public float getTemperature() {
        return temperature;
    }

    public void setTemperature(float temperature) {
        this.temperature = temperature;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }

    public int getSensorId() {
        return sensorId;
    }

    public void setSensorId(int sensorId) {
        this.sensorId = sensorId;
    }
    public String toString(){
        long l = Long.parseLong(getData());
        Date d = new Date(l);
        return "odczyt id: "+getId()+" data: "+getData()+" value:"+getValue()+" sensorid: "+getSensorId();
    }
}
