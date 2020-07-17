package concurrente;

public class CPUPower extends Thread {

    private final Monitor monitor;
    private final String cpuId;
    private final int[] secuencia;

    /**
     * Constructor de clase
     * @param monitor monitor de la red
     * @param cpuID id del cpu
     * @param secuencia secuencia de disparos
     */
    public CPUPower(Monitor monitor, String cpuID, int[] secuencia)
    {
        setName("CPUPower " + cpuID);
        this.monitor = monitor;
        this.cpuId = cpuID;
        this.secuencia = secuencia;
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
