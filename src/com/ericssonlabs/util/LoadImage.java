package com.ericssonlabs.util;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.widget.ImageView;

import com.ericssonlabs.R;

/**
 * 加载网络图片的工具类.
 * 
 */
public class LoadImage implements Runnable {
	private String url;
	private ImageView v;
	// 缓存下载过的图片的Map
	private Map<String, SoftReference<Bitmap>> caches;
	// 任务队列
	private List<Task> taskQueue;
	private boolean isRunning = false;
	private int resId;
	private Resources res;

	public LoadImage(String url, ImageView v, int resId, Resources res) {
		this.url = url;
		this.res = res;
		this.v = v;
		this.resId = resId;// 初始化变量
		caches = new ConcurrentHashMap<String, SoftReference<Bitmap>>();
		taskQueue = new ArrayList<LoadImage.Task>();
		// 启动图片下载线程
		isRunning = true;
	}

	final Handler h2 = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				Map m = (HashMap) msg.obj;
				ImageView v = (ImageView) m.get("imageView");
				v.setImageResource((Integer) m.get("resId"));
				break;
			case 2:
				Map m2 = (HashMap) msg.obj;
				ImageView v2 = (ImageView) m2.get("imageView");
				v2.setImageBitmap((Bitmap) m2.get("bitmap"));
				break;
			default:
				break;
			}
		}
	};

	@Override
	public void run() {
		v.setTag(url);
		Bitmap bitmap = loadImageAsyn(url, getImageCallback(v, resId));
		Message message = new Message();

		Map m = new HashMap();
		m.put("imageView", v);
		if (bitmap == null) {
			m.put("resId", resId);
			message.obj = m;
			message.what = 1;
			h2.sendMessage(message);

		} else {
			m.put("bitmap", bitmap);
			message.obj = m;
			message.what = 2;
			h2.sendMessage(message);
			v.setImageBitmap(bitmap);
		}
		new Thread(runnable).start();
	}

	public Bitmap loadImageAsyn(String path, ImageCallback callback) {
		if (caches.containsKey(path)) {
			// 取出软引用
			SoftReference<Bitmap> rf = caches.get(path);
			// 通过软引用，获取图片
			Bitmap bitmap = rf.get();
			// 如果该图片已经被释放，则将该path对应的键从Map中移除掉
			if (bitmap == null) {
				caches.remove(path);
			} else {
				return bitmap;
			}
		} else {
			// 如果缓存中不常在该图片，则创建图片下载任务
			Task task = new Task();
			task.path = path;
			task.callback = callback;
			if (!taskQueue.contains(task)) {
				taskQueue.add(task);
				// 唤醒任务下载队列
				synchronized (runnable) {
					runnable.notifyAll();
				}
			}
		}

		// 缓存中没有图片则返回null
		return null;
	}

	private Runnable runnable = new Runnable() {

		@Override
		public void run() {
			while (isRunning) {
				// 当队列中还有未处理的任务时，执行下载任务
				while (taskQueue.size() > 0) {
					// 获取第一个任务，并将之从任务队列中删除
					Task task = taskQueue.remove(0);
					// 将下载的图片添加到缓存
					task.bitmap = returnBitMap(task.path);
					caches.put(task.path,
							new SoftReference<Bitmap>(task.bitmap));
					if (h != null) {
						// 创建消息对象，并将完成的任务添加到消息对象中
						Message msg = h.obtainMessage();
						msg.obj = task;
						// 发送消息回主线程
						h.sendMessage(msg);
					}
				}

				// 如果队列为空,则令线程等待
				synchronized (this) {
					try {
						this.wait();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}
	};

	private ImageCallback getImageCallback(final ImageView imageView,
			final int resId) {
		return new ImageCallback() {
			@Override
			public void loadImage(String path, Bitmap bitmap) {
				if (path.equals(imageView.getTag().toString())) {
					imageView.setImageBitmap(bitmap);
				} else {
					imageView.setImageResource(resId);
				}
			}
		};
	}

	class Task {
		// 下载任务的下载路径
		String path;
		// 下载的图片
		Bitmap bitmap;
		// 回调对象
		ImageCallback callback;

		@Override
		public boolean equals(Object o) {
			Task task = (Task) o;
			return task.path.equals(path);
		}
	}

	public interface ImageCallback {
		void loadImage(String path, Bitmap bitmap);
	}

	private Bitmap returnBitMap(String url) {
		URL myFileUrl = null;
		Bitmap bitmap = null;
		try {
			System.out.println("下载图片：" + url);
			myFileUrl = new URL(url);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		try {
			HttpURLConnection conn = (HttpURLConnection) myFileUrl
					.openConnection();
			conn.setDoInput(true);
			conn.connect();
			InputStream is = conn.getInputStream();
			bitmap = BitmapFactory.decodeStream(is);
			is.close();
		} catch (IOException e) {
			return drawableToBitmap(res.getDrawable(R.drawable.paper));
		}
		return bitmap;
	}

	public static Bitmap drawableToBitmap(Drawable drawable) {
		Bitmap bitmap = Bitmap
				.createBitmap(
						drawable.getIntrinsicWidth(),
						drawable.getIntrinsicHeight(),
						drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
								: Bitmap.Config.RGB_565);
		Canvas canvas = new Canvas(bitmap);
		drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),
				drawable.getIntrinsicHeight());
		drawable.draw(canvas);
		return bitmap;
	}

	final Handler h = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// v.setImageBitmap((Bitmap) msg.obj);
			Task task = (Task) msg.obj;
			// 调用callback对象的loadImage方法，并将图片路径和图片回传给adapter
			task.callback.loadImage(task.path, task.bitmap);
		}
	};

}
