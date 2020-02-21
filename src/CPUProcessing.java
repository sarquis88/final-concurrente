/**
 * HILO ENCARGADO DE DISPARAR TRANSICIONES 5, 6, 14 Y 15
 * PROCESAMIENTO DEL CPU
 */

public class CPUProcessing extends Thread {

    private CPUPower cpuPower;
    private Monitor monitor;
    private CPUBuffer cpuBuffer;
    private String cpuId;

    private double serviceRateAvg;
    private int procesados;

    private int[] secuencia = {99, 99};

    private static int procesadosGlobal = 0;

    /**
     * Constructor de clase
     * @param cpuPower controlador de encendido/apagado del cpu
     * @param cpuBuffer buffer en el cual procesar
     * @param serviceRateAvg tiempo promedio para terminar proceso
     * @param cpuId id del cpu
     */
    public CPUProcessing(CPUPower cpuPower, CPUBuffer cpuBuffer, double serviceRateAvg, String cpuId) {
        setName("CPUProcessing " + cpuId);
        this.cpuPower = cpuPower;
        this.monitor = cpuPower.getMonitor();
        this.cpuBuffer = cpuBuffer;
        this.serviceRateAvg = serviceRateAvg;
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
        System.out.println(Colors.RED_BOLD + "INICIO CPUProcessing " + this.cpuId + Colors.RESET);

        while(!currentThread().isInterrupted()) {

            try {
                monitor.entrar(secuencia[0]);    // tomar proceso del buffer y procesar
                this.cpuPower.setIsActive(true);
                monitor.salir();
            }
            catch (InterruptedException e) {
                interruptedReaccion();
            }

            try {
                monitor.entrar(secuencia[1]);   // fin proceso
                CPUProcess cpuProcess = this.cpuBuffer.procesar();
                this.procesados++;
                procesadosGlobal++;
                Main.dormir(this.serviceRateAvg);
                this.cpuPower.setIsActive(false);
                monitor.salir();

                if(cpuProcess != null) {
                    System.out.println("TERMINADO PROCESO NUMERO:          " + cpuProcess.getIdLocal() + " EN " + this.cpuId);
                    if (procesadosGlobal == Main.getCantidadProcesos()) {
                        Main.setFin(this.cpuId);                                                  // marca tiempo final
                        Main.exit();
                    }
                }
            }
            catch (InterruptedException e) {
                interruptedReaccion();
            }
        }
    }

    /**
     * Reaccion a interrupcion
     * VACIO
     */
    private void interruptedReaccion() {}

    /**
     * Getter de procesos terminados
     * @return int de los procesos terminados
     */
    public int getProcesados() {
        return this.procesados;
    }
}