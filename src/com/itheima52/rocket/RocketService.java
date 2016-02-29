package com.itheima52.rocket;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.graphics.drawable.AnimationDrawable;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.widget.ImageView;

public class RocketService extends Service {

	private WindowManager.LayoutParams params;
	private int winWidth;
	private int winHeight;
	private WindowManager mWM;
	private View view;

	private int startX;
	private int startY;

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();

		mWM = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);

		// 鑾峰彇灞忓箷瀹介珮
		winWidth = mWM.getDefaultDisplay().getWidth();
		winHeight = mWM.getDefaultDisplay().getHeight();

		params = new WindowManager.LayoutParams();
		params.height = WindowManager.LayoutParams.WRAP_CONTENT;
		params.width = WindowManager.LayoutParams.WRAP_CONTENT;
		params.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
				| WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
		params.format = PixelFormat.TRANSLUCENT;
		params.type = WindowManager.LayoutParams.TYPE_PHONE;// 鐢佃瘽绐楀彛銆傚畠鐢ㄤ簬鐢佃瘽浜や簰锛堢壒鍒槸鍛煎叆锛夈�傚畠缃簬鎵�鏈夊簲鐢ㄧ▼搴忎箣涓婏紝鐘舵�佹爮涔嬩笅銆�
		params.gravity = Gravity.LEFT + Gravity.TOP;// 灏嗛噸蹇冧綅缃缃负宸︿笂鏂�,
													// 涔熷氨鏄�(0,0)浠庡乏涓婃柟寮�濮�,鑰屼笉鏄粯璁ょ殑閲嶅績浣嶇疆
		params.setTitle("Toast");

		view = View.inflate(this, R.layout.rocket, null);// 鍒濆鍖栫伀绠竷灞�

		// 鍒濆鍖栫伀绠抚鍔ㄧ敾
		ImageView ivRocket = (ImageView) view.findViewById(R.id.iv_rocket);
		ivRocket.setBackgroundResource(R.drawable.anim_rocket);
		AnimationDrawable anim = (AnimationDrawable) ivRocket.getBackground();
		anim.start();

		mWM.addView(view, params);

		view.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					// 鍒濆鍖栬捣鐐瑰潗鏍�
					startX = (int) event.getRawX();
					startY = (int) event.getRawY();
					break;
				case MotionEvent.ACTION_MOVE:
					int endX = (int) event.getRawX();
					int endY = (int) event.getRawY();

					// 璁＄畻绉诲姩鍋忕Щ閲�
					int dx = endX - startX;
					int dy = endY - startY;

					// 鏇存柊娴獥浣嶇疆
					params.x += dx;
					params.y += dy;

					// 闃叉鍧愭爣鍋忕灞忓箷
					if (params.x < 0) {
						params.x = 0;
					}

					if (params.y < 0) {
						params.y = 0;
					}

					// 闃叉鍧愭爣鍋忕灞忓箷
					if (params.x > winWidth - view.getWidth()) {
						params.x = winWidth - view.getWidth();
					}

					if (params.y > winHeight - view.getHeight()) {
						params.y = winHeight - view.getHeight();
					}

					// System.out.println("x:" + params.x + ";y:" + params.y);

					mWM.updateViewLayout(view, params);

					// 閲嶆柊鍒濆鍖栬捣鐐瑰潗鏍�
					startX = (int) event.getRawX();
					startY = (int) event.getRawY();
					break;
				case MotionEvent.ACTION_UP:
					if (params.x > 100 && params.x < 1000
							&& params.y > winHeight - 120) {
						System.out.println("鍙戝皠鐏!!!");
						sendRocket();

						// 鍚姩鐑熼浘鏁堟灉
						Intent intent = new Intent(RocketService.this,
								BackgroundActivity.class);
						intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);// 鍚姩涓�涓爤鏉ュ瓨鏀綼ctivity
						startActivity(intent);
					}
					break;

				default:
					break;
				}
				return true;
			}
		});
	}

	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			// 鏇存柊鐏浣嶇疆
			int y = msg.arg1;
			params.y = y;
			mWM.updateViewLayout(view, params);
		};
	};

	/**
	 * 鍙戝皠鐏
	 */
	protected void sendRocket() {
		// 璁剧疆鐏灞呬腑
		params.x = winWidth / 2 - view.getWidth() / 2;
		mWM.updateViewLayout(view, params);

		new Thread() {
			@Override
			public void run() {
				int pos = 1700;// 绉诲姩鎬昏窛绂�
				for (int i = 0; i <= 10; i++) {

					// 绛夊緟涓�娈垫椂闂村啀鏇存柊浣嶇疆,鐢ㄤ簬鎺у埗鐏閫熷害
					try {
						Thread.sleep(50);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}

					int y = pos - 170 * i;

					Message msg = Message.obtain();
					msg.arg1 = y;

					mHandler.sendMessage(msg);
				}
			}
		}.start();

	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (mWM != null && view != null) {
			mWM.removeView(view);
			view = null;
		}
	}
}
