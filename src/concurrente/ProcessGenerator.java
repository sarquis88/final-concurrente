package concurrente;

import java.util.Random;

public class ProcessGenerator extends Thread {

    private Monitor monitor;
    private ProcessBuffer[] buffers;

    private int cantidadAGenerar;
    private int generados;
    private int[] secuencia = {99, 99, 99};

    /**
     * Constructor de clase
     * @param monitor monitor del productor
     * @param cantidadAGenerar cantidad de procesos a generar
     * @param buffers lista de buffers
     */
    public ProcessGenerator(Monitor monitor, int cantidadAGenerar, ProcessBuffer[] buffers) {
        setName("ProcessGenerator");
        this.monitor = monitor;
        this.cantidadAGenerar = cantidadAGenerar;
        this.generados = 1;
        this.buffers = buffers;

        this.secuencia[0] = 0;
        this.secuencia[1] = 1;
        this.secuencia[2] = 8;
    }

    /**
     * Accion del Thread al iniciar
     */
    @Override
    public void run() {
        if( Main.isLoggingActivated() )
            System.out.println(Colors.RED_BOLD + "INICIO ProcessGenerator" + Colors.RESET);
        Main.setInicio();

        while(!currentThread().isInterrupted() && this.generados <= this.cantidadAGenerar) {
            try {
                monitor.disparar(this.secuencia[0]);      // arrival rate
            } catch (Exception e) {
                e.printStackTrace();
            }

            int transicion, politica;
            String id;
            ProcessBuffer buffer;

            if ( buffers[0].getSize() < buffers[1].getSize() )
                politica = 0;
            else if( buffers[0].getSize() > buffers[1].getSize() )
                politica = 1;
            else
            {
                Random ran = new Random();
                politica = ran.nextInt(2);
            }

            if( politica == 0 )
            {
                transicion = secuencia[1];
                id = "A";
                buffer = buffers[0];
            }
            else
            {
                transicion = secuencia[2];
                id = "B";
                buffer = buffers[1];
            }

            try {
                monitor.disparar(transicion);                 // creacion de proceso
            } catch (Exception e) {
                e.printStackTrace();
            }
            if( Main.isLoggingActivated() )
                System.out.println("NUEVO PROCESO NUMERO " + this.generados++ + " - EN BUFFER " + id);

            if( buffer != null )
                buffer.addProceso(new Process());
        }
        if( Main.isLoggingActivated() )
            System.out.println(Colors.RED_BOLD + "FIN   ProcessGenerator" + Colors.RESET);
    }
}