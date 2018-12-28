package com.cocacola.john_li.zhokinventorygun.ZHOKIGModel;

/**
 * Created by John_Li on 27/12/2018.
 */

public class DCDBStockTakeResult {
    private String prod_id;
    private String oper_name;
    private boolean result;

    public String getProd_id() {
        return prod_id;
    }

    public void setProd_id(String prod_id) {
        this.prod_id = prod_id;
    }

    public String getOper_name() {
        return oper_name;
    }

    public void setOper_name(String oper_name) {
        this.oper_name = oper_name;
    }

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }
}
