package com.example.base;

import com.example.zhbj52.MainActivity;
import com.example.zhbj52.R;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

/**
 * 主页下几个主页面的基类
 * 
 * @author yuxuehai
 * 
 */
public class BasePager {

	public Activity mActivity;
	public View mRootView;// 布局界面

	public TextView tvTitle;
	public FrameLayout flContent;

	public ImageButton imageButton;
	public ImageButton btnPhoto;



	public BasePager(Activity activity) {
		this.mActivity = activity;
		initViews();
	}

	/**
	 * 初始化布局
	 */
	public void initViews() {
		mRootView = View.inflate(mActivity, R.layout.base_pager, null);

	    btnPhoto = (ImageButton) mRootView.findViewById(R.id.btn_photo);
		
		tvTitle = (TextView) mRootView.findViewById(R.id.tv_title);
		flContent = (FrameLayout) mRootView.findViewById(R.id.fl_content);
		imageButton = (ImageButton) mRootView.findViewById(R.id.btn_menu);
		
		
		imageButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				toggleSlidingMenu();
			}
		});
	}

	/**
	 * 切换SlidingMenu的状态
	 * 
	 * @param b
	 */
	protected void toggleSlidingMenu() {
		// TODO Auto-generated method stub
		MainActivity mainActivity = (MainActivity) mActivity;
		SlidingMenu slidingMenu = mainActivity.getSlidingMenu();

		slidingMenu.toggle();// 切换状态，显示时隐藏，隐藏时显示
	}

	/**
	 * 初始化数据
	 */
	public void initData() {

	}

	/**
	 * 设置侧边栏可用？
	 * 
	 * @param enable
	 */
	public void setSlidingMenuEnable(boolean enable) {
		MainActivity mainUi = (MainActivity) mActivity;

		SlidingMenu slidingMenu = mainUi.getSlidingMenu();
		if (enable) {
			slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		} else {
			slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
		}
	}
}
