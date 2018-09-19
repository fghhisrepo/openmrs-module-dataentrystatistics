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
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.Person;
import org.openmrs.module.dataentrystatistics.model.ReportData;
import org.openmrs.module.dataentrystatistics.util.CollectionUtil;
import org.openmrs.module.dataentrystatistics.util.DateUtil;
import org.openmrs.module.dataentrystatistics.util.Month;

public class DataEntryStatistic<K> {

	private Person user;

	private String entryType;

	private Integer numberOfEntries;

	private Integer numberOfObs;

	private Object groupBy = null;

	protected final Log log = LogFactory.getLog(this.getClass());

	public static DataTable tableByDateAndObs(final ReportData<UserObsByDate> reportData) {
		final SimpleDateFormat formatDate = new SimpleDateFormat("dd-MM-yyy");
		final DataTable table = new DataTable();

		if (reportData.getData().isEmpty()) {
			return table;
		}

		final List<String> users = CollectionUtil.mapWithoutDuplication(reportData.getData(), "user", String.class);

		final List<Date> dates = CollectionUtil.daysBetween(reportData.getStartDate(), reportData.getEndDate());

		table.addColumn("DATE");
		table.addColumn("WORKING DAY");
		table.addColumns(users);
		table.addColumn("TOTAL");

		final TableRow workingDaysRow = new TableRow();
		final TableRow daysLessThanFiftyOrZeroWorkingDaysRow = new TableRow();
		final TableRow DaysRow0Obs = new TableRow();

		table.addRow(daysLessThanFiftyOrZeroWorkingDaysRow);
		table.addRow(DaysRow0Obs);

		for (final Date date : dates) {

			final TableRow tableRow = new TableRow();

			tableRow.put("DATE", formatDate.format(date));
			daysLessThanFiftyOrZeroWorkingDaysRow.put("DATE", "DAYS WITH LESS THAN 50 OBS (INCLUDING DAYS WITH 0)");
			DaysRow0Obs.put("DATE", "DAYS WITH 0 OBS");

			Long totalPerDate = 0L;

			for (final String user : users) {

				final Long totalPerUserAndDate = getTotalObsPerUserAndDate(reportData.getData(), user, date);

				DaysWithLessThan50ObservationsIncludingDaysWithZero(daysLessThanFiftyOrZeroWorkingDaysRow, user,
						totalPerUserAndDate, date);

				DaysWith0bservations(DaysRow0Obs, user, totalPerUserAndDate, date);
				updateWorkingTime(workingDaysRow, user, totalPerUserAndDate);

				totalPerDate += totalPerUserAndDate;
				tableRow.put(user, totalPerUserAndDate);
			}

			tableRow.put("TOTAL", totalPerDate);
			tableRow.put("WORKING DAY", DateUtil.isWeekEnd(date) ? "N" : "Y ");
			table.addRow(tableRow);
		}

		final TableRow totalObsRow = new TableRow();
		final TableRow averageRow = new TableRow();

		totalObsRow.put("DATE", "TOTAL OBS");
		totalObsRow.put("WORKING DAY", "N/A");
		workingDaysRow.put("DATE", "TOTAL WORKING DAYS");
		workingDaysRow.put("WORKING DAY", "N/A");
		daysLessThanFiftyOrZeroWorkingDaysRow.put("TOTAL", "N/A");
		daysLessThanFiftyOrZeroWorkingDaysRow.put("WORKING DAY", "N/A");
		DaysRow0Obs.put("TOTAL", "N/A");
		DaysRow0Obs.put("WORKING DAY", "N/A");

		averageRow.put("DATE", "AVERAGE (OBS/DAY)");
		averageRow.put("WORKING DAY", "N/A");

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

	public static DataTable tableByDateAndObsAndLocation(final ReportData<UserObsLocation> reportData) {
		final DataTable table = new DataTable();

		if (reportData.getData().isEmpty()) {
			return table;
		}

		final List<String> users = CollectionUtil.mapWithoutDuplication(reportData.getData(), "user", String.class);

		final List<String> locations = CollectionUtil.mapWithoutDuplication(reportData.getData(), "location",
				String.class);

		table.addColumn("LOCATION");
		table.addColumns(users);

		for (String location : locations) {

			final TableRow tableRow = new TableRow();

			tableRow.put("LOCATION", location);

			for (final String user : users) {

				final Long totalObservationPerUserAndLocation = getTotalObsPerUserAndLocation(reportData.getData(),
						user, location);

				tableRow.put(user, totalObservationPerUserAndLocation);
			}
			table.addRow(tableRow);

		}

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

	private static void DaysWithLessThan50ObservationsIncludingDaysWithZero(final TableRow nonWorkingDaysRow,
			final String user, final Long totalObsPerDate, final Date date) {

		if ((totalObsPerDate >= 50)) {
			return;
		}

		if (DateUtil.isWeekEnd(date)) {
			return;
		}

		Integer daysNotworking = (Integer) nonWorkingDaysRow.get(user);

		if (daysNotworking == null) {
			nonWorkingDaysRow.put(user, 1);
			return;
		}

		nonWorkingDaysRow.put(user, ++daysNotworking);
	}

	private static void DaysWith0bservations(final TableRow nonWorkingDaysRow, final String user,
			final Long totalObsPerDate, final Date date) {

		if ((totalObsPerDate > 0)) {
			return;
		}

		if (DateUtil.isWeekEnd(date)) {
			return;
		}

		Integer daysNotworking = (Integer) nonWorkingDaysRow.get(user);

		if (daysNotworking == null) {
			nonWorkingDaysRow.put(user, 1);
			return;
		}

		nonWorkingDaysRow.put(user, ++daysNotworking);
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
			if (!userObsByDate.getUser().equals(null) || !userObsByDate.getUser().isEmpty()) {
				if ((date.compareTo(userObsByDate.getDate()) == 0) && user.equals(userObsByDate.getUser())) {
					return userObsByDate.getTotalObs();
				}
			}
		}

		return 0L;
	}

	private static Long getTotalObsPerUserAndLocation(final List<UserObsLocation> userObsLocations, final String user,
			final String location) {

		for (final UserObsLocation userObsLocation : userObsLocations) {

			if (!userObsLocation.getUser().equals(null) || !userObsLocation.getUser().isEmpty()) {

				if (location.equals(userObsLocation.getLocation()) && user.equals(userObsLocation.getUser())) {

					return userObsLocation.getTotalObs();
				}
			}
		}

		return 0L;
	}

	public static DataTable tableByFormAndEncounters(final ReportData<UserObsByFormType> reportData) {

		final List<String> users = new ArrayList<String>();

		final Set<String> forms = new HashSet<String>();

		final DataTable table = new DataTable();

		if (reportData.getData().isEmpty()) {
			return table;
		}

		for (final UserObsByFormType userObsByFormType : reportData.getData()) {

			if ((userObsByFormType.getUser() != null) || !userObsByFormType.getUser().isEmpty()) {
				users.add(userObsByFormType.getUser().toUpperCase());
				forms.add(userObsByFormType.getForm());
			}

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

				final Long totalForms = getTotalEncounterPerUserAndForm(form, user.toUpperCase(), reportData.getData());

				final Long totalObs = getTotal(form, user.toUpperCase(), reportData.getData());

				tableRowForm.put(user.toUpperCase(), totalForms);

				tableRowObs.put(user.toUpperCase(), totalObs);

				tableRowForm.put("TOTAL", getTotalFormsEncounters(form, reportData.getData()));
				tableRowObs.put("TOTAL", getTotalFormsObs(form, reportData.getData()));
			}

			table.addRow(tableRowForm);
			table.addRow(tableRowObs);

		}
		final DecimalFormat format = new DecimalFormat("#.##");

		Double avarege = (double) 0;

		for (final String user : users) {

			final Long totalEnc = getTotalPerFormType(user, reportData.getData(), "ENC");

			lastRowTotalForm.put(user, format.format(totalEnc));
			lastRowTotalForm.put("TOTAL", getTotalFormsEncounters(reportData.getData()));

			final Long totalObs = getTotalPerOBS(user, reportData.getData(), "OBS");

			lastRowTotalObs.put(user, format.format(totalObs));
			lastRowTotalObs.put("TOTAL", getTotalFormsOBS(reportData.getData()));

			avarege = totalObs.doubleValue() / totalEnc.doubleValue();

			final String value = format.format(avarege);

			tableAveregeObsPerEncounter.put(user, value);
			tableAveregeObsPerEncounter.put("TOTAL", value);
			final Long totalEncd = getTotalPerFormType(user, reportData.getData(), "ENC");

			lastRowTotalForm.put(user, totalEncd);

			tableAveregeObsPerEncounter.put(user, value);

		}
		final Long totalEnc = getTotalPerFormType(reportData.getData(), "ENC");
		final Long totalObs = getTotalPerOBS(reportData.getData(), "OBS");
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

	public static DataTable tableByMonthsByObs(final ReportData<UserObs> userObs) {

		final List<String> users = CollectionUtil.mapWithoutDuplication(userObs.getData(), "user", String.class);
		final List<Integer> months = CollectionUtil.mapWithoutDuplication(userObs.getData(), "date", Integer.class);

		final DataTable table = new DataTable();

		if (userObs.getData().isEmpty()) {
			return table;
		}

		table.setLocation(userObs.getData().get(0).getLocation());

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
				final Long totalPerMonthAndUser = getTotalPerMonthAndUser(userObs.getData(), month, user);
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

			final Long totalObsPerUser = getTotalObsPerUser(user, userObs.getData());
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

	// convenience utility methods

	@SuppressWarnings("rawtypes")
	public static DataTable tableByUserAndType(final List<DataEntryStatistic> stats, final Boolean hideAverageObs) {
		final Set<Person> users = new HashSet<Person>();
		final SortedSet<String> types = new TreeSet<String>();
		final Set<Object> groups = new HashSet<Object>();
		final Map<String, Integer> totals = new HashMap<String, Integer>();
		final Map<String, Integer> totalObs = new HashMap<String, Integer>();
		for (final DataEntryStatistic s : stats) {
			users.add(s.getUser());
			types.add(s.getEntryType());
			groups.add(s.getGroupBy());
			final String temp = s.getUser().getPersonId() + "." + s.getEntryType() + "." + s.getGroupBy();
			final Integer soFar = totals.get(temp);
			totals.put(temp, soFar == null ? s.getNumberOfEntries() : (soFar + s.getNumberOfEntries()));
			totalObs.put(temp, soFar == null ? s.getNumberOfObs() : (soFar + s.getNumberOfObs()));
		}
		final DataTable table = new DataTable();
		table.addColumn("User");
		table.addColumns(types);
		for (final Object group : groups) {
			final Map<String, Integer> groupTotals = new HashMap<String, Integer>();
			for (final Person u : users) {
				final TableRow tr = new TableRow();
				tr.put("User", u.getPersonName().getFullName());
				Integer rowTotal = 0;
				for (final String entryType : types) {
					Integer i = totals.get(u.getPersonId() + "." + entryType + "." + group);
					Integer j = totalObs.get(u.getPersonId() + "." + entryType + "." + group);
					if (i == null) {
						i = 0;
					}
					if (j == null) {
						j = 0;
					}
					String averageObs = "";
					if (!hideAverageObs && (i > 0) && (j > 0)) {
						final DecimalFormat df = new DecimalFormat("###,###.##");
						final float obss = j;
						final float encs = i;
						final float avgObs = obss / encs;
						averageObs += " (avg. " + df.format(avgObs) + " obs per enc)";
					}
					tr.put(entryType, i + averageObs);
					Integer groupTotalSoFar = groupTotals.get(entryType);
					groupTotalSoFar = groupTotalSoFar == null ? i : groupTotalSoFar + i;
					groupTotals.put(entryType, groupTotalSoFar);
					rowTotal += i;
				}
				if (rowTotal > 0) {
					table.addRow(tr);
				}
			}
			// add grouping totals
			final TableRow totalTR = new TableRow();
			totalTR.put("User", "--" + (group == null ? "Total" : group.toString()) + "--");
			for (final String entryType : types) {
				totalTR.put(entryType, groupTotals.get(entryType));
			}
			table.addRow(totalTR);
		}
		return table;
	}

	public DataEntryStatistic() {
	}

	@Override
	public String toString() {
		return this.user + " entered " + this.numberOfEntries + " of " + this.entryType;
	}

	public Integer getNumberOfEntries() {
		return this.numberOfEntries;
	}

	public void setNumberOfEntries(final Integer numberOfEntries) {
		this.numberOfEntries = numberOfEntries;
	}

	public String getEntryType() {
		return this.entryType;
	}

	public void setEntryType(final String entryType) {
		this.entryType = entryType;
	}

	public Person getUser() {
		return this.user;
	}

	public void setUser(final Person user) {
		this.user = user;
	}

	public Object getGroupBy() {
		return this.groupBy;
	}

	public void setGroupBy(final Object groupBy) {
		this.groupBy = groupBy;
	}

	public Integer getNumberOfObs() {
		return this.numberOfObs;
	}

	public void setNumberOfObs(final Integer numberOfObs) {
		this.numberOfObs = numberOfObs;
	}

}
