package concurrente;

public class CPUGarbageCollector extends Thread {

    private final Monitor monitor;
    private final String cpuId;
    private final int secuencia;

    /**
     * Constructor de clase
     * @param monitor monitor asociado a la red
     * @param cpuId id del cpu a limpiar
     * @param secuencia secuencia de disparo
     */
    public CPUGarbageCollector(Monitor monitor, String cpuId, int secuencia) {
        this.monitor = monitor;
        this.cpuId = cpuId;
        this.secuencia = secuencia;
    }

    /**
     * Limpieza de tokens en plazas 4 y 11 alternadamente
     */
    @Override
    public void run() {
        if( Main.isLoggingActivated() )
            System.out.println(Colors.RED_BOLD + "INICIO CPUGarbageCollector " + this.cpuId + Colors.RESET);

        while (!currentThread().isInterrupted() && !CPU.isFinished()) {
            monitor.disparar(secuencia);    // limpiar basura
        }
        if( Main.isLoggingActivated() )
            System.out.println(Colors.RED_BOLD + "FIN    CPUGarbageCollector " + this.cpuId + Colors.RESET);
    }
}
