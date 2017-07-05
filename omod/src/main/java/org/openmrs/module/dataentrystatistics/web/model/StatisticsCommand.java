package org.openmrs.module.dataentrystatistics.web.model;

import java.util.Date;

import org.openmrs.module.dataentrystatistics.DataTable;

public class StatisticsCommand {

	private Date fromDate;

	private Date toDate;

	private DataTable table;

	private String encUserColumn;

	private String orderUserColumn;

	private int year;

	private int month;

	private String location;

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public int getMonth() {
		return month;
	}

	public void setMonth(int month) {
		this.month = month;
	}

	public StatisticsCommand() {
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

	public String getEncUserColumn() {
		return encUserColumn;
	}

	public void setEncUserColumn(String encUserColumn) {
		this.encUserColumn = encUserColumn;
	}

	public String getOrderUserColumn() {
		return orderUserColumn;
	}

	public void setOrderUserColumn(String orderUserColumn) {
		this.orderUserColumn = orderUserColumn;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

}
