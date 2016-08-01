package com.example.zhbj52;


import com.example.zhbj52.utils.*;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract.CommonDataKinds.Relation;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.widget.AlphabetIndexer;
import android.widget.RelativeLayout;

public class SplashActivity extends Activity {
    
	RelativeLayout rlRoot;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_spanish);
		
		rlRoot = (RelativeLayout) findViewById(R.id.rl_root);
		
		startAnim();
	}
    /*
     *  ʵ�ֶ���Ч��
     */
	private void startAnim(){
		//��������
		AnimationSet set = new AnimationSet(false);
        //��ת����
		RotateAnimation rotate = new RotateAnimation(0, 360, 
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 
				0.5f);
		rotate.setDuration(2000);
		rotate.setFillAfter(true);
		//����
		ScaleAnimation scale  = new ScaleAnimation(0, 1, 0, 1,
				Animation.RELATIVE_TO_SELF, 0.5f, 
				Animation.RELATIVE_TO_SELF, 0.5f);
	    scale.setDuration(2000);
	    scale.setFillAfter(true);
	    //����
	    AlphaAnimation alpha = new AlphaAnimation(0,1);
	    alpha.setDuration(2000);
	    alpha.setFillAfter(true);
	    //�����϶������붯������
	    set.addAnimation(rotate);
	    set.addAnimation(scale);
	    set.addAnimation(alpha);
	     //��������
	    set.setAnimationListener(new AnimationListener() {
			
			public void onAnimationStart(Animation animation) {
				// TODO Auto-generated method stub
				
			}
			
			public void onAnimationRepeat(Animation animation) {
				// TODO Auto-generated method stub
				
			}
			
			public void onAnimationEnd(Animation animation) {
				// TODO Auto-generated method stub
				jumpNextPage();
			}
		});
		rlRoot.startAnimation(set);
		
		
	}
	/**
	 * ��ת��һ��ҳ��
	 */
	private void jumpNextPage() {
		// �ж�֮ǰ��û����ʾ����������
		boolean userGuide = PrefUtils.getBoolean(this, "is_user_guide_showed",
				false);

		if (!userGuide) {
			// ��ת����������ҳ
			startActivity(new Intent(SplashActivity.this, GuideActivity.class));
		} else {
			startActivity(new Intent(SplashActivity.this, MainActivity.class));
		}

		finish();
	}
}
