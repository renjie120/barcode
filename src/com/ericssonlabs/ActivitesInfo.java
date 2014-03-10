package com.ericssonlabs;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.ericssonlabs.bean.EventInfo;
import com.ericssonlabs.bean.ServerResult;
import com.ericssonlabs.util.ActionBar;
import com.ericssonlabs.util.ActionBar.OnRefreshClickListener;
import com.ericssonlabs.util.AdjustScreenUtil;
import com.ericssonlabs.util.BottomBar;
import com.ericssonlabs.util.Constant;
import com.ericssonlabs.util.LoadImage;

/**
 * 活动列表详情.
 * 
 */
public class ActivitesInfo extends BaseActivity {
	private String eventId;
	private TextView beginTime;
	private TextView endTime;
	private LinearLayout qiandao;
	private LinearLayout status;
	private TextView title;
	private TextView end_time;
	private ImageView qiandao_btn, status_btn, jiantou1, jiantou2, status_img,
			qiandao_img;
	private TextView begin_time;
	private ImageView imge;
	private String token;
	private String url;
	private ActionBar head;
	private BottomBar bottom;
	private float screenHeight = 0;
	private float screenWidth = 0;
	private float w = 0.83f;
	private float h = 0.1f;
	private float fontH1 = 9 / 470f;
	private float fontH2 = 10 / 470f;
	private float fontH3 = 26 / 470f;
	private float statusW = 60 / 270f;
	private float btnTmargin = 13 / 470f;
	private float qiandaoW = 116 / 270f;
	private float jiantouW = 15 / 270f;
	private float jiantouH = 20 / 470f;
	private float jiantouTmargin = 19 / 470f;
	private float imgTmargin = 19 / 470f;
	private float imgLmargin = 13 / 470f;
	private float btnLmargin = 14 / 270f;
	private float jiantouRmargin = 10 / 470f;
	private float imgH = 24 / 470f;
	private float imgW = 24 / 270f;

