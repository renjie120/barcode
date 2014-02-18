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
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.ericssonlabs.bean.EventInfo;
import com.ericssonlabs.bean.ServerResult;

/**
 * 活动列表详情.
 * 
 * @author 130126
 * 
 */
public class ActivitesInfo extends BaseActivity {
	private String eventId;
	private TextView beginTime;
	private TextView endTime;
	private LinearLayout qiandao;
	private LinearLayout status;
	private TextView title;
	private String token;

	private EventInfo activitiDetail(String eventId) {
		DefaultHttpClient httpclient = new DefaultHttpClient();
		String encoding = "UTF-8";
		try {

			HttpPost httpost = new HttpPost(
					"http://jb.17miyou.com/api.ashx?do=eventinfo&eventid="
							+ eventId + "&token=" + token);
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

	public void qiandao(View arg0) {
		LinearLayout layout = (LinearLayout) arg0;
		Intent intent = new Intent(ActivitesInfo.this, ActivitesInfo.class);
		intent.putExtra("eventid", layout.getTag().toString());
		intent.putExtra("token", layout.getTag().toString());
		this.startActivity(intent);
	}

	public void status(View arg0) {
		LinearLayout layout = (LinearLayout) arg0;
		Intent intent = new Intent(ActivitesInfo.this, ActivitesStatus.class);
		intent.putExtra("eventid", layout.getTag().toString());
		this.startActivity(intent);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activiti_info);
		qiandao = (LinearLayout) findViewById(R.id.qiandao);
		status = (LinearLayout) findViewById(R.id.status);
		beginTime = (TextView) findViewById(R.id.act_begin_time);
		endTime = (TextView) findViewById(R.id.act_end_time);
		title = (TextView) findViewById(R.id.title);
		Intent intent = getIntent();
		eventId = intent.getStringExtra("eventid");
		qiandao.setTag(eventId);
		status.setTag(eventId);
		token = intent.getStringExtra("token");
		new MyListLoader(eventId).execute("");

	}

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
				break;
			default:
				super.hasMessages(msg.what);
				break;
			}
		}
	};

	private class MyListLoader extends AsyncTask<String, String, String> {

		private String eventId;

		public MyListLoader(String eventId) {
			this.eventId = eventId;
		}

		@Override
		protected void onPreExecute() {
		}

		public String doInBackground(String... p) {
			activitiDetail(eventId);
			return "";
		}

		@Override
		public void onPostExecute(String Re) {

		}

		@Override
		protected void onCancelled() {
		}
	}

}