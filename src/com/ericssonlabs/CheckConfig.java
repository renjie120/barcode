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

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.ericssonlabs.bean.ServerResults;
import com.ericssonlabs.bean.TicketTypeItem;
import com.ericssonlabs.util.Constant;

/**
 * 配置界面
 * 
 */
public class CheckConfig extends BaseActivity implements OnItemClickListener {
	private ListView list;
	private String token;
	private String eventid;
	private static final int DIALOG_KEY = 0;
	private ServerResults result;
	private String xianzhi;
	private SharedPreferences mSharedPreferences;
	private ImageView xianzhiImg;
	private ProgressDialog dialog;
	private String temp;
	private TextView sss;

	protected void onResume() {
		super.onResume();
		setContentView(R.layout.check_config);
		sss = (TextView) findViewById(R.id.shaixuanqi);
		xianzhi = mSharedPreferences.getString("xianzhi", "false");
		if ("false".equals(xianzhi)) {
			sss.setText("筛选器是关闭的");
		} else {
			sss.setText("筛选器是启用的");
		}
	}

	/**
	 * 跳转到配置的过滤界面.
	 * 
	 * @param arg0
	 */
	public void gofilter(View arg0) {
		setContentView(R.layout.check_filter);
		list = (ListView) findViewById(R.id.ListView);
		list.setOnItemClickListener(this);
		xianzhi = mSharedPreferences.getString("xianzhi", "false");
		xianzhiImg = (ImageView) findViewById(R.id.xianzhiImg);
		if ("false".equals(xianzhi)) {
			xianzhiImg.setSelected(false);
		} else {
			xianzhiImg.setSelected(true);
		}
		new MyListLoader(true, eventid).execute("");
	}

	/**
	 * 页面的处理界面
	 */
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
						CheckConfig.this);
				list.setAdapter(adapter);
				break;
			default:
				super.hasMessages(msg.what);
				break;
			}
		}
	};

	/**
	 * 显示票据的类型数据信息.
	 */
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
				String typeId = "" + markerItem.get("typeid");
				viewHolder.typename.setText("" + markerItem.get("typename"));
				viewHolder.ischeck.setTag(typeId);
				String isCheck = mSharedPreferences.getString(temp + typeId,
						"false");
				if ("true".equals(isCheck)) {
					viewHolder.ischeck.setVisibility(View.VISIBLE);
				} else {
					viewHolder.ischeck.setVisibility(View.GONE);
				}
			}
			return convertView;
		}
	}

	public final static class ViewHolder {
		public TextView typename;
		public ImageView ischeck;
	}

	/**
	 * 调用远程数据请求数据.
	 * 
	 * @param eventId
	 */
	private void typelist(String eventId) {
		DefaultHttpClient httpclient = new DefaultHttpClient();
		String encoding = "UTF-8";
		try {

			HttpPost httpost = new HttpPost(Constant.HOST
					+ "?do=listtype&token=" + token + "&eventid=" + eventId);
			System.out.println("查看全部票的类型：" + Constant.HOST
					+ "?do=listtype&token=" + token + "&eventid=" + eventId);
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

	/**
	 * 查询票据的类型数据信息.
	 */
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

	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case DIALOG_KEY: {
			dialog = new ProgressDialog(this);
			dialog.setMessage("正在查询...");
			dialog.setIndeterminate(true);
			dialog.setCancelable(true);
			return dialog;
		}
		}
		return null;
	}

	/**
	 * 改变页面的是否过滤类型失效.
	 * 
	 * @param arg0
	 */
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
		setContentView(R.layout.check_config);
		sss = (TextView) findViewById(R.id.shaixuanqi);
		mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this
				.getApplication());
		Intent intent = getIntent();
		token = intent.getStringExtra("token");
		eventid = intent.getStringExtra("eventid");
		temp = token + ";" + eventid + ";";
		xianzhi = mSharedPreferences.getString("xianzhi", "false");
		if ("false".equals(xianzhi)) {
			sss.setText("筛选器是关闭的");
		} else {
			sss.setText("筛选器是启用的");
		}
	}

	/**
	 * 点击列表的时候设置是否显示对应的门票类型.
	 */
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		ImageView v = (ImageView) arg1.findViewById(R.id.checkimg);
		String typeId = "" + v.getTag();
		String isCheck = mSharedPreferences.getString(temp + typeId, "false");
		Editor e = mSharedPreferences.edit();
		if ("true".equals(isCheck)) {
			v.setVisibility(View.GONE);
			e.putString(temp + typeId, "false");
		} else {
			v.setVisibility(View.VISIBLE);
			e.putString(temp + typeId, "true");
		}
		e.commit();
	}
}