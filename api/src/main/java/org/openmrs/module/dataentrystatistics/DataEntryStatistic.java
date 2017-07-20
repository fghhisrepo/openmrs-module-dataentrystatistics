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

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.module.dataentrystatistics.util.Month;

public class DataEntryStatistic<K> {

	protected final Log log = LogFactory.getLog(getClass());

	public static DataTable tableByDateAndObs(List<UserObsByDate> obsByDates) {

		List<String> users = new ArrayList<String>();

		Set<Date> dates = new HashSet<Date>();

		DataTable table = new DataTable();

		for (UserObsByDate userObsByDate : obsByDates) {

			dates.add(userObsByDate.getDate());
			users.add(userObsByDate.getUser().toUpperCase());
		}

		table.addColumn("DATA");
		table.addColumns(users);
		table.addColumn("TOTAL");

		for (Date date : dates) {
			TableRow tableRow = new TableRow();

			tableRow.put("DATA", date);

			for (int j = 0; j < users.size(); j++) {

				Long total = getTotalObsPerUserAndDate(date, users.get(j).toUpperCase(), obsByDates);
				tableRow.put(users.get(j).toUpperCase(), total);
				tableRow.put("TOTAL", getTotal(date, obsByDates));

			}
			table.addRow(tableRow);

		}

		TableRow lastRowTotal = new TableRow();
		TableRow lastRowAverege = new TableRow();

		DecimalFormat format = new DecimalFormat("#.##");

		lastRowTotal.put("DATA", "TOTAL OBS");
		lastRowAverege.put("DATA", "MEDIA OBS");

		for (String u : users) {

			Long totalObs = getTotal(u, obsByDates);
			Double avarege = totalObs.doubleValue() / table.getRowCount();

			lastRowTotal.put(u, totalObs);
			lastRowAverege.put(u, format.format(avarege));

			lastRowTotal.put("TOTAL", getTotal(obsByDates));
			lastRowAverege.put("TOTAL", format.format(getTotal(table.getRowCount(), obsByDates)));

		}

		table.addRow(lastRowTotal);
		table.addRow(lastRowAverege);

		return table;
	}

	private static Long getTotal(String user, List<UserObsByDate> obsByDates) {

		Long sum = 0L;

		for (UserObsByDate userObsByDate : obsByDates) {

			if (user.equalsIgnoreCase(userObsByDate.getUser())) {
				sum = sum + userObsByDate.getTotalObs();
			}
		}
		return sum;
	}

	private static Long getTotal(Date date, List<UserObsByDate> obsByDates) {

		Long sum = 0L;

		for (UserObsByDate userObsByDate : obsByDates) {

			if (date.equals(userObsByDate.getDate())) {
				sum = sum + userObsByDate.getTotalObs();
			}
		}
		return sum;
	}

	private static Long getTotal(List<UserObsByDate> obsByDates) {

		Long sum = 0L;

		for (UserObsByDate userObsByDate : obsByDates) {

			sum = sum + userObsByDate.getTotalObs();

		}
		return sum;
	}

	private static Double getTotal(Integer value, List<UserObsByDate> obsByDates) {

		Double sum = (double) 0;

		for (UserObsByDate userObsByDate : obsByDates) {

			sum = sum + userObsByDate.getTotalObs().doubleValue();

		}
		return sum / value;
	}

	private static Long getTotalObsPerUserAndDate(Date date, String user, List<UserObsByDate> userObsByDates) {

		SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");

		String formatedDate = format.format(date);

		for (UserObsByDate userObsByDate : userObsByDates) {

			String dataUser = format.format(userObsByDate.getDate());

			if (formatedDate.equals(dataUser) && user.equals(userObsByDate.getUser().toUpperCase())) {
				return userObsByDate.getTotalObs();
			}
		}

		return 0L;

	}

