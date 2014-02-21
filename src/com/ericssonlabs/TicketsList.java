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
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ericssonlabs.bean.ServerResult;
import com.ericssonlabs.bean.TicketList;
import com.ericssonlabs.bean.TicketListItem;
import com.ericssonlabs.util.Constant;
import com.ericssonlabs.util.PingYinUtil;

/**
 * 订票列表界面.
 * 
 */
public class TicketsList extends BaseActivity {
	private ListView list;
	private String token;
	private TextView search;
	private TextView totalcountText;
	private String eventId;
	private static final int DIALOG_KEY = 0;
	private ServerResult result;
	private MyImgAdapter adapter;
	private ProgressDialog dialog;
	private String temp;
	private SharedPreferences mSharedPreferences;

	public void cancel(View arg0) {
		search.setText("");
	}

	protected void onResume() {
		super.onResume();
		// 重新刷新一下列表
		if (adapter != null) {
			adapter.searchByType();
			list.setAdapter(adapter);
		}
	}

	/**
	 * 搜索文本框变化内容之后自动搜索.
	 */
	private TextWatcher searchWatcher = new TextWatcher() {

		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
		}

		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
		}

		public void afterTextChanged(Editable s) {
			if (s != null) {
				adapter.search(s.toString().trim());
			} else {
				adapter.search("");
			}
			list.setAdapter(adapter);
		}
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.tickets_list);
		mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this
				.getApplication());
		list = (ListView) findViewById(R.id.ListView);
		totalcountText = (TextView) findViewById(R.id.totalcount);
		Intent intent = getIntent();
		search = (TextView) findViewById(R.id.searchText);
		// 设置文本框的搜索事件.
		search.addTextChangedListener(searchWatcher);

		token = intent.getStringExtra("token");
		eventId = intent.getStringExtra("eventid");
		temp = token + ";" + eventId + ";";
		// 加载票据列表页面.
		new MyListLoader(true, eventId).execute("");
	}

	/**
	 * 页面元素处理事件.
	 */
	public Handler myHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				alert("对不起，出现异常");
				break;
			case 2:
				// 解析返回来的json数据信息.
				JSONObject json = result.getData();
				TicketList t = (TicketList) JSON.parseObject(
						json.toJSONString(), TicketList.class);
				totalcountText.setText("截止目前，入场人数为：" + t.getTotalcount() + "人");
				List<TicketListItem> items = t.getItems();
				ArrayList<HashMap<String, Object>> listItem = new ArrayList<HashMap<String, Object>>();
				if (items != null && items.size() >= 1) {
					for (TicketListItem i : items) {
						HashMap<String, Object> map = new HashMap<String, Object>();
						map.put("status", i.getCheckstatus());// 图像资源的ID
						map.put("name", i.getName());
						map.put("type", i.getType() + "");
						map.put("phone", i.getPhone());
						map.put("charge", i.getChargetype());
						// 计算得到汉字名称的首字母的第一个拼音.
						map.put("firstletter",
								PingYinUtil.getPinyin2(i.getName())
										.substring(0, 1).toUpperCase());
						listItem.add(map);
					}
				}
				// 显示列表.
				adapter = new MyImgAdapter(listItem, TicketsList.this);
				list.setAdapter(adapter);
				break;
			default:
				super.hasMessages(msg.what);
				break;
			}
		}
	};

	/**
	 * 创建等待条.
	 */
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
	 * 列表加载操作.
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
			tickeslist(eventId);
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

	/**
	 * 调用请求返回订票的列表信息.
	 * 
	 * @param eventId
	 */
	private void tickeslist(String eventId) {
		DefaultHttpClient httpclient = new DefaultHttpClient();
		String encoding = "UTF-8";
		try {
			HttpPost httpost = new HttpPost(Constant.HOST
					+ "?do=mytickets&eventid=" + eventId + "&token=" + token);
			HttpResponse response = httpclient.execute(httpost);
			HttpEntity entity = response.getEntity();
			BufferedReader br = new BufferedReader(new InputStreamReader(
					entity.getContent(), encoding));
			result = (ServerResult) JSON.parseObject(br.readLine(),
					ServerResult.class);
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
	 * 用于票据列表的数据适配器.
	 */
	class MyImgAdapter extends BaseAdapter {
		private ArrayList<HashMap<String, Object>> data, olddata;// 用于接收传递过来的Context对象
		private Context context;

		public MyImgAdapter(ArrayList<HashMap<String, Object>> data,
				Context context) {
			super();
			this.data = data;
			olddata = new ArrayList<HashMap<String, Object>>();
			if (data != null && data.size() > 0) {
				olddata.addAll(data);
			}
			this.context = context;
		}

		/**
		 * 根据名字等信息查询方法的具体实现.
		 * 
		 * @param searchText
		 */
		public void search(String searchText) {
			try {
				data.clear();
				if (searchText == null || searchText.trim().equals("")) {
					if (olddata != null && olddata.size() > 0) {
						data.addAll(olddata);
					}
				} else {
					for (HashMap<String, Object> m : olddata) {
						if (!data.contains(m)) {
							boolean hit = false;
							// 按照名字查询
							if (!hit) {
								String text = m.get("name") + "";
								if (text != null && text.contains(searchText)) {
									hit = true;
								}
							}

							// 按照首字母查询
							if (!hit) {
								String text = m.get("firstletter") + "";
								if (text != null
										&& text.contains(searchText
												.toUpperCase())) {
									hit = true;
								}
							}

							// 查询电话号码
							if (!hit) {
								String text = m.get("phone") + "";
								if (text != null
										&& text.toString().startsWith(
												searchText.toUpperCase())) {
									hit = true;
								}
							}
							if (hit) {
								data.add(m);
							}
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		/**
		 * 根据设置的类型进行过滤.
		 */
		public void searchByType() {
			try {
				// 如果设置了要进行类型的过滤就进行下面的筛选.
				if ("true".equals(mSharedPreferences.getString("xianzhi",
						"false"))) {
					data.clear();
					// 逐行扫描数据，添加满足类型的数据
					for (HashMap<String, Object> m : olddata) {
						if (!data.contains(m)) {
							boolean hit = false;
							// 按照类型查询对应的结果.
							if (!hit) {
								String type = m.get("type") + "";
								// 判断是否设置了要显示数据.
								if ("true".equals(mSharedPreferences.getString(
										temp + type, "false"))) {
									hit = true;
								}
							}
							if (hit) {
								data.add(m);
							}
						}

					}
				}
				// 否则就显示全部的数据.
				else {
					data.clear();
					data.addAll(olddata);
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
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
				convertView = mInflater.inflate(R.layout.tickets_item, null);

				viewHolder.name = (TextView) convertView
						.findViewById(R.id.ticket_name);
				viewHolder.type = (LinearLayout) convertView
						.findViewById(R.id.type);
				viewHolder.state_img = (ImageView) convertView
						.findViewById(R.id.state_img);
				viewHolder.phone = (TextView) convertView
						.findViewById(R.id.phone);
				viewHolder.status = (TextView) convertView
						.findViewById(R.id.state_text);
				viewHolder.charge = (TextView) convertView
						.findViewById(R.id.chargeType);
				viewHolder.first_letter = (TextView) convertView
						.findViewById(R.id.first_letter);
				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}

			HashMap<String, Object> markerItem = getItem(position);
			// 显示票据列表信息.
			if (null != markerItem) {
				viewHolder.phone.setText("手机号码：" + markerItem.get("phone"));
				viewHolder.status
						.setText("1".equals(markerItem.get("status")) ? "已签到"
								: "未签到");
				if ("1".equals(markerItem.get("status"))) {
					viewHolder.state_img
							.setBackgroundResource(R.drawable.state1);
				} else {
					viewHolder.state_img
							.setBackgroundResource(R.drawable.state2);
				}
				viewHolder.name.setText("" + markerItem.get("name"));
				viewHolder.charge
						.setText("2".equals(markerItem.get("charge")) ? "收费"
								: "免费");
				// 根据第一个首字母显示对应的分类标题。
				if (position == 0) {
					viewHolder.type.setVisibility(View.VISIBLE);
					viewHolder.first_letter.setText(markerItem
							.get("firstletter") + "");
				} else if (position > 0) {
					HashMap<String, Object> lastMap = data.get(position - 1);
					if (markerItem.get("firstletter").equals(
							lastMap.get("firstletter"))) {
						viewHolder.type.setVisibility(View.GONE);
					} else {
						viewHolder.type.setVisibility(View.VISIBLE);
						viewHolder.first_letter.setText(markerItem
								.get("firstletter") + "");
					}
				}
			}
			return convertView;
		}
	}

	public final static class ViewHolder {
		public TextView name;// 购票者姓名
		public LinearLayout type;// 类型
		public TextView status;// 签到状态
		public ImageView state_img;// 签到状态
		public TextView phone;// 电话号码
		public TextView charge;// 是否免费
		public TextView first_letter; // 首字母
	}
}