package com.uniteddata.bleiot.app;

import android.app.Application;

import com.clj.fastble.BleManager;

/***********************************************************************
 * @creator : li.zy
 * @create-time : 2018/7/11
 * @email : li.zy@uniteddata.com
 * @description :
 ***********************************************************************/
public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        BleManager.getInstance().init(this);
        BleManager.getInstance()
                .enableLog(true)
                .setReConnectCount(1,5000)
                .setSplitWriteNum(20)
                .setConnectOverTime(10000)
                .setOperateTimeout(5000);
    }
}
