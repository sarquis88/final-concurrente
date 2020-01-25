public class CPUProcessing extends Thread {

    private CPUPower cpuPower;
    private Monitor monitor;
    private CPUBuffer cpuBuffer;

    /**
     * Constructor de clase
     * @param cpuPower controlador de encendido del CPU
     */
    public CPUProcessing(CPUPower cpuPower) {
        setName("CPUProcessing" + cpuPower.getCPUId());
        this.cpuPower = cpuPower;
        this.monitor = cpuPower.getMonitor();
        this.cpuBuffer = cpuPower.getCpuBuffer();
    }

    /**
     * Accion del hilo
     * Mantener encendido el CPU y procesar datos
     */
    @Override
    public void run() {
        int[] secuencia = {99, 99, 99};
        if(this.cpuPower.getCPUId().equalsIgnoreCase("A")) {      // secuencia de transiciones de CPU A
            secuencia[0] = 3;
            secuencia[1] = 4;
            secuencia[2] = 13;
        }
        else if(this.cpuPower.getCPUId().equalsIgnoreCase("B")) {
            secuencia[0] = 9;                                       // secuencia de transiciones de CPU B
            secuencia[1] = 10;
            secuencia[2] = 14;
        }
        else
            return;                             // error

        while(!currentThread().isInterrupted()) {
            try {
                monitor.disparo(secuencia[0]);    // mantener CPU encendido
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                monitor.disparo(secuencia[1]);    // tomar proceso del buffer y procesar
                this.cpuPower.setActive(true);
                this.cpuBuffer.procesar();
                Thread.sleep(3);
            }
            catch (Exception e) {
                e.printStackTrace();
            }

            try {
                monitor.disparo(secuencia[2]);   // fin proceso
                this.cpuPower.setActive(false);
                CPUProcess cpuProcess = this.cpuBuffer.procesar();
                if(cpuProcess != null)
                    System.out.println("TERMINADO PROCESO NUMERO:          " + cpuProcess.getIdLocal());
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}