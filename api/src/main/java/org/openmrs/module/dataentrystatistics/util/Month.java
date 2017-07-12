package org.openmrs.module.dataentrystatistics.util;

public enum Month {

	JANUARY(1), FEBRUARY(2), MARCH(3), APRIL(4), MAY(5), JUNE(6), JULY(7), AUGUST(8), SEPTEMBER(9), OCTOBER(
			10), NOVEMBER(11), DECEMBER(12);

	private int value;

	Month(int valor) {
		this.value = valor;
	}

	public int getValue() {
		return value;
	}

	public static String getMonthName(Integer code) {
		if (code == null)
			return null;
		for (Month month : Month.values()) {

			if (month.getValue() == (code))

				return month.name();
		}
		return null;
	}
}
