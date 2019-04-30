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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.openmrs.util.OpenmrsUtil;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataTable {
	protected static final Log log = LogFactory.getLog(DataTable.class);

	public static class MessageId {
		public static final String LOCATION = "dataentrystatistics.location";
		public static final String REPORT_TYPE = "dataentrystatistics.type";
		public static final String START_DATE = "dataentrystatistics.startDate";
		public static final String END_DATE = "dataentrystatistics.endDate";
	}
	private List<String> columnOrder;

	private ArrayList<TableRow> rows;

	private String location;

	private String parentLocation;

	private String reportType;

	private String fromDate;

	private String toDate;

	public DataTable() {
		this.columnOrder = new ArrayList<String>();
		this.rows = new ArrayList<TableRow>();
	}

	public DataTable(final List<TableRow> rows) {
		columnOrder = new ArrayList<String>();
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
		sb.append("<thead>");

		sb.append(this.getHtmlHeader(columns.size()));
		sb.append("<tr><td colspan=" + columns.size() + "/></tr>");

		sb.append("<tr>");
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

	public HSSFWorkbook generateSpreadsheet() {
		HSSFWorkbook workbook = new HSSFWorkbook();

		HSSFSheet sheet = workbook.createSheet("statistics");
		if (rows.size() == 0) {
			HSSFRow row0 = sheet.createRow(0);
			HSSFCell cellA1 = row0.createCell(0);
			cellA1.setCellValue("DataTable with no rows");
			return workbook;
		}

		List<String> columns;
		if (columnOrder.size() > 0) {
			columns = columnOrder;
		} else {
			columns = new ArrayList<String>(rows.get(0).getColumnNames());
			Collections.sort(columns);
		}

		int nextRow = createSpreadsheetHeader(sheet, columns.size());
		HSSFRow row = sheet.createRow(nextRow);
		for(int i=0; i < columns.size() ; i++) {
			row.createCell(i).setCellValue(columns.get(i));
		}

		for(final TableRow tableRow: rows) {
			row = sheet.createRow(++nextRow);
			for(int i=0; i < columns.size() ; i++) {
				Object value = tableRow.get(columns.get(i));
				if(value != null) {
					try {
						row.createCell(i).setCellValue(Double.parseDouble(value.toString()));
					} catch (NumberFormatException e) {
						log.debug("Could not parse value " + value + " to double, set as is.");
						row.createCell(i).setCellValue(value.toString());
					}
				} else {
					row.createCell(i).setCellValue("");
				}
			}
		}

		return  workbook;
	}

	private int createSpreadsheetHeader(@NotNull final HSSFSheet worksheet, final int numberOfColumns) {
		int rowNumber = 0;
		HSSFRow row = worksheet.createRow(rowNumber);
		row.createCell(0).setCellValue(OpenmrsUtil.getMessage(MessageId.LOCATION));
		row.createCell(1).setCellValue(getLocation());

		// Add cells to be merged in order to align with the columns to be created in subsequent data rows.
		for(int i=2; i < numberOfColumns; i++) {
			row.createCell(i);
		}

		// Merge the cells.
		worksheet.addMergedRegion(new CellRangeAddress(0, 0, 1, numberOfColumns - 1));

		// Report type header
		rowNumber++;
		row = worksheet.createRow(rowNumber);
		row.createCell(0).setCellValue(OpenmrsUtil.getMessage(MessageId.REPORT_TYPE));

		row.createCell(1).setCellValue(getReportType());

		// Add cells to be merged in order to align with the columns to be created in subsequent data rows.
		for(int i=2; i < numberOfColumns; i++) {
			row.createCell(i);
		}

		// Merge the cells.
		worksheet.addMergedRegion(new CellRangeAddress(1, 1, 1, numberOfColumns -1));

		// From date header
		rowNumber++;
		row = worksheet.createRow(rowNumber);
		row.createCell(0).setCellValue(OpenmrsUtil.getMessage(MessageId.START_DATE));

		row.createCell(1).setCellValue(getFromDate());

		// Add cells to be merged in order to align with the columns to be created in subsequent data rows.
		for(int i=2; i < numberOfColumns; i++) {
			row.createCell(i);
		}

		// Merge the cells.
		worksheet.addMergedRegion(new CellRangeAddress(2, 2, 1, numberOfColumns - 1));

		// To date header
		rowNumber++;
		row = worksheet.createRow(rowNumber);
		row.createCell(0).setCellValue(OpenmrsUtil.getMessage(MessageId.END_DATE));

		row.createCell(1).setCellValue(getToDate());

		// Add cells to be merged in order to align with the columns to be created in subsequent data rows.
		for(int i=2; i < numberOfColumns; i++) {
			row.createCell(i);
		}

		// Merge the cells.
		worksheet.addMergedRegion(new CellRangeAddress(3, 3, 1, numberOfColumns - 1));

		return ++rowNumber;
	}

	private String getHtmlHeader(final int columnsSize) {
		final StringBuilder builder = new StringBuilder();
		
		builder.append("<tr><th><strong>").append(OpenmrsUtil.getMessage(MessageId.LOCATION).toUpperCase())
				.append(":</strong></th><th colspan=").append(columnsSize).append(">").append(this.getLocation()).append("</th></tr>");
		builder.append("<tr><th><strong>").append(OpenmrsUtil.getMessage(MessageId.REPORT_TYPE).toUpperCase())
				.append(":</strong><th colspan=").append(columnsSize).append(" id=\"reportSelected\">").append(this.getReportType()).append("</th></tr>");
		builder.append("<tr><th><strong>").append(OpenmrsUtil.getMessage(MessageId.START_DATE).toUpperCase())
				.append(":</strong><th colspan=").append(columnsSize).append(">").append(this.getFromDate()).append("</th></tr>");
		builder.append("<tr><th><strong>").append(OpenmrsUtil.getMessage(MessageId.END_DATE).toUpperCase())
				.append(":</strong><th colspan=").append(columnsSize).append(">").append(this.getToDate()).append("</th></tr>");

		return builder.toString();
	}

	public String getParentLocation() {
		return this.parentLocation;
	}

	public void setParentLocation(final String parentLocation) {
		this.parentLocation = parentLocation;
	}
}