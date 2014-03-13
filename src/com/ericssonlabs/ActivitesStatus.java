package com.ericssonlabs;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.ericssonlabs.bean.EventInfo;
import com.ericssonlabs.bean.ServerResult;
import com.ericssonlabs.util.ActionBar;
import com.ericssonlabs.util.ActionBar.OnRefreshClickListener;
import com.ericssonlabs.util.BottomBar;
import com.ericssonlabs.util.HttpRequire;
import com.ericssonlabs.util.LoadImage;

/**
 * 活动列表详情.
 * 
 * @author 130126
 * 
 */
public class ActivitesStatus extends BaseActivity {
	private String eventId;
	private TextView yishouchu;
	private TextView yiqiandao;
	private String auth;
	private ImageView activity_pic, img2, img1;
	private LinearLayout all;
	private ActionBar head;
	private BottomBar bottom;
	private float screenHeight = 0;
	private float screenWidth = 0;

	/**
	 * 调用远程数据请求数据.
	 * 
	 * @param eventId
	 * @return
	 */
	private EventInfo activitiDetail(String eventId) {
		try {
			ServerResult result = HttpRequire.activitiDetail(eventId, auth);
			EventInfo t = (EventInfo) JSON.parseObject(result.getData()
					.toJSONString(), EventInfo.class);
			Message msg = new Message();
			msg.what = 2;
			Bundle b = new Bundle();
			b.putString("totalcount", t.getTotalcount() + "");
			b.putString("joincount", t.getJoincount() + "");
			b.putString("checkcount", t.getCheckcount() + "");
			msg.setData(b);
			myHandler.sendMessage(msg);
			return t;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	// 页面活动调用.
	public Handler myHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				alert("对不起，该用户没有权限");
				break;
			case 2:
				Bundle info = msg.getData();
				yishouchu.setText(info.getString("joincount") + "/"
						+ info.getString("totalcount"));
				yiqiandao.setText(info.getString("checkcount") + "/"
						+ info.getString("joincount"));
				break;
			default:
				super.hasMessages(msg.what);
				break;
			}
		}
	};

	/**
	 * 页面字体适配.
	 */
	private void adjustScreen() {
		float[] screen2 = getScreen2();
		screenHeight = screen2[1];
		screenWidth = screen2[0];
		head.init(R.drawable.i5_top_activity_detail, true, true,
				(int) (screenHeight * barH));
		head.setTitleSize((int) (screenWidth * titleW4),
				(int) (screenHeight * titleH));
		head.setLeftSize((int) (screenWidth * lftBtnW),
				(int) (screenHeight * lftBtnH), (int) (screenHeight * lftBtnT));
		head.setRightSize((int) (screenWidth * rgtBtnW),
				(int) (screenHeight * rgtBtnH));
		bottom.init(null, true, true, LinearLayout.LayoutParams.FILL_PARENT,
				(int) (screenHeight * barH));
		bottom.setRightAction(new BottomBar.CallAction(this));

		setWidthHeight(all, 366 / 378f, 160 / 670f);
		setWidthHeight(activity_pic, 114 / 378f, 152 / 670f, 4 / 670f,
				10 / 378f);
		setWidthHeight(img1, 75 / 378f, 75 / 670f, 20 / 670f, 20 / 378f);
		setWidthHeight(img2, 75 / 378f, 75 / 670f, 20 / 670f, 20 / 378f);
	}

	/**
	 * 得到页面元素.
	 */
	private void initLayout() {
		yishouchu = (TextView) findViewById(R.id.yishouchu);
		yiqiandao = (TextView) findViewById(R.id.yiqiandao);

		activity_pic = (ImageView) findViewById(R.id.activity_pic);
		img1 = (ImageView) findViewById(R.id.img1);
		img2 = (ImageView) findViewById(R.id.img2);

		all = (LinearLayout) findViewById(R.id.all);

		head = (ActionBar) findViewById(R.id.status_head);
		head.setLeftAction(new ActionBar.BackAction(this));
		head.setRightAction(new ActionBar.RefreshAction(head));
		head.setRefreshEnabled(new OnRefreshClickListener() {
			public void onRefreshClick() {
				new ActivityStatusLoader(eventId).execute("");
			}
		});
		bottom = (BottomBar) findViewById(R.id.status_bottom);
		adjustScreen();

		Intent intent = getIntent();
		eventId = intent.getStringExtra("eventid");
		auth = intent.getStringExtra("auth");
		new Thread(new LoadImage(intent.getStringExtra("url"), activity_pic,
				R.drawable.huodong_paper, getResources())).start();
		new ActivityStatusLoader(eventId).execute("");
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activiti_status);
		initLayout();

	}

	/**
	 * 显示活动的详情信息.
	 */
	private class ActivityStatusLoader extends
			AsyncTask<String, String, String> {

		private String eventId;

		public ActivityStatusLoader(String eventId) {
			this.eventId = eventId;
		}

		public String doInBackground(String... p) {
			activitiDetail(eventId);
			return "";
		}
	}

}