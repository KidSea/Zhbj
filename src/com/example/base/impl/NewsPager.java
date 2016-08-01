package com.example.base.impl;

import java.util.ArrayList;

import android.app.Activity;
import android.graphics.Color;
import android.provider.Settings.Global;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.base.BaseMenuDetailPager;
import com.example.base.BasePager;
import com.example.bean.NewsData;
import com.example.bean.NewsData.NewsMenuData;
import com.example.fragment.LeftMenuFragment;
import com.example.global.GlobalContants;
import com.example.menudetail.InteractMenuDetailPager;
import com.example.menudetail.NewsMenuDetailPager;
import com.example.menudetail.PhotoMenuDetailPager;
import com.example.menudetail.TopicMenuDetailPager;
import com.example.zhbj52.MainActivity;
import com.example.zhbj52.utils.CacheUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.google.gson.Gson;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

public class NewsPager extends BasePager {

	private ArrayList<BaseMenuDetailPager> mPagers;// 菜单详情集合
	private NewsData newsData;

	public NewsPager(Activity activity) {
		super(activity);
		// TODO Auto-generated constructor stub
	}

	public void initData() {

		setSlidingMenuEnable(true);// 开启侧边栏

		// 先判断是否有缓存，有的话直接调缓存
		String cache = CacheUtils.getCache(GlobalContants.CATEGORIES_URL,
				mActivity);
		if (!TextUtils.isEmpty(cache)) {
			System.out.println("发现缓存了");
			parseData(cache);
		}
		
			// 请求服务器，获取数据
			// 开源框架 ,Xutils
			getDatafromServer();


	}

	/**
	 * 从服务器获得数据
	 */
	private void getDatafromServer() {
		// TODO Auto-generated method stub
		HttpUtils utils = new HttpUtils();
		// 使用HTTP发送请求，
		utils.send(HttpMethod.GET, GlobalContants.CATEGORIES_URL,
				new RequestCallBack<String>() {
					// 成功，在主线程调用
					@Override
					public void onSuccess(ResponseInfo<String> responseInfo) {
						// TODO Auto-generated method stub
						String result = (String) responseInfo.result;
						// System.out.println("返回结果"+ result);

						parseData(result);
						// 写缓存
						CacheUtils.setCache(GlobalContants.CATEGORIES_URL,
								result, mActivity);
					}

					// 失败，在主线程调用
					@Override
					public void onFailure(HttpException error, String msg) {
						// TODO Auto-generated method stub
						Toast.makeText(mActivity, msg, Toast.LENGTH_SHORT)
								.show();
						error.printStackTrace();
					}
				});
	}

	/**
	 * Gson解析
	 * 
	 * @param result
	 */
	protected void parseData(String result) {
		// TODO Auto-generated method stub
		Gson gson = new Gson();
		newsData = gson.fromJson(result, NewsData.class);

		// 刷新侧边栏数据
		MainActivity mainActivity = (MainActivity) mActivity;
		LeftMenuFragment leftMenuFragment = mainActivity.getLeftMenuFragment();
		leftMenuFragment.setMenuData(newsData);

		// 准备菜单详情页
		mPagers = new ArrayList<BaseMenuDetailPager>();
		mPagers.add(new NewsMenuDetailPager(mActivity,
				newsData.data.get(0).children));
		mPagers.add(new TopicMenuDetailPager(mActivity));
		mPagers.add(new PhotoMenuDetailPager(mActivity, btnPhoto));
		mPagers.add(new InteractMenuDetailPager(mActivity));

		setCurrentMenuDetailPager(0);// 设置菜单详情页新闻为默认当前页
	}

	/**
	 * 设置当前菜单详情页
	 */
	public void setCurrentMenuDetailPager(int position) {
		BaseMenuDetailPager pager = mPagers.get(position);// 获取当前显示的菜单页

		flContent.removeAllViews();// 清除之前的页面
		flContent.addView(pager.mRootView);// 将菜单详情页的布局设置给布局
		// 设置当前页的标题
		NewsMenuData data = newsData.data.get(position);
		tvTitle.setText(data.title);

		pager.initData();// 初始化当前页面数据

		// 如果是组图页面，需要展示切换按钮
		if (pager instanceof PhotoMenuDetailPager) {
			btnPhoto.setVisibility(View.VISIBLE);
		} else {
			// 隐藏切换按钮
			btnPhoto.setVisibility(View.GONE);
		}
	}
}
