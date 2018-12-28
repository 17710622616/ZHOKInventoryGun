package com.cocacola.john_li.zhokinventorygun.ZHOKIGModel;

/**
 * Created by John_Li on 17/12/2018.
 */

public class ProductModel {
    /**
     * prod_id : 2000060
     * prod_name : 维他奶250ml/盒
     * dept_id : 061
     * cat_id : 05
     * subcat_id : 01
     * bar_code : 116906742664711
     */

    private String prod_id;
    private String prod_name;
    private String dept_id;
    private String cat_id;
    private String subcat_id;
    private String bar_code;

    public String getProd_id() {
        return prod_id;
    }

    public void setProd_id(String prod_id) {
        this.prod_id = prod_id;
    }

    public String getProd_name() {
        return prod_name;
    }

    public void setProd_name(String prod_name) {
        this.prod_name = prod_name;
    }

    public String getDept_id() {
        return dept_id;
    }

    public void setDept_id(String dept_id) {
        this.dept_id = dept_id;
    }

    public String getCat_id() {
        return cat_id;
    }

    public void setCat_id(String cat_id) {
        this.cat_id = cat_id;
    }

    public String getSubcat_id() {
        return subcat_id;
    }

    public void setSubcat_id(String subcat_id) {
        this.subcat_id = subcat_id;
    }

    public String getBar_code() {
        return bar_code;
    }

    public void setBar_code(String bar_code) {
        this.bar_code = bar_code;
    }
}
