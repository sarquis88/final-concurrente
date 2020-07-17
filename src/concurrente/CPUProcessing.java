package concurrente;

import java.util.concurrent.locks.ReentrantLock;

public class CPUProcessing extends Thread {

    private final Monitor monitor;
    private final ProcessBuffer buffer;
    private final String cpuId;
    private int procesados;
    private final int[] secuencia;
    private static int procesadosGlobal = 0;
    private static final ReentrantLock mutex = new ReentrantLock();

    /**
     * Constructor de clase
     * @param cpuPower controlador de encendido/apagado del cpu
     * @param cpuId id del cpu
     * @param buffer buffer que contiene los procesos
     * @param secuencia secuencia de disparos
     */
    public CPUProcessing(CPUPower cpuPower, String cpuId, ProcessBuffer buffer, int[] secuencia)
    {
        setName("CPUProcessing " + cpuId);
        this.monitor = cpuPower.getMonitor();
        this.cpuId = cpuId;
        this.procesados = 0;
        this.buffer = buffer;
        this.secuencia = secuencia;
    }

    /**
     * Accion del hilo
     * Mantener encendido el CPU y procesar datos
     */
    @Override
    public void run() {
        if( Main.isLoggingActivated() )
            System.out.println(Colors.RED_BOLD + "INICIO CPUProcessing " + this.cpuId + Colors.RESET);

        while(!currentThread().isInterrupted() && !CPU.isFinished()) {

            monitor.disparar(secuencia[0]);    // tomar proceso del buffer y procesar

            monitor.disparar(secuencia[1]);   // fin proceso

            if( this.buffer != null )
                this.buffer.procesar();

            this.procesados++;

            // si no se aumentan los procesadosGlobal en exlusion mutua
            // hay problemas de inanicion en varias ejecuciones
            mutex.lock();
            procesadosGlobal++;
            mutex.unlock();

            if( Main.isLoggingActivated() )
                System.out.println("TERMINADO PROCESO NUMERO " + procesadosGlobal + " - EN " + this.cpuId);
            if (procesadosGlobal == Main.getCantidadProcesos()) {
                CPU.finish();
                Main.setFin();                                                  // marca tiempo final
                Main.exit();
            }
        }
    }

    /**
     * Getter de procesos terminados
     * @return int de los procesos terminados
     */
    public int getProcesados() {
        return this.procesados;
    }
}