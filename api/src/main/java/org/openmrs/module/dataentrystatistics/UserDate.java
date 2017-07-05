package org.openmrs.module.dataentrystatistics;

import java.util.Date;

public class UserDate {

	private String user;
	private Date date;
	private Long totalObs;
	private DataTable table;
	private String encUserColumn;
	private String orderUserColumn;
	private Date fromDate;
	private Date toDate;

	public UserDate(String user, Date date, Long totalObs) {
		this.user = user;
		this.date = date;
		this.totalObs = totalObs;
	}

	public UserDate() {

	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Long getTotalObs() {
		return totalObs;
	}

	public void setTotalObs(Long totalObs) {
		this.totalObs = totalObs;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public DataTable getTable() {
		return table;
	}

	public void setTable(DataTable table) {
		this.table = table;
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

	public Date getFromDate() {
		return fromDate;
	}

	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}

	public Date getToDate() {
		return toDate;
	}

	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}

}
