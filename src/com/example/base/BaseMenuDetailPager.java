package com.example.base;

import android.app.Activity;
import android.view.View;

/**
 * 菜单详情基类
 * @author yuxuehai
 *
 */
public abstract class BaseMenuDetailPager {

	public Activity mActivity;
	
	public View mRootView;//根布局
	
	public BaseMenuDetailPager(Activity activity) {
		// TODO Auto-generated constructor stub
		this.mActivity = activity;
		mRootView = initView();
	}
	/**
	 * 初始化界面
	 */
	public abstract View initView();
	/**
	 * 初始化数据
	 */
	public void initData(){
		
	}
}
