package com.example.tonio.projektkoncowy.com.example.tonio.mockups;

import java.util.ArrayList;
import java.util.List;

public class GenerateOdczytMockups {
    double random;
    List<Float> floatList;

    public GenerateOdczytMockups(){
        floatList=new ArrayList<>();
        random = 1 + Math.random() * (100 - 1);
        for(int i=0;i<100;i++){
            random = 0 + Math.random() * (100 - 1);
            float f =(float)random;
            floatList.add(f);
        }

    }

    public List<Float> getFloatList() {
        return floatList;
    }
}
