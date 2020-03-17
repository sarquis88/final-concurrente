/*
  HILO ENCARGADO DE DISPARAR TRANSICIONES 0, 1 Y 8
  GENERADOR DE PROCESOS
 */

import java.util.Random;

public class ProcessGenerator extends Thread {

    private Monitor monitor;

    private int cantidadAGenerar;
    private int generados;
    private int[] secuencia = {99, 99, 99};

    /**
     * Constructor de clase
     * @param monitor monitor del productor
     * @param cantidadAGenerar cantidad de procesos a generar
     */
    public ProcessGenerator(Monitor monitor, int cantidadAGenerar) {
        setName("ProcessGenerator");
        this.monitor = monitor;
        this.cantidadAGenerar = cantidadAGenerar;
        this.generados = 1;

        this.secuencia[0] = 0;
        this.secuencia[1] = 1;
        this.secuencia[2] = 8;
    }

    /**
     * Accion del Thread al iniciar
     */
    @Override
    public void run() {
        System.out.println(Colors.RED_BOLD + "INICIO ProcessGenerator" + Colors.RESET);
        Main.setInicio();

        while(!currentThread().isInterrupted() && this.generados <= this.cantidadAGenerar) {
            try {
                monitor.disparar(this.secuencia[0]);      // arrival rate
            } catch (Exception e) {
                e.printStackTrace();
            }

            int transicion;
            String id;
            Random random = new Random();
            int politica = random.nextInt(2);       // 0 para A, 1 para B

            if (politica == 0) {
                transicion = secuencia[1];
                id = "A";
            } else {
                transicion = secuencia[2];
                id = "B";
            }

            try {
                monitor.disparar(transicion);                 // creacion de proceso
            } catch (Exception e) {
                e.printStackTrace();
            }
            System.out.println("NUEVO PROCESO NUMERO " + this.generados++ + " - EN BUFFER " + id);
        }
        System.out.println(Colors.RED_BOLD + "FIN   ProcessGenerator" + Colors.RESET);
    }
}