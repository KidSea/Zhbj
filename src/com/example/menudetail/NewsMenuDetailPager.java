package com.example.menudetail;

import java.util.ArrayList;

import android.app.Activity;
import android.graphics.Color;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.base.BaseMenuDetailPager;
import com.example.base.TabDetailPager;
import com.example.bean.NewsData.NewsTabData;
import com.example.zhbj52.MainActivity;
import com.example.zhbj52.R;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.viewpagerindicator.TabPageIndicator;

/**
 * 新闻菜单详情页
 * 
 * @author yuxuehai
 * 
 */
public class NewsMenuDetailPager extends BaseMenuDetailPager implements
		OnPageChangeListener {

	private ViewPager mViewPager;

	private ArrayList<TabDetailPager> mArrayList;

	private ArrayList<NewsTabData> mNewsTabDatas;// 页签网络数据

	private TabPageIndicator indicator;

	private ImageButton imageButton;

	public NewsMenuDetailPager(Activity activity,
			ArrayList<NewsTabData> children) {
		super(activity);
		// TODO Auto-generated constructor stub
		mNewsTabDatas = children;
	}

	@Override
	public View initView() {
		// TODO Auto-generated method stub
		View view = View.inflate(mActivity, R.layout.news_menu_detail, null);
		mViewPager = (ViewPager) view.findViewById(R.id.vp_menu_detail);
		// 初始话Indicator1
		indicator = (TabPageIndicator) view.findViewById(R.id.indicator);
		ViewUtils.inject(this, view);

		// mViewPager.setOnPageChangeListener(this);//当viewpager与indicator绑定时，设置滑动监听需要设置给indicator
		indicator.setOnPageChangeListener(this);
		return view;
	}

	// 初始化数据
	public void initData() {
		mArrayList = new ArrayList<TabDetailPager>();

		// 初始化页签
		for (int i = 0; mNewsTabDatas.size() > i; i++) {
			TabDetailPager pager = new TabDetailPager(mActivity,
					mNewsTabDatas.get(i));
			mArrayList.add(pager);
		}
		mViewPager.setAdapter(new MenuDetailAdapter());
		// 必须在初始化Viewpager后调用
		indicator.setViewPager(mViewPager);
	}

	//
	@OnClick(R.id.btn_next)
	public void nextPage(View view) {
		int CurrentItem = mViewPager.getCurrentItem();
		mViewPager.setCurrentItem(++CurrentItem);
	}

	class MenuDetailAdapter extends PagerAdapter {

		/**
		 * 重写方法，得到标签标题
		 */
		@Override
		public CharSequence getPageTitle(int position) {
			// TODO Auto-generated method stub
			return mNewsTabDatas.get(position).title;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return mArrayList.size();
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			// TODO Auto-generated method stub
			return view == object;
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			// TODO Auto-generated method stub
			TabDetailPager pager = mArrayList.get(position);
			container.addView(pager.mRootView);
			pager.initData();
			return pager.mRootView;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			// TODO Auto-generated method stub
			container.removeView((View) object);
		}
	}

	@Override
	public void onPageScrolled(int position, float positionOffset,
			int positionOffsetPixels) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPageSelected(int position) {
		// TODO Auto-generated method stub
		MainActivity mainActivity = (MainActivity) mActivity;
		SlidingMenu slidingMenu = mainActivity.getSlidingMenu();

		if (position == 0) {
			slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		} else {
			slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
		}

	}

	@Override
	public void onPageScrollStateChanged(int state) {
		// TODO Auto-generated method stub

	}

}
