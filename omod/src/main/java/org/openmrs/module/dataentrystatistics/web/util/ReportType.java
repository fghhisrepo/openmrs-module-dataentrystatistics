package org.openmrs.module.dataentrystatistics.web.util;

public enum ReportType {

	FORM_TYPES, DAILY_OBS, MONTH_OBS;

	private String types;

	public String getType() {
		return types;
	}

	@Override
	public String toString() {
		return types;
	}

}
