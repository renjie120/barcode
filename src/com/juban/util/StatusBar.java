/*******************************************************************************
 * Copyright 2011, 2012, 2013 fanfou.com, Xiaoke, Zhang
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package com.juban.util;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.juban.R;

/**
 * 底部菜单栏的组件.
 */
public class StatusBar extends LinearLayout {

	public static abstract class AbstractAction implements Action {
		final private int mDrawable;

		public AbstractAction(final int drawable) {
			this.mDrawable = drawable;
		}

		@Override
		public int getDrawable() {
			return this.mDrawable;
		}
	}

	public interface Action {
		public int getDrawable();

		public void performAction(View view);
	}

	public interface OnRefreshClickListener {
		public void onRefreshClick();
	}

	public static final int TYPE_HOME = 0; // 左侧LOGO，中间标题文字，右侧编辑图标

	public static final int TYPE_NORMAL = 1; // 左侧LOGO，中间标题文字，右侧编辑图标

	// private boolean mRefreshable=false;

	public static final int TYPE_EDIT = 2; // 左侧LOGO，中间标题文字，右侧发送图标

	private Context mContext;

	private LayoutInflater mInflater;

	private ViewGroup mActionBar;// 标题栏
	private LinearLayout status_bar;

	private TextView status;// 居中标题

	public StatusBar(final Context context) {
		super(context);
		initViews(context);
	}

	public StatusBar(final Context context, final AttributeSet attrs) {
		super(context, attrs);
		initViews(context);
	}

	private void initViews(final Context context) {
		this.mContext = context;
		this.mInflater = LayoutInflater.from(this.mContext);
		this.mActionBar = (ViewGroup) this.mInflater.inflate(
				R.layout.status_bar, null);
		addView(this.mActionBar);
		this.status_bar = (LinearLayout) this.mActionBar
				.findViewById(R.id.status_bar);
		this.status = (TextView) this.mActionBar.findViewById(R.id.status);
	}

	public void setWidthHeight(int width, int height) {
		// 这里设置布局的参数，因为ActionBar是继承自LinearLayout，所以使用线性的布局.
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		lp.width = width;
		lp.height  = height;
//		lp.bottomMargin = 20;
//		lp.topMargin = 20;
//		lp.leftMargin = 120;
		this.mActionBar.setLayoutParams(lp);
	}

	public void setTextWidthHeight(int width, int height) {
		this.status.setWidth(width);
		this.status.setHeight(height);
	}
}
