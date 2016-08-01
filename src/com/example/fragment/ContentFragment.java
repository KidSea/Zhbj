package com.example.fragment;

import java.util.ArrayList;

import com.example.base.BasePager;
import com.example.base.impl.GovPager;
import com.example.base.impl.HomePager;
import com.example.base.impl.NewsPager;
import com.example.base.impl.SettingPager;
import com.example.base.impl.SmartPager;
import com.example.zhbj52.R;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

/**
 * 主页内容
 * 
 * @author yuxuehai
 * 
 */
public class ContentFragment extends BaseFragment {

	@ViewInject(R.id.rb_group)
	private RadioGroup radioGroup;

	@ViewInject(R.id.vp_content)
	private ViewPager mViewPager;

	private ArrayList<BasePager> mPagerlist;
	
	@Override
	public View initViews() {
		// TODO Auto-generated method stub
		View view = View.inflate(mActivity, R.layout.fragment_content_menu,
				null);
		// radioGroup = (RadioGroup) view.findViewById(R.id.rb_group);
		ViewUtils.inject(this, view);// 注入view和事件

		return view;
	}

	public void initData() {
		radioGroup.check(R.id.rb_home);
		
		//初始化5个子页面
		mPagerlist = new ArrayList<BasePager>();
//		for(int i=0;i<5;i++){
//			BasePager pager = new BasePager(mActivity);
//			mPagerlist.add(pager);
//		}
		mPagerlist.add(new HomePager(mActivity));
		mPagerlist.add(new NewsPager(mActivity));
		mPagerlist.add(new SmartPager(mActivity));
		mPagerlist.add(new GovPager(mActivity));
		mPagerlist.add(new SettingPager(mActivity));
		mViewPager.setAdapter(new ContentAdapter());
		
		//设置RADIOGROUP的事件监听
        radioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// TODO Auto-generated method stub
				switch(checkedId){
				case R.id.rb_home:
					mViewPager.setCurrentItem(0);
					break;
				case R.id.rb_news:
					mViewPager.setCurrentItem(1);
					break;
				case R.id.rb_smart:
					mViewPager.setCurrentItem(2);
					break;
				case R.id.rb_gov:
					mViewPager.setCurrentItem(3);
					break;
				case R.id.rb_setting:
					mViewPager.setCurrentItem(4);
					break;
				}
			}
		});
        mViewPager.setOnPageChangeListener(new OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int position) {
				// TODO Auto-generated method stub
				mPagerlist.get(position).initData();//获取当前被选中的页面，初始化数据
			}
			
			@Override
			public void onPageScrolled(int position, float positionOffset,
					int positionOffsetPixels) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onPageScrollStateChanged(int state) {
				// TODO Auto-generated method stub
				
			}
		});
        mPagerlist.get(0).initData();
	}

	class ContentAdapter extends PagerAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return mPagerlist.size();
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			// TODO Auto-generated method stub
			return view  == object;
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			// TODO Auto-generated method stub
			BasePager pager = mPagerlist.get(position);
			container.addView(pager.mRootView);
		   // pager.initData();//初始化界面不放在此处，否则会预加载下面一个页面
			return pager.mRootView;
		}
		
		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			// TODO Auto-generated method stub
			container.removeView((View)object);
		}

	}
	/**
	 * 获得新闻中心页面
	 * @return
	 */
	public NewsPager getNewsCenterPager(){
		
		return (NewsPager) mPagerlist.get(1);
	}
}
