public class CPUProcessing extends Thread {

    private CPUPower cpuPower;
    private Monitor monitor;
    private CPUBuffer cpuBuffer;
    private int serviceRate;

    /**
     * Constructor de clase
     * @param cpuPower controlador de encendido del CPU
     */
    public CPUProcessing(CPUPower cpuPower, int serviceRate) {
        setName("CPUProcessing");
        this.cpuPower = cpuPower;
        this.monitor = cpuPower.getMonitor();
        this.cpuBuffer = cpuPower.getCpuBuffer();
        this.serviceRate = serviceRate;
    }

    /**
     * Accion del hilo
     * Mantener encendido el CPU y procesar datos
     */
    @Override
    public void run() {
        System.out.println(Colors.RED_BOLD + "INICIO CPUProcessing" + Colors.RESET);

        int[] secuencia = {5, 6};

        while(!currentThread().isInterrupted()) {

            try {
                monitor.entrar(secuencia[0]);    // tomar proceso del buffer y procesar
                this.cpuPower.setIsActive(true);
                monitor.salir();

                Thread.sleep(this.serviceRate);
            }
            catch (InterruptedException e) {
                interruptedReaccion();
                return;
            }

            try {
                monitor.entrar(secuencia[1]);   // fin proceso
                this.cpuPower.setIsActive(false);
                CPUProcess cpuProcess = this.cpuBuffer.procesar();
                monitor.salir();

                if(cpuProcess != null) {
                    this.cpuPower.setIsActive(false);
                    if(!Main.isPrintMarcado())
                        System.out.println("TERMINADO PROCESO NUMERO:          " + cpuProcess.getIdLocal());
                    if (cpuProcess.getIdLocal() == Main.getCantidadProcesos()) {
                        Main.setFin();
                        this.cpuPower.interrupt();
                        throw new InterruptedException();
                    }
                }
            }
            catch (InterruptedException e) {
                interruptedReaccion();
                return;
            }
        }
    }

    /**
     * Reaccion cuando el hilo se ve interrumpido
     */
    private void interruptedReaccion() {
        System.out.println(Colors.RED_BOLD + "FIN CPUProcessing");
    }
}