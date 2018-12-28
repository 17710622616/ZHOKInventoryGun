package com.cocacola.john_li.zhokinventorygun.ZHOKIGModel;

/**
 * Created by John_Li on 26/12/2018.
 */

public class DCStockTake {
    private String prod_id;
    private int qty;
    private String oper_name;
    private int status;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getProd_id() {
        return prod_id;
    }

    public void setProd_id(String prod_id) {
        this.prod_id = prod_id;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public String getOper_name() {
        return oper_name;
    }

    public void setOper_name(String oper_name) {
        this.oper_name = oper_name;
    }
}
