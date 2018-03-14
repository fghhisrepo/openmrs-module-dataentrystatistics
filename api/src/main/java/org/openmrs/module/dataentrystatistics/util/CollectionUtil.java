package org.openmrs.module.dataentrystatistics.util;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

public class CollectionUtil {

	public static <T> List<T> map(final Collection<?> collection, final String key, final Class<T> classType) {

		final List<T> list = new ArrayList<T>();

		for (final Object object : collection) {

			final Object fieldValue = getFieldValue(key, object);
			list.add(classType.cast(fieldValue));
		}

		return list;
	}

	public static <T> List<T> mapWithoutDuplication(final Collection<?> collection, final String key,
			final Class<T> classType) {

		final List<T> list = new ArrayList<T>();

		for (final Object object : collection) {

			final Object fieldValue = getFieldValue(key, object);

			if (!list.contains(fieldValue)) {
				list.add(classType.cast(fieldValue));
			}
		}

		return list;
	}

	public static <T> List<T> group(final Collection<?> collection, final String key, final Object value,
			final Class<T> classType) {

		final List<T> list = new ArrayList<T>();

		if (value instanceof Date) {

			for (final Object object : collection) {
				final Date fieldValue = (Date) getFieldValue(key, object);

				if (isEqualDate((Date) value, fieldValue)) {
					list.add(classType.cast(object));
				}
			}

			return list;
		}

		for (final Object object : collection) {

			final Object fieldValue = getFieldValue(key, object);

			if (fieldValue.equals(value)) {
				list.add(classType.cast((object)));
			}
		}

		return list;
	}

	@SuppressWarnings("unchecked")
	private static <E> E getFieldValue(final String key, final E object) {
		Object value = null;

		try {

			final Field field = object.getClass().getDeclaredField(key);
			field.setAccessible(true);
			value = field.get(object);

		} catch (final Exception e) {
			e.printStackTrace();
		}

		return (E) value;
	}

	private static boolean isEqualDate(final Date date1, final Date date2) {
		return date1.compareTo(date2) == 0;
	}

	public static List<Date> daysBetween(final Date startDate, final Date enDate) {

		final Calendar calendar = new GregorianCalendar();
		calendar.setTime(startDate);

		final List<Date> dates = new ArrayList<Date>();

		while (calendar.getTime().getTime() <= enDate.getTime()) {
			dates.add(calendar.getTime());
			calendar.add(Calendar.DATE, 1);
		}

		return dates;
	}

}
