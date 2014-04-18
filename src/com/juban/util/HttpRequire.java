package com.juban.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import com.alibaba.fastjson.JSON;
import com.juban.bean.ServerResult;
import com.juban.bean.ServerResults;

public class HttpRequire {

	private static ServerResult request(String url, String auth)
			throws Exception {
		// 得到url请求.
		System.out.println("请求的url" + url);

		DefaultHttpClient httpclient = new DefaultHttpClient();
		try {
			HttpPost httpost = new HttpPost(url);
			if (auth != null)
				httpost.addHeader("auth", auth);
			HttpResponse response = httpclient.execute(httpost);
			HttpEntity entity = response.getEntity();
			BufferedReader br = new BufferedReader(new InputStreamReader(
					entity.getContent(), "UTF-8"));
			String str = br.readLine();
			// 如果没有登录成功，就弹出提示信息.
			ServerResult result = (ServerResult) JSON.parseObject(str,
					ServerResult.class);
			return result;
		} catch (Exception e) {
			throw e;
		} finally {
			// 关闭连接.
			httpclient.getConnectionManager().shutdown();
		}
	}

	private static ServerResults requestArr(String url, String auth)
			throws Exception {
		System.out.println("请求的url" + url);
		// 得到url请求.
		DefaultHttpClient httpclient = new DefaultHttpClient();
		try {
			HttpPost httpost = new HttpPost(url);
			if (auth != null)
				httpost.addHeader("auth", auth);
			HttpResponse response = httpclient.execute(httpost);
			HttpEntity entity = response.getEntity();
			BufferedReader br = new BufferedReader(new InputStreamReader(
					entity.getContent(), "UTF-8"));
			// 如果没有登录成功，就弹出提示信息.
			ServerResults result = (ServerResults) JSON.parseObject(
					br.readLine(), ServerResults.class);
			return result;
		} catch (Exception e) {
			throw e;
		} finally {
			// 关闭连接.
			httpclient.getConnectionManager().shutdown();
		}
	}

	public static ServerResult login(String userName, String password)
			throws Exception {
		System.out.println("登陆地址：" + Constant.HOST + ",,11password="
				+ getMD5(password));
		String url = Constant.HOST + "?do=login&username=" + userName
				+ "&password=" + getMD5(password);// md5.toString(16);
		return request(url, null);
	}

	private static final char HEX_DIGITS[] = { '0', '1', '2', '3', '4', '5',
			'6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };

	public static String toHexString(byte[] b) { // String to byte
		StringBuilder sb = new StringBuilder(b.length * 2);
		for (int i = 0; i < b.length; i++) {
			sb.append(HEX_DIGITS[(b[i] & 0xf0) >>> 4]);
			sb.append(HEX_DIGITS[b[i] & 0x0f]);
		}
		return sb.toString();
	}

	public static String md5(String s) {
		try {
			// Create MD5 Hash
			MessageDigest digest = java.security.MessageDigest
					.getInstance("MD5");
			digest.update(s.getBytes());
			byte messageDigest[] = digest.digest();

			return toHexString(messageDigest);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}

		return "";
	}

	public static String getMD5(String val) throws NoSuchAlgorithmException {
		return md5(val);
	}

	public static ServerResult userActities(int page, int size, String auth)
			throws Exception {
		return request(Constant.HOST + "?do=myevents" + "&page=" + page
				+ "&size=" + size + "&auth=" + auth, auth);
	}

	public static ServerResult activitiDetail(String eventId, String auth)
			throws Exception {
		return request(Constant.HOST + "?do=eventinfo&eventid=" + eventId
				+ "&auth=" + auth, auth);
	}

	public static ServerResults typelist(String eventId, String auth)
			throws Exception {
		return requestArr(Constant.HOST + "?do=listtype&eventid=" + eventId
				+ "&auth=" + auth, auth);
	}

	public static ServerResult tickeslist(String eventId, String auth)
			throws Exception {
		return request(Constant.HOST + "?do=mytickets&eventid=" + eventId
				+ "&auth=" + auth, auth);
	}

	public static boolean qiandao(String tickid, String auth) throws Exception {
		try {
			// 如果没有登录成功，就弹出提示信息.
			ServerResult result = request(Constant.HOST
					+ "?do=checkticket&ticketid=" + tickid
					+ "&check=true&auth=" + auth, auth);
			return "true".equals(result.getData());
		} catch (Exception e) {
			throw e;
		}
	}
}
