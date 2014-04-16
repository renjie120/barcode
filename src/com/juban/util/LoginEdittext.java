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
import android.text.InputType;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.juban.R;

/**
 * 底部菜单栏的组件.
 */
public class LoginEdittext extends LinearLayout {

	public LoginEdittext(final Context context) {
		super(context);
		initViews(context);
	}

	private Context mContext;
	private LinearLayout wrap;

	private LayoutInflater mInflater;
	private ViewGroup mActionBar;// 标题栏

	private ImageView leftImage;
	private ImageView rightImage;
	private EditText input;

	public LoginEdittext(final Context context, final AttributeSet attrs) {
		super(context, attrs);
		initViews(context);
	}

	private void initViews(final Context context) {
		this.mContext = context;
		this.mInflater = LayoutInflater.from(this.mContext);

		this.mActionBar = (ViewGroup) this.mInflater.inflate(R.layout.input,
				null);

		addView(this.mActionBar);
		this.wrap = (LinearLayout) this.mActionBar.findViewById(R.id.wrap_);
		this.leftImage = (ImageView) this.mActionBar.findViewById(R.id.left_t);
		this.rightImage = (ImageView) this.mActionBar
				.findViewById(R.id.right_t);
		this.input = (EditText) this.mActionBar.findViewById(R.id.input_);
	}

	public void setSize(final int width, final int height) {
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(width, height);
		this.wrap.setLayoutParams(lp);
	}

	public void setText(String str) {
		this.input.setText(str);
	}

	public String getText() {
		return this.input.getText().toString();
	}

	public void setPassword() {
		this.input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD); 
	}
}
