package com.ericssonlabs;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.ericssonlabs.bean.EventInfo;
import com.ericssonlabs.bean.ServerResult;
import com.ericssonlabs.util.ActionBar;
import com.ericssonlabs.util.ActionBar.OnRefreshClickListener;
import com.ericssonlabs.util.BottomBar;
import com.ericssonlabs.util.Constant;
import com.ericssonlabs.util.HttpRequire;
import com.ericssonlabs.util.LoadImage;

/**
 * 活动列表详情.
 * 
 */
public class ActivitesInfo extends BaseActivity {
	private String eventId;
	private LinearLayout qiandao;
	private LinearLayout status;
	private LinearLayout temp1, img_temp1, img_temp2;
	private LinearLayout temp2;
	private TextView title;
	private TextView end_time;
	private ImageView qiandao_btn, status_btn, jiantou1, jiantou2, status_img,
			qiandao_img;
	private TextView begin_time;
	private ImageView imge;
	private String token, auth;
	private String url;
	private ActionBar head;
	private BottomBar bottom;
	private float screenHeight = 0;
	private float screenWidth = 0;
	private float w = 0.83f;
	private float h = 0.1f;
	private float textW = 10 / 17f;
	private float fontH3 = 26 / 470f;
	private float statusW = 60 / 270f;
	private float btnTmargin = 13 / 470f;
	private float qiandaoW = 116 / 270f;
	private float tempW = 130 / 270f;
	private float jiantouW = 15 / 270f;
	private float jiantouH = 20 / 470f;
	private float imgLmargin = -13 / 413f;
	private float btnLmargin = 14 / 270f;
	private float jiantouTmargin = 17 / 470f;
	private float jiantouLmargin = 27 / 264f;
	private float imgH = 24 / 470f;
	private float frame_imgH = 35 / 673f;
	private float frame_img_mar_l = 19 / 379f;
	private float frame_img_mar_t = 17 / 673f;
	private float imgW = 24 / 270f;
	private float contentW = 252 / 264f;
	private float contentH = 109 / 471f;
	private float contentLM = 6 / 264f;
	private float contentTM = 4 / 471f;
	private RelativeLayout layout;

	private EventInfo activitiDetail(String eventId) {
		if (Constant.debug) {
			myHandler.sendEmptyMessage(9);
			return null;
		} else {
			try {
				ServerResult result = HttpRequire.activitiDetail(eventId, auth);

				EventInfo t = (EventInfo) JSON.parseObject(result.getData()
						.toJSONString(), EventInfo.class);
				Message msg = new Message();
				msg.what = 2;
				Bundle b = new Bundle();
				String[] str = (t.getStarttime()).split(" ");
				String[] str2 = (t.getEndtime()).split(" ");

				b.putString("starttime", toDateString(getDate(str[0])));
				b.putString("endtime", toDateString(getDate(str2[0])));
				b.putString("name", t.getName());
				b.putString("url", t.getImageurl());
				msg.setData(b);
				myHandler.sendMessage(msg);
				return t;
			} catch (Exception e) {
				e.printStackTrace();
			}  
		}
		return null;
	}

	/**
	 * 签到按钮触发事件.
	 * 
	 * @param arg0
	 */
	public void qiandao(View arg0) {
		LinearLayout layout = (LinearLayout) arg0;
		Intent intent = new Intent(ActivitesInfo.this, SignInActivity.class);
		// intent.putExtra("eventid", layout.getTag().toString());
		intent.putExtra("eventid", "1");
		intent.putExtra("token", token);
		this.startActivity(intent);
	}

	/**
	 * 查看状态按钮触发事件.
	 * 
	 * @param arg0
	 */
	public void status(View arg0) {
		LinearLayout layout = (LinearLayout) arg0;
		Intent intent = new Intent(ActivitesInfo.this, ActivitesStatus.class);
		intent.putExtra("eventid", layout.getTag().toString());
		intent.putExtra("token", token);
		intent.putExtra("url", url);
		this.startActivity(intent);
	}

