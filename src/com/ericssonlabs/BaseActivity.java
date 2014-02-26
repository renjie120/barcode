package com.ericssonlabs;

import android.app.Activity;
import android.app.AlertDialog;
import android.util.DisplayMetrics;

/**
 * Activity基类，包含一些常用函数.
 */
public class BaseActivity extends Activity {
	// 上下标题栏的高度比例
	public static float barH = 0.1f;

	/**
	 * 动态设置标题的字体.
	 * 
	 * @param screenWidth
	 * @param screenHeight
	 * @return
	 */
	public static int adjustTitleFontSize(int screenWidth) {
		if (screenWidth <= 240) { // 240X320 屏幕
			return 10;
		} else if (screenWidth <= 320) { // 320X480 屏幕
			return 14;
		} else if (screenWidth <= 480) { // 480X800 或 480X854 屏幕
			return 24;
		} else if (screenWidth <= 540) { // 540X960 屏幕
			return 26;
		} else if (screenWidth <= 800) { // 800X1280 屏幕
			return 30;
		} else { // 大于 800X1280
			return 30;
		}
	}

	

	/**
	 * 设置按钮的字体大小.
	 * 
	 * @param screenWidth
	 * @return
	 */
	public static int adjusBtnTextFontSize(int screenWidth) {
		if (screenWidth <= 240) { // 240X320 屏幕
			return 10;
		} else if (screenWidth <= 320) { // 320X480 屏幕
			return 14;
		} else if (screenWidth <= 480) { // 480X800 或 480X854 屏幕
			return 24;
		} else if (screenWidth <= 540) { // 540X960 屏幕
			return 26;
		} else if (screenWidth <= 800) { // 800X1280 屏幕
			return 30;
		} else { // 大于 800X1280
			return 30;
		}
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
