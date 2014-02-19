package com.ericssonlabs;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.ericssonlabs.bean.ServerResults;
import com.ericssonlabs.bean.TicketTypeItem;

/**
 * 类型过滤器.
 * 
 * @author 130126
 * 
 */
public class CheckFilter extends BaseActivity {
	private ListView list;
	private String token;
	private String eventId;
	private static final int DIALOG_KEY = 0;
	private ServerResults result;
	private String xianzhi;
	private SharedPreferences mSharedPreferences;
	private ImageView xianzhiImg;

	public void changetype(View arg0) {
		ImageView v = (ImageView) arg0;
		SharedPreferences.Editor mEditor = mSharedPreferences.edit(); 
		if ("false".equals(xianzhi)) {
			mEditor.putString("xianzhi", "true");
			xianzhi = "true";
			v.setSelected(true);
		} else {
			xianzhi = "false";
			mEditor.putString("xianzhi", "false");
			v.setSelected(false);
		} 
		mEditor.commit();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.check_filter);
		list = (ListView) findViewById(R.id.ListView);
		Intent intent = getIntent();
		token = intent.getStringExtra("token");
		eventId = intent.getStringExtra("eventid");
		mSharedPreferences = PreferenceManager
				.getDefaultSharedPreferences(this);
		xianzhi = mSharedPreferences.getString("xianzhi", "false");
		xianzhiImg = (ImageView) findViewById(R.id.xianzhiImg); 
		if ("false".equals(xianzhi)) {
			xianzhiImg.setSelected(false);
		} else {
			xianzhiImg.setSelected(true);
		}
		new MyListLoader(true, eventId).execute("");
	}

	public Handler myHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				alert("对不起，出现异常");
				break;
			case 2:
				JSONArray json = result.getData();
				List<TicketTypeItem> t = (List<TicketTypeItem>) JSON
						.parseArray(json.toJSONString(), TicketTypeItem.class);
				ArrayList<HashMap<String, Object>> listItem = new ArrayList<HashMap<String, Object>>();
				if (t != null && t.size() >= 1) {
					for (TicketTypeItem i : t) {
						HashMap<String, Object> map = new HashMap<String, Object>();
						map.put("typeid", i.getTypeid());// 图像资源的ID
						map.put("typename", i.getName());
						listItem.add(map);
					}
				}
				MyImgAdapter adapter = new MyImgAdapter(listItem,
						CheckFilter.this);
				list.setAdapter(adapter);
				break;
			default:
				super.hasMessages(msg.what);
				break;
			}
		}
	};

	private void typelist(String eventId) {
		DefaultHttpClient httpclient = new DefaultHttpClient();
		String encoding = "UTF-8";
		try {

			HttpPost httpost = new HttpPost(
					"http://jb.17miyou.com/api.ashx?do=listtype&token=" + token
							+ "&eventid=" + eventId);
			HttpResponse response = httpclient.execute(httpost);
			HttpEntity entity = response.getEntity();
			BufferedReader br = new BufferedReader(new InputStreamReader(
					entity.getContent(), encoding));
			result = (ServerResults) JSON.parseObject(br.readLine(),
					ServerResults.class);
			if (1 != result.getErrorcode()) {
				myHandler.sendEmptyMessage(1);
			}
			// 否则就进行文件解析处理.
			else {
				myHandler.sendEmptyMessage(2);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			httpclient.getConnectionManager().shutdown();
		}
	}

	class MyImgAdapter extends BaseAdapter {
		private ArrayList<HashMap<String, Object>> data;// 用于接收传递过来的Context对象
		private Context context;

		public MyImgAdapter(ArrayList<HashMap<String, Object>> data,
				Context context) {
			super();
			this.data = data;
			this.context = context;
		}

		@Override
		public int getCount() {
			int count = 0;
			if (null != data) {
				count = data.size();
			}
			return count;
		}

		@Override
		public HashMap<String, Object> getItem(int position) {
			return data.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder viewHolder = null;
			if (null == convertView) {
				viewHolder = new ViewHolder();
				LayoutInflater mInflater = LayoutInflater.from(context);
				convertView = mInflater.inflate(R.layout.type_item, null);

				viewHolder.typename = (TextView) convertView
						.findViewById(R.id.typename);
				viewHolder.ischeck = (ImageView) convertView
						.findViewById(R.id.checkimg);

				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}

			HashMap<String, Object> markerItem = getItem(position);
			if (null != markerItem) {
				viewHolder.typename.setText("" + markerItem.get("typename"));
				viewHolder.ischeck.setTag(markerItem.get("typeid"));
			}
			return convertView;
		}
	}

	public final static class ViewHolder {
		public TextView typename;
		public ImageView ischeck;
	}

	private class MyListLoader extends AsyncTask<String, String, String> {

		private boolean showDialog;
		private String eventId;

		public MyListLoader(boolean showDialog, String eventId) {
			this.showDialog = showDialog;
			this.eventId = eventId;
		}

		@Override
		protected void onPreExecute() {
			if (showDialog) {
				showDialog(DIALOG_KEY);
			}
		}

		public String doInBackground(String... p) {
			typelist(eventId);
			return "";
		}

		@Override
		public void onPostExecute(String Re) {
			if (showDialog) {
				removeDialog(DIALOG_KEY);
			}
		}

		@Override
		protected void onCancelled() {
			if (showDialog) {
				removeDialog(DIALOG_KEY);
			}
		}
	}

}