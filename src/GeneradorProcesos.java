import java.util.Random;

import static java.lang.Math.round;

public class GeneradorProcesos extends Thread {

    private int cantidadAGenerar;
    private Monitor monitor;
    private int arrivalRate;
    private CPUBuffer cpuBufferA;
    private CPUBuffer cpuBufferB;

    /**
     * Constructor de clase
     * @param monitor monitor del productor
     * @param cantidadAGenerar cantidad de procesos a generar
     */
    public GeneradorProcesos(Monitor monitor, int cantidadAGenerar, int arrivalRate,
                             CPUBuffer cpuBufferA, CPUBuffer cpuBufferB) {
        Thread.currentThread().setName("GeneradorProcesos");
        this.monitor = monitor;
        this.cantidadAGenerar = cantidadAGenerar;
        this.arrivalRate = arrivalRate;
        this.cpuBufferA = cpuBufferA;
        this.cpuBufferB = cpuBufferB;
    }

    /**
     * Accion de generar procesos
     * Ingreso a monitor, disparo de red, salida de monitor
     */
    private void generarProceso() {
        boolean disparo = false;
        try {
            disparo = monitor.entrar(12);
            monitor.salir();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(disparo) {
            Random random = new Random();
            int ran = random.nextInt(2);
            String cpu = "A";
            CPUBuffer cpuBuffer = cpuBufferA;

            if(ran == 1) {
                ran = 6;
                cpu = "B";
                cpuBuffer = cpuBufferB;
            }

            CPUProcess cpuProcess = null;
            try {
                monitor.entrar(ran);
                cpuProcess = new CPUProcess();
                cpuBuffer.addProceso(cpuProcess);
                monitor.salir();
            } catch (Exception e) {
                e.printStackTrace();
            }
            if(cpuProcess != null)
                System.out.println("NUEVO PROCESO NUMERO:              " + cpuProcess.getIdLocal() + " EN CPU " + cpu);
        }
        dormir();
    }

    /**
     * Dormida del Thread durante un tiempo
     * Si arrivalRate > 3, el tiempo tiene distribucion gaussiana centrada en arrivalRate con desvio unitario
     * De lo contrario, el tiempo es igual a arrivalRate
     */
    private void dormir() {
        Random random = new Random();
        long sleep;
        if(arrivalRate > 3)
            sleep = round(random.nextGaussian() + arrivalRate);
        else
            sleep = arrivalRate;
        try {
            sleep(sleep);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Accion del Thread al iniciar
     */
    @Override
    public void run() {
        for (int i = 0; i < cantidadAGenerar; i++)
            this.generarProceso();
    }
}