	/**
	 * 根据屏幕适配文本大小.
	 */
	private void adjustScreen() {

		float[] screen2 = getScreen2();
		screenHeight = screen2[1];
		screenWidth = screen2[0];
		// 设置标题布局
		head.init(R.drawable.i5_top_activity_detail, true, true,
				(int) (screenHeight * barH));
		head.setTitleSize((int) (screenWidth * titleW4),
				(int) (screenHeight * titleH));
		head.setLeftAction(new ActionBar.BackAction(this));
		head.setRightAction(new ActionBar.RefreshAction(head));
		head.setLeftSize((int) (screenWidth * lftBtnW),
				(int) (screenHeight * lftBtnH), (int) (screenHeight * lftBtnT));
		head.setRightSize((int) (screenWidth * rgtBtnW),
				(int) (screenHeight * rgtBtnH));
		head.setRefreshEnabled(new OnRefreshClickListener() {
			public void onRefreshClick() {
				new ActivityInfoLoader(eventId).execute("");
			}
		});
		// 设置文字字体
		title.setWidth((int) (textW * screenWidth));
		begin_time.setWidth((int) (textW * screenWidth));
		end_time.setWidth((int) (textW * screenWidth));

		// 设置主要的大框框
		LinearLayout.LayoutParams p2 = new LinearLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		p2.width = (int) (contentW * screenWidth);
		p2.height = (int) (contentH * screenHeight);
		p2.leftMargin = (int) (contentLM * screenWidth);
		p2.topMargin = (int) (contentTM * screenHeight);
		layout.setLayoutParams(p2);

		// 设置底部菜单.
		bottom.init(null, true, true, LinearLayout.LayoutParams.FILL_PARENT,
				(int) (screenHeight * barH));
		bottom.setRightAction(new BottomBar.CallAction(this));

		// 设置查询状态的大框框布局.
		LinearLayout.LayoutParams p1 = new LinearLayout.LayoutParams(
				(int) (screenWidth * w), (int) (screenHeight * h));
		p1.gravity = Gravity.CENTER_HORIZONTAL;
		status.setLayoutParams(p1);
		// 设置签到的大框框布局
		p1.topMargin = 20;
		qiandao.setLayoutParams(p1);

		// 设置两个图标所在的正方形的布局
		LinearLayout.LayoutParams lp5 = new LinearLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		lp5.height = (int) (frame_imgH * screenHeight);
		lp5.width = (int) (frame_imgH * screenHeight);
		lp5.leftMargin = (int) (frame_img_mar_l * screenWidth);
		lp5.topMargin = (int) (frame_img_mar_t * screenHeight);
		img_temp1.setLayoutParams(lp5);
		img_temp2.setLayoutParams(lp5);

		// 设置两个小图片的布局
		LinearLayout.LayoutParams lp4 = new LinearLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		lp4.height = (int) (imgH * screenHeight);
		lp4.width = (int) (imgW * screenHeight);
		lp4.leftMargin = (int) (imgLmargin * screenWidth);
		// lp4.topMargin = 3; // (int) (imgTmargin * screenHeight);

		qiandao_img.setLayoutParams(lp4);
		status_img.setLayoutParams(lp4);

		// 设置两个文字图片所在的框框的布局
		LinearLayout.LayoutParams lp_temp = new LinearLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		lp_temp.width = (int) (tempW * screenWidth);
		lp_temp.height = (int) (screenHeight * h);
		temp2.setLayoutParams(lp_temp);
		temp1.setLayoutParams(lp_temp);

		// 设置两个文字图片的布局
		LinearLayout.LayoutParams lp2 = new LinearLayout.LayoutParams(
				(int) (qiandaoW * screenWidth), (int) (fontH3 * screenHeight));
		lp2.leftMargin = (int) (btnLmargin * screenWidth);
		lp2.topMargin = (int) (btnTmargin * screenHeight);
		qiandao_btn.setLayoutParams(lp2);

		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
				(int) (statusW * screenWidth), (int) (fontH3 * screenHeight));
		lp.leftMargin = (int) (btnLmargin * screenWidth);
		lp.topMargin = (int) (btnTmargin * screenHeight);
		status_btn.setLayoutParams(lp);

