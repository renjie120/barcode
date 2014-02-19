package com.ericssonlabs;

import android.app.AlertDialog;
import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

/**
 * 签到界面.采用tab的布局方式.
 * 
 */
public class SignInActivity extends TabActivity implements
		OnCheckedChangeListener {
	AlertDialog menuDialog;// menu菜单Dialog

	public static final String TAB_ITEM_1 = "search";
	public static final String TAB_ITEM_2 = "code";
	public static final String TAB_ITEM_3 = "config";
	private RadioGroup group;
	private TabHost tabHost;
	private String token;
	private String eventId;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.signin);

		group = (RadioGroup) findViewById(R.id.main_radio);
		group.setOnCheckedChangeListener(this);
		//得到前面页面传递来的参数
		Intent intent = getIntent();
		token = intent.getStringExtra("token");
		eventId = intent.getStringExtra("eventid");
		tabHost = this.getTabHost();
		TabSpec tab1 = tabHost.newTabSpec(TAB_ITEM_1);
		TabSpec tab2 = tabHost.newTabSpec(TAB_ITEM_2);
		TabSpec tab3 = tabHost.newTabSpec(TAB_ITEM_3);
		//点击第一个菜单，跳转到查询界面
		tab1.setIndicator(TAB_ITEM_1).setContent(
				new Intent(SignInActivity.this, TicketsList.class).putExtra(
						"token", token).putExtra("eventid", eventId));
		//点击第二个菜单，跳转到二维码扫描界面
		tab2.setIndicator(TAB_ITEM_3).setContent(
				new Intent(SignInActivity.this, BarCodeActivity.class)
						.putExtra("token", token).putExtra("eventid", eventId));
		//点击第三个菜单，跳转到配置界面
		tab3.setIndicator(TAB_ITEM_2).setContent(
				new Intent(SignInActivity.this, CheckConfig.class).putExtra(
						"token", token).putExtra("eventid", eventId));
		tabHost.addTab(tab1);
		tabHost.addTab(tab2);
		tabHost.addTab(tab3);
	}

	/**
	 * 设置tab的选择状态.
	 */
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		switch (checkedId) {
		case R.id.radio_button1:
			tabHost.setCurrentTabByTag(TAB_ITEM_1);
			break;
		case R.id.radio_button2:
			tabHost.setCurrentTabByTag(TAB_ITEM_2);
			break;
		case R.id.radio_button3:
			tabHost.setCurrentTabByTag(TAB_ITEM_3);
			break;
		default:
			break;
		}
	}
}