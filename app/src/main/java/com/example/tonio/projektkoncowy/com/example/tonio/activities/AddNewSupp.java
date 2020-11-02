package com.example.tonio.projektkoncowy.com.example.tonio.activities;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;


import com.example.tonio.projektkoncowy.DatabaseHelper;
import com.example.tonio.projektkoncowy.MainActivity;
import com.example.tonio.projektkoncowy.R;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;


import static android.os.Environment.getExternalStoragePublicDirectory;

public class AddNewSupp extends AppCompatActivity {
    EditText interval;
    Bitmap bitmap;
    String macAddress;
    String imagePath;
    ImageView iv;
    String pathToFile;
    Button takePicButton;
    Button assign;
    EditText duration;
    DatabaseHelper dbHelper;
    EditText doseInput;
    EditText nameInput;
    Button saveButton;
    ArrayList<String> dates;
    DateFormat dateFormat;
    Date currentDate;
    Button myButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_supp);
        dates = new ArrayList<>();
        dbHelper=new DatabaseHelper(this);
        imagePath=new String();
        assign=findViewById(R.id.assignButton);
        interval = findViewById(R.id.intervalInput);
        takePicButton=findViewById(R.id.button23);
        iv=findViewById(R.id.imageView);
        nameInput=findViewById(R.id.nameInput);

        saveButton = findViewById(R.id.button2);
        if(Build.VERSION.SDK_INT>=23){
            requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE},2);
        }
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String place = nameInput.getText().toString();
                System.out.println("path w save: "+pathToFile);
                System.out.println("place: "+place);
                boolean inserted= dbHelper.insertCzujnik(pathToFile,place,macAddress);
                if(inserted==true)
                    Toast.makeText(AddNewSupp.this,"success",Toast.LENGTH_SHORT).show();
                if(inserted==false)
                    Toast.makeText(AddNewSupp.this,"fail",Toast.LENGTH_SHORT).show();
                startActivity(new Intent(AddNewSupp.this , MainActivity.class));

            }
        });
        assign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(AddNewSupp.this, NewBluetooth.class);
                startActivityForResult(i,1);
            }
        });
        takePicButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("take pic activity open");
                dispatch();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK){
            if(requestCode==1){
                bitmap = BitmapFactory.decodeFile(pathToFile);
                try{
                    macAddress= data.getStringExtra("result");
                    bitmap = BitmapFactory.decodeFile(pathToFile);
                    Toast.makeText(this, macAddress,Toast.LENGTH_SHORT).show();
                }
                catch(Exception e){
                    System.out.println("exception");
                }
                iv.setImageBitmap(bitmap);
            }
        }
    }

    private void dispatch(){
        Intent takePic = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if(takePic.resolveActivity(getPackageManager())!=null){
            File photoFile = null;
            try{
                photoFile=createPhotoFile();
                if(photoFile!=null){
                    pathToFile = photoFile.getAbsolutePath();
                    imagePath=pathToFile;
                    System.out.println("path to file in dispatch: "+pathToFile);
                    Uri photoURI = FileProvider.getUriForFile(AddNewSupp.this, "com.thecodecity.cameraandroid.fileprovider",photoFile);
                    takePic.putExtra(MediaStore.EXTRA_OUTPUT,photoURI);
                    startActivityForResult(takePic,1);
                }

            }
            catch (Exception e){

            }
        }

    }
    private File createPhotoFile(){
        String name = "zdjecieZInzynierki"+System.currentTimeMillis();
        File storageDir = getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);

        File image=null;
        try {
            image = File.createTempFile(name,".jpg",storageDir);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return image;
    }
}
