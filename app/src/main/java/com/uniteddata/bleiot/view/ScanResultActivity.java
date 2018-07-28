package com.uniteddata.bleiot.view;

import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import com.clj.fastble.BleManager;
import com.clj.fastble.data.BleDevice;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.uniteddata.bleiot.R;

import java.util.List;
import java.util.UUID;

public class ScanResultActivity extends AppCompatActivity {

    private int frequencyIndex = -1;
    private int kuopinIndex = -1;
    private int adrIndex = -1;
    private String[] frequencys = new String[]{"6", "8", "10","12","14"};
    private String[] kuopins = new String[]{"00", "01", "02","03","04","05"};
    private String[] adrs = new String[]{"00", "01"};

    private EditText etInput;
    private EditText etOutput;
    private EditText hour;
    private EditText minute;
    private EditText second;
    private BleDevice bleDevice;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_result);

        etInput = findViewById(R.id.input);
        etOutput = findViewById(R.id.output);
        hour = findViewById(R.id.hour);
        minute = findViewById(R.id.minute);
        second = findViewById(R.id.second);
        bleDevice = getIntent().getParcelableExtra("ADDRESS");
        openNotify();
    }

    private void openNotify() {
        BluetoothGatt bluetoothGatt = BleManager.getInstance().getBluetoothGatt(bleDevice);
        List<BluetoothGattService> services = bluetoothGatt.getServices();
        List<BluetoothGattCharacteristic> characteristics = services.get(0).getCharacteristics();
        UUID uuid = characteristics.get(0).getService().getUuid();//参数1
        UUID uuid1 = characteristics.get(0).getUuid();//参数2

//        BleManager.getInstance().notify(bleDevice,);
    }

    /**
     * 发射频率
     * @param v
     */
    public void configFrequency(View v) {

        new QMUIDialog.CheckableDialogBuilder(this)
                .setCheckedIndex(frequencyIndex)
                .addItems(frequencys, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        frequencyIndex = which;
                        dialog.dismiss();
                    }
                })
                .show();
    }

    public void configKuoPin(View v) {

        new QMUIDialog.CheckableDialogBuilder(this)
                .setCheckedIndex(kuopinIndex)
                .addItems(kuopins, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        kuopinIndex = which;
                        dialog.dismiss();
                    }
                })
                .show();
    }

    public void configADR(View v){

        new QMUIDialog.CheckableDialogBuilder(this)
                .setCheckedIndex(adrIndex)
                .addItems(adrs, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        adrIndex = which;
                        dialog.dismiss();
                    }
                })
                .show();
    }

    public void submit(View v){
        String writeStr = "AABB00CCDD";
        String h = Integer.toHexString(Integer.valueOf(hour.getText().toString().trim()));
        String m = Integer.toHexString(Integer.valueOf(minute.getText().toString().trim()));
        String s = Integer.toHexString(Integer.valueOf(second.getText().toString().trim()));
    }

}
