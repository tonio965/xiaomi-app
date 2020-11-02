package com.example.tonio.projektkoncowy.com.example.tonio.activities;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.tonio.projektkoncowy.DatabaseHelper;
import com.example.tonio.projektkoncowy.R;
import com.example.tonio.projektkoncowy.com.example.tonio.entities.Czujnik;
import com.example.tonio.projektkoncowy.com.example.tonio.entities.Odczyt;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class SingleSensorActivity extends AppCompatActivity {

    DatabaseHelper dbHelper;
    ImageView image;
    Bitmap bitmap;
    TextView name;
    TextView overall;
    List<Float> floatList;
    List<Float> floatList2;
    List<PieEntry> pieEntries;
    float goodData;
    float badData;
    float bestData;
    float goodData2;
    float badData2;
    float bestData2;



    Czujnik czujnik;
    List<Odczyt> lista;
    Odczyt lastOdczyt;
    PieChart pieChart;



    PieChart pieChartTemp;
    ImageView tempEmoji;
    TextView tempOverall;
    TextView tempValue;




    int id;
    TextView currentDateText;
    TextView currentValueText;
    ImageView currentState;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_sensor);
        Intent intent = getIntent();
        Bundle b = intent.getExtras();
        id = b.getInt("Czujnik");
        dbHelper=new DatabaseHelper(this);
        czujnik =dbHelper.getCzujnikById(id);
        overall=findViewById(R.id.overall);
        lista=fetchOdczyts();
        floatList=new ArrayList<Float>();
        floatList2=new ArrayList<Float>();
        name=findViewById(R.id.nameOfSensor);
        image = findViewById(R.id.roomImage);
        bitmap=BitmapFactory.decodeFile(czujnik.getLocation());
        image.setImageBitmap(bitmap);
        currentDateText=findViewById(R.id.currentDate);
        currentValueText=findViewById(R.id.currentValue);
        name.setText("Location: "+czujnik.getPlace());
        image.setScaleType(ImageView.ScaleType.FIT_XY);
        currentState=findViewById(R.id.currentState);
        pieChart =findViewById(R.id.pieChartSingle);

        pieChartTemp=findViewById(R.id.pieChartTemp);
        tempEmoji=findViewById(R.id.currentState2);
        tempOverall=findViewById(R.id.overall2);
        tempValue=findViewById(R.id.currentTemp);
        pieChartSetup();


        try{
            lastOdczyt=dbHelper.getLastOdczyt(String.valueOf(czujnik.getId()));
            long l = Long.parseLong(lastOdczyt.getData());
            Calendar c = Calendar.getInstance();
            c.setTimeInMillis(l);
            int month =c.get(Calendar.MONTH)+1;
            int minute=c.get(Calendar.MINUTE);
            String minut=new String();
            if(minute<10){
                minut="0"+String.valueOf(minute);
            }
            else{
                minut=String.valueOf(minute);
            }
            currentDateText.setText("Last reading: "+c.get(Calendar.HOUR)+":"+minut+" "+ c.get(Calendar.DAY_OF_MONTH)+"/"+month+"/"+c.get(Calendar.YEAR));
            currentValueText.setText("Humidity: "+String.valueOf(lastOdczyt.getValue())+"%RH");
            tempValue.setText("Temperature: "+String.valueOf(lastOdczyt.getTemperature())+"°C");
            int currentS=checkStateOfHumidity();
            int currentT=checkStateOfTemperature();
            if(currentS<0){
                currentState.setImageResource(R.drawable.badicon);
            }
            if(currentS==0){
                currentState.setImageResource(R.drawable.mediumicon);
            }
            if(currentS>0){
                currentState.setImageResource(R.drawable.goodicon);
            }
            if(currentT<0){
                tempEmoji.setImageResource(R.drawable.badicon);
            }
            if(currentT==0){
                tempEmoji.setImageResource(R.drawable.mediumicon);
            }
            if(currentT>0){
                tempEmoji.setImageResource(R.drawable.goodicon);
            }
        }
        catch (Exception e){

        }








    }

    private long caltulateDifferFromMidnight(){
        Calendar c = Calendar.getInstance();
        long now = c.getTimeInMillis();
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        long passed = now - c.getTimeInMillis();
        return passed;

    }

    private void pieChartSetup(){
        Calendar c = Calendar.getInstance();
        long now = c.getTimeInMillis();
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        long oldest=System.currentTimeMillis()-caltulateDifferFromMidnight();
        String old=String.valueOf(oldest);
        List<Odczyt>odczyty;
        String noww= String.valueOf(System.currentTimeMillis());
        Cursor filteredOdczyts=dbHelper.filterOdczytsByDate(old, noww,id);
        if(filteredOdczyts.getCount() > 0){
            odczyty = new ArrayList<Odczyt>();
            while(filteredOdczyts.moveToNext()){
                Odczyt odczyt=new Odczyt(changeToInt(filteredOdczyts.getString(0)), filteredOdczyts.getString(1),changeToFloat(filteredOdczyts.getString(2)),changeToInt(filteredOdczyts.getString(3)),changeToFloat(filteredOdczyts.getString(4)));
                floatList.add(odczyt.getValue());
                floatList2.add(odczyt.getTemperature());
            }
        }
        List<PieEntry> entries = new ArrayList<>();
        entries=getData();
        List<PieEntry> tempEntries = new ArrayList<>();
        tempEntries=getData2();

        PieDataSet dataSet = new PieDataSet(entries, "Air humidity today");
        ArrayList<Integer> colors = new ArrayList<>();

        for (int m : ColorTemplate.VORDIPLOM_COLORS)
            colors.add(m);

        for (int m : ColorTemplate.JOYFUL_COLORS)
            colors.add(m);

        for (int m : ColorTemplate.COLORFUL_COLORS)
            colors.add(m);

        for (int m : ColorTemplate.LIBERTY_COLORS)
            colors.add(m);

        for (int m : ColorTemplate.PASTEL_COLORS)
            colors.add(m);

        colors.add(ColorTemplate.getHoloBlue());

        dataSet.setColors(colors);
        PieData data = new PieData(dataSet);
        pieChart.setData(data);
        pieChart.invalidate();

        PieDataSet dataSet2 = new PieDataSet(tempEntries, "Air temperature today");
        ArrayList<Integer> colors2 = new ArrayList<>();

        for (int m : ColorTemplate.VORDIPLOM_COLORS)
            colors2.add(m);

        for (int m : ColorTemplate.JOYFUL_COLORS)
            colors2.add(m);

        for (int m : ColorTemplate.COLORFUL_COLORS)
            colors2.add(m);

        for (int m : ColorTemplate.LIBERTY_COLORS)
            colors2.add(m);

        for (int m : ColorTemplate.PASTEL_COLORS)
            colors2.add(m);

        colors2.add(ColorTemplate.getHoloBlue());

        dataSet2.setColors(colors);
        PieData data2 = new PieData(dataSet2);
        pieChartTemp.setData(data2);
        pieChartTemp.invalidate();






    }



    private void sortData(){ //przydzielam do przedzialow ponizej do grafow
        //30-65 okej
        //40-60 super
        //reszta zle
        try{
            for(int i=0;i<floatList.size();i++){
                if(floatList.get(i)<30  || floatList.get(i)>65){
                    badData++;
                    continue;
                }
                if(floatList.get(i)>=30 && floatList.get(i)<=65){
                    if(floatList.get(i)>=40 && floatList.get(i)<=60){
                        bestData++;
                        continue;
                    }
                    goodData++;
                    continue;
                }

            }
        }
        catch (Exception e){

        }

    }

    private void sortData2() {
        //20-25super
        //17-28 ok
        //reszta lipa
        try{
            for(int i=0;i<floatList2.size();i++){
                if(floatList2.get(i)<17  || floatList2.get(i)>28){
                    badData2++;
                    continue;
                }
                if(floatList2.get(i)>=17 && floatList2.get(i)<=28){
                    if(floatList2.get(i)>=20 && floatList2.get(i)<=25){
                        bestData2++;
                        continue;
                    }
                    goodData2++;
                    continue;
                }

            }
        }
        catch (Exception e){

        }

    }






    private List<Odczyt> fetchOdczyts(){
        List<Odczyt>odczyty=new ArrayList<Odczyt>();
        try{
            Cursor filteredOdczyts=dbHelper.filterOdczytsByDate("0",String.valueOf(System.currentTimeMillis()),czujnik.getId());
            if(filteredOdczyts.getCount() > 0){
                while(filteredOdczyts.moveToNext()){
                    Odczyt odczyt=new Odczyt(changeToInt(filteredOdczyts.getString(0)), filteredOdczyts.getString(1),changeToFloat(filteredOdczyts.getString(2)),changeToInt(filteredOdczyts.getString(3)),changeToFloat(filteredOdczyts.getString(4)));
                    odczyty.add(odczyt);
                }
            }
        }
        catch (Exception e){

        }
        return odczyty;
    }
    private float changeToFloat(String input){
        return Float.parseFloat(input);
    }
    private int changeToInt(String input){
        return Integer.parseInt(input);
    }

    public List getData(){
        pieEntries=new ArrayList<>();
        int length=floatList.size();
        sortData();
        System.out.println("good data: "+goodData);
        System.out.println("badData:"+badData);
        System.out.println("bestData:"+bestData);
        pieEntries.add(new PieEntry(bestData,"fantastic"));
        pieEntries.add(new PieEntry(goodData,"good"));
        pieEntries.add(new PieEntry(badData,"bad"));

        return pieEntries;
    }
    private List getData2() {
        pieEntries=new ArrayList<>();
        int length=floatList.size();
        sortData2();
        System.out.println("good data: "+goodData2);
        System.out.println("badData:"+badData2);
        System.out.println("bestData:"+bestData2);
        pieEntries.add(new PieEntry(bestData2,"fantastic"));
        pieEntries.add(new PieEntry(goodData2,"good"));
        pieEntries.add(new PieEntry(badData2,"bad"));

        return pieEntries;
    }



    private int checkStateOfHumidity(){
        System.out.println("checking state of humidity");
        float last=lastOdczyt.getValue();
        for(int i=0;i<1;i++){
            if(last<30  || last>65){
                overall.setText("humidity: bad");
                return -1;
            }
            if(last>=30 && last<=65){
                if(last>=40 && last<=60){
                    overall.setText("humidity: fantastic");
                    return 1;
                }
                overall.setText("humidity: medium");
                return 0;
            }

        }
        return 0;
    }

    private int checkStateOfTemperature(){
        //20-25super
        //17-28 ok
        //reszta lipa
        float last=lastOdczyt.getTemperature();
        for(int i=0;i<1;i++){
            if(last<17  || last>28){
                tempOverall.setText("temperature: bad");
                return -1;
            }
            if(last>=17 && last<=28){
                if(last>=20 && last<=25){
                    tempOverall.setText("temperature: fantastic");
                    return 1;
                }
                tempOverall.setText("temperature: medium");
                return 0;
            }

        }
        return 0;
    }
}
