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

	private ArrayList<BaseMenuDetailPager> mPagers;// �˵����鼯��
	private NewsData newsData;

	public NewsPager(Activity activity) {
		super(activity);
		// TODO Auto-generated constructor stub
	}

	public void initData() {

		setSlidingMenuEnable(true);// ���������

		// ���ж��Ƿ��л��棬�еĻ�ֱ�ӵ�����
		String cache = CacheUtils.getCache(GlobalContants.CATEGORIES_URL,
				mActivity);
		if (!TextUtils.isEmpty(cache)) {
			System.out.println("���ֻ�����");
			parseData(cache);
		}
		
			// �������������ȡ����
			// ��Դ��� ,Xutils
			getDatafromServer();


	}

	/**
	 * �ӷ������������
	 */
	private void getDatafromServer() {
		// TODO Auto-generated method stub
		HttpUtils utils = new HttpUtils();
		// ʹ��HTTP��������
		utils.send(HttpMethod.GET, GlobalContants.CATEGORIES_URL,
				new RequestCallBack<String>() {
					// �ɹ��������̵߳���
					@Override
					public void onSuccess(ResponseInfo<String> responseInfo) {
						// TODO Auto-generated method stub
						String result = (String) responseInfo.result;
						// System.out.println("���ؽ��"+ result);

						parseData(result);
						// д����
						CacheUtils.setCache(GlobalContants.CATEGORIES_URL,
								result, mActivity);
					}

					// ʧ�ܣ������̵߳���
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
	 * Gson����
	 * 
	 * @param result
	 */
	protected void parseData(String result) {
		// TODO Auto-generated method stub
		Gson gson = new Gson();
		newsData = gson.fromJson(result, NewsData.class);

		// ˢ�²��������
		MainActivity mainActivity = (MainActivity) mActivity;
		LeftMenuFragment leftMenuFragment = mainActivity.getLeftMenuFragment();
		leftMenuFragment.setMenuData(newsData);

		// ׼���˵�����ҳ
		mPagers = new ArrayList<BaseMenuDetailPager>();
		mPagers.add(new NewsMenuDetailPager(mActivity,
				newsData.data.get(0).children));
		mPagers.add(new TopicMenuDetailPager(mActivity));
		mPagers.add(new PhotoMenuDetailPager(mActivity, btnPhoto));
		mPagers.add(new InteractMenuDetailPager(mActivity));

		setCurrentMenuDetailPager(0);// ���ò˵�����ҳ����ΪĬ�ϵ�ǰҳ
	}

	/**
	 * ���õ�ǰ�˵�����ҳ
	 */
	public void setCurrentMenuDetailPager(int position) {
		BaseMenuDetailPager pager = mPagers.get(position);// ��ȡ��ǰ��ʾ�Ĳ˵�ҳ

		flContent.removeAllViews();// ���֮ǰ��ҳ��
		flContent.addView(pager.mRootView);// ���˵�����ҳ�Ĳ������ø�����
		// ���õ�ǰҳ�ı���
		NewsMenuData data = newsData.data.get(position);
		tvTitle.setText(data.title);

		pager.initData();// ��ʼ����ǰҳ������

		// �������ͼҳ�棬��Ҫչʾ�л���ť
		if (pager instanceof PhotoMenuDetailPager) {
			btnPhoto.setVisibility(View.VISIBLE);
		} else {
			// �����л���ť
			btnPhoto.setVisibility(View.GONE);
		}
	}
}
