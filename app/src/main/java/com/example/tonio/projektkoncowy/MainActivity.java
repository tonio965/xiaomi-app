package com.example.tonio.projektkoncowy;

import androidx.recyclerview.widget.LinearLayoutManager;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.tonio.projektkoncowy.com.example.tonio.activities.AddNewSupp;
import com.example.tonio.projektkoncowy.com.example.tonio.activities.CalendarsActivity;
import com.example.tonio.projektkoncowy.com.example.tonio.activities.ListOfSensors;
import com.example.tonio.projektkoncowy.com.example.tonio.entities.Czujnik;
import com.example.tonio.projektkoncowy.com.example.tonio.entities.DataToDisplayInMain;
import com.example.tonio.projektkoncowy.com.example.tonio.entities.Odczyt;
import com.example.tonio.projektkoncowy.com.example.tonio.helpercalsses.RealFilePathUtil;
import com.example.tonio.projektkoncowy.com.example.tonio.helpercalsses.XiaomiBluetoothGattCallback;
import com.example.tonio.projektkoncowy.com.example.tonio.mockups.GenerateOdczytMockups;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.recyclerview.widget.RecyclerView;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity implements MyRecyclerViewAdapter.ItemClickListener {
     //stworzyc jakas klase ktora bedzie z bazy czytac wszystko i wybierac tam gdzie dzisiejsza data i wrzucac do holdera bec


    // zrobione activity z kalendarzami ktore juz robi po kliknieciu daty, teraz przefiltrowac i historyczne same sie zrobia

    List<Czujnik> startList;
    List<Odczyt> lastOdczyts;
    ImageView roomScheme;
    Button calendarButton;
    Date currentDate;
    private int PICK_IMAGE_REQUEST = 1;
    Button roomPlanAdd;
    FloatingActionButton fab;
    List<Float>lista1;
    List<Float>lista2;
    DatabaseHelper dbHelper;
    Button discoverBT;
    Button addNewCzuj2;
//    Button fetchButton;
    Button fetchAllCzuj;

    private RecyclerView recyclerView;
    private RecyclerView.Adapter recyclerViewAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private MyRecyclerViewAdapter adapter;
    private static final String DEVICE_NAME = "MJ_HT_V1";
    private static int SELECT_PICTURE = 1;
    public List<DataToDisplayInMain> listToDisplay;
    private BluetoothDevice myDevice;
    private BluetoothAdapter bluetoothAdapter;
    private BroadcastReceiver onNotice;
    private String deviceMac;
    Context con;


    GenerateOdczytMockups g1;
    GenerateOdczytMockups g2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dbHelper=new DatabaseHelper(this);
        fab = findViewById(R.id.fab);
        roomScheme=findViewById(R.id.houseScheme);
        recyclerView=findViewById(R.id.mainRecycler);
        layoutManager=new LinearLayoutManager(this);
        startList=dbHelper.getAllCzujnik();
        con=getApplicationContext();





        startList = new ArrayList<>();
        lastOdczyts = new ArrayList<>();

        startList=dbHelper.getAllCzujnik();
        for(int i=0;i<startList.size();i++){
            try{
                lastOdczyts.add(dbHelper.getLastOdczyt(String.valueOf(startList.get(i).getId())));
            }
            catch (Exception e){

            }
        }
        listToDisplay = new ArrayList<>();

        for(int i=0;i<startList.size();i++){
            try{
                String location = startList.get(i).getPlace();
                float temp = lastOdczyts.get(i).getTemperature();
                float humid = lastOdczyts.get(i).getValue();
                int id =startList.get(i).getId();
                DataToDisplayInMain d = new DataToDisplayInMain(location,temp,humid,id);
                listToDisplay.add(d);
            }
            catch (Exception e){

            }
        }

        RecyclerView recyclerView = findViewById(R.id.mainRecycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MyRecyclerViewAdapter(this, listToDisplay);
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);
        g1=new GenerateOdczytMockups();
        lista1=g1.getFloatList();
        g2 = new GenerateOdczytMockups();
        lista2=g2.getFloatList();
        newCzuj2();
        roomAdd();
        fetchAllCzujniks();
        openCalendars();
        discoverbt();
        startDaemon();
        System.out.println(dbHelper.getDatabaseName());
        currentDate = new Date();
        Odczyt o;

        try{
            String parsedAddress= dbHelper.getLastRoomScheme();
            System.out.println("address: "+parsedAddress);
            Bitmap myBitmap = BitmapFactory.decodeFile(parsedAddress);
            roomScheme.setImageBitmap(myBitmap);
        }
        catch (Exception e){
            System.out.println("uri exception");
        }
        final BluetoothManager bluetoothManager =
                (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        this.bluetoothAdapter = bluetoothManager.getAdapter();

        this.bluetoothAdapter.startLeScan(
                (bluetoothDevice, i, bytes) -> {
                    if (DEVICE_NAME.equals(bluetoothDevice.getName())) {
                        myDevice = bluetoothDevice;
                        deviceMac=myDevice.getAddress();
                    }
                });
        this.bluetoothAdapter.stopLeScan((bluetoothDevice, i, bytes) -> {});

//        b.setOnClickListener(
//                view ->
//                        runOnUiThread(
//                                () -> {
//                                    myDevice.connectGatt(
//                                            getApplicationContext(), true, new XiaomiBluetoothGattCallback(this));
//                                }));

        onNotice =
                new BroadcastReceiver() {
                    @Override
                    public void onReceive(Context context, Intent intent) {
//                        responseText.setText(intent.getStringExtra(XiaomiBluetoothGattCallback.DATA_INTENT));
                        Toast.makeText(getApplicationContext(),intent.getStringExtra(XiaomiBluetoothGattCallback.DATA_INTENT),Toast.LENGTH_SHORT).show();
                    }
                };

    }

    private void startDaemon() {
        fab.setOnClickListener(
                view ->
                        runOnUiThread(
                                () -> {
                                    myDevice.connectGatt(
                                            getApplicationContext(), true, new XiaomiBluetoothGattCallback(this));
                                }));
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.dropdown_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.mainMenu:
                finish();
                return true;

            case R.id.browseSensors:
                Intent intent = new Intent(MainActivity.this, ListOfSensors.class);
                startActivity(intent);
                return true;

            case R.id.add_sensor:
                Intent intent2 = new Intent(MainActivity.this, AddNewSupp.class);
                startActivity(intent2);
                return true;

            case R.id.addRoomScheme:
                Intent intent3 = new Intent();
                intent3.setType("image/*");
                intent3.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent3, "Select Picture"), SELECT_PICTURE);
                return true;

            case R.id.showCalendars:
                Intent i = new Intent(MainActivity.this, CalendarsActivity.class);
                startActivity(i);
                return true;

        }


        return super.onOptionsItemSelected(item);
    }

    private void roomAdd(){


    }

    private void discoverbt() {

    }

    protected void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(onNotice);
    }


    private void openCalendars(){ //to tez jest potrzebne

    }
    public void newCzuj2(){  //to jest potrzebne

    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_PICTURE) {
                // Get the url from data
                Uri selectedImageUri = data.getData();
                String myPath = RealFilePathUtil.getPath(this,selectedImageUri);
                System.out.println("path from uri: "+myPath);
                if (null != selectedImageUri) {
                    // Get the path from the Uri
                    boolean b =dbHelper.setRoomScheme(myPath);
                    if(b)
                        Toast.makeText(MainActivity.this,"success added"+ myPath,Toast.LENGTH_SHORT).show();
                    else{
                        Toast.makeText(MainActivity.this,"failed to add",Toast.LENGTH_SHORT).show();
                    }

//                    Toast.makeText(MainActivity.this,path,Toast.LENGTH_SHORT).show();
                }
            }
        }
    }





    public void fetchAllCzujniks(){


    }

    private float changeToFloat(String input){
//        System.out.println("float " + Float.parseFloat(input));
        return Float.parseFloat(input);
    }
    private int changeToInt(String input){
//        System.out.println("int: "+ Integer.parseInt(input));
        return Integer.parseInt(input);
    }


    @Override
    public void onItemClick(View view, int position) {
//        Toast.makeText(this, "You clicked " + adapter.getItem(position) + " on row number " + position, Toast.LENGTH_SHORT).show();
    }
}
