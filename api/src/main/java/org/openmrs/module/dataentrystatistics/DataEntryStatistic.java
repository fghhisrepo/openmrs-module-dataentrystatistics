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
import org.openmrs.module.dataentrystatistics.model.ReportData;
import org.openmrs.module.dataentrystatistics.util.CollectionUtil;
import org.openmrs.module.dataentrystatistics.util.Month;

public class DataEntryStatistic<K> {

	protected final Log log = LogFactory.getLog(this.getClass());

	public static DataTable tableByDateAndObs(final ReportData<UserObsByDate> reportData) {
		final SimpleDateFormat formatDate = new SimpleDateFormat("dd-MM-yyy");
		final DataTable table = new DataTable();

		if (reportData.getData().isEmpty()) {
			return table;
		}

		final List<String> users = CollectionUtil.mapWithoutDuplication(reportData.getData(), "user", String.class);

		final List<Date> dates = CollectionUtil.daysBetween(reportData.getStartDate(), reportData.getEndDate());

		table.setLocation(reportData.getData().get(0).getLocation());
		table.addColumn("DATE");
		table.addColumns(users);
		table.addColumn("TOTAL");

		final TableRow workingDaysRow = new TableRow();

		for (final Date date : dates) {

			final TableRow tableRow = new TableRow();

			tableRow.put("DATE", formatDate.format(date));

			Long totalPerDate = 0L;

			for (final String user : users) {
				final Long totalPerUserAndDate = getTotalObsPerUserAndDate(reportData.getData(), user, date);

				updateWorkingTime(workingDaysRow, user, totalPerUserAndDate);

				totalPerDate += totalPerUserAndDate;
				tableRow.put(user, totalPerUserAndDate);
			}

			tableRow.put("TOTAL", totalPerDate);

			table.addRow(tableRow);
		}

		final TableRow totalObsRow = new TableRow();
		final TableRow averageRow = new TableRow();

		totalObsRow.put("DATE", "TOTAL OBS");
		workingDaysRow.put("DATE", "TOTAL WORKING DAYS");
		averageRow.put("DATE", "AVERAGE (OBS/DAY)");

		Long totalObservation = 0L;
		Integer totalWorkingDays = 0;

		for (final String user : users) {

			final Long totalObservationPerUser = getTotalObservationPerUser(reportData.getData(), user);
			totalObsRow.put(user, totalObservationPerUser);

			totalObservation += totalObservationPerUser;

			final Integer workingDays = (Integer) workingDaysRow.get(user);
			totalWorkingDays += workingDays;

			averageRow.put(user, calculateAverage(totalObservationPerUser, workingDays));
		}

		totalObsRow.put("TOTAL", totalObservation);
		workingDaysRow.put("TOTAL", totalWorkingDays);
		averageRow.put("TOTAL", calculateAverage(totalObservation, totalWorkingDays));

		table.addRow(totalObsRow);
		table.addRow(workingDaysRow);
		table.addRow(averageRow);

		return table;
	}

	private static String calculateAverage(final Long totalObservationPerUser, final Integer workingDays) {
		final DecimalFormat decimalFormat = new DecimalFormat("#0.0");

		if (workingDays == 0) {
			return decimalFormat.format(0.0);
		}

		return decimalFormat.format(Double.valueOf(totalObservationPerUser) / Double.valueOf(workingDays));
	}

	private static void updateWorkingTime(final TableRow workingDaysRow, final String user, final Long total) {

		if (total <= 0) {
			return;
		}

		Integer workingTime = (Integer) workingDaysRow.get(user);

		if (workingTime == null) {
			workingDaysRow.put(user, 1);
			return;
		}

		workingTime = (Integer) workingDaysRow.get(user);
		workingTime++;

		workingDaysRow.put(user, workingTime);
	}

	private static Long getTotalObservationPerUser(final List<UserObsByDate> obsByDates, final String user) {

		Long total = 0L;

		for (final UserObsByDate userObsByDate : obsByDates) {

			if (user.equals(userObsByDate.getUser())) {
				total = total + userObsByDate.getTotalObs();
			}
		}

		return total;
	}

	private static Long getTotalObsPerUserAndDate(final List<UserObsByDate> userObsByDates, final String user,
			final Date date) {

		for (final UserObsByDate userObsByDate : userObsByDates) {

			if ((date.compareTo(userObsByDate.getDate()) == 0) && user.equals(userObsByDate.getUser())) {
				return userObsByDate.getTotalObs();
			}
		}

		return 0L;
	}

