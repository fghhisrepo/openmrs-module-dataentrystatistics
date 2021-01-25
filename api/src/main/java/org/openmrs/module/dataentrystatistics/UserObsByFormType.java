package org.openmrs.module.dataentrystatistics;

import org.openmrs.Location;

public class UserObsByFormType {

	private String form;
	private Long totalEncounters;
	private Long totalObs;
	private String user;
	private String type;
	private String location;
	private Location parentLocation;
 


	public String getForm() {
		return this.form;
	}

	public void setForm(final String form) {
		this.form = form;
	}

	public Long getTotalEncounters() {
		return this.totalEncounters;
	}

	public void setTotalEncounters(final Long totalEncounters) {
		this.totalEncounters = totalEncounters;
	}

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

	public String getType() {
		return this.type;
	}

	public void setType(final String type) {
		this.type = type;
	}

	public String getLocation() {
		return this.location;
	}

	public void setLocation(final String location) {
		this.location = location;
	}

	public Location getParentLocation() {
		return this.parentLocation;
	}

	public void setParentLocation(final Location parentLocation) {
		this.parentLocation = parentLocation;
	}

}
