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
import org.openmrs.module.dataentrystatistics.UserObs;
import org.openmrs.module.dataentrystatistics.UserObsByDate;
import org.openmrs.module.dataentrystatistics.UserObsByFormType;
import org.openmrs.module.dataentrystatistics.db.DataEntryStatisticDAO;
import org.openmrs.module.dataentrystatistics.model.ReportData;

/**
 * Database methods for the DataEntryStatisticService
 */
public class HibernateDataEntryStatisticDAO implements DataEntryStatisticDAO {

	protected Log log = LogFactory.getLog(this.getClass());

	SessionFactory sessionFactory;

	public SessionFactory getSessionFactory() {
		return this.sessionFactory;
	}

	public void setSessionFactory(final SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	private org.hibernate.Session getCurrentSession() {
		try {
			return this.sessionFactory.getCurrentSession();
		} catch (final NoSuchMethodError ex) {
			try {
				final Method method = this.sessionFactory.getClass().getMethod("getCurrentSession", null);
				return (org.hibernate.Session) method.invoke(this.sessionFactory, null);
			} catch (final Exception e) {
				throw new RuntimeException("Failed to get the current hibernate session", e);
			}
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public ReportData<UserObsByDate> findObservationsByPeriodAndLocation(final Date fromDate, final Date toDate,
			final Integer location) {

		final String hql = "SELECT DATE(o.dateCreated), COUNT(o.obsId), c.username, l.name FROM Obs o "
				+ "INNER JOIN o.creator c INNER JOIN o.location l "
				+ "WHERE DATE(o.dateCreated) BETWEEN :fromDate AND :toDate AND l.locationId =:location "
				+ "AND o.voided = :voided GROUP BY DATE(o.dateCreated), c.username ORDER BY DATE(o.dateCreated) ASC";

		final Query query = this.getCurrentSession().createQuery(hql);

		query.setParameter("fromDate", fromDate);
		query.setParameter("toDate", toDate);
		query.setParameter("location", location);
		query.setParameter("voided", false);

		final List<Object[]> list = query.list();

		final ReportData<UserObsByDate> reportData = new ReportData<UserObsByDate>(null, null, null, fromDate, toDate);

		for (final Object[] object : list) {
			final UserObsByDate userDate = new UserObsByDate();

			userDate.setUser((String) object[2]);
			userDate.setDate((Date) object[0]);
			userDate.setTotalObs((Long) object[1]);
			userDate.setLocation((String) object[3]);

			reportData.addData(userDate);
		}

		return reportData;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Role> getAllRoles() {
		final String hql = "SELECT  r FROM Role r ";
		final Query query = this.getCurrentSession().createQuery(hql);
		return query.list();

	}

	@SuppressWarnings("unchecked")
	@Override
	public List<UserObsByFormType> getAllObsByUsersAndForm(final Date fromDate, final Date toDate,
			final Integer location) {

		final String hql = "SELECT f.name, c.username, COUNT(DISTINCT e.encounterId), COUNT(o.obsId),  l.name  FROM  Obs o  INNER JOIN o.encounter e INNER JOIN e.form f INNER JOIN e.creator c  INNER JOIN e.location l WHERE e.dateCreated BETWEEN :fromDate AND :toDate AND l.locationId =:location AND o.voided = :voided GROUP BY f.name, c.username";
		final Query query = this.getCurrentSession().createQuery(hql);
		query.setParameter("fromDate", fromDate);
		query.setParameter("toDate", toDate);
		query.setParameter("location", location);
		query.setParameter("voided", false);

		final List<Object[]> list = query.list();

		final List<UserObsByFormType> userObsByFormTypes = new ArrayList<UserObsByFormType>();
		for (final Object[] object : list) {

			final UserObsByFormType userObsByFormType = new UserObsByFormType();

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
	public List<UserObs> getAllMonthObs(final Date fromDate, final Date toDate, final Integer location) {

		final String hql = "SELECT c.username, l.name, MONTH(o.dateCreated), COUNT(o.obsId) FROM Obs o "
				+ "INNER JOIN o.creator c INNER JOIN o.location l "
				+ "WHERE DATE(o.dateCreated) BETWEEN :fromDate AND :toDate AND l.locationId =:location "
				+ "AND o.voided = :voided GROUP BY MONTH(o.dateCreated), c.username "
				+ "ORDER BY MONTH(o.dateCreated), c.username ASC";

		final Query query = this.getCurrentSession().createQuery(hql);
		query.setParameter("fromDate", fromDate);
		query.setParameter("toDate", toDate);
		query.setParameter("location", location);
		query.setParameter("voided", false);

		final List<Object[]> list = query.list();

		final List<UserObs> monthObss = new ArrayList<UserObs>();

		for (final Object[] object : list) {

			final UserObs userObs = new UserObs();

			userObs.setUser((String) object[0]);
			userObs.setLocation((String) object[1]);
			userObs.setDate((Integer) object[2]);
			userObs.setTotalObs((Long) object[3]);

			monthObss.add(userObs);
		}

		return monthObss;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<UserObsByDate> countTotalObsPerUserAndDate(final Date fromDate, final Date toDate,
			final Integer location) {

		final String hql = "SELECT count(o.obsId), c.username FROM  Obs o INNER JOIN o.creator c INNER JOIN o.location l  where o.dateCreated BETWEEN :fromDate AND :toDate AND l.locationId =:location  GROUP BY c.username";

		final Query query = this.getCurrentSession().createQuery(hql);
		query.setParameter("fromDate", fromDate);
		query.setParameter("toDate", toDate);
		query.setParameter("location", location);

		final List<Object[]> list = query.list();

		final List<UserObsByDate> userObsByDates = new ArrayList<UserObsByDate>();

		for (final Object[] object : list) {
			final UserObsByDate userObsByDate = new UserObsByDate();

			userObsByDate.setUser((String) object[1]);
			userObsByDate.setTotalObs((Long) object[0]);

			userObsByDates.add(userObsByDate);
		}

		return userObsByDates;
	}
}
