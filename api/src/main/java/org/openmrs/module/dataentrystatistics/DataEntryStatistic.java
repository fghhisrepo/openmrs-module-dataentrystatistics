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

public class DataEntryStatistic {

	protected final Log log = LogFactory.getLog(getClass());

	public static DataTable tableByDateAndObs(
			List<CalculateUserDateForObsCollectedByUser> calculateUserDateForObsCollectedByUsers) {

		List<String> users = new ArrayList<String>();

		Set<Date> dates = new HashSet<Date>();

		List<CalculateUserDateForObsCollectedByUser> groups = new ArrayList<CalculateUserDateForObsCollectedByUser>();

		DataTable table = new DataTable();

		for (CalculateUserDateForObsCollectedByUser calculateUserDateForObsCollectedByUser : calculateUserDateForObsCollectedByUsers) {

			dates.add(calculateUserDateForObsCollectedByUser.getDate());
			users.add(calculateUserDateForObsCollectedByUser.getUser());
			groups.add(calculateUserDateForObsCollectedByUser);
		}

		table.addColumn("Data");
		table.addColumns(users);

		for (Date date : dates) {

			TableRow tableRow = new TableRow();

			tableRow.put("Data", date);

			for (int j = 0; j < users.size(); j++) {

				Long total = getTotalObsPerUserAndDate(date, users.get(j), calculateUserDateForObsCollectedByUsers);
				tableRow.put(users.get(j), total);
			}
			table.addRow(tableRow);

		}

		TableRow lastRow = new TableRow();

		lastRow.put("Data", "Total OBS");

		table.addRow(lastRow);

		return table;

	}

	private static Long getTotalObsPerUserAndDate(Date date, String user,
			List<CalculateUserDateForObsCollectedByUser> calculateUserDateForObsCollectedByUsers) {

		SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");

		String formatedDate = format.format(date);

		for (CalculateUserDateForObsCollectedByUser calculateUserDateForObsCollectedByUser : calculateUserDateForObsCollectedByUsers) {

			String dataUser = format.format(calculateUserDateForObsCollectedByUser.getDate());

			if (formatedDate.equals(dataUser) && user.equals(calculateUserDateForObsCollectedByUser.getUser())) {
				return calculateUserDateForObsCollectedByUser.getTotalObs();
			}
		}

		return 0L;

	}

	private static Long getTotalObsPerUserAndDate(Integer date, String user, List<MonthObs> monthObs) {

		for (MonthObs month : monthObs) {

			if (date.equals(month.getDate()) && user.equals(month.getUser())) {
				return month.getTotalObs();
			}
		}

		return 0L;

	}

	public static DataTable tableByFormAndEncounters(
			List<CalculateUserDateTotalObsByForm> calculateUserDateTotalObsByForms) {

		List<String> users = new ArrayList<String>();

		Set<String> forms = new HashSet<String>();

		DataTable table = new DataTable();

		for (CalculateUserDateTotalObsByForm calculateUserDateTotalObsByForm : calculateUserDateTotalObsByForms) {

			users.add(calculateUserDateTotalObsByForm.getUser());
			forms.add(calculateUserDateTotalObsByForm.getForm());

		}
		table.addColumn("Formularios");
		table.addColumns(users);

		for (String form : forms) {

			TableRow tableRow = new TableRow();
			TableRow tr1 = new TableRow();

			tableRow.put("Formularios", form);
			tr1.put("Formularios", form);

			for (String user : users) {

				Long total = getTotalEncounterPerUserAndForm(form, user, calculateUserDateTotalObsByForms);

				Long totalObs = getTotal(form, user, calculateUserDateTotalObsByForms);

				tableRow.put(user, total);

				tr1.put(user, totalObs);

			}
			table.addRow(tableRow);
			table.addRow(tr1);

		}
		return table;
	}

	public static DataTable tableByMonthsByObs(List<MonthObs> monthObs) {

		Set<Integer> dates = new HashSet<Integer>();

		List<String> users = new ArrayList<String>();

		DataTable table = new DataTable();

		for (MonthObs m : monthObs) {

			users.add(m.getUser());
			dates.add(m.getDate());

		}

		table.addColumn("Mes");
		table.addColumns(users);

		for (Integer month : dates) {

			TableRow tableRow = new TableRow();

			tableRow.put("Mes", Month.getMonthName(month));

			for (int j = 0; j < users.size(); j++) {

				Long total = getTotalObsPerUserAndDate(month, users.get(j), monthObs);
				tableRow.put(users.get(j), total);
			}

			table.addRow(tableRow);

		}

		return table;
	}

	private static Long getTotalEncounterPerUserAndForm(String form, String user,
			List<CalculateUserDateTotalObsByForm> calculateUserDateTotalObsByForms) {

		for (CalculateUserDateTotalObsByForm calculateUserDateTotalObsByForm : calculateUserDateTotalObsByForms) {

			if (form.equals(calculateUserDateTotalObsByForm.getForm())
					&& user.equals(calculateUserDateTotalObsByForm.getUser())) {
				return calculateUserDateTotalObsByForm.getTotalEncounters();
			}
		}

		return 0L;

	}

	private static Long getTotal(String form, String user,
			List<CalculateUserDateTotalObsByForm> calculateUserDateTotalObsByForms) {

		for (CalculateUserDateTotalObsByForm calculateUserDateTotalObsByForm : calculateUserDateTotalObsByForms) {

			if (form.equals(calculateUserDateTotalObsByForm.getForm())
					&& user.equals(calculateUserDateTotalObsByForm.getUser())) {

				return calculateUserDateTotalObsByForm.getTotalObs();
			}
		}

		return 0L;

	}
}
