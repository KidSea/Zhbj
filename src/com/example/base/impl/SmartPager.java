package com.example.base.impl;

import android.app.Activity;
import android.graphics.Color;
import android.view.Gravity;
import android.widget.TextView;

import com.example.base.BasePager;

public class SmartPager extends BasePager {
	
	public SmartPager(Activity activity) {
		super(activity);
		// TODO Auto-generated constructor stub
	}
	
    public void initData() {
        tvTitle.setText("����");
        setSlidingMenuEnable(true);//���������
        TextView text = new TextView(mActivity);
        text.setText("�ǻ۷���");
        text.setTextColor(Color.RED);
        text.setTextSize(25);
        text.setGravity(Gravity.CENTER);
        
        flContent.addView(text);
        
	}

}
