public class RedDePetri {

    private int[] marcaActual;
    private int[] vectorDesInhibidor;
    private int[] transiciones;
    private int[][] incidenciaFront;
    private int[][] incidenciaBack;
    private int[][] matrizInhibidora;
    private boolean isPInvariantesCorrecto;
    private long[] timeStamp;
    private long[] alfa;
    private long[] beta;

    private String ordenTransicionesDisparadas;
    private Monitor monitor;

    /**
     * Constructor de clase
     * @param marcaInicial     marca inicial de la red
     * @param incidenciaFront  matriz de incidencia frontal
     * @param incidenciaBack   matriz de incidencia trasera
     * @param matrizInhibidora matriz de arcos inhibidores
     * @param timeStamp        ventanas de tiempo
     * @param alfa             matriz de valor de tiempo alfa
     * @param beta             matriz de valor de tiempo beta
     */
    public RedDePetri(int[] marcaInicial, int[][] incidenciaFront, int[][] incidenciaBack,
                      int[][] matrizInhibidora, Monitor monitor, long[] timeStamp, long[] alfa,
                      long[] beta) {

        this.monitor = monitor;
        this.marcaActual = marcaInicial;
        this.incidenciaFront = incidenciaFront;
        this.incidenciaBack = incidenciaBack;
        this.matrizInhibidora = matrizInhibidora;
        this.transiciones = new int[incidenciaBack[0].length];
        this.vectorDesInhibidor = new int[this.transiciones.length];
        this.ordenTransicionesDisparadas = "";
        this.isPInvariantesCorrecto = true;
        this.timeStamp = timeStamp;
        this.alfa = alfa;
        this.beta = beta;

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
        for (int i = 0; i < marcaActual.length; i++) {
            if ((marcaActual[i] - incidenciaBack[i][transicion]) < 0)
                return 0;
        }
        return 1;
    }

    /**
     * Disparo de transicion en red de Petri, modificando la marca de la red
     * @param transicion transicion a disparar
     */
    private void disparar(int transicion) {
        for (int i = 0; i < marcaActual.length; i++) {
            marcaActual[i] = marcaActual[i] + incidenciaFront[i][transicion];
            marcaActual[i] = marcaActual[i] - incidenciaBack[i][transicion];
        }

        if (this.isPInvariantesCorrecto)
            this.isPInvariantesCorrecto = isPInvariantesCorrecto();

        int concat = transicion;
        if( concat > 9 )
            concat += 55;
        else
            concat += 48;
        this.ordenTransicionesDisparadas = this.ordenTransicionesDisparadas.concat( (char) concat + " ");

        actualizarVectorDesInhibidor();
        actualizarSensibilizadas();
    }

    /**
     * Implementa el disparo con ventana de tiempo. Si la transicion no esta sensibilizada por tiempo
     * duerme al hilo hasta estar dentro de la ventana.
     * @param transicion la transicion a disparar
     * @return true si pudo disparar, false en caso contrario.
     */
    public boolean disparoTemporal(int transicion) {
        boolean k = true;
        if (isSensibilizada(transicion)) {
            if (testVentanaTiempo(transicion)) {
                timeStamp[transicion] = System.currentTimeMillis();
            }
            else {
                long ultimoDisparo = System.currentTimeMillis() - timeStamp[transicion];
                if(ultimoDisparo < alfa[transicion]) {   // falta esperar mas tiempo
                    try {
                        this.monitor.getMutex().unlock();
                        Thread.sleep(alfa[transicion] - ultimoDisparo);
                        this.monitor.getMutex().lock();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                else {
                    k = false;
                }
            }
            if (k)
                disparar(transicion);
            return k;
        } else {
            return false;
        }
    }

    /**
     * Actualizacion de transiciones sensibilizadas
     */
    public void actualizarSensibilizadas() {
        for (int i = 0; i < transiciones.length; i++)
            this.transiciones[i] = isSensibilizadaInterno(i) * this.vectorDesInhibidor[i];
    }

    /**
     * Actualiza el vector desinhibidor (para arcos inhibidores)
     * '1' para transiciones deshinibidas
     */
    private void actualizarVectorDesInhibidor() {
        for (int i = 0; i < this.matrizInhibidora.length; i++) {
            for (int j = 0; j < this.matrizInhibidora[i].length; j++) {
                if (this.matrizInhibidora[i][j] == 1) {
                    if (this.marcaActual[j] > 0) {
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
     * Chequea el estado de las P-Invariantes
     * @return true en caso de estado correcto, de lo contrario false
     */
    private boolean isPInvariantesCorrecto() {
        return (this.marcaActual[0] + this.marcaActual[1] == 1 &&
                this.marcaActual[2] + this.marcaActual[3] + this.marcaActual[5] == 1 &&
                this.marcaActual[7] + this.marcaActual[8] == 1 &&
                this.marcaActual[9] + this.marcaActual[10] + this.marcaActual[12] == 1 &&
                this.marcaActual[14] + this.marcaActual[15] == 1);
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

    /**
     * Verifica si la transicion esta sensibilizada para su ventana de tiempo.
     * @param transicion la transicion a chequear
     * @return true si esta dentro de la ventana, false en caso contrario
     */
    private boolean testVentanaTiempo(int transicion) {
        long ultimoDisparo = System.currentTimeMillis() - timeStamp[transicion];
        return (ultimoDisparo >= alfa[transicion] && ultimoDisparo < beta[transicion]);
    }
}