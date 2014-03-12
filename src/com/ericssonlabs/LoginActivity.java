package com.ericssonlabs;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigInteger;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ericssonlabs.bean.AccessToken;
import com.ericssonlabs.bean.ServerResult;
import com.ericssonlabs.util.ActionBar;
import com.ericssonlabs.util.BottomBar;
import com.ericssonlabs.util.Constant;
import com.ericssonlabs.util.LoginEdittext;

/**
 * 首页登录界面.
 * 
 * @author 130126
 * 
 */
public class LoginActivity extends BaseActivity implements OnClickListener {
	private String name;
	private String pass;
	private Button buttonLogin;
	private ImageView remeberPassword, bottomimg;
	private static final int DIALOG_KEY = 0;
	private LoginEdittext nameText;
	private LoginEdittext passwordText;
	private SharedPreferences mSharedPreferences;
	private ProgressDialog dialog;
	private TextView mess_title, remember_mess;
	private float screenHeight = 0;
	private float screenWidth = 0;
	// private LinearLayout titile_gre_ym;
	private ActionBar head;
	private BottomBar login_bottom;
	// 登陆框的高度
	private float tabH = 0.38f;
	// 登陆框的宽度
	private float tabW = 0.86f;
	// 图标的上下空白
	private float imgMrg = 0.05f;
	private TextView name_title;
	private TextView pass_title; 
	private LinearLayout buttonWrap;
	private LinearLayout row1;
	private LinearLayout row2;
	private LinearLayout row3;
	private LinearLayout table;
	private ImageView logo_img;
	// 登陆框提示文本的宽度.
	private float textViewW = 57 / 265f;
	private float textEditW = 150 / 265f;
	private float textViewH = 27 / 471f;
	private float checkboxH = 15 / 471f;
	private float checkboxTM = 10 / 471f;
	private float checkboxMesTM = 4 / 471f;
	private float checkboxLM = 8 / 170f;
	private float btnW = 75 / 268f; 
	private float wrapH = 30 / 471f;
	private float rowH = 32 / 469f; 

	/**
	 * 屏幕适配.
	 */
	private void adjustScreen() {
		// 得到屏幕大小.
		float[] screen2 = getScreen2();
		screenHeight = screen2[1];
		screenWidth = screen2[0];
		int textHeight = (int) (textViewH * screenHeight);
		int checkboxHeight = (int) (checkboxH * screenHeight);
		int checkboxLMar = (int) (checkboxLM * screenWidth);
		int checkboxTMar = (int) (checkboxTM * screenWidth);
		int textWidth = (int) (textViewW * screenWidth);
		int editWidth = (int) (textEditW * screenWidth);
		int editHeight = (int) (textViewH * 1.1 * screenHeight);
		LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(
				(int) (screenWidth * tabW), (int) (screenHeight * tabH));
		// 设置table的高度和宽度.
		table.setLayoutParams(p);
		// 设置首页标题栏显示的情况
		head.init(R.drawable.i5_top_user_logon, false, false,
				(int) (screenHeight * barH));
		head.setTitleSize((int) (screenWidth * titleW4),
				(int) (screenHeight * titleH));

		// 设置底部标题栏
		login_bottom.init(null, true, true,
				LinearLayout.LayoutParams.FILL_PARENT,
				(int) (screenHeight * barH));
		// 设置底部标题栏的右边操作
		login_bottom.setRightAction(new BottomBar.CallAction(this));

		name_title.setWidth(textWidth);
		name_title.setHeight(textHeight);
		pass_title.setWidth(textWidth);
		pass_title.setHeight(textHeight);
		LinearLayout.LayoutParams lp_check = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		lp_check.width = checkboxHeight;
		lp_check.height = checkboxHeight;
		lp_check.leftMargin = checkboxLMar;
		lp_check.topMargin = checkboxTMar;
		remeberPassword.setLayoutParams(lp_check);
		LinearLayout.LayoutParams lp_bottom = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		lp_bottom.width = (int) (screenWidth * tabW);
		lp_bottom.height = (int) (screenHeight * (24 / 1136));
		bottomimg.setLayoutParams(lp_bottom);

		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		lp.setMargins(0, (int) (screenHeight * imgMrg), 0,
				(int) (screenHeight * imgMrg));
		logo_img.setLayoutParams(lp);

		LinearLayout.LayoutParams lp2 = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		lp2.width = (int) (btnW * screenWidth);
		lp2.height = (int) (wrapH * screenHeight);
		buttonWrap.setLayoutParams(lp2);

		LinearLayout.LayoutParams lp22 = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		lp22.width = checkboxHeight * 5;
		lp22.topMargin = (int) (checkboxMesTM * screenHeight);
		remember_mess.setLayoutParams(lp22);

		LinearLayout.LayoutParams lp23 = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		lp23.width = (int) (textEditW * screenWidth);
		lp23.height = checkboxHeight;
		lp23.topMargin = (int) (checkboxMesTM * screenHeight);
		mess_title.setLayoutParams(lp23);

		LinearLayout.LayoutParams r1 = new LinearLayout.LayoutParams(
				(int) (screenWidth * tabW), (int) (rowH * screenHeight));
		row1.setLayoutParams(r1);
		row2.setLayoutParams(r1);
		row3.setLayoutParams(r1);

		nameText.setSize(editWidth, editHeight);
		nameText.setText("test140103114242328");
		passwordText.setPassword();
		passwordText.setSize(editWidth, editHeight);
		passwordText.setText("123123");
	}

