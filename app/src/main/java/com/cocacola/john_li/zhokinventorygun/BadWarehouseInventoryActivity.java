package com.cocacola.john_li.zhokinventorygun;

import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.cocacola.john_li.zhokinventorygun.ZHOKIGDAO.BadDCSockTakeDAO;
import com.cocacola.john_li.zhokinventorygun.ZHOKIGDAO.ProductDAO;
import com.cocacola.john_li.zhokinventorygun.ZHOKIGModel.DCStockTake;
import com.cocacola.john_li.zhokinventorygun.ZHOKIGModel.ProductModel;
import com.cocacola.john_li.zhokinventorygun.ZHOKIGReceiver.BadTakeStockRevicer;
import com.cocacola.john_li.zhokinventorygun.ZHOKIGReceiver.NormalTakeStockRevicer;
import com.cocacola.john_li.zhokinventorygun.ZHOKIGUtils.ZHOKIGConfigtor;
import com.cocacola.john_li.zhokinventorygun.ZHOKIGView.ZHOKIGHeadView;

/**
 * Created by John_Li on 19/12/2018.
 */

public class BadWarehouseInventoryActivity extends AppCompatActivity implements View.OnClickListener{
    private ZHOKIGHeadView headView;
    private TextView proIdTv, proNameTv, barcodeNameTv, submitTv;
    private TextView operNameTv, stNumTv;
    private EditText numEt;

