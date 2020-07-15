package concurrente;

public class Politica {

	int[] prioridades;

	/**
	 * Constructor de clase
	 * @param prioridades prioridades a aplicar
	 */
	public Politica(int[] prioridades) {
		this.prioridades = prioridades;
	}

	/**
	 * Retorna la transicion con mayor prioridad a despertar
	 * @param transicionesListas vector de transiciones transicionesListas
	 * @return transicion sensibilizada con mayor prioridad (menor numero),
	 * 			-1 si no hay ninguna transicion sensibilizada
	 */
	public int getTransicionADespertar(int[] transicionesListas) {
		int transicionADespertar = -1;
		int prioridadActual = 99;
		for (int i = 0; i < transicionesListas.length; i++){
			if(transicionesListas[i] == 1) {
				if(this.prioridades[i] < prioridadActual) {
					transicionADespertar = i;
					prioridadActual = this.prioridades[i];
				}
			}
		}
		return transicionADespertar;
	}

	/**
	 * Cambio de prioridades entre dos transiciones
	 * @param sw1 transicion 1
	 * @param sw2 transicion 2
	 */
	public void switchPrioridades(int sw1, int sw2) {
		int prioridad1 = this.prioridades[sw1];
		int prioridad2 = this.prioridades[sw2];

		this.prioridades[sw1] = prioridad2;
		this.prioridades[sw2] = prioridad1;
	}
}