	private static Long getTotalObsPerUserAndDate(Integer date, String user, List<MonthObs> monthObs) {

		for (MonthObs month : monthObs) {

			if (date.equals(month.getDate()) && user.equals(month.getUser().toUpperCase())) {
				return month.getTotalObs();
			}
		}

		return 0L;

	}

	public static DataTable tableByFormAndEncounters(List<UserObsByFormType> userObsByFormTypes) {

		List<String> users = new ArrayList<String>();

		Set<String> forms = new HashSet<String>();

		DataTable table = new DataTable();

		for (UserObsByFormType userObsByFormType : userObsByFormTypes) {

			users.add(userObsByFormType.getUser().toUpperCase());
			forms.add(userObsByFormType.getForm());

		}
		table.addColumn("FORMULARIOS");
		table.addColumns(users);
		table.addColumn("TOTAL");

		TableRow lastRowTotalForm = new TableRow();
		TableRow lastRowTotalObs = new TableRow();
		TableRow tableAveregeObsPerEncounter = new TableRow();

		lastRowTotalForm.put("FORMULARIOS", "TOTAL FORMULARIOS-ENC");
		lastRowTotalObs.put("FORMULARIOS", "TOTAL FORMULARIOS-OBS");
		tableAveregeObsPerEncounter.put("FORMULARIOS", "MEDIA DE OBS POR ENC(OBS/ENC)");

		for (String form : forms) {

			TableRow tableRowForm = new TableRow();
			TableRow tableRowObs = new TableRow();

			tableRowForm.put("FORMULARIOS", "ENC".concat(" - ").concat(form));
			tableRowObs.put("FORMULARIOS", "OBS".concat(" - ").concat(form));

			for (String user : users) {

				Long totalForms = getTotalEncounterPerUserAndForm(form, user.toUpperCase(), userObsByFormTypes);

				Long totalObs = getTotal(form, user.toUpperCase(), userObsByFormTypes);

				tableRowForm.put(user.toUpperCase(), totalForms);

				tableRowObs.put(user.toUpperCase(), totalObs);

				tableRowForm.put("TOTAL", getTotalFormsEncounters(form, userObsByFormTypes));
				tableRowObs.put("TOTAL", getTotalFormsObs(form, userObsByFormTypes));
			}

			table.addRow(tableRowForm);
			table.addRow(tableRowObs);

		}

		for (String user : users) {

			DecimalFormat df = new DecimalFormat("#.##");

			Long totalEnc = getTotalPerFormType(user, userObsByFormTypes, "ENC");
			lastRowTotalForm.put(user, df.format(totalEnc));
			lastRowTotalForm.put("TOTAL", getTotalFormsEncounters(userObsByFormTypes));

			Long totalObs = getTotalPerOBS(user, userObsByFormTypes, "OBS");
			lastRowTotalObs.put(user, df.format(totalObs));
			lastRowTotalObs.put("TOTAL", getTotalFormsOBS(userObsByFormTypes));

			Double avarege = totalObs.doubleValue() / totalEnc.doubleValue();

			String value = df.format(avarege);

			tableAveregeObsPerEncounter.put(user, value);
			tableAveregeObsPerEncounter.put("TOTAL", value);

		}

		table.addRow(lastRowTotalForm);
		table.addRow(lastRowTotalObs);
		table.addRow(tableAveregeObsPerEncounter);

		return table;
	}

	private static Long getTotalPerFormType(String user, List<UserObsByFormType> userObsByFormTypes, String type) {

		Long sum = 0L;

		for (UserObsByFormType obsByFormType : userObsByFormTypes) {

			if (user.equalsIgnoreCase(obsByFormType.getUser())) {

				if (type.substring(0, 3).equals("ENC"))
					sum = sum + obsByFormType.getTotalEncounters();
			}
		}
		return sum;
	}

	private static Long getTotalPerOBS(String user, List<UserObsByFormType> userObsByFormTypes, String type) {

		Long sum = 0L;

		for (UserObsByFormType obsByFormType : userObsByFormTypes) {
			if (user.equalsIgnoreCase(obsByFormType.getUser())) {

				if (type.substring(0, 3).equals("OBS")) {
					sum = sum + obsByFormType.getTotalObs();
				}
			}
		}
		return sum;
	}

