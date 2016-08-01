package com.example.base.impl;

import android.app.Activity;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.Gallery;
import android.widget.TextView;

import com.example.base.BasePager;
import com.example.fragment.BaseFragment;
import com.example.zhbj52.MainActivity;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

public class HomePager extends BasePager{

	public HomePager(Activity activity) {
		super(activity);
		// TODO Auto-generated constructor stub
	}

    public void initData() {
        imageButton.setVisibility(View.GONE);//���ز˵���ť
        setSlidingMenuEnable(false);//���ò�����
        
        tvTitle.setText("�ǻ۱���");
        TextView text = new TextView(mActivity);
        text.setText("��ҳ");
        text.setTextColor(Color.RED);
        text.setTextSize(25);
        text.setGravity(Gravity.CENTER);
        
        flContent.addView(text);
        
	}
}
