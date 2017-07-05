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
import java.util.Calendar;
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
import org.openmrs.module.dataentrystatistics.web.model.StatisticsCommand;
import org.openmrs.util.OpenmrsUtil;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindException;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;

@SuppressWarnings({ "rawtypes", "deprecation" })
public class DataEntryStatisticsController extends SimpleFormController {

	protected final Log log = LogFactory.getLog(getClass());
	private ModelMap modelMap = new ModelMap();

	protected void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) throws Exception {
		super.initBinder(request, binder);

		binder.registerCustomEditor(java.util.Date.class,
				new CustomDateEditor(OpenmrsUtil.getDateFormat(Context.getLocale()), true, 10));
	}

	protected Object formBackingObject(HttpServletRequest request) throws ServletException {

		return new StatisticsCommand();

	}

	protected Map referenceData(HttpServletRequest request) throws Exception {

		DataEntryStatisticService svc = (DataEntryStatisticService) Context.getService(DataEntryStatisticService.class);

		List<Integer> years = new ArrayList<>();

		List<Integer> months = new ArrayList<>();

		List<Role> roles = svc.getAllRoles();

		for (int x = 2000; x <= 2017; x++) {
			years.add(x);
		}
		for (int x = 1; x <= 31; x++) {
			months.add(x);
		}
		
		request.setAttribute("roles", roles);
		request.setAttribute("years", years);
		request.setAttribute("months", months);

		return modelMap;
	}

	public Calendar VerifyNullAtributesFromObject(StatisticsCommand command) {
	
		Calendar getDataForYearAndMonth = Calendar.getInstance();

		if (command.getYear() != null && command.getMonth() != null) {

			getDataForYearAndMonth.set(command.getYear(), command.getMonth());
		}
		return getDataForYearAndMonth;
	}

	protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object commandObj,
			BindException errors) throws Exception {

		DataEntryStatisticService svc = (DataEntryStatisticService) Context.getService(DataEntryStatisticService.class);

		StatisticsCommand command = (StatisticsCommand) commandObj;

		int locationId = Integer.parseInt(command.getLocation());

		DataTable table = DataEntryStatistic
				.tableByDateAndObs(svc.getAllObsByUsersAndDate(command.getFromDate(), command.getToDate(), locationId));

		command.setTable(table);
		return showForm(request, response, errors);
	}

}