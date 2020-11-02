package com.example.tonio.projektkoncowy.com.example.tonio.activities;

import android.content.Context;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.tonio.projektkoncowy.com.example.tonio.adapters.CzujnikAdapter;
import com.example.tonio.projektkoncowy.DatabaseHelper;
import com.example.tonio.projektkoncowy.R;
import com.example.tonio.projektkoncowy.com.example.tonio.entities.Czujnik;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ListOfSensors extends AppCompatActivity implements CzujnikAdapter.OnNoteListener {

    List<Czujnik> lista;
    RecyclerView recyclerView;
    CzujnikAdapter adapter;
    DatabaseHelper dbHelper;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_of_sensors);
        dbHelper=new DatabaseHelper(this);
        recyclerView = findViewById(R.id.recyclerView);
        context=this;
        lista = dbHelper.getAllCzujnik();
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setItemViewCacheSize(10000);
        recyclerView.setDrawingCacheEnabled(true);
        recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        adapter = new CzujnikAdapter(context);
        recyclerView.setAdapter(adapter);
        recyclerOnClick();
    }

    private void recyclerOnClick(){

    }

    @Override
    public void onNoteClick(int pos) {
        Intent i = new Intent(this, CalendarActivity.class);
        startActivity(i);
    }
}
