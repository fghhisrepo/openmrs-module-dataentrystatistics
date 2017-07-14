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

		for (Date date : dates) {

			TableRow tableRow = new TableRow();

			tableRow.put("DATA", date);

			for (int j = 0; j < users.size(); j++) {

				Long total = getTotalObsPerUserAndDate(date, users.get(j).toUpperCase(), obsByDates);
				tableRow.put(users.get(j).toUpperCase(), total);
			}
			table.addRow(tableRow);

		}

		TableRow lastRowTotal = new TableRow();
		TableRow lastRowAverege = new TableRow();

		lastRowTotal.put("DATA", "TOTAL OBS");
		lastRowAverege.put("DATA", "MEDIA OBS");

		for (String u : users) {
			Long totalObs = getTotal(u, obsByDates);

			lastRowTotal.put(u, totalObs);
			lastRowAverege.put(u, totalObs / table.getRowCount());
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

		for (String form : forms) {

			TableRow tableRowForm = new TableRow();
			TableRow tableRowObs = new TableRow();

			tableRowForm.put("FORMULARIOS", form.concat("TOTAL-ENC"));
			tableRowObs.put("FORMULARIOS", form.concat("TOTAL-OBS"));

			for (String user : users) {

				Long totalForms = getTotalEncounterPerUserAndForm(form, user.toUpperCase(), userObsByFormTypes);

				Long totalObs = getTotal(form, user.toUpperCase(), userObsByFormTypes);

				tableRowForm.put(user.toUpperCase(), totalForms);

				tableRowObs.put(user.toUpperCase(), totalObs);

			}

			table.addRow(tableRowForm);
			table.addRow(tableRowObs);

		}

		TableRow lastRowTotal = new TableRow();
		TableRow lastRowAverege = new TableRow();

		lastRowTotal.put("FORMULARIOS", "TOTAL FORMULARIOS");
		lastRowAverege.put("FORMULARIOS", "MEDIA FORMULARIOS");

		for (String u : users) {
			Long total = getTotalPerFormType(u, userObsByFormTypes);

			lastRowTotal.put(u, total);
			lastRowAverege.put(u, total / (table.getRowCount()) / 2);
		}

		table.addRow(lastRowTotal);
		table.addRow(lastRowAverege);

		return table;
	}

	private static Long getTotalPerFormType(String user, List<UserObsByFormType> userObsByFormTypes) {
		Long sum = 0L;

		for (UserObsByFormType obsByFormType : userObsByFormTypes) {

			if (user.equalsIgnoreCase(obsByFormType.getUser())) {
				sum = sum + obsByFormType.getTotalObs();
			}
		}
		return sum;
	}

	public static DataTable tableByMonthsByObs(List<MonthObs> monthObs) {

		Set<Integer> dates = new HashSet<Integer>();

		List<String> users = new ArrayList<String>();

		DataTable table = new DataTable();

		for (MonthObs m : monthObs) {

			users.add(m.getUser().toUpperCase());
			dates.add(m.getDate());

		}

		table.addColumn("MES");
		table.addColumns(users);

		for (Integer month : dates) {

			TableRow tableRow = new TableRow();

			tableRow.put("MES", Month.getMonthName(month));

			for (int j = 0; j < users.size(); j++) {

				Long total = getTotalObsPerUserAndDate(month, users.get(j).toUpperCase(), monthObs);
				tableRow.put(users.get(j).toUpperCase(), total);
			}

			table.addRow(tableRow);

		}

		return table;
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
