package com.juban.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.security.MessageDigest;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClients;

import com.alibaba.fastjson.JSON;
import com.juban.bean.ServerResult;
import com.juban.bean.ServerResults;

public class HttpRequire {
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
		// 进行Md5加密数据.
		BigInteger md5 = new BigInteger(encryptMD5(password.getBytes()));
		String url = Constant.HOST + "?do=login&username=" + userName
				+ "&password=" + md5.toString(16);
		return request(url, null);
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
