package com.ericssonlabs;

import android.app.Activity;
import android.app.AlertDialog;

public class BaseActivity extends Activity {
	public void alert(String mess) {
		new AlertDialog.Builder(this).setTitle("提示").setMessage(mess)
				.setPositiveButton("确定", null).show();
	}
}
