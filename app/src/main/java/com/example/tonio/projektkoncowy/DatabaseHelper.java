package com.example.tonio.projektkoncowy;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.tonio.projektkoncowy.com.example.tonio.entities.Czujnik;
import com.example.tonio.projektkoncowy.com.example.tonio.entities.Odczyt;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static int data;
    public static final String DATABASE_NAME = "inzynierka2.db";
    public static final String TABLE_NAME1 = "odczyty_table";
    public static final String TABLE_NAME2 = "czujniki_table";
    public static final String TABLE_NAME3 = "plany_table";
    public static final String id = "id";
    public static final String date = "date";
    public static final String value = "value";
    public static final String temperature = "temperature";
    public static final String macAddress = "macAddress";
    public static final String sensorId = "sensorId";
    public static final String location = "location";
    public static final String place="place";
    public static final String TABLE_NAME4 = "pins_table";
    public static final String coordinateX = "coordinateX";
    public static final String coordinateY = "coordinateY";

    public static final String roomScheme="roomScheme";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table "+ TABLE_NAME2 +" (ID INTEGER PRIMARY KEY AUTOINCREMENT, location TEXT,place TEXT, macAddress TEXT )");
        db.execSQL("create table "+ TABLE_NAME1 +" (ID INTEGER PRIMARY KEY AUTOINCREMENT, date TEXT, value FLOAT,sensorId INTEGER, temperature FLOAT)");
        db.execSQL("create table "+ TABLE_NAME3 +" (ID INTEGER PRIMARY KEY AUTOINCREMENT, location TEXT)");
        db.execSQL("create table "+ TABLE_NAME4 +"(ID INTEGER PRIMARY KEY AUTOINCREMENT, coordinateX INTEGER, coordinateY INTEGER)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME1);
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME2);
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME3);
        onCreate(db);

    }

    public String getLastRoomScheme(){
        SQLiteDatabase db = this.getWritableDatabase();
        String localization="x";
        Cursor res = db.rawQuery("select * "+" from "+TABLE_NAME3, null);
        try{
            while(res.moveToNext()){
                localization =res.getString(1);
            }
            return localization;
        }
        catch (Exception e ){
            System.out.println("pusta baza");
        }
        return localization;
    }

    public boolean setRoomScheme(String roomScheme){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(this.location,roomScheme);
        long success = db.insert(TABLE_NAME3,null,contentValues);
        System.out.println("success: "+success);
        if(success == -1){
            return false;
        }
        else{
            System.out.println("added room scheme: "+roomScheme);
            return true;
        }


    }

    public List<Odczyt>odczytyZjednego(String id){
        List<Odczyt>odczyty= new ArrayList<>();



        return odczyty;

    }


    public Cursor getAllOdczyt(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from "+TABLE_NAME1, null);
        return res;
    }

    public List<Czujnik> getAllCzujnik(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from "+TABLE_NAME2, null);
        List<Czujnik> odczyty = new ArrayList<Czujnik>();
        while(res.moveToNext()){
            Czujnik o=new Czujnik(changeToInt(res.getString(0)), res.getString(1),res.getString(2),
                    res.getString(3));
            System.out.println("id: "+o.getId()+" place:"+o.getPlace()+" mac:"+o.getMacAddress());
            odczyty.add(o);
        }
        return odczyty;
    }
    public List<String> getNamesOfCzujniks(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select "+ place +" from "+TABLE_NAME2, null);
        List<String> odczyty = new ArrayList<String>();
        while(res.moveToNext()){
            String s= res.getString(0);
            odczyty.add(s);
        }
        return odczyty;
    }

    private float changeToFloat(String input){
//        System.out.println("float " + Float.parseFloat(input));
        return Float.parseFloat(input);
    }
    private int changeToInt(String input){
//        System.out.println("int: "+ Integer.parseInt(input));
        return Integer.parseInt(input);
    }


    public Odczyt getLastOdczyt(String fromCzujnik){
        int parse=Integer.parseInt(fromCzujnik);
        System.out.println("int parse: "+parse);
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res =db.rawQuery("select * from "+ TABLE_NAME1 +" where "+sensorId+" = "+ parse+" order by "+date+" asc",null);
        Odczyt odczyt=null;
        while(res.moveToNext()){
            odczyt=new Odczyt(changeToInt(res.getString(0)), res.getString(1),changeToFloat(res.getString(2)),
                    changeToInt(res.getString(3)),changeToFloat(res.getString(4)));
        }
        return odczyt;
    }

    public Cursor filterOdczytsByDate(String from, String to, int sensor){ //zminic na ktory czujnik jeszcze
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from "+TABLE_NAME1+" where "+date+" between "+from+" and "+to+" and "+sensorId+" = "+sensor,null);
        return res;
    }

    public Integer getSensorByName(String given){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select "+id+" from "+TABLE_NAME2+" where "+place+" = "+given,null);
        int i=0;
        while(res.moveToNext()){
            i = Integer.parseInt(res.getString(0));
        }
        return i;
    }

    public Integer deleteAllOdczyts(){
        String s= "0";
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME1, "id != ?", new String[] {s});
    }
    public Integer deleteAllCzujniks(){
        String s= "0";
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME2, "id != ?", new String[] {s});
    }

    public Integer deleteOdczyt(String toDelete){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME1, "id = ?",new String[] {toDelete});
    }
    public Integer getCzujnikIdByMacAddress(String mac){
        SQLiteDatabase db = this.getWritableDatabase();
        List<Czujnik> lista=getAllCzujnik();
        int id=0;
        for(Czujnik o : lista){
            if(o.getMacAddress().equals(mac)){
                id=o.getId();
            }
        }
        return id;

    }

    public Czujnik getCzujnikById(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res =db.rawQuery("select * from "+ TABLE_NAME2 +" where id = "+id,null);
        Czujnik o=null;
        while(res.moveToNext()){
            o=new Czujnik(changeToInt(res.getString(0)), res.getString(1),res.getString(2),res.getString(3));
        }
        return o;
    }

    public boolean insertOdczyt(String data, float value, int sensorId, float temperature){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(date,data);
        contentValues.put(this.value,value);
        contentValues.put(this.sensorId,sensorId);
        contentValues.put(this.temperature,temperature);
        long success = db.insert(TABLE_NAME1,null,contentValues);
        System.out.println("success: "+success);
        if(success == -1){
            return false;
        }
        else{
            return true;
        }


    }

    public boolean insertCzujnik(String location, String place, String macAddress){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        System.out.println("location in db:"+location);
        System.out.println("place in db:"+place);
        contentValues.put(this.location,location);
        contentValues.put(this.place,place);
        contentValues.put(this.macAddress,macAddress);
        System.out.println("location : "+location);
        System.out.println("place : "+place);
        System.out.println("mac : "+macAddress);
        long success = db.insert(TABLE_NAME2,null,contentValues);
        System.out.println("success: "+success);
        if(success == -1){
            return false;
        }
        else{
            return true;
        }
    }
    public Integer deleteCzujnik(String toDelete){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME2, "id = ?",new String[] {toDelete});
    }
}
