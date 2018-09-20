package org.openmrs.module.dataentrystatistics;

import java.util.Date;

import org.openmrs.Location;

public class UserObsLocation {

	private String user;

	private Long totalObs;

	private String location;

	private Date startDate;

	private Location parentLocation;

	private Date endDate;

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

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public Location getParentLocation() {
		return parentLocation;
	}

	public void setParentLocation(Location parentLocation) {
		this.parentLocation = parentLocation;
	}

}
