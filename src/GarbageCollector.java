import static java.lang.Math.round;

public class GarbageCollector extends Thread {

    private RedDePetri redDePetri;
    private Monitor monitor;

    private long serviceRate;

    /**
     * Constructor de clase
     * @param redDePetri red de petri a disparar
     * @param monitor monitor asociado a la red
     * @param serviceRate tiempo entre limpiezas
     */
    public GarbageCollector(RedDePetri redDePetri, Monitor monitor, double serviceRate) {
        this.redDePetri = redDePetri;
        this.monitor = monitor;
        this.serviceRate = round(serviceRate);
    }

    /**
     * Limpieza de tokens en plazas 4 y 11 alternadamente
     */
    @Override
    public void run() {
        System.out.println(Colors.RED_BOLD + "INICIO GarbageCollector " + Colors.RESET);
        int[] secuencia = {7, 14};

        while (!currentThread().isInterrupted()) {

            try {
                monitor.entrar(secuencia[0]);    // limpiar basura CPU A
                monitor.salir();
            } catch (InterruptedException e) {
                interruptedReaccion();
            }

            try {
                monitor.entrar(secuencia[1]);    // limpiar basura CPU B
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
            System.out.println(Colors.RED_BOLD + "FIN GarbageCollector " + Colors.RESET);

        }
}
