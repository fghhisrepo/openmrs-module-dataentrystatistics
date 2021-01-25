package org.openmrs.module.dataentrystatistics.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.openmrs.Location;

public class ReportData<T> {

	private final String creator;

	private final String location;

	private Location parentLocation;

	private final ReportType reportType;

	private final Date startDate;

	private final Date endDate;

	private final List<T> data;

	public ReportData(final String creator, final String location, final ReportType reportType, final Date startDate,
			final Date endDate) {
		this.creator = creator;
		this.location = location;
		this.reportType = reportType;
		this.startDate = startDate;
		this.endDate = endDate;
		this.data = new ArrayList<T>();
	}

	public ReportData(final String creator, final String location, final Location parentLocation,
			final ReportType reportType, final Date startDate, final Date endDate) {
		this.creator = creator;
		this.location = location;
		this.parentLocation = parentLocation;
		this.reportType = reportType;
		this.startDate = startDate;
		this.endDate = endDate;
		this.data = new ArrayList<T>();
	}

	public String getCreator() {
		return this.creator;
	}

	public String getLocation() {
		return this.location;
	}

	public ReportType getReportType() {
		return this.reportType;
	}

	public Date getStartDate() {
		return this.startDate;
	}

	public Date getEndDate() {
		return this.endDate;
	}

	public List<T> getData() {
		return Collections.unmodifiableList(this.data);
	}

	public void addData(final T data) {
		this.data.add(data);
	}

	public Location getParentLocation() {
		return this.parentLocation;
	}

	public void setParentLocation(final Location parentLocation) {
		this.parentLocation = parentLocation;
	}

}
