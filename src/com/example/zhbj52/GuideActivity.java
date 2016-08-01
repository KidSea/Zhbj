package com.example.zhbj52;

import java.util.ArrayList;

import com.example.zhbj52.utils.DensityUtils;
import com.example.zhbj52.utils.PrefUtils;

import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.ContextMenu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class GuideActivity extends Activity {
	private static final int[] mImageIds = new int[] { R.drawable.guide_1,
			R.drawable.guide_2, R.drawable.guide_3 };
	private ViewPager vpGuide;
	private ArrayList<ImageView> imageViews;

	private LinearLayout pointlinearLayout;
	private int mPonitWidth;
	private View viewRedPoint;
	private Button btstart;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_guide);

		vpGuide = (ViewPager) findViewById(R.id.vp_guide);
		pointlinearLayout = (LinearLayout) findViewById(R.id.ll_point_group);

		btstart = (Button) findViewById(R.id.btn_start);
		btstart.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// ����SP����ʾ�Ѿ�չʾ����������
				PrefUtils.setBoolean(GuideActivity.this,
						"is_user_guide_showed", true);
				// ������ҳ��
				startActivity(new Intent(GuideActivity.this, MainActivity.class));
				finish();
			}
		});

		viewRedPoint = findViewById(R.id.view_red_point);
		initview();
		vpGuide.setAdapter(new GuideAdaper());
		vpGuide.setOnPageChangeListener(new GuiderPageLinstener());
	}

	/*
	 * ��ʼ������
	 */
	private void initview() {
		imageViews = new ArrayList<ImageView>();
		// ��ʼ����������
		for (int i = 0; i < mImageIds.length; i++) {
			ImageView imageView = new ImageView(this);
			imageView.setBackgroundResource(mImageIds[i]);
			imageViews.add(imageView);
		}
		// ��ʼ��Բ��
		for (int i = 0; i < mImageIds.length; i++) {
			View point = new View(this);
			point.setBackgroundResource(R.drawable.shape_point_gray);
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
					10, 10);

			if (i > 0) {
				params.leftMargin = DensityUtils.dip2px(10, this);
			}
			point.setLayoutParams(params);
			pointlinearLayout.addView(point);
		}
		// �����ͼ��
		pointlinearLayout.getViewTreeObserver().addOnGlobalLayoutListener(
				new OnGlobalLayoutListener() {
					// ִ���굱ǰ��layout����ø÷���
					@Override
					public void onGlobalLayout() {
						// TODO Auto-generated method stub
						pointlinearLayout.getViewTreeObserver()
								.removeOnGlobalLayoutListener(this);
						mPonitWidth = pointlinearLayout.getChildAt(1).getLeft()
								- pointlinearLayout.getChildAt(0).getLeft();
						System.out.println("Բ����� :" + mPonitWidth);

					}
				});
	}

	// �ڲ���ʵ��Viewpager
	class GuideAdaper extends PagerAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return mImageIds.length;
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			// TODO Auto-generated method stub
			return arg0 == arg1;
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			// TODO Auto-generated method stub

			container.addView(imageViews.get(position));
			return imageViews.get(position);

		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			// TODO Auto-generated method stub
			// super.destroyItem(container, position, object);
			((ViewPager) (container)).removeView((View) object);
		}
	}

	class GuiderPageLinstener implements OnPageChangeListener {

		// ����״̬���ͱ仯
		@Override
		public void onPageScrollStateChanged(int state) {
			// TODO Auto-generated method stub

		}

		// �����¼�
		@Override
		public void onPageScrolled(int position, float positionOffset,
				int positionOffsetPixels) {
			// TODO Auto-generated method stub
			int len = (int) (mPonitWidth * positionOffset) + position
					* mPonitWidth;
			RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) viewRedPoint
					.getLayoutParams();// ��ȡ��ǰ���Ĳ��ֲ���
			params.leftMargin = len;// ������߾�

			viewRedPoint.setLayoutParams(params);// ���¸�С������ò��ֲ���

		}

		// ĳ��ҳ�汻ѡ��
		@Override
		public void onPageSelected(int position) {
			// TODO Auto-generated method stub
			if (position == mImageIds.length - 1) {
				btstart.setVisibility(View.VISIBLE);
			} else {
				btstart.setVisibility(View.INVISIBLE);
			}
		}

	}
}
