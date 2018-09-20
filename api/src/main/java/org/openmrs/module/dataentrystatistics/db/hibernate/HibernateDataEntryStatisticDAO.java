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
import org.openmrs.EncounterType;
import org.openmrs.Form;
import org.openmrs.Location;
import org.openmrs.Person;
import org.openmrs.Role;
import org.openmrs.User;
import org.openmrs.api.db.DAOException;
import org.openmrs.module.dataentrystatistics.DataEntryStatistic;
import org.openmrs.module.dataentrystatistics.UserObs;
import org.openmrs.module.dataentrystatistics.UserObsByDate;
import org.openmrs.module.dataentrystatistics.UserObsByFormType;
import org.openmrs.module.dataentrystatistics.UserObsLocation;
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
				+ "WHERE DATE(o.dateCreated) BETWEEN :fromDate AND :toDate AND l.locationId =:location AND c.username IS NOT null "
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
	public List<Location> getAllLocations() {
		final String hql = "SELECT  l FROM Locationl ";
		final Query query = this.getCurrentSession().createQuery(hql);
		return query.list();

	}

	@SuppressWarnings("unchecked")
	@Override
	public ReportData<UserObsByFormType> getAllObsByUsersAndFormAndLocation(final Date fromDate, final Date toDate,
			final Integer location) {

		final String hql = "SELECT f.name, c.username, COUNT(DISTINCT e.encounterId), COUNT(o.obsId),  l.name   FROM  Obs o "
				+ "INNER JOIN o.encounter e INNER JOIN e.form f INNER JOIN e.creator c  INNER JOIN e.location l "
				+ "WHERE DATE(o.dateCreated) >= :fromDate AND DATE(o.dateCreated) <= :toDate AND  l.locationId =:location AND c.username IS NOT null "
				+ "AND o.voided = :voided GROUP BY f.name, c.username";

		final Query query = this.getCurrentSession().createQuery(hql);
		query.setParameter("fromDate", fromDate);
		query.setParameter("toDate", toDate);
		query.setParameter("location", location);
		query.setParameter("voided", false);

		final List<Object[]> list = query.list();

		final ReportData<UserObsByFormType> reportData = new ReportData<UserObsByFormType>(null, null, null, fromDate,
				toDate);
		for (final Object[] object : list) {

			final UserObsByFormType userObsByFormType = new UserObsByFormType();

			userObsByFormType.setUser((String) object[1]);
			userObsByFormType.setForm(((String) object[0]));
			userObsByFormType.setTotalEncounters((Long) object[2]);
			userObsByFormType.setTotalObs((Long) object[3]);
			userObsByFormType.setLocation((String) object[4]);

			reportData.addData(userObsByFormType);
		}

		return reportData;

	}

	@SuppressWarnings("unchecked")
	@Override
	public ReportData<UserObs> getAllMonthObsFromLocation(final Date fromDate, final Date toDate,
			final Integer location) {

		final String hql = "SELECT c.username, l.name, MONTH(o.dateCreated), COUNT(o.obsId) FROM Obs o "
				+ "INNER JOIN o.creator c INNER JOIN o.location l "
				+ "WHERE DATE(o.dateCreated) >= :fromDate AND DATE(o.dateCreated) <= :toDate "
				+ "AND l.locationId =:location AND c.username IS NOT NULL "
				+ "AND o.voided = :voided GROUP BY MONTH(o.dateCreated), c.username "
				+ "ORDER BY MONTH(o.dateCreated), c.username ASC";

		final Query query = this.getCurrentSession().createQuery(hql);
		query.setParameter("fromDate", fromDate);
		query.setParameter("toDate", toDate);
		query.setParameter("location", location);
		query.setParameter("voided", false);

		final List<Object[]> list = query.list();

		final ReportData<UserObs> reportData = new ReportData<UserObs>(null, null, null, fromDate, toDate);

		for (final Object[] object : list) {

			final UserObs userObs = new UserObs();

			userObs.setUser((String) object[0]);
			userObs.setLocation((String) object[1]);
			userObs.setDate((Integer) object[2]);
			userObs.setTotalObs((Long) object[3]);

			reportData.addData(userObs);
		}

		return reportData;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<UserObsByDate> countTotalObsPerUserAndDate(final Date fromDate, final Date toDate,
			final Integer location) {

		final String hql = "SELECT COUNT(o.obsId), c.username FROM  Obs o "
				+ "INNER JOIN o.creator c INNER JOIN o.location l WHERE "
				+ "DATE(o.dateCreated) >= :fromDate AND DATE(o.dateCreated) <= :toDate AND l.locationId =:location AND c.username IS NOT null "
				+ "GROUP BY c.username";

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

	@Override
	public ReportData<UserObsByDate> findObservationsByPeriod(final Date fromDate, final Date toDate,
			final Integer location) {
		final String hql = "SELECT DATE(o.dateCreated), COUNT(o.obsId), c.username,  l.parentLocation FROM Obs o "
				+ "INNER JOIN o.creator c INNER JOIN o.location l "
				+ "WHERE DATE(o.dateCreated) BETWEEN :fromDate AND :toDate AND o.voided = :voided AND c.username IS NOT null "
				+ "GROUP BY DATE(o.dateCreated), c.username " + "ORDER BY DATE(o.dateCreated) ASC ";

		final Query query = this.getCurrentSession().createQuery(hql);

		query.setParameter("fromDate", fromDate);
		query.setParameter("toDate", toDate);
		query.setParameter("voided", false);

		@SuppressWarnings("unchecked")
		final List<Object[]> list = query.list();

		final ReportData<UserObsByDate> reportData = new ReportData<UserObsByDate>(null, null, null, null, fromDate,
				toDate);

		for (final Object[] object : list) {
			final UserObsByDate userDate = new UserObsByDate();

			userDate.setUser((String) object[2]);
			userDate.setDate((Date) object[0]);
			userDate.setTotalObs((Long) object[1]);
			userDate.setParentLocation((Location) object[3]);

			reportData.addData(userDate);
		}

		return reportData;
	}

	@SuppressWarnings("unchecked")
	@Override
	public ReportData<UserObsByFormType> getAllObsByUsersAndForm(final Date fromDate, final Date toDate,
			final Integer location) {

		final String hql = "SELECT f.name, c.username, COUNT(DISTINCT e.encounterId), COUNT(o.obsId),  l.parentLocation FROM  Obs o "
				+ "INNER JOIN o.encounter e INNER JOIN e.form f INNER JOIN e.creator c  INNER JOIN e.location l "
				+ "WHERE DATE(e.dateCreated) >= :fromDate AND DATE(e.dateCreated) <= :toDate AND e.voided = :voided AND c.username IS NOT null "
				+ "GROUP BY f.name, c.username";
		final Query query = this.getCurrentSession().createQuery(hql);
		query.setParameter("fromDate", fromDate);
		query.setParameter("toDate", toDate);
		query.setParameter("voided", false);

		final List<Object[]> list = query.list();
		final ReportData<UserObsByFormType> reportData = new ReportData<UserObsByFormType>(null, null, null, fromDate,
				toDate);
		for (final Object[] object : list) {

			final UserObsByFormType userObsByFormType = new UserObsByFormType();

			userObsByFormType.setUser((String) object[1]);
			userObsByFormType.setForm(((String) object[0]));
			userObsByFormType.setTotalEncounters((Long) object[2]);
			userObsByFormType.setTotalObs((Long) object[3]);
			userObsByFormType.setParentLocation((Location) object[4]);

			reportData.addData(userObsByFormType);
		}

		return reportData;

	}

	@Override
	public ReportData<UserObs> getAllMonthObs(final Date fromDate, final Date toDate, final Integer location) {

		final String hql = "SELECT c.username,  l.parentLocation, MONTH(o.dateCreated), COUNT(o.obsId) FROM Obs o "
				+ "INNER JOIN o.creator c INNER JOIN o.location l "
				+ "WHERE DATE(o.dateCreated) >= :fromDate AND DATE(o.dateCreated) <= :toDate  AND c.username IS NOT NULL "
				+ "AND o.voided = :voided GROUP BY MONTH(o.dateCreated), c.username "
				+ "ORDER BY MONTH(o.dateCreated), c.username ASC";

		final Query query = this.getCurrentSession().createQuery(hql);
		query.setParameter("fromDate", fromDate);
		query.setParameter("toDate", toDate);
		query.setParameter("voided", false);

		@SuppressWarnings("unchecked")
		final List<Object[]> list = query.list();

		final ReportData<UserObs> reportData = new ReportData<UserObs>(null, null, null, fromDate, toDate);

		for (final Object[] object : list) {

			final UserObs userObs = new UserObs();

			userObs.setUser((String) object[0]);
			userObs.setParentLocation((Location) object[1]);
			userObs.setDate((Integer) object[2]);
			userObs.setTotalObs((Long) object[3]);

			reportData.addData(userObs);
		}

		return reportData;

	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public List<DataEntryStatistic> getDataEntryStatistics(final Date fromDate, final Date toDate,
			String encounterColumn, String orderColumn, String groupBy) throws DAOException {

		if (encounterColumn == null) {
			encounterColumn = "creator";
		}
		encounterColumn = encounterColumn.toLowerCase();

		final List<DataEntryStatistic> ret = new ArrayList<DataEntryStatistic>();

		// data entry stats with extended info
		// check if there's anything else to group by
		if (groupBy == null) {
			groupBy = "";
		}
		if (groupBy.length() != 0) {
			groupBy = "e." + groupBy + ", ";
		}
		this.log.debug("GROUP BY IS " + groupBy);

		String hql = "select " + groupBy + "e." + encounterColumn + ", e.encounterType"
				+ ", e.form, count(distinct e.encounterId), count(o.obsId) "
				+ "from Obs o right join o.encounter as e ";
		if ((fromDate != null) || (toDate != null)) {
			String s = "where ";
			if (fromDate != null) {
				s += "e.dateCreated >= :fromDate ";
			}
			if (toDate != null) {
				if (fromDate != null) {
					s += "and ";
				}
				s += "e.dateCreated <= :toDate ";
			}
			hql += s;
		}

		// remove voided obs and encounters.
		if ((fromDate != null) || (toDate != null)) {
			hql += " and ";
		} else {
			hql += " where ";
		}
		hql += " e.voided = :voided and o.voided = :voided ";

		hql += "group by ";
		if (groupBy.length() > 0) {
			hql += groupBy + " ";
		}
		hql += "e." + encounterColumn + ", e.encounterType, e.form ";
		Query q = this.getCurrentSession().createQuery(hql);
		if (fromDate != null) {
			q.setParameter("fromDate", fromDate);
		}
		if (toDate != null) {
			q.setParameter("toDate", toDate);
		}

		q.setParameter("voided", false);

		List<Object[]> l = q.list();
		for (final Object[] holder : l) {
			final DataEntryStatistic s = new DataEntryStatistic();
			int offset = 0;
			if (groupBy.length() > 0) {
				s.setGroupBy(holder[0]);
				offset = 1;
			}

			final Object temp = holder[0 + offset];
			if (temp instanceof User) {
				s.setUser(((User) temp).getPerson());
			} else {
				s.setUser((Person) holder[0 + offset]);
			}
			final EncounterType encType = ((EncounterType) holder[1 + offset]);
			final Form form = ((Form) holder[2 + offset]);
			s.setEntryType(form != null ? form.getName() : (encType != null ? encType.getName() : "null"));
			final int numEncounters = ((Number) holder[3 + offset]).intValue();
			final int numObs = ((Number) holder[4 + offset]).intValue();
			s.setNumberOfEntries(numEncounters); // not sure why this comes out
													// as a Long instead of an
													// Integer
			this.log.debug("NEW Num encounters is " + numEncounters);
			s.setNumberOfObs(numObs);
			this.log.debug("NEW Num obs is " + numObs);
			ret.add(s);
		}

		// default userColumn to creator
		if (orderColumn == null) {
			orderColumn = "creator";
		}
		orderColumn = orderColumn.toLowerCase();

		// for orders, count how many were created. (should eventually count
		// something with voided/changed)
		hql = "select o." + orderColumn + ", o.orderType.name, count(*) " + "from Order o ";
		if ((fromDate != null) || (toDate != null)) {
			String s = "where ";
			if (fromDate != null) {
				s += "o.dateCreated >= :fromDate ";
			}
			if (toDate != null) {
				if (fromDate != null) {
					s += "and ";
				}
				s += "o.dateCreated <= :toDate ";
			}
			hql += s;
		}

		// remove voided orders.
		if ((fromDate != null) || (toDate != null)) {
			hql += " and ";
		} else {
			hql += " where ";
		}
		hql += " o.voided = :voided ";

		hql += "group by o." + orderColumn + ", o.orderType.name ";
		q = this.getCurrentSession().createQuery(hql);
		if (fromDate != null) {
			q.setParameter("fromDate", fromDate);
		}
		if (toDate != null) {
			q.setParameter("toDate", toDate);
		}

		q.setParameter("voided", false);

		l = q.list();
		for (final Object[] holder : l) {
			final DataEntryStatistic s = new DataEntryStatistic();
			final Object temp = holder[0];
			if (temp instanceof User) {
				s.setUser(((User) temp).getPerson());
			} else {
				s.setUser((Person) temp);
			}
			s.setEntryType((String) holder[1]);
			s.setNumberOfEntries(((Number) holder[2]).intValue()); // not sure
																	// why this
																	// comes out
																	// as a Long
																	// instead
																	// of an
																	// Integer
			s.setNumberOfObs(0);
			ret.add(s);
		}

		return ret;
	}

	@Override
	public String findLocationByID(final Integer locationId) {
		final String hql = "SELECT  l FROM Location l WHERE l.locationId=:locationId";
		final Query query = this.getCurrentSession().createQuery(hql);
		query.setParameter("locationId", locationId);

		return query.uniqueResult().toString();

	}

	@Override
	public ReportData<UserObsLocation> countObsPerUSerALocation(Date fromDate, Date toDate) {
		final String hql = "SELECT l.name, COUNT(o.obsId), c.username FROM Obs o "
				+ "INNER JOIN o.creator c INNER JOIN o.location l "
				+ "WHERE DATE(o.dateCreated) BETWEEN :fromDate AND :toDate AND o.voided = :voided AND c.username IS NOT null "
				+ "GROUP BY l.name, c.username " + "ORDER BY l.name ";

		final Query query = this.getCurrentSession().createQuery(hql);

		query.setParameter("fromDate", fromDate);
		query.setParameter("toDate", toDate);
		query.setParameter("voided", false);

		@SuppressWarnings("unchecked")
		final List<Object[]> list = query.list();

		final ReportData<UserObsLocation> reportData = new ReportData<UserObsLocation>(null, null, null, null, fromDate,
				toDate);

		for (final Object[] object : list) {
			final UserObsLocation userObsLocation = new UserObsLocation();

			userObsLocation.setUser((String) object[2]);
			userObsLocation.setTotalObs((Long) object[1]);
			userObsLocation.setLocation(((String) object[0]));

			reportData.addData(userObsLocation);
		}

		return reportData;
	}

}
