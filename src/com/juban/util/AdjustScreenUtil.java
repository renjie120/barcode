package com.juban.util;

import android.view.ViewGroup;
import android.widget.LinearLayout;

public class AdjustScreenUtil {
	public final static double LEFT_W = 0.2;
	public final static double RIGHT_W = 0.2;

	/**
	 * 设置下面的tab的适配情况
	 * 
	 * @param screenWidth
	 * @return
	 */
	public static int adjustTabitem(int screenWidth) {
		if (screenWidth <= 240) { // 240X320 屏幕
			return -10;
		} else if (screenWidth <= 320) { // 320X480 屏幕
			return -8;
		} else if (screenWidth <= 480) { // 480X800 或 480X854 屏幕
			return -5;
		} else if (screenWidth <= 540) { // 540X960 屏幕
			return -2;
		} else if (screenWidth <= 800) { // 800X1280 屏幕
			return 1;
		} else { // 大于 800X1280
			return 1;
		}
	}

	/**
	 * tab的图片的宽度.
	 * 
	 * @param screenWidth
	 * @return
	 */
	public static int adjustTabWith(int screenWidth) {
		if (screenWidth <= 240) { // 240X320 屏幕
			return 30;
		} else if (screenWidth <= 320) { // 320X480 屏幕
			return 40;
		} else if (screenWidth <= 480) { // 480X800 或 480X854 屏幕
			return 50;
		} else if (screenWidth <= 540) { // 540X960 屏幕
			return 60;
		} else if (screenWidth <= 800) { // 800X1280 屏幕
			return 70;
		} else { // 大于 800X1280
			return 100;
		}
	}

	/**
	 * 二维码界面字体适配.
	 * 
	 * @param screenWidth
	 * @return
	 */
	public static int adjustBarcodeTextFontSize(int screenWidth) {
		if (screenWidth <= 240) { // 240X320 屏幕
			return 13;
		} else if (screenWidth <= 320) { // 320X480 屏幕
			return 18;
		} else if (screenWidth <= 480) { // 480X800 或 480X854 屏幕
			return 24;
		} else if (screenWidth <= 540) { // 540X960 屏幕
			return 28;
		} else if (screenWidth <= 800) { // 800X1280 屏幕
			return 30;
		} else { // 大于 800X1280
			return 31;
		}
	}

	 
}
