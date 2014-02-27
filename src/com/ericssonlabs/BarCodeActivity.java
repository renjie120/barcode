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
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.ericssonlabs.bean.ServerResult;
import com.ericssonlabs.util.ActionBar;
import com.ericssonlabs.util.Constant;
import com.zxing.activity.CaptureActivity;

/**
 * 二维码签到.
 */
public class BarCodeActivity extends BaseActivity {
	private TextView resultTextView;
	private String token;
	private String eventid;
	private ActionBar head;
	private float screenHeight = 0;
	private float screenWidth = 0;
	private Button scanBarCodeButton;

	/**
	 * 字体适配.
	 * 
	 * @param screenWidth
	 * @return
	 */
	public int adjustBarcodeTextFontSize(int screenWidth) {
		if (screenWidth <= 240) { // 240X320 屏幕
			return 13;
		} else if (screenWidth <= 320) { // 320X480 屏幕
			return 18;
		} else if (screenWidth <= 480) { // 480X800 或 480X854 屏幕
			return 24;
		} else if (screenWidth <= 540) { // 540X960 屏幕
			return 28;
		} else if (screenWidth <= 800) { // 800X1280 屏幕
			return 32;
		} else { // 大于 800X1280
			return 32;
		}
	}

	private boolean qiandao(String tickid) {
		DefaultHttpClient httpclient = new DefaultHttpClient();
		String encoding = "UTF-8";
		try {

			HttpPost httpost = new HttpPost(Constant.HOST
					+ "?do=checkticket&ticketid=" + tickid
					+ "&check=true&token=" + token);
			System.out.println("签到：" + Constant.HOST
					+ "?do=checkticket&ticketid=" + tickid
					+ "&check=true&token=" + token);
			HttpResponse response = httpclient.execute(httpost);
			HttpEntity entity = response.getEntity();
			BufferedReader br = new BufferedReader(new InputStreamReader(
					entity.getContent(), encoding));
			String sss = br.readLine();
			System.out.println("返回结果" + sss);
			// 如果没有登录成功，就弹出提示信息.
			ServerResult result = (ServerResult) JSON.parseObject(sss,
					ServerResult.class);

			System.out.println(tickid + "签到结果：" + result.getData());
			return "true".equals(result.getData());
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			httpclient.getConnectionManager().shutdown();
		}
		return false;
	}

	/**
	 * 屏幕适配.
	 */
	private void adjustScreen() {
		float[] screen2 = getScreen2();
		screenHeight = screen2[1];
		screenWidth = screen2[0];
		head.init(getText(R.string.title_huodong).toString(), true, false,
				LinearLayout.LayoutParams.FILL_PARENT,
				(int) (screenHeight * barH),
				adjustTitleFontSize((int) screenWidth));
		scanBarCodeButton.setTextSize(adjustBarcodeTextFontSize((int)screenWidth));
	}

	/**
	 * 初始化页面.
	 */
	private void initLayout() {
		resultTextView = (TextView) this.findViewById(R.id.result);
		scanBarCodeButton = (Button) this.findViewById(R.id.btn_scan_barcode);
		Intent intent = getIntent();
		head = (ActionBar) findViewById(R.id.barcode_head);

		token = intent.getStringExtra("token");
		eventid = intent.getStringExtra("eventid");
		scanBarCodeButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 打开扫描界面扫描条形码或二维码
				Intent openCameraIntent = new Intent(BarCodeActivity.this,
						CaptureActivity.class);
				startActivityForResult(openCameraIntent, 0);
			}
		});

		adjustScreen();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.barcode);

		initLayout();
	}

	private class QiandaoLoader extends AsyncTask<String, String, String> {

		private String tickId;

		public QiandaoLoader(String tickId) {
			this.tickId = tickId;
		}

		public String doInBackground(String... p) {
			qiandao(tickId);
			return "";
		}

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		// 处理扫描结果（在界面上显示）
		if (resultCode == RESULT_OK) {
			Bundle bundle = data.getExtras();
			String scanResult = bundle.getString("result");
			new QiandaoLoader(scanResult).execute("");
			// resultTextView.setText("票号：" + scanResult + "签到结果：" + ans);
		}
	}
}