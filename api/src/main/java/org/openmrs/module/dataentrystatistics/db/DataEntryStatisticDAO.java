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
package org.openmrs.module.dataentrystatistics.db;

import java.util.Date;
import java.util.List;

import org.openmrs.Role;
import org.openmrs.module.dataentrystatistics.UserObs;
import org.openmrs.module.dataentrystatistics.UserObsByDate;
import org.openmrs.module.dataentrystatistics.UserObsByFormType;
import org.openmrs.module.dataentrystatistics.model.ReportData;

/**
 * Database methods for the DataEntryStatisticService
 */
public interface DataEntryStatisticDAO {

	public ReportData<UserObsByDate> findObservationsByPeriodAndLocation(Date fromDate, Date toDate, Integer location);

	public List<UserObsByDate> countTotalObsPerUserAndDate(Date fromDate, Date toDate, Integer location);

	public List<UserObsByFormType> getAllObsByUsersAndForm(Date fromDate, Date toDate, Integer location);

	public List<UserObs> getAllMonthObs(Date fromDate, Date toDate, Integer location);

	public List<Role> getAllRoles();

}
