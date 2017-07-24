package org.openmrs.module.dataentrystatistics;

public class UserObsByFormType {

	private String form;
	private Long totalEncounters;
	private Long totalObs;
	private String user;
	private String type;

	public String getForm() {
		return form;
	}

	public void setForm(String form) {
		this.form = form;
	}

	public Long getTotalEncounters() {
		return totalEncounters;
	}

	public void setTotalEncounters(Long totalEncounters) {
		this.totalEncounters = totalEncounters;
	}

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

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

}
