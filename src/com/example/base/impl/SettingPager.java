package com.example.base.impl;

import android.app.Activity;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.example.base.BasePager;

public class SettingPager extends BasePager {

	public SettingPager(Activity activity) {
		super(activity);
		// TODO Auto-generated constructor stub
	}

	public void initData() {
		tvTitle.setText("����");
		imageButton.setVisibility(View.GONE);
		setSlidingMenuEnable(false);// ���������
		TextView text = new TextView(mActivity);
		text.setText("����");
		text.setTextColor(Color.RED);
		text.setTextSize(25);
		text.setGravity(Gravity.CENTER);

		flContent.addView(text);

	}

}
