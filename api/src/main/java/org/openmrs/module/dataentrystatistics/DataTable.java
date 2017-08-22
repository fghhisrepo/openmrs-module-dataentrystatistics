/**
 * The contents of this file are subject to the OpenMRS Public License
 * Version 1.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://license.openmrs.org
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations
 * under the License.
 *
 * Copyright (C) OpenMRS, LLC.  All Rights Reserved.
 */
package org.openmrs.module.dataentrystatistics;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openmrs.util.OpenmrsUtil;

public class DataTable {

	private List<String> columnOrder;

	private ArrayList<TableRow> rows;

	private String location;

	private String reportType;

	private String fromDate;

	private String toDate;

	public DataTable() {
		this.columnOrder = new ArrayList<String>();
		this.rows = new ArrayList<TableRow>();
	}

	public DataTable(final List<TableRow> rows) {
		if (rows instanceof ArrayList) {
			this.rows = (ArrayList<TableRow>) rows;
		} else {
			this.rows = new ArrayList<TableRow>();
			this.rows.addAll(rows);
		}
	}

	public void addColumn(final String colName) {
		if (!this.columnOrder.contains(colName)) {
			this.columnOrder.add(colName);
		}
	}

	public void addColumns(final Collection<String> colNames) {
		for (final String colName : colNames) {
			this.addColumn(colName);
		}
	}

	public void addColumnsValues(final Collection<Long> colValues) {
		for (final Long c : colValues) {
			this.addColumn(c.toString());
		}
	}

	public int getRowCount() {
		return this.rows.size();
	}

	public void addRow(final TableRow row) {
		this.rows.add(row);
	}

	public void addRows(final Collection<TableRow> rows) {
		this.rows.addAll(rows);
	}

	public ArrayList<TableRow> getRows() {
		return this.rows;
	}

	public String getLocation() {
		return this.location;
	}

	public void setLocation(final String location) {
		this.location = location;
	}

	public String getReportType() {
		return this.reportType;
	}

	public void setReportType(final String reportType) {
		this.reportType = reportType;
	}

	public String getFromDate() {
		return this.fromDate;
	}

	public void setFromDate(final String fromDate) {
		this.fromDate = fromDate;
	}

	public String getToDate() {
		return this.toDate;
	}

	public void setToDate(final String toDate) {
		this.toDate = toDate;
	}

	public void sortByColumn(final String colName) {
		Collections.sort(this.rows, new Comparator<TableRow>() {

			@Override
			@SuppressWarnings({ "unchecked", "rawtypes" })
			public int compare(final TableRow left, final TableRow right) {
				final Comparable l = (Comparable) left.get(colName);
				final Comparable r = (Comparable) right.get(colName);
				return OpenmrsUtil.compareWithNullAsLowest(l, r);
			}
		});
	}

	public Map<String, DataTable> split(final TableRowClassifier trc) {
		final Map<String, DataTable> ret = new HashMap<String, DataTable>();
		for (final TableRow row : this.rows) {
			final String classification = trc.classify(row);
			DataTable thisClass = ret.get(classification);
			if (thisClass == null) {
				thisClass = new DataTable();
				ret.put(classification, thisClass);
			}
			thisClass.addRow(row);
		}
		return ret;
	}

	@Override
	public String toString() {
		if (this.rows.size() == 0) {
			return "DataTable with no rows";
		}
		List<String> columns;
		if (this.columnOrder.size() > 0) {
			columns = this.columnOrder;
		} else {
			columns = new ArrayList<String>(this.rows.get(0).getColumnNames());
			Collections.sort(columns);
		}
		final StringBuilder sb = new StringBuilder();
		for (final String colName : columns) {
			sb.append(colName).append(",");
		}
		for (final TableRow row : this.rows) {
			sb.append("\n");
			for (final String colName : columns) {
				sb.append(row.get(colName)).append(",");
			}
		}
		return sb.toString();
	}

	public String getHtmlTable() {
		if (this.rows.size() == 0) {
			return "DataTable with no rows";
		}
		List<String> columns;
		if (this.columnOrder.size() > 0) {
			columns = this.columnOrder;
		} else {
			columns = new ArrayList<String>(this.rows.get(0).getColumnNames());
			Collections.sort(columns);
		}
		final StringBuilder sb = new StringBuilder();
		sb.append("<div id=\"dvData\">");
		sb.append("<table id=\"result\" class=\"display nowrap\" border=\"1\" cellspacing=\"0\" cellpadding=\"2\">");
		sb.append("<thead><tr>");
		for (final String colName : columns) {
			sb.append("<th>").append(colName).append("</th>");
		}
		sb.append("</tr></thead><tbody>");
		for (final TableRow row : this.rows) {
			sb.append("<tr>");
			for (final String colName : columns) {
				sb.append("<td>").append(row.get(colName)).append("</td>");
			}
			sb.append("</tr>");
		}
		sb.append("</tbody></table>");
		sb.append("</div>");

		return sb.toString();

	}

	public String getHtmlHeader() {
		final StringBuilder builder = new StringBuilder();
		builder.append("<div id=\"header\">");
		builder.append("<p><strong>LOCATION:</strong> ").append(this.getLocation()).append("</p>");
		builder.append("<p><strong>REPORT TYPE:</strong> ").append(this.getReportType()).append("</p>");
		builder.append("<p><strong>FROM DATE:</strong> ").append(this.getFromDate()).append("</p>");
		builder.append("<p><strong>TO DATE:</strong> ").append(this.getToDate()).append("</p>");
		builder.append("</div>");

		return builder.toString();
	}
}
