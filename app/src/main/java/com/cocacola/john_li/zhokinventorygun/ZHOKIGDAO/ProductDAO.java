package com.cocacola.john_li.zhokinventorygun.ZHOKIGDAO;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.cocacola.john_li.zhokinventorygun.ZHOKIGModel.ProductModel;
import com.cocacola.john_li.zhokinventorygun.ZHOKIGUtils.ZHOKIGSQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by John_Li on 17/12/2018.
 */

public class ProductDAO {
    private ZHOKIGSQLiteOpenHelper helper;
    private SQLiteDatabase db;
    private Context mContext;

    public ProductDAO(Context c) {
        helper = new ZHOKIGSQLiteOpenHelper(c);
        mContext = c;
    }

    public void add(ProductModel model) {
        db = helper.getWritableDatabase();
        db.beginTransaction();
        try {

            db.execSQL("insert into tb_product (prod_id,prod_name,dept_id,cat_id,subcat_id,bar_code) values(?,?,?,?,?,?)", new Object[] { model.getProd_id().replace(" ","")
                    ,model.getProd_name().replace(" ",""),model.getDept_id().replace(" ",""),model.getCat_id().replace(" ",""),model.getSubcat_id().replace(" ",""),model.getBar_code().replace(" ","")});
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }

    /**
     * 清空表
     **/
    public void clearTable()
    {
        db = helper.getWritableDatabase();
        db.execSQL("delete from tb_product");
    }


    /**
     * 根据条件查询
     */
    public ProductModel searchByProId(String proId) {
        db = helper.getWritableDatabase();
        db.beginTransaction();
        ProductModel model = null;
        try {
            Cursor cursor=db.rawQuery("select * from tb_product where prod_id = '" + proId + "'", null);
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                model = new ProductModel();
                model.setProd_id(cursor.getString(cursor.getColumnIndex("prod_id")));
                model.setProd_name(cursor.getString(cursor.getColumnIndex("prod_name")));
                model.setDept_id(cursor.getString(cursor.getColumnIndex("dept_id")));
                model.setCat_id(cursor.getString(cursor.getColumnIndex("cat_id")));
                model.setSubcat_id(cursor.getString(cursor.getColumnIndex("subcat_id")));
                model.setBar_code(cursor.getString(cursor.getColumnIndex("bar_code")));
                cursor.moveToNext();
            }

            cursor.close();
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
        return model;
    }

    /**
     * 根据条码查询
     */
    public ProductModel searchByBarcode(String barcode) {
        db = helper.getWritableDatabase();
        db.beginTransaction();
        ProductModel model = null;
        try {
            Cursor cursor=db.rawQuery("select * from tb_product where bar_code = '" + barcode + "'", null);
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                model = new ProductModel();
                model.setProd_id(cursor.getString(cursor.getColumnIndex("prod_id")));
                model.setProd_name(cursor.getString(cursor.getColumnIndex("prod_name")));
                model.setDept_id(cursor.getString(cursor.getColumnIndex("dept_id")));
                model.setCat_id(cursor.getString(cursor.getColumnIndex("cat_id")));
                model.setSubcat_id(cursor.getString(cursor.getColumnIndex("subcat_id")));
                model.setBar_code(cursor.getString(cursor.getColumnIndex("bar_code")));
                cursor.moveToNext();
            }

            cursor.close();
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
        return model;
    }
}
