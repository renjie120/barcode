package com.juban.bean;

/**
 * 用户认证类。
 */
public class AccessToken {
	private long uid;// 用户ID
	private String username;// 用户名
	private String token;// 用户认证票据

	public long getUid() {
		return uid;
	}

	public void setUid(long uid) {
		this.uid = uid;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

}
