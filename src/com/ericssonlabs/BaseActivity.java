package com.ericssonlabs;

import java.security.MessageDigest;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.DisplayMetrics;

/**
 * Activity基类，包含一些常用函数.
 */
public class BaseActivity extends Activity {
	// 上下标题栏的高度比例
	public static float barH = 0.1f;
	// 底部文字的宽度
	public static float bottomW = 91 / 263f;
	// 底部文字的高度
	public static float bottomH = 40 / 273f;
	// 首页标题的高度比例
	public static float titleH = 0.06f;
	// 首页左边按钮的比例
	public static float lftBtnW = 50 / 265f;
	// 首页右边按钮的比例
	public static float rgtBtnW = 50 / 265f;
	// 首页4字标题的宽度
	public static float titleW4 = 77 / 267f;
	public static float titleW6 = 123 / 264f;

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
	 * 判断网络状况.
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isNetworkConnected(Context context) {
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
	 * 弹出一个提示框.
	 * 
	 * @param mess
	 */
	public void alert(String mess) {
		new AlertDialog.Builder(this).setTitle("提示").setMessage(mess)
				.setPositiveButton("确定", null).show();
	}

	/**
	 * 返回屏幕大小
	 */
	public int[] getScreen() {
		int screenWidth = getWindowManager().getDefaultDisplay().getWidth();
		int screenHeight = getWindowManager().getDefaultDisplay().getHeight();
		return new int[] { screenWidth, screenHeight };
	}

	/**
	 * 第二种返回屏幕大小的方式.
	 * 
	 * @return
	 */
	public float[] getScreen2() {
		DisplayMetrics dm = new DisplayMetrics();
		System.out.println("屏幕密度1" + dm.density + ",屏幕密度2" + dm.densityDpi);
		dm = getResources().getDisplayMetrics();
		return new float[] { dm.widthPixels, dm.heightPixels };
	}
}
