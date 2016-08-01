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
        imageButton.setVisibility(View.GONE);//隐藏菜单按钮
        setSlidingMenuEnable(false);//设置不可用
        
        tvTitle.setText("智慧北京");
        TextView text = new TextView(mActivity);
        text.setText("首页");
        text.setTextColor(Color.RED);
        text.setTextSize(25);
        text.setGravity(Gravity.CENTER);
        
        flContent.addView(text);
        
	}
}
