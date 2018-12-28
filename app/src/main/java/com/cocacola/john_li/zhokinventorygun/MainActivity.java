package com.cocacola.john_li.zhokinventorygun;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import com.cocacola.john_li.zhokinventorygun.ZHOKIGAdapter.MainAdapter;
import com.cocacola.john_li.zhokinventorygun.ZHOKIGDAO.BadDCSockTakeDAO;
import com.cocacola.john_li.zhokinventorygun.ZHOKIGDAO.NormalDCSockTakeDAO;
import com.cocacola.john_li.zhokinventorygun.ZHOKIGUtils.ZHOKIGConfigtor;
import com.cocacola.john_li.zhokinventorygun.ZHOKIGView.ZHOKIGHeadView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private ZHOKIGHeadView headView;
    private GridView mGv;
    private TextView normalNumTv, badNumTv;
    private NormalDCSockTakeDAO mNormalDCSockTakeDAO;
    private BadDCSockTakeDAO mBadDCSockTakeDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        setListener();
        initData();
    }

    private void initView() {
        headView = (ZHOKIGHeadView) findViewById(R.id.main_head);
        mGv = (GridView) findViewById(R.id.main_gv);
        normalNumTv = (TextView) findViewById(R.id.normal_ts_num);
        badNumTv = (TextView) findViewById(R.id.bad_ts_num);
    }

    private void setListener() {
        mGv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        startActivity(new Intent(MainActivity.this, TakeStockActivity.class));
                        break;
                    case 1:
                        startActivity(new Intent(MainActivity.this, DataManipulationActivity.class));
                        break;
                }
            }
        });
    }

    private void initData() {
        headView.setTitle("仓库盘点");
        mGv.setAdapter(new MainAdapter(this, getData()));
        ZHOKIGConfigtor.activity = this;
        mNormalDCSockTakeDAO = new NormalDCSockTakeDAO(this);
        mBadDCSockTakeDAO = new BadDCSockTakeDAO(this);
        refreshTakeStockNum();
    }

    private List<String> getData() {
        List<String> list = new ArrayList();
        list.add("商品盘点");
        list.add("数据处理");
        return list;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        }
    }

    public void refreshTakeStockNum() {
        normalNumTv.setText("正常仓已盘：" + mNormalDCSockTakeDAO.getCount());
        badNumTv.setText("退货仓已盘：" + mBadDCSockTakeDAO.getCount());
    }
}
