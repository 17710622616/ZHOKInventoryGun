package com.cocacola.john_li.zhokinventorygun.ZHOKIGReceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.cocacola.john_li.zhokinventorygun.BadWarehouseInventoryActivity;
import com.cocacola.john_li.zhokinventorygun.NormalWarehouseInventoryActivity;

/**
 * Created by John_Li on 27/12/2018.
 */

public class BadTakeStockRevicer extends BroadcastReceiver {
    private BadWarehouseInventoryActivity badWarehouseInventoryActivity;
    public BadTakeStockRevicer(BadWarehouseInventoryActivity badWarehouseInventoryActivity) {
        this.badWarehouseInventoryActivity = badWarehouseInventoryActivity;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("com.barcode.sendBroadcast")) {
            String barcode = intent.getStringExtra("BARCODE");
            badWarehouseInventoryActivity.scanCallback(barcode);
        } else {
            Toast.makeText(context.getApplicationContext(), "未找到该产品，请确认后在扫描！", Toast.LENGTH_SHORT).show();
        }
    }
}
