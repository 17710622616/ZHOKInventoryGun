package com.cocacola.john_li.zhokinventorygun;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.cocacola.john_li.zhokinventorygun.ZHOKIGAdapter.MainAdapter;
import com.cocacola.john_li.zhokinventorygun.ZHOKIGView.ZHOKIGHeadView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by John_Li on 17/12/2018.
 */

public class TakeStockActivity extends AppCompatActivity implements View.OnClickListener{
    private ZHOKIGHeadView headView;
    private GridView mGv;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_stock);
        initView();
        setListener();
        initData();
    }

    private void initView() {
        headView = (ZHOKIGHeadView) findViewById(R.id.take_stock_head);
        mGv = (GridView) findViewById(R.id.take_stock_gv);
    }

    private void setListener() {
        mGv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        startActivity(new Intent(TakeStockActivity.this, NormalWarehouseInventoryActivity.class));
                        break;
                    case 1:
                        startActivity(new Intent(TakeStockActivity.this, BadWarehouseInventoryActivity.class));
                        break;
                }
            }
        });
    }

    private void initData() {
        headView.setTitle("商品盘点");
        headView.setLeft(this);
        mGv.setAdapter(new MainAdapter(this, getData()));
    }

    private List<String> getData() {
        List<String> list = new ArrayList();
        list.add("正常仓盘点");
        list.add("退货仓盘点");
        return list;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.head_left:
                finish();
                break;
        }
    }
}
