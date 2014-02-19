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
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
import com.ericssonlabs.bean.EventList;
import com.ericssonlabs.bean.EventListItem;
import com.ericssonlabs.bean.ServerResult;
import com.ericssonlabs.util.Constant;
import com.ericssonlabs.util.LoadImage;

/**
 * 活动列表.
 * 
 * @author 130126
 * 
 */
public class ActivitesList extends BaseActivity {
	private ListView list;
	private String token;
	private static final int DIALOG_KEY = 0;
	private ServerResult result;
	private Handler handler = null;

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
		this.startActivity(intent);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activiteslist);
		list = (ListView) findViewById(R.id.ListView);
		Intent intent = getIntent();
		token = intent.getStringExtra("token");
		// 查询全部的订到的票的信息.
		new MyListLoader(true).execute("");
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
				List<EventListItem> items = t.getItems();
				ArrayList<HashMap<String, Object>> listItem = new ArrayList<HashMap<String, Object>>();
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
				MyImgAdapter adapter = new MyImgAdapter(listItem,
						ActivitesList.this);
				list.setAdapter(adapter);
				break;
			default:
				super.hasMessages(msg.what);
				break;
			}
		}
	};

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
			userActities();
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
	private void userActities() {
		DefaultHttpClient httpclient = new DefaultHttpClient();
		String encoding = "UTF-8";
		try {

			HttpPost httpost = new HttpPost(Constant.HOST
					+ "?do=myevents&token=" + token);
			HttpResponse response = httpclient.execute(httpost);
			HttpEntity entity = response.getEntity();
			BufferedReader br = new BufferedReader(new InputStreamReader(
					entity.getContent(), encoding));
			result = (ServerResult) JSON.parseObject(br.readLine(),
					ServerResult.class);
			//如果返回数据不是1，就说明出现异常.
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
	 * 加载票据信息列表.
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
				convertView = mInflater.inflate(R.layout.activiti_item, null);

				viewHolder.name = (TextView) convertView
						.findViewById(R.id.act_name);
				viewHolder.statusbar = (LinearLayout) convertView
						.findViewById(R.id.status_bar);
				viewHolder.time = (TextView) convertView
						.findViewById(R.id.act_time);
				viewHolder.img = (ImageView) convertView
						.findViewById(R.id.activity_pic);

				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}

			HashMap<String, Object> markerItem = getItem(position);
			if (null != markerItem) {
				viewHolder.statusbar.setTag(markerItem.get("eventid"));
				viewHolder.time.setText("" + markerItem.get("endtime"));
				viewHolder.name.setText("" + markerItem.get("name"));
				new Thread(new LoadImage("" + markerItem.get("url"),
						viewHolder.img, R.drawable.huodong_paper)).start();
			}
			return convertView;
		}
	}

	public final static class ViewHolder {
		public TextView time;
		public ImageView img;
		public LinearLayout statusbar;
		public TextView name;
	}

}