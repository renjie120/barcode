package com.ericssonlabs.bean;

/**
 * 门票详情数据类.
 */
public class TicketListItem {
	private String ticketid;// 门票ID
	private long type;// 门票类型ID
	private String name;// 购票者名称
	private String email;// 购票者邮箱
	private String phone;// 购票者手机号码
	private int chargetype;// 门票收费类型 (1:免费 2:收费)
	private int paystatus;// 支付状态 (0:未支付 1:已支付)
	private int checkstatus;// 签到状态 (0:未签到 1:已签到)

	public String getTicketid() {
		return ticketid;
	}

	public void setTicketid(String ticketid) {
		this.ticketid = ticketid;
	}

	public long getType() {
		return type;
	}

	public void setType(long type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public int getChargetype() {
		return chargetype;
	}

	public void setChargetype(int chargetype) {
		this.chargetype = chargetype;
	}

	public int getPaystatus() {
		return paystatus;
	}

	public void setPaystatus(int paystatus) {
		this.paystatus = paystatus;
	}

	public int getCheckstatus() {
		return checkstatus;
	}

	public void setCheckstatus(int checkstatus) {
		this.checkstatus = checkstatus;
	}

}