	private static Long getTotalFormsEncounters(String form, List<UserObsByFormType> obsByFormTypes) {

		Long sum = 0L;

		for (UserObsByFormType obsByFormType : obsByFormTypes) {

			if (form.equals(obsByFormType.getForm())) {
				sum = sum + obsByFormType.getTotalEncounters();
			}
		}
		return sum;
	}

	private static Long getTotalFormsEncounters(List<UserObsByFormType> obsByFormTypes) {

		Long sum = 0L;

		for (UserObsByFormType obsByFormType : obsByFormTypes) {
			sum = sum + obsByFormType.getTotalEncounters();
		}
		return sum;
	}

	private static Long getTotalFormsOBS(List<UserObsByFormType> obsByFormTypes) {

		Long sum = 0L;

		for (UserObsByFormType obsByFormType : obsByFormTypes) {
			sum = sum + obsByFormType.getTotalObs();
		}
		return sum;
	}

	private static Long getTotalFormsObs(String form, List<UserObsByFormType> obsByFormTypes) {

		Long sum = 0L;

		for (UserObsByFormType obsByFormType : obsByFormTypes) {

			if (form.equals(obsByFormType.getForm())) {
				sum = sum + obsByFormType.getTotalObs();
			}
		}
		return sum;
	}

	public static DataTable tableByMonthsByObs(List<MonthObs> monthObs) {

		Set<Integer> months = new HashSet<Integer>();

		Set<Integer> years = new HashSet<Integer>();

		List<String> users = new ArrayList<String>();

		DataTable table = new DataTable();

		for (MonthObs m : monthObs) {

			users.add(m.getUser().toUpperCase());
			months.add(m.getDate());
			years.add(m.getYear());

		}

		table.addColumn("MES");
		table.addColumns(users);

		for (Integer month : months) {
			for (Integer year : years) {

				TableRow tableRow = new TableRow();

				tableRow.put("MES", Month.getMonthName(month).concat("(" + year + ")"));

				for (int j = 0; j < users.size(); j++) {
					Long total = getTotalObsPerUserAndDate(month, users.get(j).toUpperCase(), monthObs);
					tableRow.put(users.get(j).toUpperCase(), total);
				}

				table.addRow(tableRow);
			}
		}

		TableRow lastRowAverege = new TableRow();

		lastRowAverege.put("MES", "MEDIA OBS");

		for (String user : users) {

			Long totalObs = getTotalMonthReport(user, monthObs);

			Long avarege = totalObs / table.getRowCount();

			lastRowAverege.put(user, avarege);

		}

		table.addRow(lastRowAverege);

		return table;
	}

	private static Long getTotalMonthReport(String user, List<MonthObs> monthObs) {

		Long sum = 0L;

		for (MonthObs montObs : monthObs) {

			if (user.equalsIgnoreCase(montObs.getUser())) {
				sum = sum + montObs.getTotalObs();
			}
		}
		return sum;
	}

	private static Long getTotalEncounterPerUserAndForm(String form, String user,
			List<UserObsByFormType> userObsByFormTypes) {

		for (UserObsByFormType userObsByFormType : userObsByFormTypes) {

			if (form.equals(userObsByFormType.getForm()) && user.equals(userObsByFormType.getUser().toUpperCase())) {
				return userObsByFormType.getTotalEncounters();
			}
		}

		return 0L;

	}

	private static Long getTotal(String form, String user, List<UserObsByFormType> userObsByFormTypes) {

		for (UserObsByFormType userObsByFormType : userObsByFormTypes) {

			if (form.equals(userObsByFormType.getForm()) && user.equals(userObsByFormType.getUser().toUpperCase())) {

				return userObsByFormType.getTotalObs();
			}
		}

		return 0L;

	}
}
