package com.uniteddata.bleiot.view;

import android.Manifest;
import android.bluetooth.BluetoothGatt;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.PermissionChecker;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.clj.fastble.BleManager;
import com.clj.fastble.callback.BleGattCallback;
import com.clj.fastble.callback.BleScanCallback;
import com.clj.fastble.data.BleDevice;
import com.clj.fastble.exception.BleException;
import com.clj.fastble.scan.BleScanRuleConfig;
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;
import com.uniteddata.bleiot.R;
import com.uniteddata.bleiot.adapter.BleDeviceAdapter;

import java.util.List;

public class ScanBleActivity extends AppCompatActivity {
    Button startScan;
    RecyclerView bleRecyclerView;
    private BleDeviceAdapter adapter;
    private QMUITipDialog tipDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_ble);

        startScan = findViewById(R.id.startScan);
        bleRecyclerView = findViewById(R.id.bleList);
        initpremission();
    }

    private void initpremission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)!= PermissionChecker.PERMISSION_GRANTED
                    &&
                    ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)!= PermissionChecker.PERMISSION_GRANTED
//                    ||ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA)!= PermissionChecker.PERMISSION_GRANTED
//                    ||ActivityCompat.checkSelfPermission(this,Manifest.permission.CALL_PHONE)!=PermissionChecker.PERMISSION_GRANTED
//                    ||ActivityCompat.checkSelfPermission(this,Manifest.permission.READ_PHONE_STATE)!=PermissionChecker.PERMISSION_GRANTED
 ){
                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION},99);
            }else {
                initScanRule();
            }
        }else {
            initScanRule();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == 99){
            initScanRule();
        }else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    public void initScanRule(){
        BleScanRuleConfig scanRuleConfig = new BleScanRuleConfig.Builder()
//                .setServiceUuids(serviceUuids)      // 只扫描指定的服务的设备，可选
//                .setDeviceName(true, names)         // 只扫描指定广播名的设备，可选
//                .setDeviceMac(mac)                  // 只扫描指定mac的设备，可选
//                .setAutoConnect(isAutoConnect)      // 连接时的autoConnect参数，可选，默认false
                .setScanTimeOut(10000)              // 扫描超时时间，可选，默认10秒；小于等于0表示不限制扫描时间
                .build();
        BleManager.getInstance().initScanRule(scanRuleConfig);
    }

    public void startScan(View v){
        Log.e("SCAN","startScan");
        if (tipDialog== null){
            tipDialog = new QMUITipDialog.Builder(this).setIconType(QMUITipDialog.Builder.ICON_TYPE_LOADING).setTipWord("正在加载").create();
        }
        BleManager.getInstance().scan(new BleScanCallback() {
            @Override
            public void onScanStarted(boolean success) {
                // 开始扫描（主线程）
                Log.e("SCAN",success + "");
                tipDialog.show();
            }

            @Override
            public void onScanning(BleDevice bleDevice) {
                // 扫描到一个符合扫描规则的BLE设备（主线程）
                Log.e("SCAN",bleDevice.toString());
            }

            @Override
            public void onScanFinished(List<BleDevice> scanResultList) {
                // 扫描结束，列出所有扫描到的符合扫描规则的BLE设备（主线程）
                Log.e("SCAN",scanResultList.toString());
                initAdatper(scanResultList);
                tipDialog.hide();
            }
        });
    }

    private void initAdatper(final List<BleDevice> data){
        adapter = new BleDeviceAdapter(data);
        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                connectBleDevice(data.get(position));
            }
        });
        bleRecyclerView.setLayoutManager(new LinearLayoutManager(ScanBleActivity.this,
                LinearLayoutManager.VERTICAL,false));
        bleRecyclerView.setAdapter(adapter);
    }

    /**
     *
     * @param bleDevice
     */

    private void connectBleDevice(BleDevice bleDevice) {
        BleManager.getInstance().connect(bleDevice, new BleGattCallback() {
            @Override
            public void onStartConnect() {
                // 开始连接
                tipDialog.show();
            }

            @Override
            public void onConnectFail(BleDevice bleDevice, BleException exception) {
                // 连接失败
            }

            @Override
            public void onConnectSuccess(BleDevice bleDevice, BluetoothGatt gatt, int status) {
                // 连接成功，BleDevice即为所连接的BLE设备
                tipDialog.hide();
                Intent intent = new Intent(ScanBleActivity.this,ScanResultActivity.class);
                intent.putExtra("ADDRESS",bleDevice);
                startActivity(intent);
            }

            @Override
            public void onDisConnected(boolean isActiveDisConnected, BleDevice bleDevice, BluetoothGatt gatt, int status) {
                // 连接中断，isActiveDisConnected表示是否是主动调用了断开连接方法
            }
        });
    }
}
