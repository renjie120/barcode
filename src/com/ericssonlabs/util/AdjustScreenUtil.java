package com.ericssonlabs.util;

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

	/**
	 * 适配配置参数字体大小.
	 * 
	 * @param screenWidth
	 * @return
	 */
	public static int adjusCheckconfigFontSize(int screenWidth) {
		if (screenWidth <= 240) { // 240X320 屏幕
			return 12;
		} else if (screenWidth <= 320) { // 320X480 屏幕
			return 16;
		} else if (screenWidth <= 480) { // 480X800 或 480X854 屏幕
			return 18;
		} else if (screenWidth <= 540) { // 540X960 屏幕
			return 22;
		} else if (screenWidth <= 800) { // 800X1280 屏幕
			return 24;
		} else { // 大于 800X1280
			return 25;
		}
	}
 

	public static ViewGroup.LayoutParams adjusActivityLayout(int screenWidth) {
		if (screenWidth <= 240) { // 240X320 屏幕
			// 高度；宽度
			LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(15,
					150);
			lp.setMargins(12, 10, 0, 10);
			return lp;
		} else if (screenWidth <= 320) { // 320X480 屏幕
			LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(15,
					150);
			lp.setMargins(15, 15, 0, 15);
			return lp;
		} else if (screenWidth <= 480) { // 480X800 或 480X854 屏幕
			LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(15,
					150);
			lp.setMargins(20, 18, 0, 18);
			return lp;
		} else if (screenWidth <= 540) { // 540X960 屏幕
			LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(15,
					150);
			lp.setMargins(25, 20, 0, 20);
			return lp;
		} else if (screenWidth <= 800) { // 800X1280 屏幕
			LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(15,
					150);
			lp.setMargins(25, 25, 0, 25);
			return lp;
		} else { // 大于 800X1280
			LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(15,
					150);
			lp.setMargins(35, 35, 0, 35);
			return lp;
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
			return 22;
		} else if (screenWidth <= 540) { // 540X960 屏幕
			return 24;
		} else if (screenWidth <= 800) { // 800X1280 屏幕
			return 24;
		} else { // 大于 800X1280
			return 26;
		}
	}
}
