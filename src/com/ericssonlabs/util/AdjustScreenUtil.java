package com.ericssonlabs.util;

public class AdjustScreenUtil {
	public final static double LEFT_W = 0.2;
	public final static double RIGHT_W = 0.2;

	/**
	 * 设置活动详情文本框字体大小.
	 * 
	 * @param screenWidth
	 * @return
	 */
	public static int adjusActivityTextFontSize(int screenWidth) {
		if (screenWidth <= 240) { // 240X320 屏幕
			return 9;
		} else if (screenWidth <= 320) { // 320X480 屏幕
			return 15;
		} else if (screenWidth <= 480) { // 480X800 或 480X854 屏幕
			return 17;
		} else if (screenWidth <= 540) { // 540X960 屏幕
			return 20;
		} else if (screenWidth <= 800) { // 800X1280 屏幕
			return 23;
		} else { // 大于 800X1280
			return 24;
		}
	}

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
			return 90;
		}
	}

	/**
	 * 设置登录文本框前字体大小.
	 * 
	 * @param screenWidth
	 * @return
	 */
	public static int adjusLoginTextFontSize(int screenWidth) {
		if (screenWidth <= 240) { // 240X320 屏幕
			return 10;
		} else if (screenWidth <= 320) { // 320X480 屏幕
			return 14;
		} else if (screenWidth <= 480) { // 480X800 或 480X854 屏幕
			return 18;
		} else if (screenWidth <= 540) { // 540X960 屏幕
			return 22;
		} else if (screenWidth <= 800) { // 800X1280 屏幕
			return 24;
		} else { // 大于 800X1280
			return 26;
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

	/**
	 * 活动列表页面匹配文本大小.
	 * 
	 * @param screenWidth
	 * @return
	 */
	public static int adjusDescTextFontSize(int screenWidth) {
		if (screenWidth <= 240) { // 240X320 屏幕
			return 9;
		} else if (screenWidth <= 320) { // 320X480 屏幕
			return 14;
		} else if (screenWidth <= 480) { // 480X800 或 480X854 屏幕
			return 17;
		} else if (screenWidth <= 540) { // 540X960 屏幕
			return 20;
		} else if (screenWidth <= 800) { // 800X1280 屏幕
			return 20;
		} else { // 大于 800X1280
			return 22;
		}
	}

	/**
	 * 适配活动状态的字体大小.
	 * 
	 * @param screenWidth
	 * @return
	 */
	public static int adjusStatusFontSize(int screenWidth) {
		if (screenWidth <= 240) { // 240X320 屏幕
			return 9;
		} else if (screenWidth <= 320) { // 320X480 屏幕
			return 15;
		} else if (screenWidth <= 480) { // 480X800 或 480X854 屏幕
			return 17;
		} else if (screenWidth <= 540) { // 540X960 屏幕
			return 20;
		} else if (screenWidth <= 800) { // 800X1280 屏幕
			return 22;
		} else { // 大于 800X1280
			return 24;
		}
	}

	/**
	 * 动态设置标题的字体.
	 * 
	 * @param screenWidth
	 * @param screenHeight
	 * @return
	 */
	public static int adjustTitleFontSize(int screenWidth) {
		if (screenWidth <= 240) { // 240X320 屏幕
			return 13;
		} else if (screenWidth <= 320) { // 320X480 屏幕
			return 16;
		} else if (screenWidth <= 480) { // 480X800 或 480X854 屏幕
			return 18;
		} else if (screenWidth <= 540) { // 540X960 屏幕
			return 22;
		} else if (screenWidth <= 800) { // 800X1280 屏幕
			return 23;
		} else { // 大于 800X1280
			return 25;
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
