package org.openmrs.module.dataentrystatistics.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.Before;
import org.junit.Test;

public class DateUtilTest {

	private SimpleDateFormat format;

	@Before
	public void setUp() {
		this.format = new SimpleDateFormat("dd-MM-yyyy");
	}

	@Test
	public void shouldGetTheLastDay() {
		try {
			final Date date = this.format.parse("01-02-2017");
			final Date lastDay = DateUtil.getLastDay(date);
			assertEquals(0, lastDay.compareTo(this.format.parse("28-02-2017")));
		} catch (final ParseException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void shouldGetTheFirstDay() {
		try {
			final Date date = this.format.parse("25-02-2017");
			final Date lastDay = DateUtil.getFirstDay(date);
			assertEquals(0, lastDay.compareTo(this.format.parse("01-02-2017")));
		} catch (final ParseException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void shouldVerifyIfTheDateIsAWeekend() {
		try {
			final Date date = this.format.parse("02-09-2017");
			final boolean value = DateUtil.isWeekEnd(date);
			assertTrue(value);
		} catch (final ParseException e) {
			e.printStackTrace();
		}
	}
}
