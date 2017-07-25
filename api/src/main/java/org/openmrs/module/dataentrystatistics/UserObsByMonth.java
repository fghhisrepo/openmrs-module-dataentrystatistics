package org.openmrs.module.dataentrystatistics;

public class UserObsByMonth {
	private String user;
	private Integer date;
	private Integer year;
	private Integer day;
	private Long totalObs;
	private String location;


	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public Long getTotalObs() {
		return totalObs;
	}

	public void setTotalObs(Long totalObs) {
		this.totalObs = totalObs;
	}

	public Integer getDate() {
		return date;
	}

	public void setDate(Integer date) {
		this.date = date;
	}

	public Integer getYear() {
		return year;
	}

	public void setYear(Integer year) {
		this.year = year;
	}

	public Integer getDay() {
		return day;
	}

	public void setDay(Integer day) {
		this.day = day;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}
}
