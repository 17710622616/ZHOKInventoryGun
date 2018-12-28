package com.cocacola.john_li.zhokinventorygun.ZHOKIGUtils;

import com.cocacola.john_li.zhokinventorygun.MainActivity;

/**
 * Created by John_Li on 17/12/2018.
 */

public class ZHOKIGConfigtor {
    public static MainActivity activity;

    public final static String BASE_URL = "http://172.25.128.86:8001/";
    // 产品接口
    public final static String GET_PRODUCT_DATA = "api/inventoryReport/GetDCProductData";
    // 上传正常仓盘点接口
    public final static String POST_NORMAL_STOCK_TAKE = "api/inventoryReport/pOSTDCNormalStockTake";
    // 上传退货仓仓盘点接口
    public final static String POST_BAD_STOCK_TAKE = "api/inventoryReport/PostDCBadStockTake";
}
