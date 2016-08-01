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
     *  实现动画效果
     */
	private void startAnim(){
		//动画集合
		AnimationSet set = new AnimationSet(false);
        //旋转动画
		RotateAnimation rotate = new RotateAnimation(0, 360, 
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 
				0.5f);
		rotate.setDuration(2000);
		rotate.setFillAfter(true);
		//缩放
		ScaleAnimation scale  = new ScaleAnimation(0, 1, 0, 1,
				Animation.RELATIVE_TO_SELF, 0.5f, 
				Animation.RELATIVE_TO_SELF, 0.5f);
	    scale.setDuration(2000);
	    scale.setFillAfter(true);
	    //渐变
	    AlphaAnimation alpha = new AlphaAnimation(0,1);
	    alpha.setDuration(2000);
	    alpha.setFillAfter(true);
	    //将以上动画加入动画集合
	    set.addAnimation(rotate);
	    set.addAnimation(scale);
	    set.addAnimation(alpha);
	     //动画监听
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
	 * 跳转下一个页面
	 */
	private void jumpNextPage() {
		// 判断之前有没有显示过新手引导
		boolean userGuide = PrefUtils.getBoolean(this, "is_user_guide_showed",
				false);

		if (!userGuide) {
			// 跳转到新手引导页
			startActivity(new Intent(SplashActivity.this, GuideActivity.class));
		} else {
			startActivity(new Intent(SplashActivity.this, MainActivity.class));
		}

		finish();
	}
}
