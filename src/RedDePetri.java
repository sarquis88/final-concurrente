public class RedDePetri {

    private int[] marcaActual;
    private int[][] incidenciaFront;
    private int[][] incidenciaBack;
    private boolean[] transiciones;
    private int transicionesDisparadas;

    /**
     * Constructor de clase
     * @param marcaInicial marca inicial de la red
     * @param incidenciaFront matriz de incidencia frontal
     * @param incidenciaBack matriz de incidencia trasera
     */
    public RedDePetri(int[] marcaInicial, int[][] incidenciaFront, int[][] incidenciaBack) {

        this.marcaActual = marcaInicial;
        this.incidenciaFront = incidenciaFront;
        this.incidenciaBack = incidenciaBack;
        this.transiciones = new boolean[incidenciaBack[0].length];
        this.transicionesDisparadas = 0;
        actualizarSensibilizadas();
    }

    /**
     * Indica si la transicion esta sensibilizada
     * Metodo PRIVADO
     * @param transicion transicion a analizar
     * @return true si la transicion esta sensibilizada
     */
    private boolean isSensibilizadaInterno(int transicion) {
        for(int i = 0; i < marcaActual.length; i++) {
            if((marcaActual[i] - incidenciaBack[i][transicion]) < 0)
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
                marcaActual[i] = marcaActual[i] + incidenciaFront[i][transicion];
                marcaActual[i] = marcaActual[i] - incidenciaBack[i][transicion];
            }
            this.transicionesDisparadas++;
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

    /**
     * Getter del marcado actual
     * @return vector de enteros del marcado
     */
    public int[] getMarcaActual() {
        return this.marcaActual;
    }

    /**
     * Getter de situacion de transiciones
     * @return lista de booleanos, true para sensibilizada
     */
    public boolean[] getTransiciones() {
        return this.transiciones;
    }

    /**
     * Getter de transiciones disparadas
     * @return transiciones disparadas en int
     */
    public int getTransicionesDisparadas() {
        return this.transicionesDisparadas;
    }
}