    private String operName;
    private ProductDAO mProductDAO;
    private BadDCSockTakeDAO mBadDCSockTakeDAO;
    private ProductModel mProductModel;
    private BadTakeStockRevicer mBadTakeStockRevicer;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_normal_warehouse_inventory);
        initView();
        setListener();
        initData();
    }

    private void initView() {
        headView = (ZHOKIGHeadView) findViewById(R.id.normal_head);
        proIdTv = (TextView) findViewById(R.id.normal_pro_id);
        proNameTv = (TextView) findViewById(R.id.normal_pro_name);
        barcodeNameTv = (TextView) findViewById(R.id.normal_bar_code);
        submitTv = (TextView) findViewById(R.id.normal_submit);
        numEt = (EditText) findViewById(R.id.normal_num_et);
        operNameTv = (TextView) findViewById(R.id.bad_oper_name);
        stNumTv = (TextView) findViewById(R.id.bad_st_num);
    }

    private void setListener() {
        submitTv.setOnClickListener(this);
    }

    private void initData() {
        headView.setTitle("退货仓盘点");
        headView.setLeft(this);
        headView.setRightText("查询",this);
        EditNameDialogFragment editNameDialog = new EditNameDialogFragment();
        editNameDialog.show(getFragmentManager(), "EditNameDialog");
        mProductDAO = new ProductDAO(this);
        mBadDCSockTakeDAO = new BadDCSockTakeDAO(this);
        stNumTv.setText("已盘点商品：" + mBadDCSockTakeDAO.getCount());
    }


    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter intentFilter = new IntentFilter();
        mBadTakeStockRevicer = new BadTakeStockRevicer(this);
        intentFilter.addAction("com.barcode.sendBroadcast");
        registerReceiver(mBadTakeStockRevicer, intentFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(mBadTakeStockRevicer);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.head_left:
                finish();
                break;
            case R.id.head_right_tv:
                AlertDialog.Builder builder = new AlertDialog.Builder(BadWarehouseInventoryActivity.this);
                View view = View .inflate(BadWarehouseInventoryActivity.this, R.layout.dialog_search, null);
                builder.setView(view);
                builder.setCancelable(true);
                final EditText proIdEt = (EditText)view.findViewById(R.id.search_name);//输入内容
                TextView btn_cancel = (TextView)view.findViewById(R.id.search_cancle);//取消按钮
                TextView btn_comfirm = (TextView)view.findViewById(R.id.search_submit);//确定按钮
                final AlertDialog dialog = builder.create();
                //取消或确定按钮监听事件处理
                btn_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                btn_comfirm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!proIdEt.getText().toString().equals("")) {
                            mProductModel = mProductDAO.searchByProId(proIdEt.getText().toString());
                            if (mProductModel != null) {
                                dialog.dismiss();
                                refreshUI();
                            } else {
                                Toast.makeText(BadWarehouseInventoryActivity.this, "未找到该商品！", Toast.LENGTH_LONG).show();
                            }
                        } else {
                            Toast.makeText(BadWarehouseInventoryActivity.this, "请填写需要查询的商品名称！", Toast.LENGTH_LONG).show();
                        }
                    }
                });
                dialog.show();
                break;
            case R.id.normal_submit:
                if (mProductModel != null) {
                    if (!numEt.getText().toString().equals("")) {
                        // 插入数据库
                        try {
                            int num = Integer.parseInt(numEt.getText().toString());

                            DCStockTake model = new DCStockTake();
                            model.setProd_id(mProductModel.getProd_id());
                            model.setQty(num);
                            model.setOper_name(operName);
                            model.setStatus(0);
                            mBadDCSockTakeDAO.add(model);
                            // 清空界面
                            mProductModel = null;
                            refreshUI();
                        } catch (Exception e) {
                            Toast.makeText(BadWarehouseInventoryActivity.this, "请填写正确的商品盘点数量！", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(BadWarehouseInventoryActivity.this, "请填写商品盘点数量！", Toast.LENGTH_LONG).show();
                    }
                }
                break;
        }
    }

    /**
     * 找到商品及完成操作时清空界面
     */
    private void refreshUI() {
        if (mProductModel != null) {
            proIdTv.setText("货号：" + mProductModel.getProd_id());
            proNameTv.setText("品名：" + mProductModel.getProd_name());
            barcodeNameTv.setText("条码：" + mProductModel.getBar_code());
        } else {
            proIdTv.setText("货号：");
            proNameTv.setText("品名：");
            barcodeNameTv.setText("条码：");
        }

        ZHOKIGConfigtor.activity.refreshTakeStockNum();
        numEt.setText("");
        stNumTv.setText("已盘点商品：" + mBadDCSockTakeDAO.getCount());
        numEt.setSelectAllOnFocus(true);
        showSoftInputFromWindow(numEt);
    }


    public void scanCallback(String barcode) {
        mProductModel = mProductDAO.searchByBarcode(barcode);
        if (mProductModel != null) {
            refreshUI();
        } else {
            Toast.makeText(BadWarehouseInventoryActivity.this, "未找到该商品！", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * EditText获取焦点并显示软键盘
     */
    public void showSoftInputFromWindow(EditText editText) {
        editText.setFocusable(true);
        editText.setFocusableInTouchMode(true);
        editText.requestFocus();
        //显示软键盘
        //this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        //如果上面的代码没有弹出软键盘 可以使用下面另一种方式
        InputMethodManager imm = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(editText, 0);
    }

    /**
     * 登录dialog
     */
    private long clickTime = 0; // 第一次点击的时间
    public class EditNameDialogFragment extends DialogFragment {
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
            getDialog().setCancelable(false);
            getDialog().setCanceledOnTouchOutside(false);
            getDialog().setOnKeyListener(new DialogInterface.OnKeyListener() {
                @Override
                public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                    // 是否触发按键为back键
                    if (keyCode == KeyEvent.KEYCODE_BACK) {
                        if ((System.currentTimeMillis() - clickTime) > 2000) {
                            Toast.makeText(BadWarehouseInventoryActivity.this, "再按一次后退键退出盘点", Toast.LENGTH_SHORT).show();
                            clickTime = System.currentTimeMillis();
                        } else {
                            BadWarehouseInventoryActivity.this.finish();
                        }
                        return false;
                    } else { // 如果不是back键正常响应
                        return false;
                    }
                }
            });
            View view = inflater.inflate(R.layout.fragment_login, container);

            final EditText nameEt = view.findViewById(R.id.login_name);
            final EditText pwdEt = view.findViewById(R.id.login_pwd);
            TextView cancelTv = view.findViewById(R.id.login_cancle);
            TextView submitTv = view.findViewById(R.id.login_submit);
            cancelTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    BadWarehouseInventoryActivity.this.finish();
                }
            });
            submitTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!nameEt.getText().toString().equals("") &&!pwdEt.getText().toString().equals("")) {
                        switch (nameEt.getText().toString()) {
                            case "zhok001":
                                if (pwdEt.getText().toString().equals("123456")) {
                                    operName = "zhok001";
                                    dismiss();
                                } else {
                                    Toast.makeText(BadWarehouseInventoryActivity.this, "请填写正确的密码！", Toast.LENGTH_SHORT).show();
                                }
                                break;
                            case "zhok002":
                                if (pwdEt.getText().toString().equals("123456")) {
                                    operName = "zhok002";
                                    dismiss();
                                } else {
                                    Toast.makeText(BadWarehouseInventoryActivity.this, "请填写正确的密码！", Toast.LENGTH_SHORT).show();
                                }
                                break;
                            case "zhok003":
                                if (pwdEt.getText().toString().equals("123456")) {
                                    operName = "zhok003";
                                    dismiss();
                                } else {
                                    Toast.makeText(BadWarehouseInventoryActivity.this, "请填写正确的密码！", Toast.LENGTH_SHORT).show();
                                }
                                break;
                            case "zhok004":
                                if (pwdEt.getText().toString().equals("123456")) {
                                    operName = "zhok004";
                                    dismiss();
                                } else {
                                    Toast.makeText(BadWarehouseInventoryActivity.this, "请填写正确的密码！", Toast.LENGTH_SHORT).show();
                                }
                                break;
                            case "zhok005":
                                if (pwdEt.getText().toString().equals("123456")) {
                                    operName = "zhok005";
                                    dismiss();
                                } else {
                                    Toast.makeText(BadWarehouseInventoryActivity.this, "请填写正确的密码！", Toast.LENGTH_SHORT).show();
                                }
                                break;
                            case "zhok006":
                                if (pwdEt.getText().toString().equals("123456")) {
                                    operName = "zhok006";
                                    dismiss();
                                } else {
                                    Toast.makeText(BadWarehouseInventoryActivity.this, "请填写正确的密码！", Toast.LENGTH_SHORT).show();
                                }
                                break;
                            case "zhok007":
                                if (pwdEt.getText().toString().equals("123456")) {
                                    operName = "zhok007";
                                    dismiss();
                                } else {
                                    Toast.makeText(BadWarehouseInventoryActivity.this, "请填写正确的密码！", Toast.LENGTH_SHORT).show();
                                }
                                break;
                            case "zhok008":
                                if (pwdEt.getText().toString().equals("123456")) {
                                    operName = "zhok008";
                                    dismiss();
                                } else {
                                    Toast.makeText(BadWarehouseInventoryActivity.this, "请填写正确的密码！", Toast.LENGTH_SHORT).show();
                                }
                                break;
                            case "zhok009":
                                if (pwdEt.getText().toString().equals("123456")) {
                                    operName = "zhok009";
                                    dismiss();
                                } else {
                                    Toast.makeText(BadWarehouseInventoryActivity.this, "请填写正确的密码！", Toast.LENGTH_SHORT).show();
                                }
                                break;
                            case "zhok010":
                                if (pwdEt.getText().toString().equals("123456")) {
                                    operName = "zhok010";
                                    dismiss();
                                } else {
                                    Toast.makeText(BadWarehouseInventoryActivity.this, "请填写正确的密码！", Toast.LENGTH_SHORT).show();
                                }
                                break;
                            case "zhok011":
                                if (pwdEt.getText().toString().equals("123456")) {
                                    operName = "zhok011";
                                    dismiss();
                                } else {
                                    Toast.makeText(BadWarehouseInventoryActivity.this, "请填写正确的密码！", Toast.LENGTH_SHORT).show();
                                }
                                break;
                            case "zhok012":
                                if (pwdEt.getText().toString().equals("123456")) {
                                    operName = "zhok012";
                                    dismiss();
                                } else {
                                    Toast.makeText(BadWarehouseInventoryActivity.this, "请填写正确的密码！", Toast.LENGTH_SHORT).show();
                                }
                                break;
                            case "zhok013":
                                if (pwdEt.getText().toString().equals("123456")) {
                                    operName = "zhok013";
                                    dismiss();
                                } else {
                                    Toast.makeText(BadWarehouseInventoryActivity.this, "请填写正确的密码！", Toast.LENGTH_SHORT).show();
                                }
                                break;
                            case "zhok014":
                                if (pwdEt.getText().toString().equals("123456")) {
                                    operName = "zhok014";
                                    dismiss();
                                } else {
                                    Toast.makeText(BadWarehouseInventoryActivity.this, "请填写正确的密码！", Toast.LENGTH_SHORT).show();
                                }
                                break;
                            case "zhok015":
                                if (pwdEt.getText().toString().equals("123456")) {
                                    operName = "zhok015";
                                    dismiss();
                                } else {
                                    Toast.makeText(BadWarehouseInventoryActivity.this, "请填写正确的密码！", Toast.LENGTH_SHORT).show();
                                }
                                break;
                            case "zhok016":
                                if (pwdEt.getText().toString().equals("123456")) {
                                    operName = "zhok016";
                                    dismiss();
                                } else {
                                    Toast.makeText(BadWarehouseInventoryActivity.this, "请填写正确的密码！", Toast.LENGTH_SHORT).show();
                                }
                                break;
                            case "zhok017":
                                if (pwdEt.getText().toString().equals("123456")) {
                                    operName = "zhok017";
                                    dismiss();
                                } else {
                                    Toast.makeText(BadWarehouseInventoryActivity.this, "请填写正确的密码！", Toast.LENGTH_SHORT).show();
                                }
                                break;
                            case "zhok018":
                                if (pwdEt.getText().toString().equals("123456")) {
                                    operName = "zhok018";
                                    dismiss();
                                } else {
                                    Toast.makeText(BadWarehouseInventoryActivity.this, "请填写正确的密码！", Toast.LENGTH_SHORT).show();
                                }
                                break;
                            case "zhok019":
                                if (pwdEt.getText().toString().equals("123456")) {
                                    operName = "zhok019";
                                    dismiss();
                                } else {
                                    Toast.makeText(BadWarehouseInventoryActivity.this, "请填写正确的密码！", Toast.LENGTH_SHORT).show();
                                }
                                break;
                            case "zhok020":
                                if (pwdEt.getText().toString().equals("123456")) {
                                    operName = "zhok020";
                                    dismiss();
                                } else {
                                    Toast.makeText(BadWarehouseInventoryActivity.this, "请填写正确的密码！", Toast.LENGTH_SHORT).show();
                                }
                                break;
                            default:
                                Toast.makeText(BadWarehouseInventoryActivity.this, "未找到该用户！", Toast.LENGTH_SHORT).show();
                                break;
                        }

                        operNameTv.setText("操作人：" + operName);
                    } else {
                        Toast.makeText(BadWarehouseInventoryActivity.this, "请填写全账户密码！", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            return view;
        }
    }
}
