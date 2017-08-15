package org.openmrs.module.dataentrystatistics.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.openmrs.module.dataentrystatistics.UserObs;

public class CollectionUtilTest {

	private SimpleDateFormat format;

	@Before
	public void setUp() {
		this.format = new SimpleDateFormat("yyyy-MM-dd");
	}

	@Test
	public void shouldMapToList() {

		final UserObs userObs = new UserObs();
		final String username = "stelio";
		userObs.setUser(username);

		final List<String> users = CollectionUtil.map(Arrays.asList(userObs), "user", String.class);

		assertFalse(users.isEmpty());
		assertEquals(username, users.get(0));
	}

	@Test
	public void shouldGroupToList() {

		final UserObs stelio = new UserObs();
		stelio.setUser("stelio Moiane");
		stelio.setDay(1);

		final UserObs alima = new UserObs();
		alima.setUser("alima Moiane");
		alima.setDay(1);

		final UserObs nailah = new UserObs();
		nailah.setUser("alima Moiane");
		nailah.setDay(2);

		try {
			stelio.setCreationDate(this.format.parse("2017-03-21"));
			alima.setCreationDate(this.format.parse("2017-03-21"));
			nailah.setCreationDate(this.format.parse("2017-04-20"));
		} catch (final ParseException e) {
			e.printStackTrace();
		}

		final List<UserObs> group = CollectionUtil.group(Arrays.asList(stelio, alima, nailah), "creationDate",
				stelio.getCreationDate(), UserObs.class);

		assertEquals(2, group.size());
	}

	@Test
	public void shouldGetDateBetweenTwoDates() {

		Date startDate = null;
		Date enDate = null;

		try {
			startDate = this.format.parse("2017-03-01");
			enDate = this.format.parse("2017-03-10");
		} catch (final ParseException e) {
			e.printStackTrace();
		}

		final List<Date> dates = CollectionUtil.daysBetween(startDate, enDate);

		assertFalse(dates.isEmpty());
		assertEquals(10, dates.size());
	}

}
