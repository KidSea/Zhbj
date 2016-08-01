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
 * ����ˢ��ListView
 * 
 * @author yuxuehai
 * 
 */
public class RefreshListView extends ListView implements android.widget.AbsListView.OnScrollListener{

	// ����ˢ�³���
	private static final int STATE_PULL_TO_REFRESH = 1;
	private static final int STATE_RELEASE_TO_REFRESH = 2;
	private static final int STATE_REFRESHING = 3;

	private int mCurrentState = STATE_PULL_TO_REFRESH;// ��ǰ״̬

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
	 * ����Ч��
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
	 * ��ʼ��ͷ������
	 */
	private void initHeaderView() {
		mHeaderView = View.inflate(getContext(), R.layout.refresh_header, null);
		this.addHeaderView(mHeaderView);

		// �ҵ��ؼ�
		tvTitle = (TextView) mHeaderView.findViewById(R.id.tv_title);
		tvTime = (TextView) mHeaderView.findViewById(R.id.tv_time);
		ivArrow = (ImageView) mHeaderView.findViewById(R.id.iv_arr);
		pbProgress = (ProgressBar) mHeaderView.findViewById(R.id.pb_progress);

		mHeaderView.measure(0, 0);
		mHeaderViewHeight = mHeaderView.getMeasuredHeight();

		mHeaderView.setPadding(0, -mHeaderViewHeight, 0, 0);// ����ͷ����

		initAnimation();
		setCurrenttime();
	}

	// ��ʼ���ǲ���
	private void initFooterView() {
		mFooterView = View.inflate(getContext(), //���ؽŲ���
				R.layout.refresh_footer,null);
		this.addFooterView(mFooterView);
		
		mFooterView.measure(0, 0);
		mFooterViewHeight = mFooterView.getMeasuredHeight();
		
		mFooterView.setPadding(0, -mFooterViewHeight, 0, 0);
		
		this.setOnScrollListener(this);//��������
	}

	// ���ø���ʱ��
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
			if (startY == -1) {// ȷ��startY��Ч
				startY = (int) ev.getY();
			}

			if (mCurrentState == STATE_REFRESHING) {
				// ���������ˢ��, ����ѭ��
				break;
			}

			int endY = (int) ev.getY();

			int dy = endY - startY;// �ƶ�ƫ����

			int firstVisiblePosition = getFirstVisiblePosition();// ��ǰ��ʾ�ĵ�һ��item��λ��

			if (dy > 0 && firstVisiblePosition == 0) {
				// ֻ��ƫ��������0 �ҵ�ǰ�ǵ�һ��Item��������ˢ��
				int padding = dy - mHeaderViewHeight;
				mHeaderView.setPadding(0, padding, 0, 0);

				if (padding > 0 && mCurrentState != STATE_RELEASE_TO_REFRESH) {
					// ��Ϊ�ɿ�״̬
					mCurrentState = STATE_RELEASE_TO_REFRESH;
					refreshstate();
				} else if (padding < 0
						&& mCurrentState != STATE_PULL_TO_REFRESH) {
					// ��Ϊ����״̬
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

				// ����չʾͷ����
				mHeaderView.setPadding(0, 0, 0, 0);

				// ���лص�
				if (mListener != null) {
					mListener.onRefresh();
				}
			} else if (mCurrentState == STATE_PULL_TO_REFRESH) {
				// ����ͷ����
				mHeaderView.setPadding(0, -mHeaderViewHeight, 0, 0);
			}
			break;
		default:
			break;
		}

		return super.onTouchEvent(ev);
	}

	/**
	 * ���ݵ�ǰ״̬���и���
	 */
	private void refreshstate() {
		// TODO Auto-generated method stub
		switch (mCurrentState) {
		case STATE_PULL_TO_REFRESH:
			tvTitle.setText("����ˢ��");
			ivArrow.setAnimation(animDown);
			pbProgress.setVisibility(View.INVISIBLE);
			ivArrow.setVisibility(View.VISIBLE);
			break;
		case STATE_RELEASE_TO_REFRESH:
			ivArrow.setAnimation(animUp);
			tvTitle.setText("�ɿ�ˢ��");
			pbProgress.setVisibility(View.INVISIBLE);
			ivArrow.setVisibility(View.VISIBLE);
			break;
		case STATE_REFRESHING:
			tvTitle.setText("����ˢ��...");

			ivArrow.clearAnimation();// ���������������

			pbProgress.setVisibility(View.VISIBLE);
			ivArrow.setVisibility(View.INVISIBLE);

			break;
		default:
			break;

		}
	}

	/**
	 * ˢ�½���������ؼ�
	 */
	public void onRefreshComplete(boolean success) {
		if(!isLoadMore){
			mHeaderView.setPadding(0, -mHeaderViewHeight, 0, 0);

			mCurrentState = STATE_PULL_TO_REFRESH;
			tvTitle.setText("����ˢ��");
			pbProgress.setVisibility(View.INVISIBLE);
			ivArrow.setVisibility(View.VISIBLE);
			
			if (success) {// ֻ��ˢ�³ɹ���Ÿ���

				setCurrenttime();
			}
		}else{
			//���ظ���
			mFooterView.setPadding(0, -mFooterViewHeight, 0, 0);
			isLoadMore = false;
		}

	}

	// 3. �����Ա����,���ռ�������
	private OnRefreshListener mListener;
	private View mFooterView;
	private int mFooterViewHeight;

	/**
	 * 2. ��¶�ӿ�,���ü���
	 */
	public void setOnRefreshListener(OnRefreshListener listener) {
		mListener = listener;
	}

	/**
	 * 1. ����ˢ�µĻص��ӿ�
	 * 
	 * @author Kevin
	 * @date 2015-10-21
	 */
	public interface OnRefreshListener {
		public void onRefresh();

		// �������ظ���
		public void onLoadMore();
	}
	private boolean isLoadMore = false;//����Ƿ���ظ���
    //����״̬�仯
	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		// TODO Auto-generated method stub
		if(scrollState == SCROLL_STATE_IDLE){//����״̬
			int lastVisiblePosition = getLastVisiblePosition();
			
			if(lastVisiblePosition == getCount() - 1 && !isLoadMore){//��ǰ���һ����ǩ��û�м��ظ���
			  //������
			   isLoadMore = true;
			   mFooterView.setPadding(0, 0, 0, 0);
			   
			   setSelection(getCount() - 1);//���»������ʱ�Զ��������ظ���
			   
			    //֪ͨ��ҳ����ظ���
				// ���лص�
				if (mListener != null) {
					mListener.onLoadMore();
				}
			}
		}
	}
    //�������̻ص�
	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		// TODO Auto-generated method stub
		
	}


}
