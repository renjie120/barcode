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

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.juban.R;

/**
 * 底部菜单栏的组件.
 */
public class ActionBar extends LinearLayout implements OnClickListener {

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

	private ImageView mLeftButton;
	private LinearLayout left;
	private LinearLayout right;

	private ImageView mRightButton;// 右边的动作图标

	// private TextView mTitle;// 居中标题
	private ImageView mTitle;// 居中标题

	private OnRefreshClickListener mOnRefreshClickListener = null;

	public ActionBar(final Context context) {
		super(context);
		initViews(context);
	}

	public ActionBar(final Context context, final AttributeSet attrs) {
		super(context, attrs);
		initViews(context);
	}

	/**
	 * 返回按钮的操作.
	 */
	public static class BackAction extends AbstractAction {
		private final Activity context;

		public BackAction(final Activity mContext) {
			super(R.drawable.back);
			this.context = mContext;
		}

		@Override
		public void performAction(final View view) {
			this.context.finish();
		}

	}

	/**
	 * 刷新按钮的操作.
	 */
	public static class RefreshAction extends AbstractAction {
		private final ActionBar ab;

		public RefreshAction(final ActionBar ab) {
			super(R.drawable.refresh);
			this.ab = ab;
		}

		@Override
		public void performAction(final View view) {
			this.ab.onRefreshClick();
		}

	}

	private void onRefreshClick() {
		if (this.mOnRefreshClickListener != null) {
			this.mOnRefreshClickListener.onRefreshClick();
		}
	}

	public void setRefreshEnabled(
			final OnRefreshClickListener onRefreshClickListener) {
		if (onRefreshClickListener != null) {
			this.mOnRefreshClickListener = onRefreshClickListener;
			setRefreshAction(new RefreshAction(this));
		}
	}

	private void setRefreshAction(final Action action) {
		this.mRightButton.setImageResource(action.getDrawable());
		this.mRightButton.setTag(action);
	}

	/**
	 * 使用常用属性初始化标题栏
	 * 
	 * @param title
	 *            标题字符串
	 * @param left
	 *            是否显示左边图片
	 * @param right
	 *            是否显示右边图片
	 * @param width
	 *            宽度
	 * @param height
	 *            高度
	 * @param titleSize
	 *            标题字体大小
	 */
	public void init(int title, boolean left, boolean right,int height) {
		setTitle(title);
		setLeftVisible(left);
		setRightVisible(right);
		setWidthHeight(LayoutParams.FILL_PARENT, height);
		// if (titleSize > 0)
		// setTitleSize(titleSize);
	}

	private void initViews(final Context context) {
		this.mContext = context;
		this.mInflater = LayoutInflater.from(this.mContext);
		this.mActionBar = (ViewGroup) this.mInflater.inflate(R.layout.title,
				null);

		addView(this.mActionBar);
		this.left = (LinearLayout) this.mActionBar.findViewById(R.id.left_line);
		this.right = (LinearLayout) this.mActionBar
				.findViewById(R.id.right_line);
		this.mLeftButton = (ImageView) this.mActionBar
				.findViewById(R.id.left_btn1);
		this.mRightButton = (ImageView) this.mActionBar
				.findViewById(R.id.right_btn1);
		this.mTitle = (ImageView) this.mActionBar
				.findViewById(R.id.titile_gre_ym);

		this.mLeftButton.setOnClickListener(this);
		this.mRightButton.setOnClickListener(this);
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

	public void setWidthHeight(int width, int height) {
		// 这里设置布局的参数，因为ActionBar是继承自LinearLayout，所以使用线性的布局.
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(width,
				height);
		this.mActionBar.setLayoutParams(lp);
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

	public void setLeftVisible(final boolean visible) {
		if (visible)
			this.left.setVisibility(View.VISIBLE);
		else
			this.left.setVisibility(View.GONE);
	}

	public void setRightVisible(final boolean visible) {
		if (visible)
			this.right.setVisibility(View.VISIBLE);
		else {
			this.right.setVisibility(View.GONE);
		}
	}

	public void setLeftIcon(final int resId) {
		this.mLeftButton.setImageResource(resId);
	}

	public void setLeftSize(final int width, final int height,final int marginTop) {
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(width,
				height);
		lp.gravity = Gravity.CENTER_VERTICAL;
		lp.leftMargin = 10; 
		lp.topMargin = marginTop;
		mLeftButton.setLayoutParams(lp);
	}

	public void setRightSize(final int width, final int height) {
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(width,
				height);
		lp.gravity = Gravity.CENTER_VERTICAL;
		mRightButton.setLayoutParams(lp);
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

	public void setTitle(final Drawable background) {
		this.mTitle.setBackgroundDrawable(background);
	}

	public void setTitle(final int resId) {
		this.mTitle.setBackgroundResource(resId);
	}

	public void setTitleSize(final int width, final int height) {
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(width,
				height);
		this.mTitle.setLayoutParams(lp);
	}

	public void setTitleClickListener(final OnClickListener li) {
		this.mTitle.setOnClickListener(li);
	}

	public void setType() {

	}

}
