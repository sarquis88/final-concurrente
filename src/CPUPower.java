public class CPUPower extends Thread {

    private Monitor monitor;
    private String CPUId;
    private boolean isOn;
    private boolean isActive;
    private CPUBuffer cpuBuffer;

    /**
     * Constructor de clase
     * @param monitor monitor del CPUProcessing
     * @param CPUId id del CPU a controlar (A o B)
     */
    public CPUPower(Monitor monitor, String CPUId, CPUBuffer cpuBuffer) {
        Thread.currentThread().setName("CPUPower" + CPUId);
        this.monitor = monitor;
        this.CPUId = CPUId;
        this.isOn = false;
        this.isActive = false;
        this.cpuBuffer = cpuBuffer;
    }

    /**
     * Accion del hilo
     * Encender y apagar el CPU
     */
    @Override
    public void run() {
        int[] secuencia = {99, 99, 99, 99, 99};
        if(this.CPUId.equalsIgnoreCase("A")) {      // secuencia de transiciones de CPU A
            secuencia[0] = 1;
            secuencia[1] = 2;
            secuencia[2] = 4;
            secuencia[3] = 13;
            secuencia[4] = 5;
        }
        else if(this.CPUId.equalsIgnoreCase("B")) {
            secuencia[0] = 7;                           // secuencia de transiciones de CPU B
            secuencia[1] = 8;
            secuencia[2] = 10;
            secuencia[3] = 14;
            secuencia[4] = 11;
        }
        else
            return;                             // error

        while(!currentThread().isInterrupted()) {

            try {
                monitor.disparo(secuencia[0]);    // pasar de stand by a encendido
                Thread.sleep(1);
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                monitor.disparo(secuencia[1]);    // encender CPU
                this.isOn = true;
            }
            catch (Exception e) {
                e.printStackTrace();
            }

            try {
                monitor.disparo(secuencia[2]);    // tomar proceso de buffer
                this.isActive = true;
                cpuBuffer.procesar();
                Thread.sleep(3);
            }
            catch (Exception e) {
                e.printStackTrace();
            }

            try {
                monitor.disparo(secuencia[3]);    // fin procesado
                this.isActive = false;

                CPUProcess cpuProcess = this.cpuBuffer.procesar();
                if(cpuProcess != null)
                    System.out.println("TERMINADO PROCESO NUMERO:          " + cpuProcess.getIdLocal());
            }
            catch (Exception e) {
                e.printStackTrace();
            }

            try {
                monitor.disparo(secuencia[4]);   // apagado
                this.isOn = false;
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Getter del Id del CPU
     * @return String correspondiente al ID
     */
    public String getCPUId() {
        return this.CPUId;
    }

    /**
     * Getter del monitor del CPU
     * @return objeto Monitos correspondiente al CPU
     */
    public Monitor getMonitor() {
        return this.monitor;
    }

    /**
     * Getter del CPUBuffer
     * @return objeto CPUBuffer relativo al buffer de instancia
     */
    public CPUBuffer getCpuBuffer() {
        return this.cpuBuffer;
    }

    /**
     * Cambiar el estado del procesador por activo o no
     * @param isActive booleano indicando el nuevo estado
     */
    public void setActive(boolean isActive) {
        this.isActive = isActive;
    }
}
