package concurrente;/*
  HILO ENCARGADO DE DISPARAR TRANSICIONES 0, 1 Y 8
  GENERADOR DE PROCESOS
 */

public class ProcessGenerator extends Thread {

    private Monitor monitor;

    private int cantidadAGenerar;
    private int generados;
    private int[] secuencia = {99, 99};

    /**
     * Constructor de clase
     * @param monitor monitor del productor
     * @param cantidadAGenerar cantidad de procesos a generar
     */
    public ProcessGenerator(Monitor monitor, int cantidadAGenerar) {
        setName("concurrente.ProcessGenerator");
        this.monitor = monitor;
        this.cantidadAGenerar = cantidadAGenerar;
        this.generados = 1;

        this.secuencia[0] = 0;
        this.secuencia[1] = 1;
    }

    /**
     * Accion del Thread al iniciar
     */
    @Override
    public void run() {
        if( Main.isLoggingActivated() )
            System.out.println(Colors.RED_BOLD + "INICIO concurrente.ProcessGenerator" + Colors.RESET);
        Main.setInicio();

        while(!currentThread().isInterrupted() && this.generados <= this.cantidadAGenerar) {
            try {
                monitor.disparar(this.secuencia[0]);      // arrival rate
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                monitor.disparar(secuencia[1]);                 // creacion de proceso
            } catch (Exception e) {
                e.printStackTrace();
            }
            if( Main.isLoggingActivated() )
                System.out.println("NUEVO PROCESO NUMERO " + this.generados++);
        }
        if( Main.isLoggingActivated() )
            System.out.println(Colors.RED_BOLD + "FIN   concurrente.ProcessGenerator" + Colors.RESET);
    }
}