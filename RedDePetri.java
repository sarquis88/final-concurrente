public class RedDePetri {

    private int[] marcaActual;
    private int[][] matrizDeIncidencia;
    private boolean[] transiciones;

    /**
     * Constructor de clase
     * @param marcaInicial marca inicial de la red
     * @param matrizDeIncidencia matriz de incidencia de la red
     */
    public RedDePetri(int[] marcaInicial, int[][] matrizDeIncidencia) {

        this.marcaActual = marcaInicial;
        this.matrizDeIncidencia = matrizDeIncidencia;

        //TRUE = SENSIBILIZADA, FALSE = NO SENSIBILIZDA
        this.transiciones = new boolean[matrizDeIncidencia[0].length];
        actualizarSensibilizadas();
    }

    /**
     * Getter de transiciones
     * @return boolean con las transiciones
     */
    public boolean[] getTransiciones() {
        return transiciones;
    }

    /**
     * Indica si la transicion esta sensibilizada
     * Metodo PRIVADO
     * @param transicion transicion a analizar
     * @return true si la transicion esta sensibilizada
     */
    private boolean isSensibilizadaInterno(int transicion) {
        for(int i = 0; i < marcaActual.length; i++) {
            if((marcaActual[i] + matrizDeIncidencia[i][transicion]) < 0)
                return false;
        }
        return true;
    }

    /**
     * Disparo de transicion en red de Petri, modificando la marca de la red
     * La transicion debe estar sensibilizada
     * @param transicion transicion a disparar
     */
    public void disparar(int transicion) {
        if (this.isSensibilizada(transicion)) {
            for (int i = 0; i < marcaActual.length; i++) {
                marcaActual[i] = marcaActual[i] + matrizDeIncidencia[i][transicion];
            }
            actualizarSensibilizadas();
        }
    }

    /**
     * Actualizacion de transiciones sensibilizadas
     */
    public void actualizarSensibilizadas() {
        for (int i = 0; i < transiciones.length ; i++) {
            this.transiciones[i] = isSensibilizadaInterno(i);
        }
    }

    /**
     * Indica si la transicion esta sensibilizada
     * Metodo PUBLICO llamado por monitor
     * @param transicion transicion a analizar
     * @return true si la transicion esta sensibilizada
     */
    public boolean isSensibilizada(int transicion) {
        return (this.transiciones[transicion]);
    }
}