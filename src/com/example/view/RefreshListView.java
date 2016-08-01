package com.example.view;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.crypto.spec.IvParameterSpec;

import com.example.zhbj52.R;
import com.lidroid.xutils.bitmap.callback.DefaultBitmapLoadCallBack;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.ProgressBar;
import android.widget.TextView;
/**
 * 下拉刷新ListView
 * 
 * @author yuxuehai
 * 
 */
public class RefreshListView extends ListView implements android.widget.AbsListView.OnScrollListener{

	// 定义刷新常量
	private static final int STATE_PULL_TO_REFRESH = 1;
	private static final int STATE_RELEASE_TO_REFRESH = 2;
	private static final int STATE_REFRESHING = 3;

	private int mCurrentState = STATE_PULL_TO_REFRESH;// 当前状态

	private View mHeaderView;
	private int startY;
	private int mHeaderViewHeight;
	private TextView tvTitle;
	private TextView tvTime;
	private ImageView ivArrow;
	private ProgressBar pbProgress;

	private RotateAnimation animDown;
	private RotateAnimation animUp;

	public RefreshListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
		initHeaderView();
		initFooterView();
	}

	public RefreshListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		initHeaderView();
		initFooterView();
	}

	public RefreshListView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		initHeaderView();
		initFooterView();
	}

	/**
	 * 动画效果
	 */
	private void initAnimation() {
		animUp = new RotateAnimation(0, -180, Animation.RELATIVE_TO_SELF, 0.5f,
				Animation.RELATIVE_TO_SELF, 0.5f);
		animUp.setDuration(200);
		animUp.setFillAfter(true);

		animDown = new RotateAnimation(-180, 0, Animation.RELATIVE_TO_SELF,
				0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
		animDown.setDuration(200);
		animDown.setFillAfter(true);

	}

	/**
	 * 初始化头部布局
	 */
	private void initHeaderView() {
		mHeaderView = View.inflate(getContext(), R.layout.refresh_header, null);
		this.addHeaderView(mHeaderView);

		// 找到控件
		tvTitle = (TextView) mHeaderView.findViewById(R.id.tv_title);
		tvTime = (TextView) mHeaderView.findViewById(R.id.tv_time);
		ivArrow = (ImageView) mHeaderView.findViewById(R.id.iv_arr);
		pbProgress = (ProgressBar) mHeaderView.findViewById(R.id.pb_progress);

		mHeaderView.measure(0, 0);
		mHeaderViewHeight = mHeaderView.getMeasuredHeight();

		mHeaderView.setPadding(0, -mHeaderViewHeight, 0, 0);// 隐藏头布局

		initAnimation();
		setCurrenttime();
	}

	// 初始化角布局
	private void initFooterView() {
		mFooterView = View.inflate(getContext(), //加载脚布局
				R.layout.refresh_footer,null);
		this.addFooterView(mFooterView);
		
		mFooterView.measure(0, 0);
		mFooterViewHeight = mFooterView.getMeasuredHeight();
		
		mFooterView.setPadding(0, -mFooterViewHeight, 0, 0);
		
		this.setOnScrollListener(this);//滑动监听
	}

	// 设置更新时间
	private void setCurrenttime() {

		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String time = format.format(new Date());

		tvTime.setText(time);
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		// TODO Auto-generated method stub
		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			startY = (int) ev.getY();

			break;
		case MotionEvent.ACTION_MOVE:
			if (startY == -1) {// 确保startY有效
				startY = (int) ev.getY();
			}

			if (mCurrentState == STATE_REFRESHING) {
				// 如果是正在刷新, 跳出循环
				break;
			}

			int endY = (int) ev.getY();

			int dy = endY - startY;// 移动偏移量

			int firstVisiblePosition = getFirstVisiblePosition();// 当前显示的第一个item的位置

			if (dy > 0 && firstVisiblePosition == 0) {
				// 只有偏移量大于0 且当前是第一个Item才能下拉刷新
				int padding = dy - mHeaderViewHeight;
				mHeaderView.setPadding(0, padding, 0, 0);

				if (padding > 0 && mCurrentState != STATE_RELEASE_TO_REFRESH) {
					// 改为松开状态
					mCurrentState = STATE_RELEASE_TO_REFRESH;
					refreshstate();
				} else if (padding < 0
						&& mCurrentState != STATE_PULL_TO_REFRESH) {
					// 改为下拉状态
					mCurrentState = STATE_PULL_TO_REFRESH;
					refreshstate();
				}

				return true;
			}
			break;
		case MotionEvent.ACTION_UP:
			startY = -1;

			if (mCurrentState == STATE_RELEASE_TO_REFRESH) {
				mCurrentState = STATE_REFRESHING;
				refreshstate();

				// 完整展示头布局
				mHeaderView.setPadding(0, 0, 0, 0);

				// 进行回调
				if (mListener != null) {
					mListener.onRefresh();
				}
			} else if (mCurrentState == STATE_PULL_TO_REFRESH) {
				// 隐藏头布局
				mHeaderView.setPadding(0, -mHeaderViewHeight, 0, 0);
			}
			break;
		default:
			break;
		}

		return super.onTouchEvent(ev);
	}

	/**
	 * 根据当前状态进行更新
	 */
	private void refreshstate() {
		// TODO Auto-generated method stub
		switch (mCurrentState) {
		case STATE_PULL_TO_REFRESH:
			tvTitle.setText("下拉刷新");
			ivArrow.setAnimation(animDown);
			pbProgress.setVisibility(View.INVISIBLE);
			ivArrow.setVisibility(View.VISIBLE);
			break;
		case STATE_RELEASE_TO_REFRESH:
			ivArrow.setAnimation(animUp);
			tvTitle.setText("松开刷新");
			pbProgress.setVisibility(View.INVISIBLE);
			ivArrow.setVisibility(View.VISIBLE);
			break;
		case STATE_REFRESHING:
			tvTitle.setText("正在刷新...");

			ivArrow.clearAnimation();// 清除动画才能设置

			pbProgress.setVisibility(View.VISIBLE);
			ivArrow.setVisibility(View.INVISIBLE);

			break;
		default:
			break;

		}
	}

	/**
	 * 刷新结束，收起控件
	 */
	public void onRefreshComplete(boolean success) {
		if(!isLoadMore){
			mHeaderView.setPadding(0, -mHeaderViewHeight, 0, 0);

			mCurrentState = STATE_PULL_TO_REFRESH;
			tvTitle.setText("下拉刷新");
			pbProgress.setVisibility(View.INVISIBLE);
			ivArrow.setVisibility(View.VISIBLE);
			
			if (success) {// 只有刷新成功后才更新

				setCurrenttime();
			}
		}else{
			//加载更多
			mFooterView.setPadding(0, -mFooterViewHeight, 0, 0);
			isLoadMore = false;
		}

	}

	// 3. 定义成员变量,接收监听对象
	private OnRefreshListener mListener;
	private View mFooterView;
	private int mFooterViewHeight;

	/**
	 * 2. 暴露接口,设置监听
	 */
	public void setOnRefreshListener(OnRefreshListener listener) {
		mListener = listener;
	}

	/**
	 * 1. 下拉刷新的回调接口
	 * 
	 * @author Kevin
	 * @date 2015-10-21
	 */
	public interface OnRefreshListener {
		public void onRefresh();

		// 下拉加载更多
		public void onLoadMore();
	}
	private boolean isLoadMore = false;//标记是否加载更多
    //滑动状态变化
	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		// TODO Auto-generated method stub
		if(scrollState == SCROLL_STATE_IDLE){//空闲状态
			int lastVisiblePosition = getLastVisiblePosition();
			
			if(lastVisiblePosition == getCount() - 1 && !isLoadMore){//当前最后一个标签及没有加载更多
			  //到底了
			   isLoadMore = true;
			   mFooterView.setPadding(0, 0, 0, 0);
			   
			   setSelection(getCount() - 1);//让下滑到最后时自动出来加载更多
			   
			    //通知主页面加载更多
				// 进行回调
				if (mListener != null) {
					mListener.onLoadMore();
				}
			}
		}
	}
    //滑动过程回调
	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		// TODO Auto-generated method stub
		
	}


}
