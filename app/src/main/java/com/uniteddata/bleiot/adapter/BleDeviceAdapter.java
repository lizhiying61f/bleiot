package com.uniteddata.bleiot.adapter;

import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.clj.fastble.data.BleDevice;
import com.uniteddata.bleiot.R;

import java.util.List;

/***********************************************************************
 * @creator : li.zy
 * @create-time : 2018/7/12
 * @email : li.zy@uniteddata.com
 * @description :
 ***********************************************************************/
public class BleDeviceAdapter extends BaseQuickAdapter<BleDevice,BaseViewHolder> {
    private List<BleDevice> data;
    public BleDeviceAdapter(@Nullable List<BleDevice> data) {
        super(R.layout.item_bledevice,data);
        this.data = data;
    }

    @Override
    protected void convert(BaseViewHolder helper, BleDevice item) {
        String name = item.getName();
        if (name == null || TextUtils.isEmpty(name)){
            name = item.getMac();
        }
        helper.setText(R.id.item_ble_mac,name)
                .addOnClickListener(R.id.item_ble_connect);
    }
}
