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
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.api.context.Context;
import org.openmrs.module.dataentrystatistics.DataEntryStatisticService;
import org.openmrs.module.dataentrystatistics.UserDate;
import org.openmrs.module.dataentrystatistics.web.model.StatisticsCommand;
import org.openmrs.util.OpenmrsUtil;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.servlet.mvc.SimpleFormController;

@SuppressWarnings("deprecation")
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

	@SuppressWarnings("rawtypes")
	protected Map referenceData(HttpServletRequest request) throws Exception {

		Calendar c = Calendar.getInstance();
		c.set(2016, 7, 01);

		Calendar c1 = Calendar.getInstance();
		c1.set(2016, 7, 31);

		DataEntryStatisticService svc = Context.getService(DataEntryStatisticService.class);

		Set<String> nomes = new HashSet<>();

		Map<String, Long> maps = new HashMap<>();

		Map<Date, String> m = new HashMap<>();

		List<UserDate> userDates = svc.getAllObsByUsersAndDate(c.getTime(), c1.getTime());

		for (UserDate userDate : userDates) {

			nomes.add(userDate.getUser());
			maps.put(userDate.getUser(), userDate.getTotalObs());
			m.put(userDate.getDate(), userDate.getUser());
			
			Long [] positions = new Long[3];
			
			for (int i = 0; i < positions.length; i++) {
				
			}

		}

		request.setAttribute("nomes", nomes);
		request.setAttribute("m", m);

		return modelMap;
	}

}