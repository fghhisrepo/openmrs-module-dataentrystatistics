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
package org.openmrs.module.dataentrystatistics.web.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.Role;
import org.openmrs.api.context.Context;
import org.openmrs.module.dataentrystatistics.DataEntryStatistic;
import org.openmrs.module.dataentrystatistics.DataEntryStatisticService;
import org.openmrs.module.dataentrystatistics.DataTable;
import org.openmrs.module.dataentrystatistics.UserObs;
import org.openmrs.module.dataentrystatistics.UserObsByDate;
import org.openmrs.module.dataentrystatistics.UserObsByFormType;
import org.openmrs.module.dataentrystatistics.model.OrderBy;
import org.openmrs.module.dataentrystatistics.model.ReportType;
import org.openmrs.module.dataentrystatistics.util.DateUtil;
import org.openmrs.module.dataentrystatistics.web.model.EntryObject;
import org.openmrs.util.OpenmrsUtil;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindException;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;

@SuppressWarnings({ "rawtypes", "deprecation" })
public class DataEntryStatisticsController extends SimpleFormController {

	protected final Log log = LogFactory.getLog(this.getClass());
	private final ModelMap modelMap = new ModelMap();
	private DataTable table;
	private EntryObject entryObject;
	private DataEntryStatisticService dataEntryStatisticService;

	@Override
	protected void initBinder(final HttpServletRequest request, final ServletRequestDataBinder binder)
			throws Exception {
		super.initBinder(request, binder);

		binder.registerCustomEditor(java.util.Date.class,
				new CustomDateEditor(OpenmrsUtil.getDateFormat(Context.getLocale()), true, 10));

	}

	@Override
	protected Object formBackingObject(final HttpServletRequest request) throws ServletException {

		return new EntryObject();

	}

	@Override
	protected Map referenceData(final HttpServletRequest request) throws Exception {

		final DataEntryStatisticService svc = Context.getService(DataEntryStatisticService.class);

		final List<String> reportTypes = new ArrayList<String>();

		final List<Role> roles = svc.getAllRoles();

		for (final ReportType type : ReportType.values()) {
			reportTypes.add(type.name());
		}

		final List<String> orderBys = new ArrayList<String>();

		for (final OrderBy orderBy : OrderBy.values()) {
			orderBys.add(orderBy.name());
		}

		request.setAttribute("reportTypes", reportTypes);
		request.setAttribute("roles", roles);
		request.setAttribute("orderBys", orderBys);

		return this.modelMap;
	}

