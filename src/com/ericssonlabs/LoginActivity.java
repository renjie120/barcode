package com.ericssonlabs;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.security.MessageDigest;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ericssonlabs.bean.AccessToken;
import com.ericssonlabs.bean.ServerResult;

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
	private CheckBox remeberPassword;
	private static final int DIALOG_KEY = 0;
	private EditText nameText;
	private EditText passwordText;
	private SharedPreferences mSharedPreferences;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.login);
		buttonLogin = (Button) findViewById(R.id.buttonLogin);
		remeberPassword = (CheckBox) findViewById(R.id.remember_password);
		nameText = (EditText) findViewById(R.id.inputName);
		passwordText = (EditText) findViewById(R.id.inputPass);
		remeberPassword
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {
					public void onCheckedChanged(CompoundButton arg0,
							boolean arg1) {
						if (arg1) {
							SharedPreferences.Editor mEditor = mSharedPreferences
									.edit();
							mEditor.putString("remeber", "true");
							mEditor.putString("pass", passwordText.getText()
									.toString());
							mEditor.putString("userId", nameText.getText()
									.toString());
							mEditor.commit();
						} else {
							SharedPreferences.Editor mEditor = mSharedPreferences
									.edit();
							mEditor.putString("remeber", "false");
							mEditor.putString("pass", "");
							mEditor.putString("userId", "");
							mEditor.commit();
						}
					}

				});
		buttonLogin.setOnClickListener(this);
	}

	private void savePass() {
		boolean arg1 = remeberPassword.isChecked();
		if (arg1) {
			SharedPreferences.Editor mEditor = mSharedPreferences.edit();
			mEditor.putString("remeber", "true");
			mEditor.putString("pass", passwordText.getText().toString());
			mEditor.putString("userId", nameText.getText().toString());
			mEditor.commit();
		} else {
			SharedPreferences.Editor mEditor = mSharedPreferences.edit();
			mEditor.putString("remeber", "false");
			mEditor.putString("pass", "");
			mEditor.putString("userId", "");
			mEditor.commit();
		}
	}

	public boolean isNetworkConnected(Context context) {
		try {
			if (context != null) {
				ConnectivityManager mConnectivityManager = (ConnectivityManager) context
						.getSystemService(Context.CONNECTIVITY_SERVICE);
				NetworkInfo mNetworkInfo = mConnectivityManager
						.getActiveNetworkInfo();
				if (mNetworkInfo != null) {
					return mNetworkInfo.isAvailable();
				}
			}
		} catch (Exception e) {
			return false;
		}
		return false;
	}

	private class MyListLoader extends AsyncTask<String, String, String> {

		private boolean showDialog; 

		public MyListLoader(boolean showDialog ) {
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

	public Handler myHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				alert("对不起，该用户没有权限");
				break;
			case 2:
				alert("对不起，手机序列号不匹配");
				break;
			case 3:
				alert("对不起，密码错误");
				break;
			case 4:
				alert("对不起，参数错误");
				break;
			case 5:
				alert("没有安装相关软件，请安装软件后重试");
				break;
			case 6:
				alert("对不起，服务端异常或者网络异常，请稍候重试");
				break;
			default:
				super.hasMessages(msg.what);
				break;
			}
		}
	};

	public static byte[] encryptMD5(byte[] data) throws Exception {
		MessageDigest md5 = MessageDigest.getInstance("MD5");
		md5.update(data);
		return md5.digest();
	}

	public void login(final String userName, final String password) {
		DefaultHttpClient httpclient = new DefaultHttpClient();
		try {
			BigInteger md5 = new BigInteger(encryptMD5(password.getBytes()));
			HttpPost httpost = new HttpPost(
					"http://jb.17miyou.com/api.ashx?do=login&username="
							+ userName + "&password=" + md5.toString(16));
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
			// 否则就进行文件解析处理.
			else {
				Intent intent = new Intent(LoginActivity.this,
						ActivitesList.class);
				JSONObject json = result.getData();
				AccessToken token = (AccessToken) JSON.parseObject(
						json.toJSONString(), AccessToken.class);
				intent.putExtra("name", token.getUsername());
				intent.putExtra("uid", token.getUid());
				intent.putExtra("token", token.getToken());
				this.startActivity(intent);
			}
		} catch (Exception e) {
			e.printStackTrace();
			myHandler.sendEmptyMessage(6);
		} finally {
			httpclient.getConnectionManager().shutdown();
		}
	}
}