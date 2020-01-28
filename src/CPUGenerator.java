import java.util.Random;

import static java.lang.Math.round;

public class CPUGenerator extends Thread {

    private int cantidadAGenerar;
    private Monitor monitor;
    private int arrivalRate;
    private CPUBuffer cpuBuffer;

    /**
     * Constructor de clase
     * @param monitor monitor del productor
     * @param cantidadAGenerar cantidad de procesos a generar
     */
    public CPUGenerator(Monitor monitor, int cantidadAGenerar, int arrivalRate, CPUBuffer cpuBuffer) {
        setName("CPUGenerator");
        this.monitor = monitor;
        this.cantidadAGenerar = cantidadAGenerar;
        this.arrivalRate = arrivalRate;
        this.cpuBuffer = cpuBuffer;
    }

    /**
     * Accion de generar procesos
     * Ingreso a monitor, disparo de red, salida de monitor
     */
    private void generarProceso() {
        try {
            monitor.entrar(0);
            monitor.salir();
        } catch (Exception e) {
            e.printStackTrace();
        }

        CPUProcess cpuProcess = null;
        try {
            monitor.entrar(1);
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
        System.out.println(Colors.RED_BOLD + "INICIO CPUGenerator" + Colors.RESET);
        Main.setInicio();
        for (int i = 0; i < cantidadAGenerar; i++) {
            this.generarProceso();
            dormir();
        }

        System.out.println(Colors.RED_BOLD + "FIN CPUGenerator" + Colors.RESET);
    }
}