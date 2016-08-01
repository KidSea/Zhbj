package com.example.base.impl;

import android.app.Activity;
import android.graphics.Color;
import android.view.Gravity;
import android.widget.TextView;

import com.example.base.BasePager;

public class GovPager extends BasePager {

	public GovPager(Activity activity) {
		super(activity);
		// TODO Auto-generated constructor stub
	}
    public void initData() {
        
        tvTitle.setText("人口管理");
        setSlidingMenuEnable(true);//开启侧边栏
        TextView text = new TextView(mActivity);
        text.setText("政务");
        text.setTextColor(Color.RED);
        text.setTextSize(25);
        text.setGravity(Gravity.CENTER);
        
        flContent.addView(text);
        
	}

}
