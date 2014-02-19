package com.ericssonlabs.bean;

/**
 * 活动详情.
 */
public class EventInfo {
	private long eventid;// 活动ID
	private String name;// 活动名称
	private String starttime;// 开始时间
	private String endtime;// 结束时间
	private int totalcount;// 活动计划出售的门票数量
	private int joincount;// 已售出的门票数量
	private int checkcount;// 已签到的门票数量
	public String imageurl;// 活动图片

	public String getImageurl() {
		return imageurl;
	}

	public void setImageurl(String imageurl) {
		this.imageurl = imageurl;
	}

	public long getEventid() {
		return eventid;
	}

	public void setEventid(long eventid) {
		this.eventid = eventid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getStarttime() {
		return starttime;
	}

	public void setStarttime(String starttime) {
		this.starttime = starttime;
	}

	public String getEndtime() {
		return endtime;
	}

	public void setEndtime(String endtime) {
		this.endtime = endtime;
	}

	public int getTotalcount() {
		return totalcount;
	}

	public void setTotalcount(int totalcount) {
		this.totalcount = totalcount;
	}

	public int getJoincount() {
		return joincount;
	}

	public void setJoincount(int joincount) {
		this.joincount = joincount;
	}

	public int getCheckcount() {
		return checkcount;
	}

	public void setCheckcount(int checkcount) {
		this.checkcount = checkcount;
	}

}
