package com.example.tonio.projektkoncowy.com.example.tonio.activities;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.tonio.projektkoncowy.com.example.tonio.entities.Czujnik;
import com.example.tonio.projektkoncowy.DatabaseHelper;
import com.example.tonio.projektkoncowy.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CalendarsActivity extends AppCompatActivity {

    CalendarView calendar1;
    CalendarView calendar2;
    Button calendarsButton;
    Spinner sensorSpinner;
    List<Czujnik> sensors;
    List<String> sensorNames;
    long date1,date2;
    DatabaseHelper dbHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendars);
        calendar1=findViewById(R.id.calendarView);
        calendarsButton=findViewById(R.id.calendarsButton);
        calendar2=findViewById(R.id.calendarView2);
        sensorSpinner=findViewById(R.id.sensorSpinner);
        dbHelper=new DatabaseHelper(this);
        sensors=dbHelper.getAllCzujnik();
        sensorNames=new ArrayList<>();
        for(int i=0;i<sensors.size();i++){
            sensorNames.add(sensors.get(i).getPlace());
        }
        for(String s: sensorNames){
            System.out.println("sensor: "+s);
        }
        final ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
                this,R.layout.sensor_spinner_value,sensorNames);

        spinnerArrayAdapter.setDropDownViewResource(R.layout.sensor_spinner_value);
        sensorSpinner.setAdapter(spinnerArrayAdapter);

        calendar1.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                int newmon=month+1;
                String myDate=year+"/"+newmon+"/"+dayOfMonth;
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
                Date date = null;
                try {
                    date = sdf.parse(myDate);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                long millis = date.getTime();
                date1=millis;

                Toast.makeText(getApplicationContext(), String.valueOf(millis), Toast.LENGTH_LONG).show();
            }
        });
        calendar2.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                int newmon=month+1;
                String myDate=year+"/"+newmon+"/"+dayOfMonth;
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
                Date date = null;
                try {
                    date = sdf.parse(myDate);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                long millis = date.getTime();
                date2=millis;
                Toast.makeText(getApplicationContext(), String.valueOf(millis), Toast.LENGTH_LONG).show();
            }
        });


        buttonClick();
    }





    private void buttonClick(){
        calendarsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String startDate = String.valueOf(date1);
                String endDate = String.valueOf(date2);
                int id=0;
                for(int i=0;i<sensors.size();i++){
                    if(sensorSpinner.getSelectedItem().toString()==sensors.get(i).getPlace()){
                        id=sensors.get(i).getId();
                    }
                }

                System.out.println("start: "+startDate);
                System.out.println("end: "+endDate);
                System.out.println("sensorid: "+id);
                Intent intent = new Intent(CalendarsActivity.this, HistoricDataActivity.class);
                intent.putExtra("start",startDate);
                intent.putExtra("end",endDate);
                intent.putExtra("sensorId",id);
                startActivity(intent);
            }
        });
    }
}
