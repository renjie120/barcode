package com.ericssonlabs.bean;

import java.util.List;

public class EventList {
	private int totalcount;// 活动总数量
	private List<EventListItem> items;// 活动信息集合

	public int getTotalcount() {
		return totalcount;
	}

	public void setTotalcount(int totalcount) {
		this.totalcount = totalcount;
	}

	public List<EventListItem> getItems() {
		return items;
	}

	public void setItems(List<EventListItem> items) {
		this.items = items;
	}

}
