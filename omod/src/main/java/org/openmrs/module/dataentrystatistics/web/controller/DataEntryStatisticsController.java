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

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.Location;
import org.openmrs.Provider;
import org.openmrs.api.context.Context;
import org.openmrs.module.dataentrystatistics.DataEntryStatistic;
import org.openmrs.module.dataentrystatistics.DataEntryStatisticService;
import org.openmrs.module.dataentrystatistics.DataTable;
import org.openmrs.module.dataentrystatistics.PersonObsData;
import org.openmrs.module.dataentrystatistics.web.model.StatisticsCommand;
import org.openmrs.util.OpenmrsUtil;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindException;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;

@SuppressWarnings("deprecation")
@Controller
@RequestMapping(value = "/register")
public class DataEntryStatisticsController extends SimpleFormController {

	protected final Log log = LogFactory.getLog(getClass());

	private List<Location> locations;
	private List<Provider> providers;

	protected void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) throws Exception {
		super.initBinder(request, binder);

		binder.registerCustomEditor(java.util.Date.class,
				new CustomDateEditor(OpenmrsUtil.getDateFormat(Context.getLocale()), true, 10));
	}

	protected Object formBackingObject(HttpServletRequest request) throws ServletException {

		ModelAndView modelAndView = new ModelAndView();

		DataEntryStatisticService svc = (DataEntryStatisticService) Context.getService(DataEntryStatisticService.class);

		PersonObsData data = new PersonObsData();

	
		locations = new ArrayList<Location>();

		providers = new ArrayList<Provider>();

		providers.addAll(svc.findAllProvider());
		locations.addAll(svc.getAllOfLocation());

		modelAndView.addObject("providers", providers);

		return data;
	}

	protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object commandObj,
			BindException errors) throws Exception {

		DataEntryStatisticService svc = (DataEntryStatisticService) Context.getService(DataEntryStatisticService.class);

		StatisticsCommand command = (StatisticsCommand) commandObj;
		Date toDateToUse = command.getToDate() != null ? OpenmrsUtil.getLastMomentOfDay(command.getToDate()) : null;
		String encUserColumn = command.getEncUserColumn();
		String orderUserColumn = command.getOrderUserColumn();
		List<DataEntryStatistic> stats = svc.getDataEntryStatistics(command.getFromDate(), toDateToUse, encUserColumn,
				orderUserColumn, command.getGroupBy());
		DataTable table = DataEntryStatistic.tableByUserAndType(stats, command.getHideAverageObs());
		command.setTable(table);
		return showForm(request, response, errors);
	}

}
