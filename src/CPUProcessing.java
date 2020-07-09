/*
 * HILO ENCARGADO DE DISPARAR TRANSICIONES 5, 6, 14 Y 15
 * PROCESAMIENTO DEL CPU
 */

import java.util.concurrent.locks.ReentrantLock;

public class CPUProcessing extends Thread {

    private Monitor monitor;
    private String cpuId;

    private int procesados;
    private final int[] secuencia = {99, 99};

    private static int procesadosGlobal = 0;
    private static ReentrantLock mutex = new ReentrantLock();

    /**
     * Constructor de clase
     *
     * @param cpuPower controlador de encendido/apagado del cpu
     * @param cpuId id del cpu
     */
    public CPUProcessing(CPUPower cpuPower, String cpuId) {
        setName("CPUProcessing " + cpuId);
        this.monitor = cpuPower.getMonitor();
        this.cpuId = cpuId;
        this.procesados = 0;

        if(cpuId.equalsIgnoreCase("A")) {
            this.secuencia[0] = 5;
            this.secuencia[1] = 6;
        }
        else if(cpuId.equalsIgnoreCase("B")) {
            this.secuencia[0] = 12;
            this.secuencia[1] = 13;
        }
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