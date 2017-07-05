package org.openmrs.module.dataentrystatistics.web.util;

public enum MonthName {
	JANUARY(01), FEBRUARY(02), MARCH(03), APRIL(04), MAY(05), JUNE(06), JULY(07), AUGUST(8), SEPTEMBER(9), OCTOBER(
			10), NOVEMBER(11), DECEMBER(12);
	
	private int valor;

	MonthName(int valor) {
		this.valor = valor;
	}

	public int getValor() {
		return valor;
	}
}
