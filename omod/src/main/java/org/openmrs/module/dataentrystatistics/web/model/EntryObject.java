package org.openmrs.module.dataentrystatistics.web.model;

import java.util.Date;

import org.openmrs.module.dataentrystatistics.DataTable;

public class EntryObject {

	private Date fromDate;

	private Date toDate;

	private DataTable table;

	private String obsCreator;

	private Integer year;

	private Integer month;

	private String location;
	
	private String period;
	
	private String reportType;

	public Integer getYear() {
		return year;
	}

	public void setYear(Integer year) {
		this.year = year;
	}

	public Integer getMonth() {
		return month;
	}

	public void setMonth(Integer month) {
		this.month = month;
	}

	public EntryObject() {
	}

	public Date getFromDate() {
		return fromDate;
	}

	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}

	public DataTable getTable() {
		return table;
	}

	public void setTable(DataTable table) {
		this.table = table;
	}

	public Date getToDate() {
		return toDate;
	}

	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getObsCreator() {
		return obsCreator;
	}

	public void setObsCreator(String obsCreator) {
		this.obsCreator = obsCreator;
	}

	public String getPeriod() {
		return period;
	}

	public void setPeriod(String period) {
		this.period = period;
	}

	public String getReportType() {
		return reportType;
	}

	public void setReportType(String reportType) {
		this.reportType = reportType;
	}

}
