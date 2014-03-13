package com.ericssonlabs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ericssonlabs.bean.EventList;
import com.ericssonlabs.bean.EventListItem;
import com.ericssonlabs.bean.ServerResult;
import com.ericssonlabs.util.ActionBar;
import com.ericssonlabs.util.ActionBar.OnRefreshClickListener;
import com.ericssonlabs.util.BottomBar;
import com.ericssonlabs.util.Constant;
import com.ericssonlabs.util.HttpRequire;
import com.ericssonlabs.util.LoadImage;

/**
 * 活动列表.
 * 
 * @author 130126
 * 
 */
public class ActivitesList extends BaseActivity implements OnScrollListener {
	private ListView list;
	private String token, auth;
	private static final int DIALOG_KEY = 0;
	private ProgressDialog dialog;
	private ServerResult result;
	// ListView底部View
	private View moreView;
	// 设置一个最大的数据条数，超过即不再加载
	private int MaxDateNum;
	// 每页显示的条数
	private static int pageSize = 5;
	// 默认开始显示的页码
	private int currentPage = 1;
	// 最后可见条目的索引
	private int lastVisibleIndex;
	private Button bt;
	private ProgressBar pg;
	private ArrayList<HashMap<String, Object>> listItem;
	private ActivitesListAdapter adapter;
	private ActionBar head;
	private BottomBar bottom;
	private float screenHeight = 0;
	private float screenWidth = 0;
	// 查看详情按钮的高度比例
	private final static float statusBtnMT = 65 / 470f;
	private final static float statusBtnML = 92 / 266f;
	private final static float textW = 100 / 170f;
	private final static float statusBtnH = 24 / 321f;
	private final static float statusBtnW = 160 / 266f;
	private final static float contentH = 107 / 470f;
	private final static float contentW = 254 / 266f;
	private LinearLayout.LayoutParams p;
	private boolean showMorePage = true;
	private final static float contentLM = 4 / 266f;

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		// 计算最后可见条目的索引
		lastVisibleIndex = firstVisibleItem + visibleItemCount - 1;
		// 所有的条目已经和最大条数相等，则移除底部的View
		if (totalItemCount == MaxDateNum + 1) {
			showMorePage = true;
			list.removeFooterView(moreView);
			Toast.makeText(this, "数据全部加载完成，没有更多数据！", Toast.LENGTH_SHORT).show();
		}
	}

	/**
	 * 查看活动详情.
	 * 
	 * @param arg0
	 */
	public void seeDetail(View arg0) {
		LinearLayout layout = (LinearLayout) arg0;
		Intent intent = new Intent(ActivitesList.this, ActivitesInfo.class);
		intent.putExtra("eventid", layout.getTag().toString());
		intent.putExtra("token", token);
		intent.putExtra("auth", auth);
		this.startActivity(intent);
	}

	/**
	 * 取消搜索框里面的文字信息.
	 * 
	 * @param arg0
	 */
	public void cancel(View arg0) {
		LinearLayout layout = (LinearLayout) arg0;
		Intent intent = new Intent(ActivitesList.this, ActivitesInfo.class);
		intent.putExtra("eventid", layout.getTag().toString());
		intent.putExtra("token", token);
		intent.putExtra("auth", auth);
		this.startActivity(intent);
	}

	/**
	 * 进行屏幕适配.
	 */
	private void adjustScreen() {
		float[] screen2 = getScreen2();
		screenHeight = screen2[1];
		screenWidth = screen2[0];
		head.init(R.drawable.i5_top_my_activity, true, true,
				(int) (screenHeight * barH));
		head.setTitleSize((int) (screenWidth * titleW4),
				(int) (screenHeight * titleH));
		head.setLeftSize((int) (screenWidth * lftBtnW),
				(int) (screenHeight * lftBtnH), (int) (screenHeight * lftBtnT));
		head.setRightSize((int) (screenWidth * rgtBtnW),
				(int) (screenHeight * rgtBtnH));
		bottom.init(null, true, true, LinearLayout.LayoutParams.FILL_PARENT,
				(int) (screenHeight * barH));
		p = new LinearLayout.LayoutParams((int) (screenWidth * statusBtnW),
				(int) (screenHeight * statusBtnH));
	}

	/**
	 * 设置初始化界面.
	 */
	private void init() {
		list = (ListView) findViewById(R.id.ListView);
		list.setDivider(null);
		list.setOnScrollListener(this);
		Intent intent = getIntent();
		token = intent.getStringExtra("token");
		auth = intent.getStringExtra("auth");
		// 查询全部的订到的票的信息.
		// 实例化底部布局
		moreView = getLayoutInflater().inflate(R.drawable.moredata, null);
		bt = (Button) moreView.findViewById(R.id.bt_load);
		pg = (ProgressBar) moreView.findViewById(R.id.pg);
		head = (ActionBar) findViewById(R.id.list_head);
		head.setLeftAction(new ActionBar.BackAction(this));
		head.setRightAction(new ActionBar.RefreshAction(head));
		head.setRefreshEnabled(new OnRefreshClickListener() {
			public void onRefreshClick() {
				currentPage = 1;
				new MyListLoader(true).execute("");
			}
		});

		bottom = (BottomBar) findViewById(R.id.list_bottom);
		bottom.setRightAction(new BottomBar.CallAction(this));
		// 加载listview
		new MyListLoader(true).execute("");
		// 设置点击更多按钮的事件，显示进度条.
		bt.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				pg.setVisibility(View.VISIBLE);// 将进度条可见
				bt.setVisibility(View.GONE);// 按钮不可见
				currentPage++;
				new MyListLoader(true).execute("");
			}
		});
		adjustScreen();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activiteslist);
		init();
	}

	/**
	 * 对页面的元素进行处理的回调类.
	 */
	public Handler myHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				alert("对不起，出现异常");
				break;
			case 2:
				// 从url返回的数据进行解析，然后加载到列表中.
				JSONObject json = result.getData();
				EventList t = (EventList) JSON.parseObject(json.toJSONString(),
						EventList.class);
				// 设置最大数据条数
				MaxDateNum = t.getTotalcount();
				List<EventListItem> items = t.getItems();
				listItem = new ArrayList<HashMap<String, Object>>();
				if (items != null && items.size() >= 1) {
					for (EventListItem i : items) {
						HashMap<String, Object> map = new HashMap<String, Object>();
						map.put("endtime", i.getEndtime());// 图像资源的ID
						map.put("eventid", i.getEventid());
						map.put("name", i.getName());
						map.put("starttime", i.getStarttime());
						map.put("url", i.getImageurl());
						listItem.add(map);
					}
				}
				// 进行订票列表的展示.
				adapter = new ActivitesListAdapter(listItem,
						ActivitesList.this, screenWidth, screenHeight);
				// 添加上最后的一个底部视图.
				if (showMorePage) {
					list.addFooterView(moreView);
					showMorePage = false;
				}
				list.setAdapter(adapter);
				break;
			case 3:
				// 追加新的列表数据.
				JSONObject json2 = result.getData();
				EventList t2 = (EventList) JSON.parseObject(
						json2.toJSONString(), EventList.class);
				List<EventListItem> items2 = t2.getItems();
				if (items2 != null && items2.size() >= 1) {
					for (EventListItem i : items2) {
						HashMap<String, Object> map = new HashMap<String, Object>();
						map.put("endtime", i.getEndtime());
						map.put("eventid", i.getEventid());
						map.put("name", i.getName());
						map.put("starttime", i.getStarttime());
						map.put("url", i.getImageurl());
						listItem.add(map);
					}
				}
				bt.setVisibility(View.VISIBLE);
				pg.setVisibility(View.GONE);
				// 通知listView刷新数据
				adapter.notifyDataSetChanged();
				break;
			// 调试数据
			case 9:
				MaxDateNum = 5;
				listItem = new ArrayList<HashMap<String, Object>>();
				for (int i = 0; i < 3; i++) {
					HashMap<String, Object> map = new HashMap<String, Object>();
					map.put("endtime", "2013-1-1");// 图像资源的ID
					map.put("eventid", "debug");
					map.put("name", "debug");
					map.put("starttime", "2012-1-1");
					listItem.add(map);
				}
				// 进行订票列表的展示.
				adapter = new ActivitesListAdapter(listItem,
						ActivitesList.this, screenWidth, screenHeight);
				// 添加上最后的一个底部视图.
				if (showMorePage) {
					list.addFooterView(moreView);
					showMorePage = false;
				}
				list.setAdapter(adapter);
				break;
			default:
				super.hasMessages(msg.what);
				break;
			}
		}
	};

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
	 * 加载订票的列表.
	 */
	private class MyListLoader extends AsyncTask<String, String, String> {

		private boolean showDialog;

		public MyListLoader(boolean showDialog) {
			this.showDialog = showDialog;
		}

		@Override
		protected void onPreExecute() {
			// 执行过程中显示进度栏.
			if (showDialog) {
				showDialog(DIALOG_KEY);
			}
		}

		public String doInBackground(String... p) {
			userActities(currentPage, pageSize);
			return "";
		}

		@Override
		public void onPostExecute(String Re) {
			/**
			 * 完成的时候就取消进度栏.
			 */
			if (showDialog) {
				removeDialog(DIALOG_KEY);
			}
		}

		@Override
		protected void onCancelled() {
			// 取消进度栏.
			if (showDialog) {
				removeDialog(DIALOG_KEY);
			}
		}
	}

	/**
	 * 调用远程请求查询结果数据.
	 */
	private void userActities(int page, int size) {
		if (Constant.debug) {
			myHandler.sendEmptyMessage(9);
		} else {
			try {
				result = HttpRequire.userActities(page, size, auth);
				// 如果返回数据不是1，就说明出现异常.
				if (1 != result.getErrorcode()) {
					myHandler.sendEmptyMessage(1);
				}
				// 否则就进行文件解析处理.
				else {
					// 如果是第一页
					if (page == 1)
						myHandler.sendEmptyMessage(2);
					// 如果不是第一页
					else
						myHandler.sendEmptyMessage(3);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 加载票据信息列表.
	 */
	class ActivitesListAdapter extends BaseAdapter {
		private ArrayList<HashMap<String, Object>> data;// 用于接收传递过来的Context对象
		private Context context;
		private float width;
		private float height;

		public ActivitesListAdapter(ArrayList<HashMap<String, Object>> data,
				Context contex, float width, float height) {
			super();
			this.data = data;
			this.context = contex;
			this.width = width;
			this.height = height;
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
				convertView = mInflater.inflate(R.layout.activiti_item, null);
				RelativeLayout layout = (RelativeLayout) convertView
						.findViewById(R.id.activity_content);
				LinearLayout.LayoutParams p2 = new LinearLayout.LayoutParams(
						LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
				p2.width = (int) (contentW * width);
				p2.height = (int) (contentH * height);
				p2.topMargin = (int) (contentLM * width);
				p2.leftMargin = (int) (contentLM * height);
				layout.setLayoutParams(p2);
				viewHolder.name = (TextView) convertView
						.findViewById(R.id.act_name);
				viewHolder.act_time_title = (TextView) convertView
						.findViewById(R.id.act_time_title);
				viewHolder.statusbar = (LinearLayout) convertView
						.findViewById(R.id.status_bar);
				viewHolder.img = (ImageView) convertView
						.findViewById(R.id.activity_pic);

				viewHolder.name.setWidth((int) (textW * width));
				// viewHolder.act_time_title.setHeight((int) (textH2 * height));
				RelativeLayout.LayoutParams p1 = new RelativeLayout.LayoutParams(
						LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
				p1.width = (int) (statusBtnW * width);
				p1.height = (int) (statusBtnH * height);
				p1.leftMargin = (int) (statusBtnML * width);
				p1.topMargin = (int) (statusBtnMT * height);
				viewHolder.statusbar.setLayoutParams(p1);
				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}

			HashMap<String, Object> markerItem = getItem(position);
			if (null != markerItem) {
				String[] str = ("" + markerItem.get("starttime")).split(" ");
				String[] str2 = ("" + markerItem.get("endtime")).split(" ");
				viewHolder.statusbar.setTag(markerItem.get("eventid"));
				viewHolder.act_time_title.setText("活动开始时间："
						+ toDateString(getDate(str[0])) + " - "
						+ toDateString(getDate(str2[0])));
				viewHolder.name.setText("" + markerItem.get("name"));
				if (Constant.debug) {
					viewHolder.img.setBackgroundResource(R.drawable.paper);
				} else {
					new Thread(new LoadImage("" + markerItem.get("url"),
							viewHolder.img, R.drawable.huodong_paper,
							getResources())).start();
				}
			}
			return convertView;
		}
	}

	public final static class ViewHolder {
		public ImageView img;
		public LinearLayout statusbar;
		// public StatusBar statusbar;
		// public TextView status;
		public TextView act_time_title;
		public TextView name;
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
	}

}