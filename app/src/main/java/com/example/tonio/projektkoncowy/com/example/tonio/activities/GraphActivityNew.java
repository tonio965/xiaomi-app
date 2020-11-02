package com.example.tonio.projektkoncowy.com.example.tonio.activities;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import com.example.tonio.projektkoncowy.R;
import com.example.tonio.projektkoncowy.com.example.tonio.mockups.MockupHumidityData;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;

public class GraphActivityNew extends AppCompatActivity {

    PieChart pieChart;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph_new);
        pieChart = (PieChart) findViewById(R.id.pieChart);
        pieChartSetup();
    }


    private void pieChartSetup(){
        //30-65 okej
        //40-60 super
        //reszta zle
        List<PieEntry> entries = new ArrayList<>();
        entries=new MockupHumidityData().getData();
        PieDataSet dataSet = new PieDataSet(entries, "Air humidity today");
        ArrayList<Integer> colors = new ArrayList<>();

        for (int c : ColorTemplate.VORDIPLOM_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.JOYFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.COLORFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.LIBERTY_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.PASTEL_COLORS)
            colors.add(c);

        colors.add(ColorTemplate.getHoloBlue());

        dataSet.setColors(colors);
        PieData data = new PieData(dataSet);
        pieChart.setData(data);
        pieChart.invalidate();

    }
}
