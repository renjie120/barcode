package com.ericssonlabs.bean;

/**
 * 门票类型.
 */
public class TicketTypeItem {
	private long typeid;// 门票类型ID
	private String name;// 门票名称
	public long getTypeid() {
		return typeid;
	}
	public void setTypeid(long typeid) {
		this.typeid = typeid;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
}
