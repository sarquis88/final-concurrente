public class RedDePetri {

    private int[] marcaActual;
    private int[][] incidenciaFront;
    private int[][] incidenciaBack;
    private boolean[] transiciones;

    private int cantidadTransicionesDisparadas;
    private boolean isPInvariantesCorrecto;
    private String ordenTransicionesDisparadas;

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
        this.cantidadTransicionesDisparadas = 0;
        this.ordenTransicionesDisparadas = "";
        this.isPInvariantesCorrecto = true;
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
            this.cantidadTransicionesDisparadas++;

            if(this.isPInvariantesCorrecto)
                this.isPInvariantesCorrecto = isPInvariantesCorrecto();

            this.ordenTransicionesDisparadas = this.ordenTransicionesDisparadas.concat(transicion + "-");

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
    public int getCantidadTransicionesDisparadas() {
        return this.cantidadTransicionesDisparadas;
    }

    /**
     * Chequea el estado de las P-Invariantes
     * @return true en caso de estado correcto, de lo contrario false
     */
    private boolean isPInvariantesCorrecto() {
        return (    this.marcaActual[0] + this.marcaActual[1] == 1                          &&
                    this.marcaActual[2] + this.marcaActual[3] + this.marcaActual[5] == 1    &&
                    this.marcaActual[7] + this.marcaActual[8] == 1                          &&
                    this.marcaActual[9] + this.marcaActual[10] + this.marcaActual[12] == 1  &&
                    this.marcaActual[14] + this.marcaActual[15] == 1                        );
    }

    /**
     * Getter del estado de las p-invariantes
     * @return false si en algun momento las p-invariantes no se cumplieron, de lo contrario true
     */
    public boolean getIsPInvariantesCorrecto() {
        return this.isPInvariantesCorrecto;
    }

    /**
     * Getter del orden de las transiciones disparadas
     * @return string que posee dicho orden
     */
    public String getOrdenTransicionesDisparadas() {
        return this.ordenTransicionesDisparadas;
    }
}