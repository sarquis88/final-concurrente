/*
 * HILO ENCARGADO DE DISPARAR TRANSICIONES 2, 3, 4, 9, 10, Y 11
 * ENCENDIDO/APAGADO DE CPUs
 */

public class CPUPower extends Thread {

    private Monitor monitor;
    private String cpuId;

    private int[] secuencia = {99, 99, 99};

    /**
     * Constructor de clase
     * @param monitor monitor de la red
     * @param cpuID id del cpu
     */
    public CPUPower(Monitor monitor, String cpuID) {
        setName("CPUPower " + cpuID);
        this.monitor = monitor;
        this.cpuId = cpuID;

        if(cpuID.equalsIgnoreCase("A")) {
            this.secuencia[0] = 2;
            this.secuencia[1] = 3;
            this.secuencia[2] = 4;
        }
        else if(cpuID.equalsIgnoreCase("B")) {
            this.secuencia[0] = 9;
            this.secuencia[1] = 10;
            this.secuencia[2] = 11;
        }
    }

    /**
     * Accion del hilo
     * Encender y apagar el CPU
     */
    @Override
    public void run() {
        if( Main.isLoggingActivated() )
            System.out.println(Colors.RED_BOLD + "INICIO CPUPower " + this.cpuId + Colors.RESET);

        while(!currentThread().isInterrupted() && !CPU.isFinished()) {

            // intento de encendido
            monitor.disparar(secuencia[0]);    // pasar de stand by a encendido

            monitor.disparar(secuencia[1]);    // encender CPU

            if( Main.isLoggingActivated() )
                System.out.println(Colors.RED_BOLD + "ENCENDIDO:                         CPU " + this.cpuId + Colors.RESET);

            monitor.disparar(secuencia[2]);   // apagado

            if( Main.isLoggingActivated() )
                System.out.println(Colors.RED_BOLD + "APAGADO:                           CPU " + this.cpuId + Colors.RESET);

        }
    }

    /**
     * Getter del monitor del CPU
     * @return objeto Monitos correspondiente al CPU
     */
    public Monitor getMonitor() {
        return this.monitor;
    }
}