	@Override
	protected ModelAndView onSubmit(final HttpServletRequest request, final HttpServletResponse response,
			final Object commandObj, final BindException errors) throws Exception {

		final DataEntryStatisticService dataEntryStatisticService = Context.getService(DataEntryStatisticService.class);

		this.entryObject = (EntryObject) commandObj;

		if (this.entryObject.getReportType().equals(ReportType.DAILY_OBS.name())) {

			if (this.entryObject.getOrderBy().equals(OrderBy.HEALTHY_FACILITIES.name())) {

				if (this.entryObject.getLocation().isEmpty() || (this.entryObject.getFromDate() == null)
						|| (this.entryObject.getToDate() == null)) {

					this.entryObject.setTable(this.table);

				}
				this.table = DataEntryStatistic.tableByDateAndObs(
						dataEntryStatisticService.findObservationsByPeriodAndLocation(this.entryObject.getFromDate(),
								this.entryObject.getToDate(), this.parse(this.entryObject.getLocation())));

				if (!dataEntryStatisticService
						.findObservationsByPeriodAndLocation(this.entryObject.getFromDate(),
								this.entryObject.getToDate(), this.parse(this.entryObject.getLocation()))
						.getData().isEmpty()) {

					final String location = dataEntryStatisticService
							.findObservationsByPeriodAndLocation(this.entryObject.getFromDate(),
									this.entryObject.getToDate(), this.parse(this.entryObject.getLocation()))
							.getData().get(0).getLocation();

					this.table.setLocation(location);
					this.entryObject.setTable(this.table);
				}

			}
			if (this.entryObject.getOrderBy().equals(OrderBy.DISTRIC.name())) {

				this.table = DataEntryStatistic.tableByDateAndObs(dataEntryStatisticService
						.findObservationsByPeriod(this.entryObject.getFromDate(), this.entryObject.getToDate(), null));

				if (!dataEntryStatisticService
						.findObservationsByPeriod(this.entryObject.getFromDate(), this.entryObject.getToDate(), null)
						.getData().isEmpty()) {
					final String location = dataEntryStatisticService
							.findObservationsByPeriod(this.entryObject.getFromDate(), this.entryObject.getToDate(),
									null)
							.getData().get(0).getParentLocation().getName();

					this.table.setLocation(location);
				}

				final List<UserObsByDate> userObsByDates = dataEntryStatisticService
						.findObservationsByPeriod(this.entryObject.getFromDate(), this.entryObject.getToDate(), null)
						.getData();

				if (!userObsByDates.isEmpty()) {
					this.table.setLocation(userObsByDates.get(0).getParentLocation().getName());
				}

			}
			this.table.setFromDate(DateUtil.format(this.entryObject.getFromDate()));
			this.table.setToDate(DateUtil.format(this.entryObject.getToDate()));
		}

		if (this.entryObject.getReportType().equals(ReportType.FORM_TYPES.name())) {

			if (this.entryObject.getOrderBy().equals(OrderBy.HEALTHY_FACILITIES.name())) {

				this.table = DataEntryStatistic.tableByFormAndEncounters(
						dataEntryStatisticService.getAllObsByUsersAndFormAndLocation(this.entryObject.getFromDate(),
								this.entryObject.getToDate(), this.parse(this.entryObject.getLocation())));

				if (!dataEntryStatisticService
						.getAllObsByUsersAndFormAndLocation(this.entryObject.getFromDate(),
								this.entryObject.getToDate(), this.parse(this.entryObject.getLocation()))
						.getData().isEmpty()) {

					final String location = dataEntryStatisticService
							.getAllObsByUsersAndFormAndLocation(this.entryObject.getFromDate(),
									this.entryObject.getToDate(), this.parse(this.entryObject.getLocation()))
							.getData().get(0).getLocation();

					this.table.setLocation(location);

					this.table.setFromDate(DateUtil.format(this.entryObject.getFromDate()));
					this.table.setToDate(DateUtil.format(this.entryObject.getToDate()));
				}

			}

			if (this.entryObject.getOrderBy().equals(OrderBy.DISTRIC.name())) {

				this.table = DataEntryStatistic.tableByFormAndEncounters(dataEntryStatisticService
						.getAllObsByUsersAndForm(this.entryObject.getFromDate(), this.entryObject.getToDate(), null));

				if (!dataEntryStatisticService
						.findObservationsByPeriod(this.entryObject.getFromDate(), this.entryObject.getToDate(), null)
						.getData().isEmpty()) {

					final String location = dataEntryStatisticService
							.findObservationsByPeriod(this.entryObject.getFromDate(), this.entryObject.getToDate(),
									null)
							.getData().get(0).getParentLocation().getName();

					this.table.setFromDate(DateUtil.format(this.entryObject.getFromDate()));
					this.table.setToDate(DateUtil.format(this.entryObject.getToDate()));
					this.table.setLocation(location);
				}

				final List<UserObsByFormType> obsByFormTypes = dataEntryStatisticService
						.getAllObsByUsersAndForm(this.entryObject.getFromDate(), this.entryObject.getToDate(), null)
						.getData();

				if (!obsByFormTypes.isEmpty()) {
					this.table.setLocation(obsByFormTypes.get(0).getParentLocation().getName());
				}

				this.table.setFromDate(DateUtil.format(this.entryObject.getFromDate()));
				this.table.setToDate(DateUtil.format(this.entryObject.getToDate()));

			}
		}

		if (this.entryObject.getReportType().equals(ReportType.MONTHLY_OBS.name())) {

			if (this.entryObject.getOrderBy().equals(OrderBy.HEALTHY_FACILITIES.name())) {

				this.table = DataEntryStatistic.tableByMonthsByObs(dataEntryStatisticService.getAllMonthObsFromLocation(
						this.entryObject.getFromMonth(), DateUtil.getLastDay(this.entryObject.getToMonth()),
						this.parse(this.entryObject.getLocation())));

				if (!dataEntryStatisticService.getAllMonthObsFromLocation(this.entryObject.getFromMonth(),
						DateUtil.getLastDay(this.entryObject.getToMonth()), this.parse(this.entryObject.getLocation()))
						.getData().isEmpty()) {

					final String location = dataEntryStatisticService
							.getAllMonthObsFromLocation(this.entryObject.getFromMonth(),
									DateUtil.getLastDay(this.entryObject.getToMonth()),
									this.parse(this.entryObject.getLocation()))
							.getData().get(0).getLocation();
					this.table.setLocation(location);

					this.table.setFromDate(DateUtil.format(this.entryObject.getFromMonth()));
					this.table.setToDate(DateUtil.format(this.entryObject.getToMonth()));
				}

			}

			if (this.entryObject.getOrderBy().equals(OrderBy.DISTRIC.name())) {

				this.table = DataEntryStatistic.tableByMonthsByObs(dataEntryStatisticService.getAllMonthObs(
						this.entryObject.getFromMonth(), DateUtil.getLastDay(this.entryObject.getToMonth()), null));

				if (!dataEntryStatisticService.getAllMonthObs(this.entryObject.getFromMonth(),
						DateUtil.getLastDay(this.entryObject.getToMonth()), null).getData().isEmpty()) {
					final String location = dataEntryStatisticService
							.getAllMonthObs(this.entryObject.getFromMonth(),
									DateUtil.getLastDay(this.entryObject.getToMonth()), null)
							.getData().get(0).getParentLocation().getName();

					this.table.setFromDate(DateUtil.format(this.entryObject.getFromMonth()));
					this.table.setToDate(DateUtil.format(this.entryObject.getToMonth()));
					this.table.setLocation(location);

				}
				final List<UserObs> userObs = dataEntryStatisticService.getAllMonthObs(this.entryObject.getFromMonth(),
						DateUtil.getLastDay(this.entryObject.getToMonth()), null).getData();

				if (!userObs.isEmpty()) {
					this.table.setLocation(userObs.get(0).getParentLocation().getName());
				}

				this.table.setFromDate(DateUtil.format(this.entryObject.getFromMonth()));
				this.table.setToDate(DateUtil.format(this.entryObject.getToMonth()));

			}

		}

		if (this.entryObject.getReportType().equals(ReportType.FORM_TYPES_OLD.name())) {

			final Date toDateToUse = this.entryObject.getToDate() != null
					? OpenmrsUtil.getLastMomentOfDay(this.entryObject.getToDate())
					: null;
			final String encUserColumn = this.entryObject.getEncUserColumn();
			final String orderUserColumn = this.entryObject.getOrderUserColumn();

			final List<DataEntryStatistic> stats = dataEntryStatisticService.getDataEntryStatistics(
					this.entryObject.getFromDate(), toDateToUse, encUserColumn, orderUserColumn,
					this.entryObject.getGroupBy());

			this.table = DataEntryStatistic.tableByUserAndType(stats, this.entryObject.getHideAverageObs());
			this.table.setFromDate(DateUtil.format(this.entryObject.getFromDate()));
			this.table.setToDate(DateUtil.format(this.entryObject.getToDate()));
			this.table.setLocation("ALL LOCATION");

		}

		if (this.entryObject.getReportType().equals(ReportType.USER_OBS.name())) {

			this.table = DataEntryStatistic.tableByDateAndObsAndLocation(dataEntryStatisticService
					.countObsPerUSerALocation(this.entryObject.getFromDate(), this.entryObject.getToDate()));

			if (!dataEntryStatisticService.countObsPerUSerALocation(entryObject.getFromDate(), entryObject.getToDate())
					.getData().isEmpty()) {

				this.table.setLocation("ALL DISTRICT");

				this.table.setFromDate(DateUtil.format(this.entryObject.getFromDate()));
				this.table.setToDate(DateUtil.format(this.entryObject.getToDate()));

			}
		}
		if (!this.entryObject.getReportType().isEmpty()) {
			this.table.setReportType(this.entryObject.getReportType());
		}
		this.entryObject.setTable(this.table);

		return this.showForm(request, response, errors);
	}

	public DataEntryStatisticService getDataEntryStatisticService() {
		return this.dataEntryStatisticService;
	}

	public Integer parse(final String location) {
		return Integer.parseInt(location);
	}

	public void setDataEntryStatisticService(final DataEntryStatisticService dataEntryStatisticService) {
		this.dataEntryStatisticService = dataEntryStatisticService;
	}

}
