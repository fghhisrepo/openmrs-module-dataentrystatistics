package org.openmrs.module.dataentrystatistics.web.model;

import java.util.Date;

import org.openmrs.module.dataentrystatistics.DataTable;

public class StatisticsCommand {

	private Date fromDate;

	private Date toDate;

	private DataTable table;

	private String encUserColumn;

	private String orderUserColumn;

	private String groupBy;

	private Boolean hideAverageObs = false;

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

	public String getGroupBy() {
		return groupBy;
	}

	public void setGroupBy(String groupBy) {
		this.groupBy = groupBy;
	}

	public Boolean getHideAverageObs() {
		return hideAverageObs;
	}

	public void setHideAverageObs(Boolean hideAverageObs) {
		this.hideAverageObs = hideAverageObs;
	}
}
