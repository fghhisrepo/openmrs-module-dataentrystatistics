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
package org.openmrs.module.dataentrystatistics.impl;

import java.util.Date;
import java.util.List;

import org.openmrs.Role;
import org.openmrs.api.impl.BaseOpenmrsService;
import org.openmrs.module.dataentrystatistics.DataEntryStatisticService;
import org.openmrs.module.dataentrystatistics.UserObs;
import org.openmrs.module.dataentrystatistics.UserObsByDate;
import org.openmrs.module.dataentrystatistics.UserObsByFormType;
import org.openmrs.module.dataentrystatistics.db.DataEntryStatisticDAO;
import org.openmrs.module.dataentrystatistics.model.ReportData;
import org.springframework.transaction.annotation.Transactional;

/**
 * Contains all Service methods for managing DataEntryStatistics
 */
@Transactional
public class DataEntryStatisticServiceImpl extends BaseOpenmrsService implements DataEntryStatisticService {

	protected DataEntryStatisticDAO dao;

	/**
	 * @return the dao
	 */
	public DataEntryStatisticDAO getDao() {
		return this.dao;
	}

	/**
	 * @param dao
	 *            the dao to set
	 */
	public void setDao(final DataEntryStatisticDAO dao) {
		this.dao = dao;
	}

	@Override
	public ReportData<UserObsByDate> findObservationsByPeriodAndLocation(final Date fromDate, final Date toDate,
			final Integer location) {
		return this.dao.findObservationsByPeriodAndLocation(fromDate, toDate, location);
	}

	@Override
	public List<Role> getAllRoles() {
		return this.dao.getAllRoles();
	}

	@Override
	public ReportData<UserObsByFormType> getAllObsByUsersAndForm(final Date fromDate, final Date toDate,
			final Integer location) {

		return this.dao.getAllObsByUsersAndForm(fromDate, toDate, location);
	}

	@Override
	public ReportData<UserObs>  getAllMonthObs(final Date fromDate, final Date toDate, final Integer location) {
		return this.dao.getAllMonthObs(fromDate, toDate, location);
	}

	@Override
	public List<UserObsByDate> countTotalObsPerUserAndDate(final Date fromDate, final Date toDate,
			final Integer location) {
		return this.dao.countTotalObsPerUserAndDate(fromDate, toDate, location);
	}

	@Override
	public ReportData<UserObsByDate> findObservationsByPeriod(final Date fromDate, final Date toDate,
			final Integer location) {
		return this.dao.findObservationsByPeriod(fromDate, toDate, location);
	}

	@Override
	public ReportData<UserObsByFormType>  getAllObsByUsersAndFormAndLocation(final Date fromDate, final Date toDate,
			final Integer location) {
		return this.dao.getAllObsByUsersAndFormAndLocation(fromDate, toDate, location);
	}

	@Override
	public ReportData<UserObs>  getAllMonthObsFromLocation(final Date fromDate, final Date toDate, final Integer location) {
		return this.dao.getAllMonthObsFromLocation(fromDate, toDate, location);
	}

}
