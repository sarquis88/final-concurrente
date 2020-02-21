/**
 * HILO ENCARGADO DE DISPARAR TRANSICIONES 0, 1 Y 8
 * GENERADOR DE PROCESOS
 */

import java.util.Random;

public class CPUGenerator extends Thread {

    private Monitor monitor;
    private CPUBuffer cpuBufferA;
    private CPUBuffer cpuBufferB;

    private double arrivalRateAvg;
    private int cantidadAGenerar;

    private int[] secuencia = {99, 99, 99};

    /**
     * Constructor de clase
     * @param monitor monitor del productor
     * @param cantidadAGenerar cantidad de procesos a generar
     * @param arrivalRateAvg tiempo promedio a crear procesos
     * @param cpuBufferA buffer a agregar procesos
     * @param cpuBufferB buffer a agregar procesos
     */
    public CPUGenerator(Monitor monitor, int cantidadAGenerar, double arrivalRateAvg,
                        CPUBuffer cpuBufferA, CPUBuffer cpuBufferB) {
        setName("CPUGenerator");
        this.monitor = monitor;
        this.cantidadAGenerar = cantidadAGenerar;
        this.arrivalRateAvg = arrivalRateAvg;
        this.cpuBufferA = cpuBufferA;
        this.cpuBufferB = cpuBufferB;

        this.secuencia[0] = 0;
        this.secuencia[1] = 1;
        this.secuencia[2] = 8;
    }

    /**
     * Accion de generar procesos
     * Ingreso a monitor, disparo de red, salida de monitor
     */
    private void generarProceso() {
        CPUProcess cpuProcess = null;

        try {
            monitor.entrar(this.secuencia[0]);      // arrival rate
            cpuProcess = new CPUProcess();
            monitor.salir();
        } catch (Exception e) {
            e.printStackTrace();
        }

        CPUBuffer cpuBuffer;
        int transicion;
        String id;

        int politica;       // 0 para A, 1 para B
        if(cpuBufferA.getSize() < cpuBufferB.getSize())
            politica = 0;
        else if(cpuBufferA.getSize() > cpuBufferB.getSize())
            politica = 1;
        else {
            Random random = new Random();
            politica = random.nextInt(2);
        }

        if(politica == 0) {
            cpuBuffer = cpuBufferA;
            transicion = secuencia[1];
            id = "A";
        }
        else {
            cpuBuffer = cpuBufferB;
            transicion = secuencia[2];
            id = "B";
        }

        try {
            monitor.entrar(transicion);                 // creacion de proceso
            cpuBuffer.addProceso(cpuProcess);
            monitor.salir();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(cpuProcess != null)
            System.out.println("NUEVO PROCESO NUMERO:              " + cpuProcess.getIdLocal() + " - TAMAÑO BUFFER "
                    + id + " = " + cpuBuffer.getSize());
    }

    /**
     * Accion del Thread al iniciar
     */
    @Override
    public void run() {
        System.out.println(Colors.RED_BOLD + "INICIO CPUGenerator" + Colors.RESET);
        Main.setInicio();
        for (int i = 0; i < cantidadAGenerar; i++) {
            this.generarProceso();
            try {
                Main.dormir(this.arrivalRateAvg);
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
    private void interruptedReaccion() {

    }
}