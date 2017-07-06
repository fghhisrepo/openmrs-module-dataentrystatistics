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

public class DataEntryStatistic {

	protected final Log log = LogFactory.getLog(getClass());

	public static DataTable tableByDateAndObs(
			List<CalculateUserDateForObsCollectedByUser> calculateUserDateForObsCollectedByUsers) {

		List<String> users = new ArrayList<String>();

		Set<Date> dates = new HashSet<Date>();

		DataTable table = new DataTable();

		for (CalculateUserDateForObsCollectedByUser calculateUserDateForObsCollectedByUser : calculateUserDateForObsCollectedByUsers) {

			dates.add(calculateUserDateForObsCollectedByUser.getDate());
			users.add(calculateUserDateForObsCollectedByUser.getUser());

		}

		table.addColumn("Data");
		table.addColumns(users);

		for (Date date : dates) {

			TableRow tableRow = new TableRow();
			tableRow.put("Data", date);

			for (String user : users) {

				Long total = getTotalObsPerUserAndDate(date, user, calculateUserDateForObsCollectedByUsers);
				tableRow.put(user, total);

			}

			table.addRow(tableRow);
		}

		return table;
	}

	private static Long getTotalObsPerUserAndDate(Date date, String user,
			List<CalculateUserDateForObsCollectedByUser> calculateUserDateForObsCollectedByUsers) {

		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

		String formatedDate = format.format(date);

		for (CalculateUserDateForObsCollectedByUser calculateUserDateForObsCollectedByUser : calculateUserDateForObsCollectedByUsers) {

			String dataUser = format.format(calculateUserDateForObsCollectedByUser.getDate());

			if (formatedDate.equals(dataUser) && user.equals(calculateUserDateForObsCollectedByUser.getUser())) {
				return calculateUserDateForObsCollectedByUser.getTotalObs();
			}
		}

		return 0L;

	}

}
