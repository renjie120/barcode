package com.juban;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
import com.juban.bean.ServerResult;
import com.juban.bean.TicketList;
import com.juban.bean.TicketListItem;
import com.juban.util.ActionBar;
import com.juban.util.ActionBar.OnRefreshClickListener;
import com.juban.util.HttpRequire;
import com.juban.util.PingYinUtil;

/**
 * 订票列表界面.
 * 
 */
public class TicketsList extends BaseActivity {
	private ListView list;
	private String token, auth;
	private TextView search;
	private TextView totalcountText;
	private String eventId;
	private static final int DIALOG_KEY = 0;
	private ServerResult result;
	private TicketListAdapter adapter;
	private ProgressDialog dialog;
	private String temp;
	private SharedPreferences mSharedPreferences;
	private ActionBar head;
	private float screenHeight = 0;
	private float screenWidth = 0;

	public void cancel(View arg0) {
		search.setText("");
	}

	protected void onResume() {
		super.onResume();
		// 重新刷新一下列表
		if (adapter != null) {
			search.setText("");
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
			// list.setAdapter(adapter);
		}
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.tickets_list);
		float[] screen2 = getScreen2();
		screenHeight = screen2[1];
		screenWidth = screen2[0];
		head = (ActionBar) findViewById(R.id.ticket_list_head);
		head.init(R.drawable.i5_top_checkin, true, true,
				(int) (screenHeight * barH));
		head.setTitleSize((int) (screenWidth * titleW4),
				(int) (screenHeight * titleH));
		head.setLeftSize((int) (screenWidth * lftBtnW),
				(int) (screenHeight * lftBtnH), (int) (screenHeight * lftBtnT));
		head.setRightSize((int) (screenWidth * rgtBtnW),
				(int) (screenHeight * rgtBtnH));

		head.setLeftAction(new ActionBar.BackAction(this));
		head.setRightAction(new ActionBar.RefreshAction(head));
		head.setRefreshEnabled(new OnRefreshClickListener() {
			public void onRefreshClick() {
				new TicketsLoader(true, eventId).execute("");
			}
		});
		mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this
				.getApplication());
		list = (ListView) findViewById(R.id.ListView);
		// list.setDivider(R.drawable.);
		totalcountText = (TextView) findViewById(R.id.totalcount);
		Intent intent = getIntent();
		search = (TextView) findViewById(R.id.searchText);
		// 设置文本框的搜索事件.
		search.addTextChangedListener(searchWatcher);

		token = intent.getStringExtra("token");
		eventId = intent.getStringExtra("eventid");
		auth = intent.getStringExtra("auth");
		temp = auth + ";" + eventId + ";";
		// 加载票据列表页面.
		new TicketsLoader(true, eventId).execute("");
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
				adapter = new TicketListAdapter(listItem, TicketsList.this);
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
	private class TicketsLoader extends AsyncTask<String, String, String> {

		private boolean showDialog;
		private String eventId;

		public TicketsLoader(boolean showDialog, String eventId) {
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
		try {
			result = HttpRequire.tickeslist(eventId, auth); 
			if (1 != result.getErrorcode()) {
				myHandler.sendEmptyMessage(1);
			}
			// 否则就进行文件解析处理.
			else {
				myHandler.sendEmptyMessage(2);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}  
	}

	/**
	 * 用于票据列表的数据适配器.
	 */
	class TicketListAdapter extends BaseAdapter {
		private ArrayList<HashMap<String, Object>> data, olddata;// 用于接收传递过来的Context对象
		private Context context;

		public TicketListAdapter(ArrayList<HashMap<String, Object>> data,
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
				// 如果没有设置要进行类型的过滤就进行下面的筛选.
				if (!"true".equals(mSharedPreferences.getString("xianzhi",
						"false"))) {
					if (searchText == null || searchText.trim().equals("")) {
						if (olddata != null && olddata.size() > 0) {
							data.addAll(olddata);
						}
					} else {
						for (HashMap<String, Object> m : olddata) {
							if (!data.contains(m)) {
								if (filterByText(m, searchText)) {
									data.add(m);
								}
							}
						}
					}
				}
				// 设置了要根据过滤的类型进行查询.
				else {
					if (searchText == null || searchText.trim().equals("")) {
						if (olddata != null && olddata.size() > 0) {
							// 逐行扫描数据，添加满足类型的数据
							for (HashMap<String, Object> m : olddata) {
								if (!data.contains(m) && filterByType(m)) {
									data.add(m);
								}
							}
							// data.addAll(olddata);
						}
					} else {
						for (HashMap<String, Object> m : olddata) {
							if (!data.contains(m)
									&& filterByText(m, searchText)
									&& filterByType(m)) {
								data.add(m);
							}
						}
					}
				}

				notifyDataSetInvalidated();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		/**
		 * 根据关键字过滤.
		 * 
		 * @param m
		 * @return
		 */
		private boolean filterByText(HashMap<String, Object> m,
				String searchText) {
			// 根据名字查询
			String t = m.get("name") + "";
			if (t != null && t.contains(searchText)) {
				return true;
			}
			// 根据首字母查询
			t = m.get("firstletter") + "";
			if (t != null && t.contains(searchText.toUpperCase())) {
				return true;
			}
			// 根据电话号码查询
			t = m.get("phone") + "";
			if (t != null && t.startsWith(searchText)) {
				return true;
			}
			return false;
		}

		/**
		 * 根据选择的类型过滤 .
		 * 
		 * @param m
		 * @return
		 */
		private boolean filterByType(HashMap<String, Object> m) {
			String type = m.get("type") + "";
			// 判断是否设置了要显示数据.
			return "true".equals(mSharedPreferences.getString(temp + type,
					"false"));
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