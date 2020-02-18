import static java.lang.Math.round;

public class GarbageCollector extends Thread {

    private Monitor monitor;
    private String cpuId;

    private long serviceRate;

    /**
     * Constructor de clase
     * @param monitor monitor asociado a la red
     * @param serviceRate tiempo entre limpiezas
     * @param cpuId id del cpu a limpiar
     */
    public GarbageCollector(Monitor monitor, double serviceRate, String cpuId) {
        this.monitor = monitor;
        this.serviceRate = round(serviceRate);
        this.cpuId = cpuId;
    }

    /**
     * Limpieza de tokens en plazas 4 y 11 alternadamente
     */
    @Override
    public void run() {
        System.out.println(Colors.RED_BOLD + "INICIO GarbageCollector " + this.cpuId + Colors.RESET);
        int transicion;

        if(this.cpuId.equalsIgnoreCase("A"))
            transicion = 7;
        else if(this.cpuId.equalsIgnoreCase("B"))
            transicion = 14;
        else
            return;

        while (!currentThread().isInterrupted()) {

            try {
                monitor.entrar(transicion);    // limpiar basura
                monitor.salir();
            } catch (InterruptedException e) {
                interruptedReaccion();
            }

            try {
                sleep(serviceRate);
            }
            catch (InterruptedException e) {
                interruptedReaccion();
            }
        }

    }
        /**
         * Reaccion a interrupcion
         * Impresion de mensaje
         */
        private void interruptedReaccion() {
            System.out.println(Colors.RED_BOLD + "FIN GarbageCollector " + this.cpuId + Colors.RESET);

        }
}
