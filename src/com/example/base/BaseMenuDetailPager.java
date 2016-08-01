package com.example.base;

import android.app.Activity;
import android.view.View;

/**
 * �˵��������
 * @author yuxuehai
 *
 */
public abstract class BaseMenuDetailPager {

	public Activity mActivity;
	
	public View mRootView;//������
	
	public BaseMenuDetailPager(Activity activity) {
		// TODO Auto-generated constructor stub
		this.mActivity = activity;
		mRootView = initView();
	}
	/**
	 * ��ʼ������
	 */
	public abstract View initView();
	/**
	 * ��ʼ������
	 */
	public void initData(){
		
	}
}
