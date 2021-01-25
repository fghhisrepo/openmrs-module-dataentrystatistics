package org.openmrs.module.dataentrystatistics.util;

public enum Month {

	JANEIRO(1), FEVEREIRO(2), MARCO(3), ABRIL(4), MAIO(5), JUNHO(6), JULHO(7), AGOSTO(8), SETEMBRO(9), OUTUBRO(
			10), NOVEMBRO(11), DEZEMBRO(12);

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
