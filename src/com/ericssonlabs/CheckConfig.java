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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.ericssonlabs.bean.ServerResults;
import com.ericssonlabs.bean.TicketTypeItem;
import com.ericssonlabs.util.ActionBar;
import com.ericssonlabs.util.ActionBar.OnRefreshClickListener;
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
	private TextView shaixuanqi, name_title, filter_msg, filter_title;
	private ActionBar head;
	private ActionBar head2;
	private float screenHeight = 0;
	private float screenWidth = 0;

	/**
	 * 设置参数配置的字体大小.
	 * @param screenWidth
	 * @return
	 */
	public int adjusCheckconfigFontSize(int screenWidth) {
		if (screenWidth <= 240) { // 240X320 屏幕
			return 12;
		} else if (screenWidth <= 320) { // 320X480 屏幕
			return 16;
		} else if (screenWidth <= 480) { // 480X800 或 480X854 屏幕
			return 18;
		} else if (screenWidth <= 540) { // 540X960 屏幕
			return 22;
		} else if (screenWidth <= 800) { // 800X1280 屏幕
			return 26;
		} else { // 大于 800X1280
			return 26;
		}
	}

	protected void onResume() {
		super.onResume();
		initConfig();
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
		head2 = (ActionBar) findViewById(R.id.ticket_filter_head);
		head2.setLeftAction(new ActionBar.BackAction(this));
		head2.setRightAction(new ActionBar.RefreshAction(head2));
		head2.setRefreshEnabled(new OnRefreshClickListener() {
			public void onRefreshClick() {
				new ActivityTypeLoader(true, eventid).execute("");
			}
		});
		xianzhi = mSharedPreferences.getString("xianzhi", "false");
		xianzhiImg = (ImageView) findViewById(R.id.xianzhiImg);
		filter_title = (TextView) findViewById(R.id.filter_title);
		filter_msg = (TextView) findViewById(R.id.filter_msg);
		if ("false".equals(xianzhi)) {
			xianzhiImg.setSelected(false);
		} else {
			xianzhiImg.setSelected(true);
		}
		new ActivityTypeLoader(true, eventid).execute("");

		adjustScreen2();
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
				TicketTypeAdapter adapter = new TicketTypeAdapter(listItem,
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
	class TicketTypeAdapter extends BaseAdapter {
		private ArrayList<HashMap<String, Object>> data;// 用于接收传递过来的Context对象
		private Context context;

		public TicketTypeAdapter(ArrayList<HashMap<String, Object>> data,
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
				viewHolder.typename.setTextSize(adjusCheckconfigFontSize((int)screenWidth));
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
	private class ActivityTypeLoader extends AsyncTask<String, String, String> {

		private boolean showDialog;
		private String eventId;

		public ActivityTypeLoader(boolean showDialog, String eventId) {
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

	/**
	 * 屏幕适配.
	 */
	private void adjustScreen() {
		float[] screen2 = getScreen2();
		screenHeight = screen2[1];
		screenWidth = screen2[0];
		head = (ActionBar) findViewById(R.id.ticket_config_head);
		head.init(getText(R.string.title_config).toString(), true, true,
				LinearLayout.LayoutParams.FILL_PARENT,
				(int) (screenHeight * barH),
				adjustTitleFontSize((int) screenWidth));
		head.setLeftAction(new ActionBar.BackAction(this));
		head.setRightAction(new ActionBar.RefreshAction(head));
		head.setRefreshEnabled(new OnRefreshClickListener() {
			public void onRefreshClick() {
				xianzhi = mSharedPreferences.getString("xianzhi", "false");
				if ("false".equals(xianzhi)) {
					shaixuanqi.setText("筛选器是关闭的");
				} else {
					shaixuanqi.setText("筛选器是启用的");
				}
			}
		});
		name_title.setTextSize(adjusCheckconfigFontSize((int) screenWidth));
		shaixuanqi.setTextSize(adjusCheckconfigFontSize((int) screenWidth) - 4);
	}

	/**
	 * 参数过滤界面配置屏幕适配.
	 */
	private void adjustScreen2() {
		head2.init(getText(R.string.title_filter).toString(), true, true,
				LinearLayout.LayoutParams.FILL_PARENT,
				(int) (screenHeight * barH),
				adjustTitleFontSize((int) screenWidth));
		
		filter_title.setTextSize(adjusCheckconfigFontSize((int) screenWidth)+2);
		filter_msg.setTextSize(adjusCheckconfigFontSize((int) screenWidth)+2);
	}

	/**
	 * 初始化配置界面.
	 */
	private void initConfig() {
		setContentView(R.layout.check_config);
		name_title = (TextView) findViewById(R.id.name_title);
		shaixuanqi = (TextView) findViewById(R.id.shaixuanqi);
		mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this
				.getApplication());
		mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this
				.getApplication());
		xianzhi = mSharedPreferences.getString("xianzhi", "false");
		if ("false".equals(xianzhi)) {
			shaixuanqi.setText("筛选器是关闭的");
		} else {
			shaixuanqi.setText("筛选器是启用的");
		}
		Intent intent = getIntent();
		token = intent.getStringExtra("token");
		eventid = intent.getStringExtra("eventid");
		temp = token + ";" + eventid + ";";
		adjustScreen();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		initConfig();

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