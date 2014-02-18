package com.ericssonlabs.bean;

import com.alibaba.fastjson.JSONObject;

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
