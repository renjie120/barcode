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
package com.ericssonlabs.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ericssonlabs.R;

/**
 * 底部菜单栏的组件.
 */
public class BottomBar extends LinearLayout implements OnClickListener {

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

	public static class CallAction extends AbstractAction {

		private final Activity context;

		public CallAction(final Activity mContext) {
			super(R.drawable.call);
			this.context = mContext;
		}

		@Override
		public void performAction(final View view) {
			Intent intent = new Intent(Intent.ACTION_CALL,
					Uri.parse("tel:15000516416"));
			this.context.startActivity(intent);
		}

	}

	/**
	 * 使用常用属性初始化底部栏.
	 * 
	 * @param title
	 * @param left
	 * @param right
	 * @param width
	 * @param height
	 * @param titleSize
	 */
	public void init(String title, boolean left, boolean right, int width,
			int height, int titleSize) {
		if (title != null)
			setTitle(title);
		setLeftVisible(left);
		setRightVisible(right);
		setWidthHeight(width, height);
		//setLeftImgWidthAndHeight(height);
		if (titleSize > 0)
			setTitleSize(titleSize);
	}

	public interface Action {
		public int getDrawable();

		public void performAction(View view);
	}

	public interface OnRefreshClickListener {
		public void onRefreshClick();
	}

	public static final int TYPE_HOME = 0; // 左侧LOGO，中间标题文字，右侧编辑图标

	public void setTitleSize(final int resId) {
		this.mTitle.setTextSize(resId);
	}

	public static final int TYPE_NORMAL = 1; // 左侧LOGO，中间标题文字，右侧编辑图标

	// private boolean mRefreshable=false;

	public static final int TYPE_EDIT = 2; // 左侧LOGO，中间标题文字，右侧发送图标

	private Context mContext;

	private LayoutInflater mInflater;

	private ViewGroup mActionBar;// 标题栏

	private ImageView mLeftButton;

	private ImageView mRightButton;// 右边的动作图标

	private TextView mTitle;// 居中标题

	private OnRefreshClickListener mOnRefreshClickListener = null;

	public BottomBar(final Context context) {
		super(context);
		initViews(context);
	}

	public void setLeftVisible(final boolean visible) {
		if (visible)
			this.mLeftButton.setVisibility(View.VISIBLE);
		else
			this.mLeftButton.setVisibility(View.GONE);
	}

	public void setRightVisible(final boolean visible) {
		if (visible)
			this.mRightButton.setVisibility(View.VISIBLE);
		else
			this.mRightButton.setVisibility(View.GONE);
	}

	public BottomBar(final Context context, final AttributeSet attrs) {
		super(context, attrs);
		initViews(context);
	}

	private void setLeftImgWidthAndHeight(int height) {
		ViewGroup.LayoutParams params = this.mLeftButton.getLayoutParams();
		params.height = height - 2;
		params.width = (int) (height * 1.2);
		this.mLeftButton.setLayoutParams(params);
	}

	private void initViews(final Context context) {
		this.mContext = context;
		this.mInflater = LayoutInflater.from(this.mContext);

		this.mActionBar = (ViewGroup) this.mInflater.inflate(R.layout.bottom,
				null);

		addView(this.mActionBar);
		this.mLeftButton = (ImageView) this.mActionBar
				.findViewById(R.id.bottom_left);
		this.mRightButton = (ImageView) this.mActionBar
				.findViewById(R.id.bottom_right);
		this.mTitle = (TextView) this.mActionBar.findViewById(R.id.menu_name);

		this.mLeftButton.setOnClickListener(this);
		this.mRightButton.setOnClickListener(this);
	}

	public void setWidthHeight(int width, int height) {
		LinearLayout l = (LinearLayout) this.mActionBar;
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(width,
				height);
		l.setLayoutParams(lp);
	}

	@Override
	public void onClick(final View view) {
		final Object tag = view.getTag();
		if (tag instanceof Action) {
			final Action action = (Action) tag;
			action.performAction(view);
		}
	}

	public void removeLeftIcon() {
		this.mLeftButton.setVisibility(View.GONE);
	}

	public void removeRightIcon() {
		this.mRightButton.setVisibility(View.GONE);
	}

	public void setLeftAction(final Action action) {
		this.mLeftButton.setImageResource(action.getDrawable());
		this.mLeftButton.setTag(action);
	}

	public void setLeftActionEnabled(final boolean enabled) {
		this.mLeftButton.setEnabled(enabled);
	}

	public void setLeftIcon(final int resId) {
		this.mLeftButton.setImageResource(resId);
	}

	public void setRightAction(final Action action) {
		this.mRightButton.setImageResource(action.getDrawable());
		this.mRightButton.setTag(action);
	}

	public void setRightActionEnabled(final boolean enabled) {
		this.mRightButton.setEnabled(enabled);
	}

	public void setRightIcon(final int resId) {
		this.mRightButton.setImageResource(resId);
	}

	public void setTitle(final CharSequence text) {
		this.mTitle.setText(text);
	}

	public void setTitle(final int resId) {
		this.mTitle.setText(resId);
	}

	public void setTitleClickListener(final OnClickListener li) {
		this.mTitle.setOnClickListener(li);
	}

	public void setType() {

	}

}