	public static DataTable tableByFormAndEncounters(final List<UserObsByFormType> userObsByFormTypes) {

		final List<String> users = new ArrayList<String>();

		final Set<String> forms = new HashSet<String>();

		final DataTable table = new DataTable();

		for (final UserObsByFormType userObsByFormType : userObsByFormTypes) {

			users.add(userObsByFormType.getUser().toUpperCase());
			forms.add(userObsByFormType.getForm());
			table.setLocation(userObsByFormType.getLocation());

		}
		table.addColumn("FORMULARIOS");
		table.addColumns(users);
		table.addColumn("TOTAL");

		final TableRow lastRowTotalForm = new TableRow();
		final TableRow lastRowTotalObs = new TableRow();
		final TableRow tableAveregeObsPerEncounter = new TableRow();

		lastRowTotalForm.put("FORMULARIOS", "ENC-TOTAL FORMULARIOS");
		lastRowTotalObs.put("FORMULARIOS", "OBS-TOTAL FORMULARIOS");
		tableAveregeObsPerEncounter.put("FORMULARIOS", "MEDIA DE OBS POR ENC(OBS/ENC)");

		for (final String form : forms) {

			final TableRow tableRowForm = new TableRow();
			final TableRow tableRowObs = new TableRow();

			tableRowForm.put("FORMULARIOS", "ENC".concat(" - ").concat(form));
			tableRowObs.put("FORMULARIOS", "OBS".concat(" - ").concat(form));

			for (final String user : users) {

				final Long totalForms = getTotalEncounterPerUserAndForm(form, user.toUpperCase(), userObsByFormTypes);

				final Long totalObs = getTotal(form, user.toUpperCase(), userObsByFormTypes);

				tableRowForm.put(user.toUpperCase(), totalForms);

				tableRowObs.put(user.toUpperCase(), totalObs);

				tableRowForm.put("TOTAL", getTotalFormsEncounters(form, userObsByFormTypes));
				tableRowObs.put("TOTAL", getTotalFormsObs(form, userObsByFormTypes));
			}

			table.addRow(tableRowForm);
			table.addRow(tableRowObs);

		}
		final DecimalFormat format = new DecimalFormat("#.##");

		Double avarege = (double) 0;

		for (final String user : users) {

			final Long totalEnc = getTotalPerFormType(user, userObsByFormTypes, "ENC");

			lastRowTotalForm.put(user, format.format(totalEnc));
			lastRowTotalForm.put("TOTAL", getTotalFormsEncounters(userObsByFormTypes));

			final Long totalObs = getTotalPerOBS(user, userObsByFormTypes, "OBS");

			lastRowTotalObs.put(user, format.format(totalObs));
			lastRowTotalObs.put("TOTAL", getTotalFormsOBS(userObsByFormTypes));

			avarege = totalObs.doubleValue() / totalEnc.doubleValue();

			final String value = format.format(avarege);

			tableAveregeObsPerEncounter.put(user, value);
			tableAveregeObsPerEncounter.put("TOTAL", value);
			final Long totalEncd = getTotalPerFormType(user, userObsByFormTypes, "ENC");

			lastRowTotalForm.put(user, totalEncd);

			tableAveregeObsPerEncounter.put(user, value);

		}
		final Long totalEnc = getTotalPerFormType(userObsByFormTypes, "ENC");
		final Long totalObs = getTotalPerOBS(userObsByFormTypes, "OBS");
		final DecimalFormat f = new DecimalFormat("#.####");

		tableAveregeObsPerEncounter.put("TOTAL", f.format(totalObs.doubleValue() / totalEnc.doubleValue()));

		table.addRow(lastRowTotalForm);
		table.addRow(lastRowTotalObs);
		table.addRow(tableAveregeObsPerEncounter);

		return table;
	}

	private static Long getTotalPerFormType(final String user, final List<UserObsByFormType> userObsByFormTypes,
			final String type) {

		Long sum = 0L;

		for (final UserObsByFormType obsByFormType : userObsByFormTypes) {

			if (user.equalsIgnoreCase(obsByFormType.getUser())) {

				if (type.substring(0, 3).equals("ENC")) {
					sum = sum + obsByFormType.getTotalEncounters();
				}
			}
		}
		return sum;
	}

	private static Long getTotalPerFormType(final List<UserObsByFormType> userObsByFormTypes, final String type) {

		Long sum = 0L;

		for (final UserObsByFormType obsByFormType : userObsByFormTypes) {

			if (type.substring(0, 3).equals("ENC")) {
				sum = sum + obsByFormType.getTotalEncounters();
			}
		}
		return sum;
	}

	private static Long getTotalPerOBS(final String user, final List<UserObsByFormType> userObsByFormTypes,
			final String type) {

		Long sum = 0L;

		for (final UserObsByFormType obsByFormType : userObsByFormTypes) {
			if (user.equalsIgnoreCase(obsByFormType.getUser())) {

				if (type.substring(0, 3).equals("OBS")) {
					sum = sum + obsByFormType.getTotalObs();
				}
			}
		}
		return sum;
	}

	private static Long getTotalPerOBS(final List<UserObsByFormType> userObsByFormTypes, final String type) {

		Long sum = 0L;

		for (final UserObsByFormType obsByFormType : userObsByFormTypes) {

			if (type.substring(0, 3).equals("OBS")) {
				sum = sum + obsByFormType.getTotalObs();
			}
		}
		return sum;
	}

