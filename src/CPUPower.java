public class CPUPower extends Thread {

    private Monitor monitor;
    private CPUBuffer cpuBuffer;
    private int standByDelay;
    private boolean isActive;

    /**
     * Constructor de clase
     * @param monitor monitor del CPUProcessing
     */
    public CPUPower(Monitor monitor, CPUBuffer cpuBuffer, int standbyDelay) {
        Thread.currentThread().setName("CPUPower");
        this.monitor = monitor;
        this.cpuBuffer = cpuBuffer;
        this.standByDelay = standbyDelay;
        this.isActive = false;
    }

    /**
     * Accion del hilo
     * Encender y apagar el CPU
     */
    @Override
    public void run() {
        System.out.println(Colors.RED_BOLD + "INICIO CPUPower" + Colors.RESET);

        int[] secuencia = {2, 3, 4};

        while(!currentThread().isInterrupted()) {

            try {
                monitor.entrar(secuencia[0]);    // pasar de stand by a encendido
                monitor.salir();
                Thread.sleep(this.standByDelay);
            } catch (InterruptedException e) {
                interruptedReaccion();
                return;
            }

            try {
                monitor.entrar(secuencia[1]);    // encender CPU
                monitor.salir();
            } catch (InterruptedException e) {
                interruptedReaccion();
                return;
            }

            if(this.cpuBuffer.getSize() <= 0 && !this.isActive) {
                try {
                    monitor.entrar(secuencia[2]);   // apagado
                    monitor.salir();
                } catch (InterruptedException e) {
                    interruptedReaccion();
                    return;
                }
            }
        }
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
     * Reaccion cuando el hilo se ve interrumpido
     */
    private void interruptedReaccion() {
        System.out.println(Colors.RED_BOLD + "FIN CPUPower" + Colors.RESET);
    }

    /**
     * Seteo el estado de actividad del cpu
     * @param isActive true para activo, false para inactivo
     */
    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }
}
