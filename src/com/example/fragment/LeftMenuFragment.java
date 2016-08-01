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
 * �����
 * @author yuxuehai
 *
 */
public class LeftMenuFragment extends BaseFragment {

	@ViewInject(R.id.lv_list)
	private ListView listView;
	private ArrayList<NewsMenuData> mMenuList;
	
	private int mCurrentPos;//��ǰ����Ĳ˵���
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
	 * �л�SlidingMenu��״̬
	 * @param b
	 */
	protected void toggleSlidingMenu() {
		// TODO Auto-generated method stub
		MainActivity mainActivity = (MainActivity) mActivity;
	    SlidingMenu slidingMenu = mainActivity.getSlidingMenu();
	    
	    slidingMenu.toggle();//�л�״̬����ʾʱ���أ�����ʱ��ʾ
	}
	/**
	 * ���õ�ǰ�˵�����ҳ
	 * @param position
	 */
	protected void setCurrentMenuDetailPager(int position) {
		// TODO Auto-generated method stub
		MainActivity mainActivity = (MainActivity)mActivity;
		ContentFragment fragment= mainActivity.getContentFragment();//��ȡ��ҳ��Fragment
		NewsPager newsPager= fragment.getNewsCenterPager();//��ȡ��������ҳ��
		newsPager.setCurrentMenuDetailPager(position);
	}
	//��ȡ����
	public void setMenuData(NewsData newsData){
		 mMenuList = newsData.data;
		 mAdapter = new MenuAdapter();
		 listView.setAdapter(mAdapter);
	}
	
	/**
	 * ��������ݵ�������
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
				//��ɫ
				textView.setEnabled(true);
			}else{
				//��ɫ
				textView.setEnabled(false);
			}
			
			return view;
		}
		
	}
}
