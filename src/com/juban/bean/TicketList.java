package com.juban.bean;

import java.util.List;

/**
 * 门票集合类.
 */
public class TicketList {
	private int totalcount;// 门票总数量
	private int checkcount;// 已签到的门票数量
	private List<TicketListItem> items;// 门票信息集合
	public int getTotalcount() {
		return totalcount;
	}
	public void setTotalcount(int totalcount) {
		this.totalcount = totalcount;
	}
	public int getCheckcount() {
		return checkcount;
	}
	public void setCheckcount(int checkcount) {
		this.checkcount = checkcount;
	}
	public List<TicketListItem> getItems() {
		return items;
	}
	public void setItems(List<TicketListItem> items) {
		this.items = items;
	}
}
