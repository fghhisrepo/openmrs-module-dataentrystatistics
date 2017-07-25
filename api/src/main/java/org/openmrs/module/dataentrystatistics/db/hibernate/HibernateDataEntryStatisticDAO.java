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
package org.openmrs.module.dataentrystatistics.db.hibernate;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.openmrs.Role;
import org.openmrs.module.dataentrystatistics.UserObsByMonth;
import org.openmrs.module.dataentrystatistics.UserObsByDate;
import org.openmrs.module.dataentrystatistics.UserObsByFormType;
import org.openmrs.module.dataentrystatistics.db.DataEntryStatisticDAO;

/**
 * Database methods for the DataEntryStatisticService
 */
public class HibernateDataEntryStatisticDAO implements DataEntryStatisticDAO {

	protected Log log = LogFactory.getLog(getClass());

	SessionFactory sessionFactory;

	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	private org.hibernate.Session getCurrentSession() {
		try {
			return sessionFactory.getCurrentSession();
		} catch (NoSuchMethodError ex) {
			try {
				Method method = sessionFactory.getClass().getMethod("getCurrentSession", null);
				return (org.hibernate.Session) method.invoke(sessionFactory, null);
			} catch (Exception e) {
				throw new RuntimeException("Failed to get the current hibernate session", e);
			}
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<UserObsByDate> getAllObsByUsersAndDate(Date fromDate, Date toDate, Integer location) {

		String hql = "SELECT  DATE(o.dateCreated), count(o.obsId), c.username, l.name FROM  Obs o INNER JOIN o.creator c INNER JOIN o.location l  where o.dateCreated BETWEEN :fromDate AND :toDate AND l.locationId =:location AND o.voided = :voided  GROUP BY DATE(o.dateCreated),  c.username ORDER BY DATE(o.dateCreated) ASC ";

		Query query = getCurrentSession().createQuery(hql);
		query.setParameter("fromDate", fromDate);
		query.setParameter("toDate", toDate);
		query.setParameter("location", location);
		query.setParameter("voided", false);

		List<Object[]> list = query.list();

		List<UserObsByDate> userDates = new ArrayList<UserObsByDate>();

		for (Object[] object : list) {
			UserObsByDate userDate = new UserObsByDate();

			userDate.setUser((String) object[2]);
			userDate.setDate((Date) object[0]);
			userDate.setTotalObs((Long) object[1]);
			userDate.setLocation((String) object[3]);

			userDates.add(userDate);
		}

		return userDates;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Role> getAllRoles() {
		String hql = "SELECT  r FROM Role r ";
		Query query = getCurrentSession().createQuery(hql);
		return query.list();

	}

	@SuppressWarnings("unchecked")
	@Override
	public List<UserObsByFormType> getAllObsByUsersAndForm(Date fromDate, Date toDate, Integer location) {

		String hql = "SELECT f.name, c.username, COUNT(DISTINCT e.encounterId), COUNT(o.obsId),  l.name  FROM  Obs o  INNER JOIN o.encounter e INNER JOIN e.form f INNER JOIN e.creator c  INNER JOIN e.location l WHERE e.dateCreated BETWEEN :fromDate AND :toDate AND l.locationId =:location AND o.voided = :voided GROUP BY f.name, c.username";
		Query query = getCurrentSession().createQuery(hql);
		query.setParameter("fromDate", fromDate);
		query.setParameter("toDate", toDate);
		query.setParameter("location", location);
		query.setParameter("voided", false);

		List<Object[]> list = query.list();

		List<UserObsByFormType> userObsByFormTypes = new ArrayList<UserObsByFormType>();
		for (Object[] object : list) {

			UserObsByFormType userObsByFormType = new UserObsByFormType();

			userObsByFormType.setUser((String) object[1]);
			userObsByFormType.setForm(((String) object[0]));
			userObsByFormType.setTotalEncounters((Long) object[2]);
			userObsByFormType.setTotalObs((Long) object[3]);
			userObsByFormType.setLocation((String) object[4]);

			userObsByFormTypes.add(userObsByFormType);
		}

		return userObsByFormTypes;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<UserObsByMonth> getAllMonthObs(Date fromDate, Date toDate, Integer location) {

		String hql = "SELECT  MONTH(o.dateCreated), count(o.obsId), c.username, YEAR(o.dateCreated), DAY(o.dateCreated), l.name FROM  Obs o INNER JOIN o.creator c INNER JOIN o.location l  where o.dateCreated BETWEEN :fromDate AND :toDate AND l.locationId =:location AND  o.voided = :voided  GROUP BY MONTH(o.dateCreated), c.username, YEAR(o.dateCreated)";

		Query query = getCurrentSession().createQuery(hql);
		query.setParameter("fromDate", fromDate);
		query.setParameter("toDate", toDate);
		query.setParameter("location", location);
		query.setParameter("voided", false);

		List<Object[]> list = query.list();

		List<UserObsByMonth> monthObss = new ArrayList<UserObsByMonth>();

		for (Object[] object : list) {

			UserObsByMonth monthObs = new UserObsByMonth();

			monthObs.setUser((String) object[2]);
			monthObs.setDate((Integer) object[0]);
			monthObs.setTotalObs((Long) object[1]);
			monthObs.setYear((Integer) object[3]);
			monthObs.setDay((Integer) object[4]);
			monthObs.setLocation((String) object[5]);

			monthObss.add(monthObs);
		}

		return monthObss;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<UserObsByDate> countTotalObsPerUserAndDate(Date fromDate, Date toDate, Integer location) {

		String hql = "SELECT count(o.obsId), c.username FROM  Obs o INNER JOIN o.creator c INNER JOIN o.location l  where o.dateCreated BETWEEN :fromDate AND :toDate AND l.locationId =:location  GROUP BY c.username";

		Query query = getCurrentSession().createQuery(hql);
		query.setParameter("fromDate", fromDate);
		query.setParameter("toDate", toDate);
		query.setParameter("location", location);

		List<Object[]> list = query.list();

		List<UserObsByDate> userObsByDates = new ArrayList<UserObsByDate>();

		for (Object[] object : list) {
			UserObsByDate userObsByDate = new UserObsByDate();

			userObsByDate.setUser((String) object[1]);
			userObsByDate.setTotalObs((Long) object[0]);

			userObsByDates.add(userObsByDate);
		}

		return userObsByDates;
	}
}
