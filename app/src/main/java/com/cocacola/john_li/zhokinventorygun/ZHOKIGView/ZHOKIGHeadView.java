package com.cocacola.john_li.zhokinventorygun.ZHOKIGView;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cocacola.john_li.zhokinventorygun.R;

/**
 * Created by John_Li on 20/7/2018.
 */

public class ZHOKIGHeadView extends LinearLayout {
    public TextView leftTv,titleTv,rightTv;
    private LinearLayout headLL;
    private Context mContext;
    public ZHOKIGHeadView(Context context) {
        super(context);
        init(context);
    }

    public ZHOKIGHeadView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ZHOKIGHeadView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        mContext = context;
        LayoutInflater inflater = LayoutInflater.from(context);
        inflater.inflate(R.layout.layout_head, this);
        leftTv = (TextView) this.findViewById(R.id.head_left);
        titleTv = (TextView) this.findViewById(R.id.head_title);
        rightTv = (TextView) this.findViewById(R.id.head_right_tv);
        headLL = this.findViewById(R.id.head_ll);
        leftTv.setVisibility(INVISIBLE);
        titleTv.setVisibility(INVISIBLE);
        rightTv.setVisibility(INVISIBLE);
    }

    /**
     * 标题
     */
    public void setTitle(String title){
        titleTv.setVisibility(VISIBLE);
        titleTv.setText(title);
    }

    /**
     * 左边按钮
     */
    public void setLeft(OnClickListener listener){
        leftTv.setVisibility(VISIBLE);
        leftTv.setOnClickListener(listener);
    }

    /**
     * 右边按钮文字
     */
    public void setRightText(String str,OnClickListener listener){
        rightTv.setVisibility(VISIBLE);
        rightTv.setText(str);
        rightTv.setOnClickListener(listener);
    }

    /**
     * 右边按钮文字
     */
    public void setHeadHight(){
        headLL.setPadding(0,0,0,0);
        ViewGroup.LayoutParams lp = headLL.getLayoutParams();
        lp.height = 90;
        headLL.setLayoutParams(lp);
    }
}