	/**
	 * 初始化控件.
	 */
	private void init() {
		buttonWrap = (LinearLayout) findViewById(R.id.row4);
		row1 = (LinearLayout) findViewById(R.id.row1);
		row2 = (LinearLayout) findViewById(R.id.row2);
		row3 = (LinearLayout) findViewById(R.id.row3);
		name_title = (TextView) findViewById(R.id.name_title);
		pass_title = (TextView) findViewById(R.id.pass_title);
		logo_img = (ImageView) findViewById(R.id.logo_img); 
		buttonLogin = (Button) findViewById(R.id.buttonLogin);
		head = (ActionBar) findViewById(R.id.login_head);
		table = (LinearLayout) findViewById(R.id.login_table);
		mess_title = (TextView) findViewById(R.id.mess_title);
		remeberPassword = (ImageView) findViewById(R.id.remember_check);
		nameText = (LoginEdittext) findViewById(R.id.inputName);
		passwordText = (LoginEdittext) findViewById(R.id.inputPass);
		mSharedPreferences = PreferenceManager
				.getDefaultSharedPreferences(this);
		login_bottom = (BottomBar) findViewById(R.id.login_bottom);
		remember_mess = (TextView) findViewById(R.id.remember_mess);
		bottomimg = (ImageView) findViewById(R.id.bottomimg);
		adjustScreen();
	}

