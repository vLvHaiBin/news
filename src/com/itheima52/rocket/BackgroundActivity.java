package com.itheima52.rocket;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;
/**
 * 鐑熼浘鑳屾櫙
 * @author Kevin
 *
 */
public class BackgroundActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		int i =1;
		int o = 1;
		setContentView(R.layout.activity_bg);

		ImageView ivTop = (ImageView) findViewById(R.id.iv_top);
		ImageView ivBottom = (ImageView) findViewById(R.id.iv_bottom);

		// 娓愬彉鍔ㄧ敾
		AlphaAnimation anim = new AlphaAnimation(0, 1);
		anim.setDuration(1000);
		anim.setFillAfter(true);// 鍔ㄧ敾缁撴潫鍚庝繚鎸佺姸鎬�

		// 杩愯鍔ㄧ敾
		ivTop.startAnimation(anim);
		ivBottom.startAnimation(anim);

		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {
				finish();
			}
		}, 1000);// 寤舵椂1绉掑悗缁撴潫activity
	}

}
