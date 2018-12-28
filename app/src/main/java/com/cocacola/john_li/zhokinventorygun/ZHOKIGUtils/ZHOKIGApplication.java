package com.cocacola.john_li.zhokinventorygun.ZHOKIGUtils;

import android.app.Application;

import org.xutils.x;

/**
 * Created by John_Li on 17/12/2018.
 */

public class ZHOKIGApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        x.Ext.init(this);
    }
}
