import java.util.Random;

import static java.lang.Math.round;

public class CPUProcessing extends Thread {

    private CPUPower cpuPower;
    private Monitor monitor;
    private CPUBuffer cpuBuffer;
    private double serviceRateMax;
    private double serviceRateMin;
    private int[] secuencia = {99, 99};
    private String cpuId;
    private int procesados;

    /**
     * Constructor de clase
     * @param cpuPower controlador de encendido del CPU
     */
    public CPUProcessing(CPUPower cpuPower, double serviceRateMax, double serviceRateMin, String cpuId) {
        setName("CPUProcessing " + cpuId);
        this.cpuPower = cpuPower;
        this.monitor = cpuPower.getMonitor();
        this.cpuBuffer = cpuPower.getCpuBuffer();
        this.serviceRateMax = serviceRateMax;
        this.serviceRateMin = serviceRateMin;
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
                dormir();
            }
            catch (InterruptedException e) {
                interruptedReaccion();
            }

            try {

                monitor.entrar(secuencia[1]);   // fin proceso
                this.cpuPower.setIsActive(false);
                CPUProcess cpuProcess = this.cpuBuffer.procesar();
                this.procesados++;
                monitor.salir();

                if(cpuProcess != null) {
                    this.cpuPower.setIsActive(false);
                    if(!Main.isPrintMarcado())
                        System.out.println("TERMINADO PROCESO NUMERO:          " + cpuProcess.getIdLocal() + " " + this.cpuId);
                    if (cpuProcess.getIdLocal() == Main.getCantidadProcesos()) {
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

    /**
     * Dormida del Thread durante un tiempo minimo de serviceRateMin y maximo de serviceRateMax
     */
    private void dormir() throws InterruptedException {
        Random random = new Random();
        double sleepTimeDouble = serviceRateMin + (serviceRateMax - serviceRateMin) * random.nextDouble();
        long sleepTime = round(sleepTimeDouble);
        sleep(sleepTime);
    }
}