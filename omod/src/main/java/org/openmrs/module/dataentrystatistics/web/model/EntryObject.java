package org.openmrs.module.dataentrystatistics.web.model;

import java.util.Date;

import org.openmrs.module.dataentrystatistics.DataTable;

public class EntryObject {

	private Date fromDate;

	private Date toDate;

	private Date fromMonth;

	private Date toMonth;

	private DataTable table;

	private String obsCreator;

	private Integer year;

	private Integer month;

	private String location;

	private String period;

	private String reportType;

	public Date getFromMonth() {
		return this.fromMonth;
	}

	public void setFromMonth(final Date fromMonth) {
		this.fromMonth = fromMonth;
	}

	public Date getToMonth() {
		return this.toMonth;
	}

	public void setToMonth(final Date toMonth) {
		this.toMonth = toMonth;
	}

	public Integer getYear() {
		return this.year;
	}

	public void setYear(final Integer year) {
		this.year = year;
	}

	public Integer getMonth() {
		return this.month;
	}

	public void setMonth(final Integer month) {
		this.month = month;
	}

	public EntryObject() {
	}

	public Date getFromDate() {
		return this.fromDate;
	}

	public void setFromDate(final Date fromDate) {
		this.fromDate = fromDate;
	}

	public DataTable getTable() {
		return this.table;
	}

	public void setTable(final DataTable table) {
		this.table = table;
	}

	public Date getToDate() {
		return this.toDate;
	}

	public void setToDate(final Date toDate) {
		this.toDate = toDate;
	}

	public String getLocation() {
		return this.location;
	}

	public void setLocation(final String location) {
		this.location = location;
	}

	public String getObsCreator() {
		return this.obsCreator;
	}

	public void setObsCreator(final String obsCreator) {
		this.obsCreator = obsCreator;
	}

	public String getPeriod() {
		return this.period;
	}

	public void setPeriod(final String period) {
		this.period = period;
	}

	public String getReportType() {
		return this.reportType;
	}

	public void setReportType(final String reportType) {
		this.reportType = reportType;
	}

}