	private EventInfo activitiDetail(String eventId) {
		DefaultHttpClient httpclient = new DefaultHttpClient();
		String encoding = "UTF-8";
		try {

			HttpPost httpost = new HttpPost(Constant.HOST
					+ "?do=eventinfo&eventid=" + eventId + "&token=" + token);
			System.out.println("查看活动详情：" + Constant.HOST
					+ "?do=eventinfo&eventid=" + eventId + "&token=" + token);
			HttpResponse response = httpclient.execute(httpost);
			HttpEntity entity = response.getEntity();
			BufferedReader br = new BufferedReader(new InputStreamReader(
					entity.getContent(), encoding));
			String sss = br.readLine();
			// 如果没有登录成功，就弹出提示信息.
			ServerResult result = (ServerResult) JSON.parseObject(sss,
					ServerResult.class);

			EventInfo t = (EventInfo) JSON.parseObject(result.getData()
					.toJSONString(), EventInfo.class);
			Message msg = new Message();
			msg.what = 2;
			Bundle b = new Bundle();
			b.putString("starttime", t.getStarttime());
			b.putString("endtime", t.getEndtime());
			b.putString("name", t.getName());
			b.putString("url", t.getImageurl());
			msg.setData(b);
			myHandler.sendMessage(msg);
			return t;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			httpclient.getConnectionManager().shutdown();
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
		head.init(R.drawable.i5_top_my_activity, true, true,
				LinearLayout.LayoutParams.FILL_PARENT,
				(int) (screenHeight * barH));
		head.setTitleSize((int) (screenWidth * titleW4),
				(int) (screenHeight * titleH));
		head.setLeftAction(new ActionBar.BackAction(this));
		head.setRightAction(new ActionBar.RefreshAction(head));

		head.setRefreshEnabled(new OnRefreshClickListener() {
			public void onRefreshClick() {
				new ActivityInfoLoader(eventId).execute("");
			}
		});
		bottom.init(null, true, true, LinearLayout.LayoutParams.FILL_PARENT,
				(int) (screenHeight * barH));
		LinearLayout.LayoutParams p1 = new LinearLayout.LayoutParams(
				(int) (screenWidth * w), (int) (screenHeight * h));
		p1.gravity = Gravity.CENTER_HORIZONTAL;
		status.setLayoutParams(p1);
		p1.topMargin = 20;
		qiandao.setLayoutParams(p1);
		bottom.setRightAction(new BottomBar.CallAction(this));
		title.setHeight((int) (fontH1 * screenHeight));
		beginTime.setHeight((int) (fontH2 * screenHeight));
		endTime.setHeight((int) (fontH1 * screenHeight));
		begin_time.setHeight((int) (fontH1 * screenHeight));
		end_time.setHeight((int) (fontH1 * screenHeight));

		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
				(int) (statusW * screenWidth), (int) (fontH3 * screenHeight));
		lp.leftMargin = (int) (btnLmargin * screenWidth);
		lp.topMargin = (int) (btnTmargin * screenHeight);
		// 设置logo的位置布局.
		status_btn.setLayoutParams(lp);

		LinearLayout.LayoutParams lp2 = new LinearLayout.LayoutParams(
				(int) (qiandaoW * screenWidth), (int) (fontH3 * screenHeight));
		lp2.leftMargin = (int) (btnLmargin * screenWidth);
		lp2.topMargin = (int) (btnTmargin * screenHeight);
		qiandao_btn.setLayoutParams(lp2);

		LinearLayout.LayoutParams lp3 = new LinearLayout.LayoutParams(
				(int) (jiantouW * screenWidth), (int) (jiantouH * screenHeight)); 
		lp3.rightMargin = (int) (jiantouRmargin * screenWidth);
		lp3.topMargin = (int) (jiantouTmargin * screenHeight);
		// 设置logo的位置布局.
		jiantou1.setLayoutParams(lp3);
		jiantou2.setLayoutParams(lp3);

		LinearLayout.LayoutParams lp4 = new LinearLayout.LayoutParams(
				(int) (imgW * screenWidth), (int) (imgH * screenHeight));
		lp4.leftMargin = (int) (imgLmargin * screenWidth);
		lp4.topMargin = (int) (imgTmargin * screenHeight);
		// 设置logo的位置布局.
		qiandao_img.setLayoutParams(lp4);
		status_img.setLayoutParams(lp4);
	}

	/**
	 * 得到控件.
	 */
	private void initLayout() {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activiti_info);
		qiandao = (LinearLayout) findViewById(R.id.qiandao);
		status = (LinearLayout) findViewById(R.id.status);
		beginTime = (TextView) findViewById(R.id.act_begin_time);
		endTime = (TextView) findViewById(R.id.act_end_time);
		title = (TextView) findViewById(R.id.title);
		imge = (ImageView) findViewById(R.id.activity_pic);
		head = (ActionBar) findViewById(R.id.info_head);
		bottom = (BottomBar) findViewById(R.id.info_bottom);
		begin_time = (TextView) findViewById(R.id.begin_time);
		end_time = (TextView) findViewById(R.id.end_time);
		status_btn = (ImageView) findViewById(R.id.status_btn);
		jiantou1 = (ImageView) findViewById(R.id.jiantou1);
		status_img = (ImageView) findViewById(R.id.status_img);
		qiandao_img = (ImageView) findViewById(R.id.qiandao_img);
		jiantou2 = (ImageView) findViewById(R.id.jiantou2);
		qiandao_btn = (ImageView) findViewById(R.id.qiandao_btn);
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
				beginTime.setText(info.getString("starttime"));
				endTime.setText(info.getString("endtime"));
				title.setText(info.getString("name"));
				url = info.getString("url");
				new Thread(new LoadImage(url, imge, R.drawable.huodong_paper))
						.start();
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