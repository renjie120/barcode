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
import android.view.Window;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.ericssonlabs.bean.EventInfo;
import com.ericssonlabs.bean.ServerResult;
import com.ericssonlabs.util.Constant;

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
	private String token;

	/**
	 * 调用远程数据请求数据.
	 * @param eventId
	 * @return
	 */
	private EventInfo activitiDetail(String eventId) {
		DefaultHttpClient httpclient = new DefaultHttpClient();
		String encoding = "UTF-8";
		try {

			HttpPost httpost = new HttpPost(Constant.HOST
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
			b.putString("totalcount", t.getTotalcount() + "");
			b.putString("joincount", t.getJoincount() + "");
			b.putString("checkcount", t.getCheckcount() + "");
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

	//页面活动调用.
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

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activiti_status);
		yishouchu = (TextView) findViewById(R.id.yishouchu);
		yiqiandao = (TextView) findViewById(R.id.yiqiandao);
		Intent intent = getIntent();
		eventId = intent.getStringExtra("eventid");
		token = intent.getStringExtra("token");
		new MyListLoader(eventId).execute("");
	}

	/**
	 * 显示活动的详情信息.
	 */
	private class MyListLoader extends AsyncTask<String, String, String> {

		private String eventId;

		public MyListLoader(String eventId) {
			this.eventId = eventId;
		}

		public String doInBackground(String... p) {
			activitiDetail(eventId);
			return "";
		}
	}

}