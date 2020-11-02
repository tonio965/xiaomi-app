package com.example.tonio.projektkoncowy.com.example.tonio.activities;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import com.example.tonio.projektkoncowy.R;
import com.github.mikephil.charting.charts.LineChart;

public class LineChartActivity extends AppCompatActivity {

    LineChart lineChart;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_line_chart);
        lineChart=findViewById(R.id.lineChart);
        lineChartSetup();
    }

    private void lineChartSetup(){

    }
}
