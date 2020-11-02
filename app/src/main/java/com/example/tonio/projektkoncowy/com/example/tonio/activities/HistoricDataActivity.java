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

public class HistoricDataActivity extends AppCompatActivity {

    DatabaseHelper dbHelper;
    ImageView image;
    Bitmap bitmap;
    TextView name;
    TextView overall;
    TextView tfDates;
    List<Float> floatList;
    List<Float> floatList2;
    List<PieEntry> pieEntries;
    float goodData;
    float badData;
    float bestData;
    float goodData2;
    float badData2;
    float bestData2;


    ///fetchuje sie lista teraz wypadaloby zrobic:
    //to aby te dane co dostaly wpadaly jakos na te grafy
    //pokazzywac aktualny stan powietrza (wyswietalie ostatniego rekordu)
    //zaimplementowac historyczne wyswietlanie po daciezz

    Czujnik czujnik;
    List<Odczyt> lista;
    Odczyt lastOdczyt;
    PieChart pieChart;
    int id;
    String startDate;
    String endDate;
    TextView currentDateText;
    TextView currentValueText;
    TextView currentValueText2;
    ImageView currentState;

    PieChart pieChartTemp;
    ImageView tempEmoji;
    TextView tempOverall;
    TextView tempValue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historic_data);
        Intent intent = getIntent();
        Bundle b = intent.getExtras();
        tfDates=findViewById(R.id.tfDates);
        id = b.getInt("sensorId");
        startDate=b.getString("start");
        endDate=b.getString("end");

        long startLong = Long.parseLong(startDate);
        long endLong = Long.parseLong(endDate);
        Calendar cal1 = Calendar.getInstance();
        cal1.setTimeInMillis(startLong);
        Calendar cal2 = Calendar.getInstance();
        cal2.setTimeInMillis(endLong);
        int year1 = cal1.get(Calendar.YEAR);
        int month1= cal1.get(Calendar.MONTH)+1;
        int day1 = cal1.get(Calendar.DAY_OF_MONTH);

        int year2 = cal2.get(Calendar.YEAR);
        int month2= cal2.get(Calendar.MONTH)+1;
        int day2 = cal2.get(Calendar.DAY_OF_MONTH);

        tfDates.setText("From: "+day1+"/"+month1+"/"+year1+ " to: "+day2+"/"+month2+"/"+year2);
        dbHelper=new DatabaseHelper(this);
        czujnik =dbHelper.getCzujnikById(id);
        overall=findViewById(R.id.overall);
        lista=fetchOdczyts();
        System.out.println("lista len: "+lista.size());
        floatList=new ArrayList<Float>();
        floatList2=new ArrayList<Float>();
        name=findViewById(R.id.nameOfSensor);
        image = findViewById(R.id.roomImage);
        bitmap=BitmapFactory.decodeFile(czujnik.getLocation());
        image.setImageBitmap(bitmap);
        currentDateText=findViewById(R.id.currentDate);
        currentValueText=findViewById(R.id.currentValue);
        currentValueText2=findViewById(R.id.currentTemp);
        name.setText("Location: "+czujnik.getPlace());
        image.setScaleType(ImageView.ScaleType.FIT_XY);
        currentState=findViewById(R.id.currentState);
        pieChart = (PieChart) findViewById(R.id.pieChartSingle);

        pieChartTemp=findViewById(R.id.pieChartTemp);
        tempEmoji=findViewById(R.id.currentState2);
        tempOverall=findViewById(R.id.overall2);
        tempValue=findViewById(R.id.currentTemp);
        pieChartSetup();
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



    private void pieChartSetup(){
        for(Odczyt o : lista){
            floatList.add(o.getValue());
            floatList2.add(o.getTemperature());
        }
        List<PieEntry> entries = new ArrayList<>();
        entries=getData();
        List<PieEntry> tempEntries = new ArrayList<>();
        tempEntries=getData2();

        PieDataSet dataSet = new PieDataSet(entries, "Air humidity");
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
        checkStateOfHumidity();
        checkStateOfTemperature();






    }


    private void sortData(){ //przydzielam do przedzialow ponizej do grafow
        //30-65 okej
        //40-60 super
        //reszta zle
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






    private List<Odczyt> fetchOdczyts(){
        Cursor filteredOdczyts=dbHelper.filterOdczytsByDate(startDate,endDate,czujnik.getId());
        List<Odczyt>odczyty=new ArrayList<Odczyt>();
        if(filteredOdczyts.getCount() > 0){
            while(filteredOdczyts.moveToNext()){
                Odczyt odczyt=new Odczyt(changeToInt(filteredOdczyts.getString(0)), filteredOdczyts.getString(1),changeToFloat(filteredOdczyts.getString(2)),changeToInt(filteredOdczyts.getString(3)),changeToFloat(filteredOdczyts.getString(4)));
                odczyty.add(odczyt);
            }


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

    private float mean(List<Float> list){
        int amount=list.size();
        float sum=0;
        for(float f: list){
            sum+=f;
        }
        float mean = sum/amount;
        return sum/amount;

    }
    private int checkStateOfHumidity(){
        System.out.println("checking state of humidity");
        float last=mean(floatList);
        String toDisplay =String.valueOf(last);
        currentValueText.setText(toDisplay.substring(0,5)+"%RH");
        for(int i=0;i<1;i++){
            if(last<30  || last>65){
                overall.setText("mean humidity: bad");
                return -1;
            }
            if(last>=30 && last<=65){
                if(last>=40 && last<=60){
                    overall.setText("mean humidity: fantastic");
                    return 1;
                }
                overall.setText("mean humidity: medium");
                return 0;
            }

        }
        return 0;
    }

    private int checkStateOfTemperature(){
        //20-25super
        //17-28 ok
        //reszta lipa
        float last=mean(floatList2);
        String toDisplay =String.valueOf(last);
        currentValueText2.setText(toDisplay.substring(0,5)+"°C");
        for(int i=0;i<1;i++){
            if(last<17  || last>28){
                tempOverall.setText("mean temp: bad");
                return -1;
            }
            if(last>=17 && last<=28){
                if(last>=20 && last<=25){
                    tempOverall.setText("mean temp: fantastic");
                    return 1;
                }
                tempOverall.setText("mean temp: medium");
                return 0;
            }

        }
        return 0;
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

}
