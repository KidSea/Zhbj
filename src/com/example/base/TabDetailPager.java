package com.example.base;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.provider.MediaStore.Images;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.base.BaseMenuDetailPager;
import com.example.bean.NewsData;
import com.example.bean.NewsData.NewsTabData;
import com.example.bean.TabData;
import com.example.bean.TabData.TabNewsData;
import com.example.bean.TabData.TopNewsData;
import com.example.global.GlobalContants;
import com.example.menudetail.NewsMenuDetailPager;
import com.example.view.RefreshListView;
import com.example.view.RefreshListView.OnRefreshListener;
import com.example.zhbj52.NewsDetailActivity;
import com.example.zhbj52.R;
import com.example.zhbj52.utils.CacheUtils;
import com.example.zhbj52.utils.PrefUtils;
import com.google.gson.Gson;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.viewpagerindicator.CirclePageIndicator;

/**
 * 页签锟斤拷锟斤拷页
 * 
 * @author yuxuehai
 * 
 */

public class TabDetailPager extends BaseMenuDetailPager implements
		OnPageChangeListener {

	NewsTabData mTabData;
	private TextView tvText;
	private String mUrl;

	private TabData mTabDetailData;

	@ViewInject(R.id.vp_tab_detail)
	private ViewPager mViewPager;

	@ViewInject(R.id.tv_title)
	private TextView tvTitle;// 头锟斤拷锟斤拷锟脚憋拷锟斤拷
	private ArrayList<TopNewsData> mTopNewsList;

	@ViewInject(R.id.indicator)
	private CirclePageIndicator mIndicator;// 头锟斤拷锟斤拷锟斤拷位锟斤拷指示锟斤拷

	@ViewInject(R.id.lv_list)
	private RefreshListView lvList;// 锟斤拷锟斤拷锟叫憋拷
	private ArrayList<TabNewsData> mNewsList;// 锟斤拷锟斤拷锟斤拷锟捷硷拷锟斤拷
	private NewsAdapter mNewsAdapter;
	private String mMoreUrl;// 更多的数据连接

	private Handler mHandler;

	public TabDetailPager(Activity activity, NewsTabData tabData) {
		super(activity);
		mTabData = tabData;

		mUrl = GlobalContants.SERVER_URL + mTabData.url;
	}

	@Override
	public View initView() {
		View view = View.inflate(mActivity, R.layout.tab_detail_pager, null);
		// 锟斤拷锟斤拷头锟斤拷锟斤拷
		View headerView = View.inflate(mActivity, R.layout.list_header_topnews,
				null);

		ViewUtils.inject(this, view);
		ViewUtils.inject(this, headerView);

		// 锟斤拷头锟斤拷锟斤拷锟斤拷锟斤拷头锟斤拷锟斤拷锟斤拷式锟斤拷锟斤拷Listview
		lvList.addHeaderView(headerView);

		lvList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				int headerViewCount = lvList.getHeaderViewsCount();
				position = position - headerViewCount;

				System.out.println("第" + position + "个Item被点击了");

				TabNewsData news = mNewsList.get(position);

				// read_ids:1101,1102,1103
				String readIds = PrefUtils.getString(mActivity, "read_ids", "");
				if (!readIds.contains(news.id + "")) {// 只有不存在才保存
					readIds = readIds + news.id + ",";
					PrefUtils.setString(mActivity, "read_ids", readIds);
				}
				// 要将被点击的Item的textview颜色改变，view对象就是当前被点击的对象
				TextView textview = (TextView) view.findViewById(R.id.tv_title);
				textview.setTextColor(Color.GRAY);

				// mNewsAdapter.notifyDataSetChanged();

				// 点击调到新闻详情页
				Intent intent = new Intent(mActivity, NewsDetailActivity.class);
				intent.putExtra("url", news.url);
				mActivity.startActivity(intent);
			}
		});
		// mViewPager.setOnPageChangeListener(this);

		lvList.setOnRefreshListener(new OnRefreshListener() {

			@Override
			public void onRefresh() {
				// TODO Auto-generated method stub
				getDataFromServer();
			}

			@Override
			public void onLoadMore() {
				// TODO Auto-generated method stub
				// 判断是否有下一页
				if (mMoreUrl != null) {
					// 有下一页
					getMoreDataFromServer();
				} else {
					// 没有更多数据
					Toast.makeText(mActivity, "没有更多数据了", Toast.LENGTH_SHORT)
							.show();
					// 没有数据时也要收起
					lvList.onRefreshComplete(true);
				}
			}
		});

		return view;
	}

	protected void getMoreDataFromServer() {
		// TODO Auto-generated method stub
		HttpUtils utils = new HttpUtils();
		utils.send(HttpMethod.GET, mMoreUrl, new RequestCallBack<String>() {

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				// TODO Auto-generated method stub
				String result = (String) responseInfo.result;
				// System.out.println("锟斤拷锟截斤拷锟�"+ result);

				parseData(result, true);

				// 收起下拉刷新
				lvList.onRefreshComplete(true);
			}

			@Override
			public void onFailure(HttpException error, String msg) {
				// TODO Auto-generated method stub
				Toast.makeText(mActivity, msg, Toast.LENGTH_SHORT).show();
				error.printStackTrace();
				// 收起下拉刷新
				lvList.onRefreshComplete(false);
			}
		});
	}

	/**
	 * 加载下一页
	 */
	@Override
	public void initData() {
		
		String cache = CacheUtils.getCache(mUrl, mActivity);
		if (!TextUtils.isEmpty(cache)) {
			parseData(cache, false);
		}
		
		getDataFromServer();
	}

	private void getDataFromServer() {
		// TODO Auto-generated method stub
		HttpUtils utils = new HttpUtils();
		utils.send(HttpMethod.GET, mUrl, new RequestCallBack<String>() {

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				// TODO Auto-generated method stub
				String result = (String) responseInfo.result;
				// System.out.println("锟斤拷锟截斤拷锟�"+ result);

				parseData(result, false);
				
				CacheUtils.setCache(mUrl, result, mActivity);

				// 收起下拉刷新
				lvList.onRefreshComplete(true);
			}

			@Override
			public void onFailure(HttpException error, String msg) {
				// TODO Auto-generated method stub
				Toast.makeText(mActivity, msg, Toast.LENGTH_SHORT).show();
				error.printStackTrace();
				// 收起下拉刷新
				lvList.onRefreshComplete(false);
			}
		});
	}

	protected void parseData(String result, boolean isMore) {
		// TODO Auto-generated method stub
		Gson gson = new Gson();
		mTabDetailData = gson.fromJson(result, TabData.class);
		// System.out.println(mTabDetailData);

		String moreUrl = mTabDetailData.data.more;
		if (!TextUtils.isEmpty(moreUrl)) {
			mMoreUrl = GlobalContants.SERVER_URL + moreUrl;
		} else {
			moreUrl = null;
		}

		if (!isMore) {
			mTopNewsList = mTabDetailData.data.topnews;
			mNewsList = mTabDetailData.data.news;

			if (mTopNewsList != null) {
				mViewPager.setAdapter(new TopNewsAdapter());
				mIndicator.setViewPager(mViewPager);
				mIndicator.setSnap(true);
				mIndicator.setOnPageChangeListener(this);

				mIndicator.onPageSelected(0);

				tvTitle.setText(mTopNewsList.get(0).title);
			}

			if (mNewsList != null) {
				mNewsAdapter = new NewsAdapter();
				lvList.setAdapter(mNewsAdapter);
			}

			if (mHandler == null) {
				mHandler = new Handler() {
					public void handleMessage(android.os.Message msg) {
						int currentItem = mViewPager.getCurrentItem();
						currentItem++;
						if(currentItem > mTopNewsList.size()-1){
							currentItem = 0;
						}
						
						mViewPager.setCurrentItem(currentItem);
						
						mHandler.sendEmptyMessageDelayed(0, 2000);//继续发送延时3秒
						
					};
				};
				// 保证启动自动轮播逻辑只执行一次
				mHandler.sendEmptyMessageDelayed(0, 2000);//发送延时3秒
				
				mViewPager.setOnTouchListener(new OnTouchListener() {

					@Override
					public boolean onTouch(View v, MotionEvent event) {
						switch (event.getAction()) {
						case MotionEvent.ACTION_DOWN:
							System.out.println("ACTION_DOWN");
							// 停止广告自动轮播
							// 删除handler的所有消息
							mHandler.removeCallbacksAndMessages(null);
							// mHandler.post(new Runnable() {
							//
							// @Override
							// public void run() {
							// //在主线程运行
							// }
							// });
							break;
						case MotionEvent.ACTION_CANCEL:// 取消事件,
														// 当按下viewpager后,直接滑动listview,导致抬起事件无法响应,但会走此事件
							System.out.println("ACTION_CANCEL");
							// 启动广告
							mHandler.sendEmptyMessageDelayed(0, 3000);
							break;
						case MotionEvent.ACTION_UP:
							System.out.println("ACTION_UP");
							// 启动广告
							mHandler.sendEmptyMessageDelayed(0, 3000);
							break;

						default:
							break;
						}
						return false;
					}
				});
			}
		} else {
			// 加载更多
			ArrayList<TabNewsData> moreNews = mTabDetailData.data.news;
			mNewsList.addAll(moreNews);
			// 数据刷新
			mNewsAdapter.notifyDataSetChanged();
		}
	}

	/**
	 * 头锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷
	 * 
	 * @author yuxuehai
	 * 
	 */
	class TopNewsAdapter extends PagerAdapter {

		private BitmapUtils utils;

		public TopNewsAdapter() {
			utils = new BitmapUtils(mActivity);
			utils.configDefaultLoadingImage(R.drawable.topnews_item_default);// 锟斤拷锟斤拷默锟斤拷图片
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return mTabDetailData.data.topnews.size();
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			// TODO Auto-generated method stub
			return view == object;
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			// TODO Auto-generated method stub
			ImageView imageView = new ImageView(mActivity);
			// imageView.setImageResource(R.drawable.topnews_item_default);
			imageView.setScaleType(ScaleType.FIT_XY);// 锟斤拷锟节控硷拷锟斤拷锟酵计�

			TopNewsData topNewsData = mTopNewsList.get(position);
			utils.display(imageView, topNewsData.topimage);// 锟斤拷锟斤拷ImageView锟斤拷锟斤拷偷锟街吠计�

			container.addView(imageView);
			return imageView;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			// TODO Auto-generated method stub
			container.removeView((View) object);
		}
	}

	/**
	 * 锟斤拷锟斤拷锟叫憋拷锟斤拷锟斤拷锟斤拷锟�
	 * 
	 * @author yuxuehai
	 * 
	 */
	class NewsAdapter extends BaseAdapter {
		private BitmapUtils bitmapUtils;

		public NewsAdapter() {
			bitmapUtils = new BitmapUtils(mActivity);
			bitmapUtils
					.configDefaultLoadingImage(R.drawable.pic_item_list_default);
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return mNewsList.size();
		}

		@Override
		public TabNewsData getItem(int position) {
			// TODO Auto-generated method stub
			return mNewsList.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			ViewHolder holder;
			if (convertView == null) {
				convertView = View.inflate(mActivity, R.layout.list_news_item,
						null);
				holder = new ViewHolder();
				holder.tvTitle = (TextView) convertView
						.findViewById(R.id.tv_title);
				holder.tvDate = (TextView) convertView
						.findViewById(R.id.tv_date);
				holder.ivPic = (ImageView) convertView
						.findViewById(R.id.iv_pic);

				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			TabNewsData item = getItem(position);

			holder.tvTitle.setText(item.title);
			holder.tvDate.setText(item.pubdate);

			// 根据状态更改标记已读未读
			String readIds = PrefUtils.getString(mActivity, "read_ids", "");
			if (readIds.contains(item.id + "")) {// 只有不存在才保存
				holder.tvTitle.setTextColor(Color.GRAY);
			} else {
				holder.tvTitle.setTextColor(Color.BLACK);
			}
			bitmapUtils.display(holder.ivPic, item.listimage);

			return convertView;
		}

	}

	static class ViewHolder {
		public TextView tvTitle;
		public TextView tvDate;
		public ImageView ivPic;
	}

	@Override
	public void onPageScrolled(int position, float positionOffset,
			int positionOffsetPixels) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPageSelected(int position) {
		// TODO Auto-generated method stub
		TopNewsData newsData = mTopNewsList.get(position);
		tvTitle.setText(newsData.title);
	}

	@Override
	public void onPageScrollStateChanged(int state) {
		// TODO Auto-generated method stub

	}
}