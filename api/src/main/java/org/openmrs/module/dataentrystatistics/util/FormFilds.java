package org.openmrs.module.dataentrystatistics.util;

public enum FormFilds {

	ADULTO_PROCESSO_PARTE_A_ANAMNESE(104), ADULTO_PROCESSO_PARTE_B_EXAME_CLINICO(69), ADULTO_SEGUIMENTO(
			96), CONSENTIMENTO_DE_TESTE_DE_CASO_INDICE(5);

	private int totalFilds;

	FormFilds(int totalFilds) {
		this.setTotalFilds(totalFilds);
	}

	public int getTotalFilds() {
		return totalFilds;
	}

	public void setTotalFilds(int totalFilds) {
		this.totalFilds = totalFilds;
	}
}
