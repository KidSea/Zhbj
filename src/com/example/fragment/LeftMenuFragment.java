package com.example.fragment;

import java.util.ArrayList;

import com.example.base.impl.NewsPager;
import com.example.bean.NewsData;
import com.example.bean.NewsData.NewsMenuData;
import com.example.zhbj52.MainActivity;
import com.example.zhbj52.R;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
/**
 * 侧边栏
 * @author yuxuehai
 *
 */
public class LeftMenuFragment extends BaseFragment {

	@ViewInject(R.id.lv_list)
	private ListView listView;
	private ArrayList<NewsMenuData> mMenuList;
	
	private int mCurrentPos;//当前点击的菜单项
	private MenuAdapter mAdapter;
	
	@Override
	public View initViews() {
		// TODO Auto-generated method stub
		View view = View.inflate(mActivity, R.layout.fragment_left_menu, null);
		ViewUtils.inject(this,view);
		
		
		return view;
	}
	public void initData(){
		listView.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				mCurrentPos = position;
				
				mAdapter.notifyDataSetChanged();
				
				setCurrentMenuDetailPager(position);
				
				toggleSlidingMenu();
			}
			
		});
			
	}
	/**
	 * 切换SlidingMenu的状态
	 * @param b
	 */
	protected void toggleSlidingMenu() {
		// TODO Auto-generated method stub
		MainActivity mainActivity = (MainActivity) mActivity;
	    SlidingMenu slidingMenu = mainActivity.getSlidingMenu();
	    
	    slidingMenu.toggle();//切换状态，显示时隐藏，隐藏时显示
	}
	/**
	 * 设置当前菜单详情页
	 * @param position
	 */
	protected void setCurrentMenuDetailPager(int position) {
		// TODO Auto-generated method stub
		MainActivity mainActivity = (MainActivity)mActivity;
		ContentFragment fragment= mainActivity.getContentFragment();//获取主页面Fragment
		NewsPager newsPager= fragment.getNewsCenterPager();//获取新闻中心页面
		newsPager.setCurrentMenuDetailPager(position);
	}
	//获取数据
	public void setMenuData(NewsData newsData){
		 mMenuList = newsData.data;
		 mAdapter = new MenuAdapter();
		 listView.setAdapter(mAdapter);
	}
	
	/**
	 * 侧边栏数据的适配器
	 * @author yuxuehai
	 *
	 */
	class MenuAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return mMenuList.size();
		}

		@Override
		public NewsMenuData getItem(int position) {
			// TODO Auto-generated method stub
			return mMenuList.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			View view = View.inflate(mActivity, R.layout.list_menu_item, null);
			TextView textView  = (TextView) view.findViewById(R.id.tv_title);
			
			NewsMenuData menuData = getItem(position);
			textView.setText(menuData.title);
			
			if(mCurrentPos == position){
				//红色
				textView.setEnabled(true);
			}else{
				//白色
				textView.setEnabled(false);
			}
			
			return view;
		}
		
	}
}