	/**
	 * 绑定事件.
	 */
	private void prepareListener() {
		// 勾选是否记住密码调用.
		remeberPassword.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				boolean haveSelected = !"true".equals(arg0.getTag());
				savePass(haveSelected);
			}

		});
		remember_mess.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				boolean haveSelected = !"true".equals(remeberPassword.getTag());
				savePass(haveSelected);
			}

		});
		buttonLogin.setOnClickListener(this);
	}

	/**
	 * 登陆控制.
	 */
	private void autoLogin() {
		// 网络不通
		if (!isNetworkConnected(this)) {
			myHandler.sendEmptyMessage(2);
		} else {
			// 如果选择了记住密码，就自动登陆.
			if ("true".equals(mSharedPreferences.getString("remeber", "false"))) {
				nameText.setText(mSharedPreferences.getString("userId", ""));
				passwordText.setText(mSharedPreferences.getString("pass", ""));
				remeberPassword.setSelected(true);
				remeberPassword.setTag("true");
				new MyListLoader(true).execute("");
			} else {
				remeberPassword.setSelected(false);
				remeberPassword.setTag("false");
			}
		}
	}

	/**
	 * 界面初始化函数.
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.login);
		init();

		prepareListener();

		autoLogin();
	}

	/**
	 * 勾选了记住密码的处理.
	 * 
	 * @param arg1
	 */
	private void savePass(boolean arg1) {
		if (arg1) {
			SharedPreferences.Editor mEditor = mSharedPreferences.edit();
			mEditor.putString("remeber", "true");
			mEditor.putString("pass", passwordText.getText().toString());
			mEditor.putString("userId", nameText.getText());
			mEditor.commit();
			remeberPassword.setSelected(true);
			remeberPassword.setTag("true");
		} else {
			SharedPreferences.Editor mEditor = mSharedPreferences.edit();
			mEditor.putString("remeber", "false");
			mEditor.putString("pass", "");
			mEditor.putString("userId", "");
			mEditor.commit();
			remeberPassword.setSelected(false);
			remeberPassword.setTag("false");
		}
	}

	/**
	 * 开启异步任务登陆.
	 */
	private class MyListLoader extends AsyncTask<String, String, String> {

		private boolean showDialog;

		public MyListLoader(boolean showDialog) {
			this.showDialog = showDialog;
		}

		@Override
		protected void onPreExecute() {
			if (showDialog) {
				showDialog(DIALOG_KEY);
			}
			buttonLogin.setEnabled(false);
		}

		public String doInBackground(String... p) {
			name = nameText.getText().toString();
			pass = passwordText.getText().toString();
			login(name, pass);
			return "";
		}

		@Override
		public void onPostExecute(String Re) {
			if (showDialog) {
				removeDialog(DIALOG_KEY);
			}
			buttonLogin.setEnabled(true);
		}

		@Override
		protected void onCancelled() {
			if (showDialog) {
				removeDialog(DIALOG_KEY);
			}
			buttonLogin.setEnabled(true);
		}
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.buttonLogin) {
			new MyListLoader(true).execute("");
		}
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case DIALOG_KEY: {
			dialog = new ProgressDialog(this);
			dialog.setMessage("正在登陆,请稍候");
			dialog.setIndeterminate(true);
			dialog.setCancelable(true);
			return dialog;
		}
		}
		return null;
	}

	public Handler myHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				mess_title.setVisibility(View.VISIBLE);
				mess_title.setText("您输入的账号或密码有误,请重新输入!");
				break;
			case 2:
				mess_title.setVisibility(View.VISIBLE);
				mess_title.setText("请检查网络连接状况!");
				break;
			// 跳转到活动列表页面.
			case 3:
				mess_title.setVisibility(View.GONE);
				Intent intent = new Intent(LoginActivity.this,
						ActivitesList.class);
				JSONObject json = (JSONObject) msg.obj;
				// 解析返回的json串信息，传递参数到后面的页面.
				AccessToken token = (AccessToken) JSON.parseObject(
						json.toJSONString(), AccessToken.class);
				intent.putExtra("name", token.getUsername());
				intent.putExtra("uid", token.getUid());
				intent.putExtra("token", token.getToken());
				startActivity(intent);
				break;
			case 6:
				mess_title.setVisibility(View.VISIBLE);
				mess_title.setText("您输入的账号或密码有误,请重新输入!");
				break;
			case 9:
				mess_title.setVisibility(View.GONE);
				Intent intent2 = new Intent(LoginActivity.this,
						ActivitesList.class);
				intent2.putExtra("name", "debug");
				intent2.putExtra("uid", "debug");
				intent2.putExtra("token", "debug");
				startActivity(intent2);
				break;
			default:
				super.hasMessages(msg.what);
				break;
			}
		}
	};

	/**
	 * 登陆请求服务器数据
	 * 
	 * @param userName
	 * @param password
	 */
	public void login(final String userName, final String password) {
		// 得到url请求.
		DefaultHttpClient httpclient = new DefaultHttpClient();
		if (Constant.debug) {
			Message mes = new Message();
			mes.what = 9;
			myHandler.sendMessage(mes);
		} else {
			try {
				// 进行Md5加密数据.
				BigInteger md5 = new BigInteger(encryptMD5(password.getBytes()));
				HttpPost httpost = new HttpPost(Constant.HOST
						+ "?do=login&username=" + userName + "&password="
						+ md5.toString(16));
				HttpResponse response = httpclient.execute(httpost);
				HttpEntity entity = response.getEntity();
				BufferedReader br = new BufferedReader(new InputStreamReader(
						entity.getContent(), "UTF-8"));
				// 如果没有登录成功，就弹出提示信息.
				ServerResult result = (ServerResult) JSON.parseObject(
						br.readLine(), ServerResult.class);
				if (1 != result.getErrorcode()) {
					myHandler.sendEmptyMessage(1);
				}
				// 成功了就跳转到活动列表页面.
				else {
					Message mes = new Message();
					mes.obj = result.getData();
					mes.what = 3;
					myHandler.sendMessage(mes);
				}
			} catch (Exception e) {
				e.printStackTrace();
				myHandler.sendEmptyMessage(6);
			} finally {
				// 关闭连接.
				httpclient.getConnectionManager().shutdown();
			}
		}
	}
}