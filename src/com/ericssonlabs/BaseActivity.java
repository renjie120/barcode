package com.ericssonlabs;

import android.app.Activity;
import android.app.AlertDialog;

/**
 * Activity基类，包含一些常用函数.
 */
public class BaseActivity extends Activity {
	/**
	 * 弹出一个提示框.
	 * @param mess
	 */
	public void alert(String mess) {
		new AlertDialog.Builder(this).setTitle("提示").setMessage(mess)
				.setPositiveButton("确定", null).show();
	}
}
