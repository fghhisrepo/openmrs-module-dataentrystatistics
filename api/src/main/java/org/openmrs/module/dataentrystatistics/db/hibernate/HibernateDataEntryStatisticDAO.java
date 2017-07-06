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
import org.openmrs.module.dataentrystatistics.CalculateUserDateForObsCollectedByUser;
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
	public List<CalculateUserDateForObsCollectedByUser> getAllObsByUsersAndDate(Date fromDate, Date toDate, Integer location) {

		String hql = "SELECT  DATE(o.dateCreated), count(o.obsId), u.username FROM  Obs o INNER JOIN o.creator u INNER JOIN o.location l  where o.dateCreated BETWEEN :fromDate AND :toDate AND l.locationId =:location  GROUP BY DATE(o.dateCreated),  u.username ORDER BY DATE(o.dateCreated) ASC ";

		Query query = getCurrentSession().createQuery(hql);
		query.setParameter("fromDate", fromDate);
		query.setParameter("toDate", toDate);
		query.setParameter("location", location);

		List<Object[]> list = query.list();

		List<CalculateUserDateForObsCollectedByUser> userDates = new ArrayList<CalculateUserDateForObsCollectedByUser>();

		for (Object[] object : list) {
			CalculateUserDateForObsCollectedByUser userDate = new CalculateUserDateForObsCollectedByUser();

			userDate.setUser((String) object[2]);
			userDate.setDate((Date) object[0]);
			userDate.setTotalObs((Long) object[1]);

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
}
