package concurrente;/*
 * HILO ENCARGADO DE DISPARAR TRANSICIONES 2, 3, 4, 9, 10, Y 11
 * ENCENDIDO/APAGADO DE CPUs
 */

public class CPUPower extends Thread {

    private Monitor monitor;

    private int[] secuencia = {99, 99, 99};

    /**
     * Constructor de clase
     * @param monitor monitor de la red
     */
    public CPUPower(Monitor monitor) {
        setName("concurrente.CPUPower");
        this.monitor = monitor;

        this.secuencia[0] = 2;
        this.secuencia[1] = 3;
        this.secuencia[2] = 4;
    }

    /**
     * Accion del hilo
     * Encender y apagar el concurrente.CPU
     */
    @Override
    public void run() {
        if( Main.isLoggingActivated() )
            System.out.println(Colors.RED_BOLD + "INICIO concurrente.CPUPower " + Colors.RESET);

        while(!currentThread().isInterrupted() && !CPU.isFinished()) {

            // intento de encendido
            monitor.disparar(secuencia[0]);    // pasar de stand by a encendido

            monitor.disparar(secuencia[1]);    // encender concurrente.CPU

            if( Main.isLoggingActivated() )
                System.out.println(Colors.RED_BOLD + "ENCENDIDO:                         concurrente.CPU " + Colors.RESET);

            monitor.disparar(secuencia[2]);   // apagado

            if( Main.isLoggingActivated() )
                System.out.println(Colors.RED_BOLD + "APAGADO:                           concurrente.CPU " + Colors.RESET);

        }
    }

    /**
     * Getter del monitor del concurrente.CPU
     * @return objeto Monitos correspondiente al concurrente.CPU
     */
    public Monitor getMonitor() {
        return this.monitor;
    }
}
