package com.ericssonlabs;

import android.app.TabActivity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabWidget;
import android.widget.TextView;

import com.ericssonlabs.util.AdjustScreenUtil;

/**
 * 签到界面.采用tab的布局方式.
 * 
 */
public class SignInActivity extends TabActivity {

	private static final String Tab1 = "Tab1";
	private static final String Tab2 = "Tab2";
	private static final String Tab3 = "Tab3";
	private String token;
	private String eventId;
	private float screenHeight = 0;
	private float screenWidth = 0;
	public static float barH = 0.1f;

	/**
	 * 得到屏幕的高宽.
	 * 
	 * @return
	 */
	public float[] getScreen2() {
		DisplayMetrics dm = new DisplayMetrics();
		dm = getResources().getDisplayMetrics();
		return new float[] { dm.widthPixels, dm.heightPixels };
	}

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.main);
		Intent intent = getIntent();
		eventId = intent.getStringExtra("eventid");
		token = intent.getStringExtra("token");

		float[] screen2 = getScreen2();
		screenHeight = screen2[1];
		screenWidth = screen2[0];

		final TabHost tabHost = this.getTabHost();
		final TabWidget tabWidget = tabHost.getTabWidget();

		// 设置第一个tab页的对应的intent布局
		TabHost.TabSpec tabSpec = tabHost.newTabSpec(Tab1);
		tabSpec.setIndicator(composeLayout("搜索", R.drawable.icon1));
		tabSpec.setContent(new Intent(SignInActivity.this, TicketsList.class)
				.putExtra("token", token).putExtra("eventid", eventId));
		tabHost.addTab(tabSpec);

		// 设置第二个tab页的对应的intent布局
		TabHost.TabSpec tabSpec2 = tabHost.newTabSpec(Tab2);
		tabSpec2.setIndicator(composeLayout("二维码签到", R.drawable.icon2));
		tabSpec2.setContent(new Intent(SignInActivity.this,
				BarCodeActivity.class).putExtra("token", token).putExtra(
				"eventid", eventId));
		tabHost.addTab(tabSpec2);

		// 设置第三个tab页的对应的intent布局
		TabHost.TabSpec tabSpec3 = tabHost.newTabSpec(Tab3);
		tabSpec3.setIndicator(composeLayout("设置", R.drawable.icon3));
		tabSpec3.setContent(new Intent(SignInActivity.this, CheckConfig.class)
				.putExtra("token", token).putExtra("eventid", eventId));
		tabHost.addTab(tabSpec3);

		// 这是对Tab标签本身的设置
		int height = (int) (screenHeight * barH);
		for (int i = 0; i < tabWidget.getChildCount(); i++) {
			// 设置高度、宽度，不过宽度由于设置为fill_parent，在此对它没效果
			tabWidget.getChildAt(i).getLayoutParams().height = height;
			View v = tabWidget.getChildAt(i);
			if (tabHost.getCurrentTab() == i) { 
				v.setBackgroundColor(Color.GRAY);
			} else {
				v.setBackgroundDrawable(getResources().getDrawable(
						R.drawable.selected));
			}
		}

		// 设置Tab变换时的监听事件
		tabHost.setOnTabChangedListener(new OnTabChangeListener() {
			@Override
			public void onTabChanged(String tabId) {
				for (int i = 0; i < tabWidget.getChildCount(); i++) {
					View v = tabWidget.getChildAt(i);
					if (tabHost.getCurrentTab() == i) {
						v.setBackgroundColor(Color.GRAY);
					} else {
						v.setBackgroundDrawable(getResources().getDrawable(
								R.drawable.selected));						
					}
				}
			}
		});

	}

	/**
	 * 动态生成下面的tab底部图标的布局.
	 * 
	 * @param s
	 * @param i
	 * @return
	 */
	public View composeLayout(String s, int i) {
		LinearLayout layout = new LinearLayout(this);
		layout.setOrientation(LinearLayout.VERTICAL);
		ImageView iv = new ImageView(this);
		iv.setImageResource(i);
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.FILL_PARENT,
				AdjustScreenUtil.adjustTabWith((int) screenWidth));
		lp.setMargins(0, AdjustScreenUtil.adjustTabitem((int) screenWidth), 0,
				0);
		iv.setLayoutParams(lp);
		layout.addView(iv);

		TextView tv = new TextView(this);
		tv.setGravity(Gravity.CENTER);
		tv.setSingleLine(true);
		tv.setText(s);
		tv.setTextSize(12);
		tv.setTextColor(Color.WHITE);
		LinearLayout.LayoutParams lp2 = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.FILL_PARENT, 0, 1);
		lp2.setMargins(0, AdjustScreenUtil.adjustTabitem((int) screenWidth), 0,
				0);
		tv.setLayoutParams(lp2);
		layout.addView(tv);
		return layout;
	}
}