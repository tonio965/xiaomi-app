package com.example.tonio.projektkoncowy.com.example.tonio.helpercalsses;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.tonio.projektkoncowy.DatabaseHelper;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

public class XiaomiBluetoothGattCallback extends BluetoothGattCallback {

    private static final String TAG = XiaomiBluetoothGattCallback.class.getSimpleName();
    public static final String DATA_INTENT = "xiaomi.data.value";
    private static final UUID SERVICE_UUID = UUID.fromString("226c0000-6476-4566-7562-66734470666d");
    private static final UUID SERVICE_NOTIFY_UUID =
            UUID.fromString("226caa55-6476-4566-7562-66734470666d");
    private static final UUID CLIENT_CHARACTERISTIC_CONFIG =
            UUID.fromString("00002902-0000-1000-8000-00805f9b34fb");
    private Context context;
    private DatabaseHelper dbHelper;

    public XiaomiBluetoothGattCallback(AppCompatActivity context) {
        this.context = context;
        dbHelper=new DatabaseHelper(context);
    }



    @Override
    public void onServicesDiscovered(BluetoothGatt gatt, int status) {
        BluetoothGattCharacteristic characteristic =
                gatt.getService(SERVICE_UUID).getCharacteristic(SERVICE_NOTIFY_UUID);
        gatt.setCharacteristicNotification(characteristic, true);

        BluetoothGattDescriptor descriptor = characteristic.getDescriptor(CLIENT_CHARACTERISTIC_CONFIG);

        descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
        gatt.writeDescriptor(descriptor);
    }

    @Override
    public void onCharacteristicChanged(
            BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
        String value = new String(characteristic.getValue(), StandardCharsets.UTF_8);
        Log.d(TAG, "Received data: " + value);
        String s = prepareVal(value);
        String s2 = prepareHum(value);
        String mac= gatt.getDevice().getAddress();
        try{
            int id=dbHelper.getCzujnikIdByMacAddress(mac);
            dbHelper.insertOdczyt(String.valueOf(System.currentTimeMillis()),Float.valueOf(s2),id,Float.valueOf(s));
        }
        catch (Exception e){
            System.out.println("wyjatek przy znajdowaniu mac i dodatniu do bazy");
        }
        Log.d(TAG, "temperature: " + s);
        Log.d(TAG, "humidity: " + s2);
        Intent intent = new Intent(XiaomiBluetoothGattCallback.class.getSimpleName());
        intent.putExtra(DATA_INTENT, value);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }

    @Override
    public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
        if (newState == BluetoothAdapter.STATE_CONNECTED) {
            gatt.discoverServices();
        }
    }
    private String prepareVal(String val){
        return val.substring(2,6);
    }
    private String prepareHum(String val){
        return val.substring(9,13);
    }
}
