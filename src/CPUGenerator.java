import java.util.Random;

import static java.lang.Math.round;

public class CPUGenerator extends Thread {

    private int cantidadAGenerar;
    private Monitor monitor;
    private double arrivalRateMax;
    private double arrivalRateMin;
    private CPUBuffer cpuBufferA;
    private CPUBuffer cpuBufferB;
    private int[] secuencia = {99, 99, 99};

    /**
     * Constructor de clase
     * @param monitor monitor del productor
     * @param cantidadAGenerar cantidad de procesos a generar
     */
    public CPUGenerator(Monitor monitor, int cantidadAGenerar, double arrivalRateMax, double arrivalRateMin,
                        CPUBuffer cpuBufferA, CPUBuffer cpuBufferB) {
        setName("CPUGenerator");
        this.monitor = monitor;
        this.cantidadAGenerar = cantidadAGenerar;
        this.arrivalRateMax = arrivalRateMax;
        this.arrivalRateMin = arrivalRateMin;
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
        try {
            monitor.entrar(this.secuencia[0]);      // arrival rate
            monitor.salir();
        } catch (Exception e) {
            e.printStackTrace();
        }

        Random random = new Random();           // numero random para crear proceso en un buffer o en el otro
        CPUProcess cpuProcess = null;

        CPUBuffer cpuBuffer = cpuBufferA;
        int transicion = this.secuencia[1];

        if(random.nextInt(2) == 1) {
            cpuBuffer = this.cpuBufferB;
            transicion = this.secuencia[2];
        }

        try {
            monitor.entrar(transicion);                 // creacion de proceso
            cpuProcess = new CPUProcess();
            cpuBuffer.addProceso(cpuProcess);
            monitor.salir();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(!Main.isPrintMarcado() && cpuProcess != null)
            System.out.println("NUEVO PROCESO NUMERO:              " + cpuProcess.getIdLocal());
    }

    /**
     * Dormida del Thread durante un tiempo minimo de arrivalRateMin y maximo de arrivalRateMax
     */
    private void dormir() throws InterruptedException {
        Random random = new Random();
        double sleepTimeDouble = arrivalRateMin + (arrivalRateMax - arrivalRateMin) * random.nextDouble();
        long sleepTime = round(sleepTimeDouble);
        sleep(sleepTime);
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
                dormir();
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