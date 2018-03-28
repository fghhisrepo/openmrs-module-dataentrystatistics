package org.openmrs.module.dataentrystatistics.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {

	public static Date getLastDay(final Date date) {

		final Calendar instance = Calendar.getInstance();
		instance.setTime(date);
		instance.set(Calendar.DAY_OF_MONTH, instance.getActualMaximum(Calendar.DAY_OF_MONTH));

		return instance.getTime();
	}

	public static Date get21Day(final Date date) {

		final Calendar instance = Calendar.getInstance();
		instance.setTime(date);
		instance.set(Calendar.DAY_OF_MONTH, 21);

		return instance.getTime();

	}

	public static Date get20Day(final Date date) {

		final Calendar instance = Calendar.getInstance();
		instance.setTime(date);
		instance.set(Calendar.DAY_OF_MONTH, 20);

		return instance.getTime();

	}

	public static Date getFirstDay(final Date date) {
		final Calendar instance = Calendar.getInstance();

		instance.setTime(date);
		instance.set(Calendar.DAY_OF_MONTH, instance.getActualMinimum(Calendar.DAY_OF_MONTH));

		return instance.getTime();
	}

	public static String format(final Date date) {
		final SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
		return format.format(date);
	}

	public static boolean isWeekEnd(final Date date) {

		final Calendar instance = Calendar.getInstance();
		instance.setTime(date);

		final int dayOfWeek = instance.get(Calendar.DAY_OF_WEEK);

		if ((dayOfWeek == Calendar.SATURDAY) || (dayOfWeek == Calendar.SUNDAY)) {
			return true;
		}

		return false;
	}
}
