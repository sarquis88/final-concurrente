package concurrente;

public class CPUGarbageCollector extends Thread {

    private Monitor monitor;
    private int transicion;

    /**
     * Constructor de clase
     *
     * @param monitor monitor asociado a la red
     */
    public CPUGarbageCollector(Monitor monitor) {
        this.monitor = monitor;
        this.transicion = 7;
    }

    /**
     * Limpieza de tokens en plazas 4 y alternadamente
     */
    @Override
    public void run() {
        if( Main.isLoggingActivated() )
            System.out.println(Colors.RED_BOLD + "INICIO CPUGarbageCollector " + Colors.RESET);

        while (!currentThread().isInterrupted() && !CPU.isFinished()) {
            monitor.disparar(transicion);    // limpiar basura
        }
        if( Main.isLoggingActivated() )
            System.out.println(Colors.RED_BOLD + "FIN    CPUGarbageCollector " + Colors.RESET);
    }
}
