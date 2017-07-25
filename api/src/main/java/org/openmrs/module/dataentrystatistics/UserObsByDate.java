package org.openmrs.module.dataentrystatistics;

import java.util.Date;

public class UserObsByDate {

	private String user;
	private Date date;
	private Long totalObs;
	private String location;

	public UserObsByDate(String user, Date date, Long totalObs) {
		this.user = user;
		this.date = date;
		this.totalObs = totalObs;
	}

	public UserObsByDate() {

	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Long getTotalObs() {
		return totalObs;
	}

	public void setTotalObs(Long totalObs) {
		this.totalObs = totalObs;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

}
