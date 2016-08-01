package com.example.menudetail;

import android.app.Activity;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.example.base.BaseMenuDetailPager;
/**
 * »¥¶¯²Ëµ¥ÏêÇéÒ³
 * @author yuxuehai
 *
 */
public class InteractMenuDetailPager extends BaseMenuDetailPager {

	public InteractMenuDetailPager(Activity activity) {
		super(activity);
		// TODO Auto-generated constructor stub
	}

	@Override
	public View initView() {
		// TODO Auto-generated method stub
        TextView text = new TextView(mActivity);
        text.setText("²Ëµ¥ÏêÇéÒ³-»¥¶¯");
        text.setTextColor(Color.RED);
        text.setTextSize(25);
        text.setGravity(Gravity.CENTER);
		return text;
	}

}
