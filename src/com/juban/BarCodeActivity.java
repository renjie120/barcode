package com.juban;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.juban.util.ActionBar;
import com.juban.util.AdjustScreenUtil;
import com.juban.util.HttpRequire;
import com.zxing.activity.CaptureActivity;

/**
 * 二维码签到.
 */
public class BarCodeActivity extends BaseActivity {
	private TextView resultTextView;
	private String token,auth;
	private String eventid;
	private ActionBar head;
	private float screenHeight = 0;
	private float screenWidth = 0;
	private Button scanBarCodeButton;

	private boolean qiandao(String tickid) {
		 try { 
			return HttpRequire.qiandao(tickid, auth);
		} catch (Exception e) {
			e.printStackTrace();
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
		head.init(R.drawable.i5_top_checkin, true, false,
				 (int) (screenHeight * barH) );
		head.setTitleSize((int) (screenWidth * titleW4),
				(int) (screenHeight * titleH));
		head.setLeftAction(new ActionBar.BackAction(this));
		scanBarCodeButton.setTextSize(AdjustScreenUtil
				.adjustBarcodeTextFontSize((int) screenWidth));
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
		auth = intent.getStringExtra("auth");
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