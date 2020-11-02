package com.example.tonio.projektkoncowy.com.example.tonio.activities;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.CalendarView;
import android.widget.Toast;

import com.example.tonio.projektkoncowy.R;

public class CalendarActivity extends AppCompatActivity {

    CalendarView calendar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        calendar=findViewById(R.id.calendar);
        calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                String dom = String.valueOf(dayOfMonth);
                String moy = String.valueOf(month+1);
                String curr=dom+"/"+moy+"/"+year;

                Toast.makeText(getApplicationContext(), curr, Toast.LENGTH_LONG).show();
            }
        });
    }
}
