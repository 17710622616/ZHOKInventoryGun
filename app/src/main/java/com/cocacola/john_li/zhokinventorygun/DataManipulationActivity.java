package com.cocacola.john_li.zhokinventorygun;

import android.accounts.NetworkErrorException;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.cocacola.john_li.zhokinventorygun.ZHOKIGAdapter.MainAdapter;
import com.cocacola.john_li.zhokinventorygun.ZHOKIGDAO.BadDCSockTakeDAO;
import com.cocacola.john_li.zhokinventorygun.ZHOKIGDAO.NormalDCSockTakeDAO;
import com.cocacola.john_li.zhokinventorygun.ZHOKIGDAO.ProductDAO;
import com.cocacola.john_li.zhokinventorygun.ZHOKIGModel.DCDBStockTakeResult;
import com.cocacola.john_li.zhokinventorygun.ZHOKIGModel.DCStockTake;
import com.cocacola.john_li.zhokinventorygun.ZHOKIGModel.ProductModel;
import com.cocacola.john_li.zhokinventorygun.ZHOKIGUtils.ZHOKIGConfigtor;
import com.cocacola.john_li.zhokinventorygun.ZHOKIGView.ZHOKIGHeadView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.HttpMethod;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by John_Li on 17/12/2018.
 */

public class DataManipulationActivity extends AppCompatActivity implements View.OnClickListener{
    private ZHOKIGHeadView headView;
    private GridView mGv;
    private ProgressDialog dialog;
    private ProductDAO mProductDAO;
    private NormalDCSockTakeDAO mNormalDCSockTakeDAO;
    private BadDCSockTakeDAO mBadDCSockTakeDAO;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_manipulation);
        initView();
        setListener();
        initData();
    }

    private void initView() {
        headView = (ZHOKIGHeadView) findViewById(R.id.data_manipulation_head);
        mGv = (GridView) findViewById(R.id.data_manipulation_gv);
    }

    private void setListener() {
        mGv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        dialog = new ProgressDialog(DataManipulationActivity.this);
                        dialog.setTitle("提示");
                        dialog.setMessage("正在上传所有资料......");
                        dialog.setCancelable(false);
                        dialog.show();
                        callNetUploadData();
                        break;
                    case 1:
                        dialog = new ProgressDialog(DataManipulationActivity.this);
                        dialog.setTitle("提示");
                        dialog.setMessage("正在下载所有资料......");
                        dialog.setCancelable(false);
                        dialog.show();
                        mProductDAO.clearTable();
                        callNetUpdtaeData();
                        break;
                    case 2:
                        dialog = new ProgressDialog(DataManipulationActivity.this);
                        dialog.setTitle("提示");
                        dialog.setMessage("正在清空所有盤點數......");
                        dialog.setCancelable(false);
                        dialog.show();
                        mNormalDCSockTakeDAO.clearTable();
                        mBadDCSockTakeDAO.clearTable();
                        break;
                }
            }
        });
    }

    private void initData() {
        headView.setTitle("数据处理");
        headView.setLeft(this);
        mGv.setAdapter(new MainAdapter(this, getData()));
        mProductDAO = new ProductDAO(this);
        mNormalDCSockTakeDAO = new NormalDCSockTakeDAO(this);
        mBadDCSockTakeDAO = new BadDCSockTakeDAO(this);
    }

    private List<String> getData() {
        List<String> list = new ArrayList();
        list.add("数据上传");
        list.add("数据下载");
        list.add("清空盤點數");
        return list;
    }

    /**
     * 上传正常仓数据
     */
    private void callNetUploadData() {
        RequestParams params = new RequestParams(ZHOKIGConfigtor.BASE_URL + ZHOKIGConfigtor.POST_NORMAL_STOCK_TAKE);
        params.setConnectTimeout(30 * 1000);
        List<DCStockTake> list = mNormalDCSockTakeDAO.search();
        params.setAsJsonContent(true);
        params.setBodyContent(new Gson().toJson(list));
        x.http().request(HttpMethod.POST ,params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                List<DCDBStockTakeResult> dcdbStockTakeResults = new Gson().fromJson(result, new TypeToken<ArrayList<DCDBStockTakeResult>>() {}.getType());
                for (DCDBStockTakeResult model : dcdbStockTakeResults) {
                    if (model.isResult()) {
                        mNormalDCSockTakeDAO.deleteByProdId(model.getProd_id());
                    } else {
                        mNormalDCSockTakeDAO.updataStatus(model.getProd_id(), 2);
                    }
                }
                Toast.makeText(DataManipulationActivity.this, "正常仓盘点上传完成！", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Toast.makeText(DataManipulationActivity.this, "正常仓盘点上传失败" + ex.getStackTrace(), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {
                callNetUploadBadStackTakeData();
            }
        });
    }

    /**
     * 上传退货仓数据
     */
    private void callNetUploadBadStackTakeData() {
        RequestParams params = new RequestParams(ZHOKIGConfigtor.BASE_URL + ZHOKIGConfigtor.POST_BAD_STOCK_TAKE);
        params.setConnectTimeout(30 * 1000);
        List<DCStockTake> list = mBadDCSockTakeDAO.search();
        params.setAsJsonContent(true);
        String json = new Gson().toJson(list);
        params.setBodyContent(json);
        x.http().request(HttpMethod.POST ,params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                List<DCDBStockTakeResult> dcdbStockTakeResults = new Gson().fromJson(result, new TypeToken<ArrayList<DCDBStockTakeResult>>() {}.getType());
                for (DCDBStockTakeResult model : dcdbStockTakeResults) {
                    if (model.isResult()) {
                        mBadDCSockTakeDAO.deleteByProdId(model.getProd_id());
                    } else {
                        mBadDCSockTakeDAO.updataStatus(model.getProd_id(), 2);
                    }
                }
                Toast.makeText(DataManipulationActivity.this, "退货仓盘点上传完成！", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Toast.makeText(DataManipulationActivity.this, "退货仓盘点上传失败" + ex.getStackTrace(), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {
                dialog.dismiss();
            }
        });
    }

    /**
     *下载数据
     */
    private void callNetUpdtaeData() {
        RequestParams params = new RequestParams(ZHOKIGConfigtor.BASE_URL + ZHOKIGConfigtor.GET_PRODUCT_DATA);
        params.setConnectTimeout(30 * 1000);
        String url = params.getUri();
        x.http().request(HttpMethod.GET ,params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                List<ProductModel> productModelList = new Gson().fromJson(result, new TypeToken<ArrayList<ProductModel>>() {}.getType());
                for (ProductModel model : productModelList) {
                    mProductDAO.add(model);
                }
                Toast.makeText(DataManipulationActivity.this, "数据下载成功！", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Toast.makeText(DataManipulationActivity.this, "数据下载失败" + ex.getStackTrace(), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {
                dialog.dismiss();
            }
        });
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
