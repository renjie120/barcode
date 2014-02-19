package com.ericssonlabs.bean;

import com.alibaba.fastjson.JSONArray;

/**
 * 包装服务端请求返回数组的类.
 *
 * @param <T>
 */
public class ServerResults<T> {
	public int errorcode; // 错误码，参见下面错误码说明
	public String errormsg; // 错误说明，调用正确时为空
	public JSONArray data;// 具体响应数据

	public int getErrorcode() {
		return errorcode;
	}

	public void setErrorcode(int errorcode) {
		this.errorcode = errorcode;
	}

	public JSONArray getData() {
		return data;
	}

	public void setData(JSONArray data) {
		this.data = data;
	}

	public String getErrormsg() {
		return errormsg;
	}

	public void setErrormsg(String errormsg) {
		this.errormsg = errormsg;
	}

}