		// 设置两个箭头的布局
		LinearLayout.LayoutParams lp3 = new LinearLayout.LayoutParams(
				(int) (jiantouW * screenWidth), (int) (jiantouH * screenHeight));
		lp3.leftMargin = (int) (jiantouLmargin * screenWidth);
		lp3.topMargin = (int) (jiantouTmargin * screenHeight);
		// 设置logo的位置布局.
		jiantou1.setLayoutParams(lp3);
		jiantou2.setLayoutParams(lp3);

	}

	/**
	 * 得到控件.
	 */
	private void initLayout() {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activiti_info);
		temp1 = (LinearLayout) findViewById(R.id.temp1);
		img_temp1 = (LinearLayout) findViewById(R.id.img_temp1);
		img_temp2 = (LinearLayout) findViewById(R.id.img_temp2);
		temp2 = (LinearLayout) findViewById(R.id.temp2);
		qiandao = (LinearLayout) findViewById(R.id.qiandao);
		status = (LinearLayout) findViewById(R.id.status);
		head = (ActionBar) findViewById(R.id.info_head);
		title = (TextView) findViewById(R.id.title);
		imge = (ImageView) findViewById(R.id.activity_pic);
		bottom = (BottomBar) findViewById(R.id.info_bottom);
		begin_time = (TextView) findViewById(R.id.begin_time);
		end_time = (TextView) findViewById(R.id.end_time);
		status_btn = (ImageView) findViewById(R.id.status_btn);
		jiantou1 = (ImageView) findViewById(R.id.jiantou1);
		status_img = (ImageView) findViewById(R.id.status_img);
		qiandao_img = (ImageView) findViewById(R.id.qiandao_img);
		jiantou2 = (ImageView) findViewById(R.id.jiantou2);
		qiandao_btn = (ImageView) findViewById(R.id.qiandao_btn);
		layout = (RelativeLayout) findViewById(R.id.activity_content);

		adjustScreen();
	}

	/**
	 * 绑定事件.
	 */
	private void initListeners() {
		Intent intent = getIntent();
		eventId = intent.getStringExtra("eventid");
		qiandao.setTag(eventId);
		status.setTag(eventId);
		token = intent.getStringExtra("token");
		auth = intent.getStringExtra("auth");
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initLayout();

		initListeners();

		// 加载列表
		new ActivityInfoLoader(eventId).execute("");
	}

	/**
	 * 对页面元素的处理类.
	 */
	public Handler myHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				alert("对不起，该用户没有权限");
				break;
			case 2:
				Bundle info = msg.getData();
				begin_time.setText("开始时间：" + info.getString("starttime"));
				end_time.setText("结束时间：" + info.getString("endtime"));
				title.setText(info.getString("name"));
				url = info.getString("url");
				new Thread(new LoadImage(url, imge, R.drawable.huodong_paper,
						getResources())).start();
				break;
			// 测试情况
			case 9:
				begin_time.setText("开始时间：2013-1-1");
				end_time.setText("结束时间：2014-1-1");
				title.setText("快付款所讲的房价快速的回复将快速的回复即可");
				imge.setBackgroundResource(R.drawable.huodong_paper);
				break;
			default:
				super.hasMessages(msg.what);
				break;
			}
		}
	};

	/**
	 * 加载全部活动的内部类.
	 */
	private class ActivityInfoLoader extends AsyncTask<String, String, String> {

		private String eventId;

		public ActivityInfoLoader(String eventId) {
			this.eventId = eventId;
		}

		public String doInBackground(String... p) {
			// 调用方法进行加载.
			activitiDetail(eventId);
			return "";
		}

	}

}