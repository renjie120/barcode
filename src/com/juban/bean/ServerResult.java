package com.juban.bean;

import com.alibaba.fastjson.JSONObject;

/**
 * 包装服务器端请求结果的类.
 *
 * @param <T>
 */
public class ServerResult<T> {
	public int errorcode; // 错误码，参见下面错误码说明
	public String errormsg; // 错误说明，调用正确时为空
	public JSONObject data;// 具体响应数据

	public int getErrorcode() {
		return errorcode;
	}

	public void setErrorcode(int errorcode) {
		this.errorcode = errorcode;
	}

	public JSONObject getData() {
		return data;
	}

	public void setData(JSONObject data) {
		this.data = data;
	}

	public String getErrormsg() {
		return errormsg;
	}

	public void setErrormsg(String errormsg) {
		this.errormsg = errormsg;
	}

}
