package com.cocacola.john_li.zhokinventorygun.ZHOKIGReceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.cocacola.john_li.zhokinventorygun.NormalWarehouseInventoryActivity;

/**
 * Created by John_Li on 27/12/2018.
 */

public class NormalTakeStockRevicer extends BroadcastReceiver {
    private NormalWarehouseInventoryActivity normalWarehouseInventoryActivity;
    public NormalTakeStockRevicer(NormalWarehouseInventoryActivity normalWarehouseInventoryActivity) {
        this.normalWarehouseInventoryActivity = normalWarehouseInventoryActivity;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("com.barcode.sendBroadcast")) {
            String barcode = intent.getStringExtra("BARCODE");
            normalWarehouseInventoryActivity.scanCallback(barcode);
        } else {
            Toast.makeText(context.getApplicationContext(), "未找到该产品，请确认后在扫描！", Toast.LENGTH_SHORT).show();
        }
    }
}
