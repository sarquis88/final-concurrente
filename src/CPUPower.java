/**
 * HILO ENCARGADO DE DISPARAR TRANSICIONES 2, 3, 4, 9, 10, Y 11
 * ENCENDIDO/APAGADO DE CPUs
 */

public class CPUPower extends Thread {

    private Monitor monitor;
    private String cpuId;

    private double standByDelayAvg;
    private boolean isActive;
    private boolean isOn;
    private long inicioSleep;
    private long tiempoSleep;

    private int[] secuencia = {99, 99, 99};

    private static boolean isFinished = false;

    /**
     * Constructor de clase
     * @param monitor monitor de la red
     * @param standByDelayAvg tiempo promedio de delay al prenderse
     * @param cpuID id del cpu
     */
    public CPUPower(Monitor monitor, double standByDelayAvg, String cpuID) {
        setName("CPUPower " + cpuID);
        this.monitor = monitor;
        this.standByDelayAvg = standByDelayAvg;
        this.isActive = false;
        this.isOn = false;
        this.cpuId = cpuID;
        this.tiempoSleep = 0;

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
        System.out.println(Colors.RED_BOLD + "INICIO CPUPower " + this.cpuId + Colors.RESET);
        boolean flag = false;

        while(!currentThread().isInterrupted()) {

            if(this.isFinished)
                return;

            // intento de encendido
            try {
                monitor.entrar(secuencia[0]);    // pasar de stand by a encendido
                monitor.salir();
            } catch (InterruptedException e) {
                interruptedReaccion();
            }

            try {
                monitor.entrar(secuencia[1]);    // encender CPU

                Main.dormir(this.standByDelayAvg);
                this.isOn = true;
                if(flag)
                    this.tiempoSleep = this.tiempoSleep + (System.currentTimeMillis() - this.inicioSleep);
                else
                    flag = true;

                monitor.salir();
                System.out.println(Colors.RED_BOLD + "ENCENDIDO:                         CPU " + this.cpuId + Colors.RESET);
            } catch (InterruptedException e) {
                interruptedReaccion();
            }

            // intento de apagado
            try {
                monitor.entrar(secuencia[2]);   // apagado

                this.isOn = false;
                this.inicioSleep = System.currentTimeMillis();

                monitor.salir();
                System.out.println(Colors.RED_BOLD + "APAGADO:                           CPU " + this.cpuId + Colors.RESET);
            } catch (InterruptedException e) {
                interruptedReaccion();
            }

            // duerme el hilo para volver a chequear, dentro de un tiempo, el encendido/apagado
            // si no se duerme acá, el programa se vuelve mas lento y sobrecargado
            try {
                Main.dormir(this.standByDelayAvg);
            }
            catch (InterruptedException e) {
                interruptedReaccion();
            }
        }
    }

    /**
     * Getter del monitor del CPU
     * @return objeto Monitos correspondiente al CPU
     */
    public Monitor getMonitor() {
        return this.monitor;
    }

    /**
     * Seteo el estado de actividad del cpu
     * @param isActive true para activo, false para inactivo
     */
    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }

    /**
     * Reaccion a interrupcion
     * VACIO
     */
    private void interruptedReaccion() {}

    /**
     * Getter del tiempo total en estado Off
     * @return tiempo de double
     */
    public double getTiempoSleep() {
        return (this.tiempoSleep / 1000.00);
    }

    /**
     * Marca el final del procesado
     * Metodo llamado por CPUProcessing
     */
    public static void setFin() {
        isFinished = true;
    }
}
