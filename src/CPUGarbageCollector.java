/*
 * HILO ENCARGADO DE DISPARAR LAS TRANSICIONES 7 Y 14
 * DICHAS TRANSICIONES FUNCIONAN COMO GARBAGECOLLECTOR
 */

public class CPUGarbageCollector extends Thread {

    private Monitor monitor;
    private String cpuId;

    private double serviceRateAvg;

    /**
     * Constructor de clase
     * @param monitor monitor asociado a la red
     * @param serviceRateAvg tiempo promedio entre limpiezas
     * @param cpuId id del cpu a limpiar
     */
    public CPUGarbageCollector(Monitor monitor, double serviceRateAvg, String cpuId) {
        this.monitor = monitor;
        this.serviceRateAvg = serviceRateAvg;
        this.cpuId = cpuId;
    }

    /**
     * Limpieza de tokens en plazas 4 y 11 alternadamente
     */
    @Override
    public void run() {
        System.out.println(Colors.RED_BOLD + "INICIO CPUGarbageCollector " + this.cpuId + Colors.RESET);
        int transicion;

        if(this.cpuId.equalsIgnoreCase("A"))
            transicion = 7;
        else if(this.cpuId.equalsIgnoreCase("B"))
            transicion = 14;
        else
            return;

        while (!currentThread().isInterrupted() && !CPU.isFinished()) {

            monitor.disparar(transicion);    // limpiar basura

            try {
                Main.dormir(this.serviceRateAvg);
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
            System.out.println(Colors.RED_BOLD + "FIN CPUGarbageCollector " + this.cpuId + Colors.RESET);

        }
}
