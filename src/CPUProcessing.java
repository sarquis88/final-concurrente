public class CPUProcessing extends Thread {

    private CPUPower cpuPower;

    /**
     * Constructor de clase
     * @param cpuPower controlador de encendido del CPU
     */
    public CPUProcessing(CPUPower cpuPower) {
        this.cpuPower = cpuPower;
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

        while( !(currentThread().isInterrupted()) ) {
            if(cpuPower.isOn()) {
                try {
                    cpuPower.getMonitor().entrar(secuencia[0]);    // mantener CPU encendido
                    cpuPower.getMonitor().salir();

                    cpuPower.getMonitor().entrar(secuencia[1]);    // tomar proceso del buffer y procesar
                    Thread.sleep(1);
                    cpuPower.getMonitor().salir();

                    cpuPower.getMonitor().entrar(secuencia[2]);   // fin proceso
                    cpuPower.getMonitor().salir();
                } catch (Exception e) {
                    if (e instanceof InterruptedException)
                        this.cpuPower.apagar();
                    else
                        e.printStackTrace();
                }
            }
        }
    }
}