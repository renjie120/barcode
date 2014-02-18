package com.ericssonlabs;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;

/**
 * 首页.
 * 
 * @author 130126
 * 
 */
public class FirstPageActivity extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activiti_status);

	}

}