	private static Long getTotalFormsEncounters(final String form, final List<UserObsByFormType> obsByFormTypes) {

		Long sum = 0L;

		for (final UserObsByFormType obsByFormType : obsByFormTypes) {

			if (form.equals(obsByFormType.getForm())) {
				sum = sum + obsByFormType.getTotalEncounters();
			}
		}
		return sum;
	}

	private static Long getTotalFormsEncounters(final List<UserObsByFormType> obsByFormTypes) {

		Long sum = 0L;

		for (final UserObsByFormType obsByFormType : obsByFormTypes) {
			sum = sum + obsByFormType.getTotalEncounters();
		}
		return sum;
	}

	private static Long getTotalFormsOBS(final List<UserObsByFormType> obsByFormTypes) {

		Long sum = 0L;

		for (final UserObsByFormType obsByFormType : obsByFormTypes) {
			sum = sum + obsByFormType.getTotalObs();
		}
		return sum;
	}

	private static Long getTotalFormsObs(final String form, final List<UserObsByFormType> obsByFormTypes) {

		Long sum = 0L;

		for (final UserObsByFormType obsByFormType : obsByFormTypes) {

			if (form.equals(obsByFormType.getForm())) {
				sum = sum + obsByFormType.getTotalObs();
			}
		}
		return sum;
	}

	public static DataTable tableByMonthsByObs(final List<UserObs> userObs) {

		final List<String> users = CollectionUtil.mapWithoutDuplication(userObs, "user", String.class);
		final List<Integer> months = CollectionUtil.mapWithoutDuplication(userObs, "date", Integer.class);

		final DataTable table = new DataTable();

		if (userObs.isEmpty()) {
			return table;
		}

		table.setLocation(userObs.get(0).getLocation());

		table.addColumn("MES");
		table.addColumns(users);
		table.addColumn("TOTAL");

		final TableRow workingMonthsRow = new TableRow();
		workingMonthsRow.put("MES", "TOTAL WORKING MONTHS");

		for (final Integer month : months) {

			final TableRow tableRow = new TableRow();

			tableRow.put("MES", Month.getMonthName(month));

			Long totalPerMonth = 0L;

			for (final String user : users) {
				final Long totalPerMonthAndUser = getTotalPerMonthAndUser(userObs, month, user);
				updateWorkingTime(workingMonthsRow, user, totalPerMonthAndUser);

				totalPerMonth += totalPerMonthAndUser;
				tableRow.put(user, totalPerMonthAndUser);
			}

			tableRow.put("TOTAL", totalPerMonth);
			table.addRow(tableRow);
		}

		final TableRow averageRow = new TableRow();
		final TableRow totalObsRow = new TableRow();

		totalObsRow.put("MES", "TOTAL OBS");
		averageRow.put("MES", "MEDIA/OBS");

		Long totalObs = 0L;
		Integer totalWorkingMonths = 0;

		for (final String user : users) {

			final Long totalObsPerUser = getTotalObsPerUser(user, userObs);
			totalObs += totalObsPerUser;

			final Integer totalWorkingMonthPerUser = (Integer) workingMonthsRow.get(user);
			totalWorkingMonths += totalWorkingMonthPerUser;

			totalObsRow.put(user, totalObsPerUser);
			averageRow.put(user, calculateAverage(totalObsPerUser, totalWorkingMonthPerUser));
		}

		totalObsRow.put("TOTAL", totalObs);
		workingMonthsRow.put("TOTAL", totalWorkingMonths);
		averageRow.put("TOTAL", calculateAverage(totalObs, totalWorkingMonths));

		table.addRow(totalObsRow);
		table.addRow(workingMonthsRow);
		table.addRow(averageRow);

		return table;
	}

	private static Long getTotalPerMonthAndUser(final List<UserObs> userObsData, final Integer monthId,
			final String username) {

		Long total = 0L;

		for (final UserObs userObs : userObsData) {
			if ((userObs.getDate() == monthId) && username.equals(userObs.getUser())) {
				total += userObs.getTotalObs();
			}
		}

		return total;
	}

	private static Long getTotalObsPerUser(final String user, final List<UserObs> monthObs) {

		Long total = 0L;

		for (final UserObs montObs : monthObs) {

			if (user.equals(montObs.getUser())) {
				total += montObs.getTotalObs();
			}
		}
		return total;
	}

	private static Long getTotalEncounterPerUserAndForm(final String form, final String user,
			final List<UserObsByFormType> userObsByFormTypes) {

		for (final UserObsByFormType userObsByFormType : userObsByFormTypes) {

			if (form.equals(userObsByFormType.getForm()) && user.equals(userObsByFormType.getUser().toUpperCase())) {
				return userObsByFormType.getTotalEncounters();
			}
		}

		return 0L;

	}

	private static Long getTotal(final String form, final String user,
			final List<UserObsByFormType> userObsByFormTypes) {

		for (final UserObsByFormType userObsByFormType : userObsByFormTypes) {

			if (form.equals(userObsByFormType.getForm()) && user.equals(userObsByFormType.getUser().toUpperCase())) {

				return userObsByFormType.getTotalObs();
			}
		}

		return 0L;
	}
}
