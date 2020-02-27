public class RedDePetri {

    private int[] marcaActual;
    private int[] vectorDesInhibidor;
    private int[] transiciones;
    private int[][] incidenciaFront;
    private int[][] incidenciaBack;
    private int[][] matrizInhibidora;

    private int cantidadTransicionesDisparadas;
    private boolean isPInvariantesCorrecto;

    private String ordenTransicionesDisparadas;

    /**
     * Constructor de clase
     * @param marcaInicial marca inicial de la red
     * @param incidenciaFront matriz de incidencia frontal
     * @param incidenciaBack matriz de incidencia trasera
     * @param matrizInhibidora matriz de arcos inhibidores
     */
    public RedDePetri(int[] marcaInicial, int[][] incidenciaFront, int[][] incidenciaBack, int[][] matrizInhibidora) {

        this.marcaActual = marcaInicial;
        this.incidenciaFront = incidenciaFront;
        this.incidenciaBack = incidenciaBack;
        this.matrizInhibidora = matrizInhibidora;
        this.transiciones = new int[incidenciaBack[0].length];
        this.vectorDesInhibidor = new int[this.transiciones.length];
        this.cantidadTransicionesDisparadas = 0;
        this.ordenTransicionesDisparadas = "";
        this.isPInvariantesCorrecto = true;
        actualizarVectorDesInhibidor();
        actualizarSensibilizadas();
    }

    /**
     * Indica si la transicion esta sensibilizada
     * Metodo PRIVADO
     * @param transicion transicion a analizar
     * @return '1' si la transicion esta sensibilizada
     */
    private int isSensibilizadaInterno(int transicion) {
        for(int i = 0; i < marcaActual.length; i++) {
            if((marcaActual[i] - incidenciaBack[i][transicion]) < 0)
                return 0;
        }
        return 1;
    }

    /**
     * Disparo de transicion en red de Petri, modificando la marca de la red
     * @param transicion transicion a disparar
     */
    public void disparar(int transicion) {
        for (int i = 0; i < marcaActual.length; i++) {
            marcaActual[i] = marcaActual[i] + incidenciaFront[i][transicion];
            marcaActual[i] = marcaActual[i] - incidenciaBack[i][transicion];
        }
        this.cantidadTransicionesDisparadas++;

        if(this.isPInvariantesCorrecto)
            this.isPInvariantesCorrecto = isPInvariantesCorrecto();

        this.ordenTransicionesDisparadas = this.ordenTransicionesDisparadas.concat(transicion + "-");

        actualizarVectorDesInhibidor();
        actualizarSensibilizadas();
    }

    /**
     * Actualizacion de transiciones sensibilizadas
     */
    public void actualizarSensibilizadas() {
        for (int i = 0; i < transiciones.length ; i++)
            this.transiciones[i] = isSensibilizadaInterno(i) * this.vectorDesInhibidor[i];
    }

    /**
     * Actualiza el vector desinhibidor (para arcos inhibidores)
     * '1' para transiciones deshinibidas
     */
    private void actualizarVectorDesInhibidor() {
        for(int i = 0; i < this.matrizInhibidora.length; i++) {
            for(int j = 0; j < this.matrizInhibidora[i].length; j++) {
                if(this.matrizInhibidora[i][j] == 1) {
                    if(this.marcaActual[j] > 0) {
                        this.vectorDesInhibidor[i] = 0;
                        break;
                    }
                }
                this.vectorDesInhibidor[i] = 1;
            }
        }
    }

    /**
     * Indica si la transicion esta sensibilizada
     * Metodo PUBLICO llamado por monitor
     * @param transicion transicion a analizar
     * @return true si la transicion esta sensibilizada
     */
    public boolean isSensibilizada(int transicion) {
        return (this.transiciones[transicion] == 1);
    }

    /**
     * Getter de situacion de transiciones
     * @return lista de ints, '1' para sensibilizada
     */
    public int[] getTransiciones() {
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