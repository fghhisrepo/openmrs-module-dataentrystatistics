package org.openmrs.module.dataentrystatistics;

import java.util.Date;

public class UserObs {

	private String user;
	private Integer date;
	private Integer year;
	private Integer day;
	private Long totalObs;
	private String location;
	private Date creationDate;

	public String getUser() {
		return this.user;
	}

	public void setUser(final String user) {
		this.user = user;
	}

	public Long getTotalObs() {
		return this.totalObs;
	}

	public void setTotalObs(final Long totalObs) {
		this.totalObs = totalObs;
	}

	public Integer getDate() {
		return this.date;
	}

	public void setDate(final Integer date) {
		this.date = date;
	}

	public Integer getYear() {
		return this.year;
	}

	public void setYear(final Integer year) {
		this.year = year;
	}

	public Integer getDay() {
		return this.day;
	}

	public void setDay(final Integer day) {
		this.day = day;
	}

	public String getLocation() {
		return this.location;
	}

	public void setLocation(final String location) {
		this.location = location;
	}

	public void setCreationDate(final Date creationDate) {
		this.creationDate = creationDate;
	}

	public Date getCreationDate() {
		return this.creationDate;
	}
}
