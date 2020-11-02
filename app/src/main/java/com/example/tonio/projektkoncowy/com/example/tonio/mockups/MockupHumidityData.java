package com.example.tonio.projektkoncowy.com.example.tonio.mockups;

import com.github.mikephil.charting.data.PieEntry;

import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.List;

public class MockupHumidityData {
    List<Float> floatList;
    List<PieEntry> pieEntries;
    double random;
    float goodData;
    float badData;
    float bestData;

    public MockupHumidityData(){
        goodData=0;
        badData=0;
        bestData=0;
        floatList=new ArrayList<>();
        pieEntries=new ArrayList<>();
        random = 1 + Math.random() * (100 - 1);
        for(int i=0;i<100;i++){
            random = 0 + Math.random() * (100 - 1);
            float f =(float)random;
            floatList.add(f);
            System.out.println("jp jd" + random);
        }

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
}
