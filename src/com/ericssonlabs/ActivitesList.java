package com.ericssonlabs;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;

/**
 * 活动列表.
 * 
 * @author 130126
 * 
 */
public class ActivitesList extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activiteslist);
	}

}