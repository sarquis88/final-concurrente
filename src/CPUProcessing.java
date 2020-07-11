/*
 * HILO ENCARGADO DE DISPARAR TRANSICIONES 5, 6, 14 Y 15
 * PROCESAMIENTO DEL CPU
 */

public class CPUProcessing extends Thread {

    private Monitor monitor;

    private int procesados;
    private final int[] secuencia = {99, 99};

    /**
     * Constructor de clase
     *
     * @param cpuPower controlador de encendido/apagado del cpu
     */
    public CPUProcessing(CPUPower cpuPower) {
        setName("CPUProcessing");
        this.monitor = cpuPower.getMonitor();
        this.procesados = 0;

        this.secuencia[0] = 5;
        this.secuencia[1] = 6;
    }

    /**
     * Accion del hilo
     * Mantener encendido el CPU y procesar datos
     */
    @Override
    public void run() {
        if( Main.isLoggingActivated() )
            System.out.println(Colors.RED_BOLD + "INICIO CPUProcessing " + Colors.RESET);

        while(!currentThread().isInterrupted() && !CPU.isFinished()) {

            monitor.disparar(secuencia[0]);    // tomar proceso del buffer y procesar

            monitor.disparar(secuencia[1]);   // fin proceso

            this.procesados++;

            if( Main.isLoggingActivated() )
                System.out.println("TERMINADO PROCESO NUMERO " + procesados);
            if (procesados == Main.getCantidadProcesos()) {
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