package com.ericssonlabs;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.security.MessageDigest;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ericssonlabs.bean.AccessToken;
import com.ericssonlabs.bean.ServerResult;
import com.ericssonlabs.util.Constant;

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
	private ProgressDialog dialog;
	private TextView mess_title;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.login);
		buttonLogin = (Button) findViewById(R.id.buttonLogin);
		mess_title = (TextView) findViewById(R.id.mess_title);
		remeberPassword = (CheckBox) findViewById(R.id.remember_password);
		nameText = (EditText) findViewById(R.id.inputName);
		passwordText = (EditText) findViewById(R.id.inputPass);
		mSharedPreferences = PreferenceManager
				.getDefaultSharedPreferences(this);
		if (!isNetworkConnected(this)) {
			myHandler.sendEmptyMessage(2);
		}else{
			//如果选择了记住密码，就自动登陆.
			if("true".equals(mSharedPreferences.getString("remeber", "false"))){
				nameText.setText(mSharedPreferences.getString("userId", ""));
				passwordText.setText(mSharedPreferences.getString("pass", ""));
				remeberPassword.setChecked(true);
				new MyListLoader(true).execute("");				
			}
		}
		// 勾选是否记住密码调用.
		remeberPassword
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {
					public void onCheckedChanged(CompoundButton arg0,
							boolean arg1) {
						savePass(arg1);
					}

				});
		buttonLogin.setOnClickListener(this);
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

	/**
	 * 判断网络状况.
	 * 
	 * @param context
	 * @return
	 */
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
				mess_title.setText("您输入的账号或密码有误!请重新输入!");
				break;
			case 2:
				mess_title.setVisibility(View.VISIBLE);
				mess_title.setText("请检查网络连接状况!");
				break;
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
				mess_title.setText("您输入的账号或密码有误!请重新输入!");
				break;
			default:
				super.hasMessages(msg.what);
				break;
			}
		}
	};

	/**
	 * md5加密方法.
	 * 
	 * @param data
	 * @return
	 * @throws Exception
	 */
	public static byte[] encryptMD5(byte[] data) throws Exception {
		MessageDigest md5 = MessageDigest.getInstance("MD5");
		md5.update(data);
		return md5.digest();
	}

	/**
	 * 登陆请求服务器数据
	 * 
	 * @param userName
	 * @param password
	 */
	public void login(final String userName, final String password) {
		// 得到url请求.
		DefaultHttpClient httpclient = new DefaultHttpClient();
		try {
			// 进行Md5加密数据.
			BigInteger md5 = new BigInteger(encryptMD5(password.getBytes()));
			HttpPost httpost = new HttpPost(Constant.HOST
					+ "?do=login&username=" + userName + "&password="
					+ md5.toString(16));
			System.out.println("登陆："+Constant.HOST
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