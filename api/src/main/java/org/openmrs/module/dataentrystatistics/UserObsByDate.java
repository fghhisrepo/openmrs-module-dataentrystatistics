package org.openmrs.module.dataentrystatistics;

import java.util.Date;

import org.openmrs.Location;

public class UserObsByDate {

	private String user;

	private Date date;

	private Long totalObs;

	private String location;

	private Location parentLocation;

	private Date startDate;

	private Date endDate;

	public UserObsByDate(final String user, final Date date, final Long totalObs) {
		this.user = user;
		this.date = date;
		this.totalObs = totalObs;
	}

	public UserObsByDate() {
	}

	public Date getDate() {
		return this.date;
	}

	public void setDate(final Date date) {
		this.date = date;
	}

	public Long getTotalObs() {
		return this.totalObs;
	}

	public void setTotalObs(final Long totalObs) {
		this.totalObs = totalObs;
	}

	public String getUser() {
		return this.user;
	}

	public void setUser(final String user) {
		this.user = user;
	}

	public String getLocation() {
		return this.location;
	}

	public void setLocation(final String location) {
		this.location = location;
	}

	public void setStartDate(final Date startDate) {
		this.startDate = startDate;
	}

	public Date getStartDate() {
		return this.startDate;
	}

	public void setEndDate(final Date endDate) {
		this.endDate = endDate;
	}

	public Date getEndDate() {
		return this.endDate;
	}

	public Location getParentLocation() {
		return this.parentLocation;
	}

	public void setParentLocation(final Location parentLocation) {
		this.parentLocation = parentLocation;
	}



}
