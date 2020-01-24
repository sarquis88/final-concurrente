public class CPUPower extends Thread {

    private Monitor monitor;
    private String CPUId;
    private boolean isOn;

    /**
     * Constructor de clase
     * @param monitor monitor del CPUProcessing
     * @param CPUId id del CPU a controlar (A o B)
     */
    public CPUPower(Monitor monitor, String CPUId){
        this.monitor = monitor;
        this.CPUId = CPUId;
        this.isOn = false;
    }

    /**
     * Accion del hilo
     * Encender y apagar el CPU
     */
    @Override
    public void run() {
        int[] secuencia = {99, 99};
        if(this.CPUId.equalsIgnoreCase("A")) {      // secuencia de transiciones de CPU A
            secuencia[0] = 1;
            secuencia[1] = 2;
        }
        else if(this.CPUId.equalsIgnoreCase("B")) {
            secuencia[0] = 7;                           // secuencia de transiciones de CPU B
            secuencia[1] = 8;
        }
        else
            return;                             // error

        while( !(currentThread().isInterrupted()) ) {
            try {
                monitor.entrar(secuencia[0]);    // pasar de stand by a encendido
                Thread.sleep(1);
                monitor.salir();

                monitor.entrar(secuencia[1]);    // encender CPU
                this.isOn = true;
                monitor.salir();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Apagado de CPU
     * Disparo de transicion y cambio de estado
     */
    public void apagar() {

        int transicion = 99;
        if(this.CPUId.equalsIgnoreCase("A"))
            transicion = 5;
        else if(this.CPUId.equalsIgnoreCase("B"))
            transicion = 11;
        else
            return;                         // error

        try {
            if(monitor.entrar(transicion)) {     // apagado de CPU
                this.isOn = false;
                System.out.println("APAGADO CPU:                  " + CPUId);
            }
            monitor.salir();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Getter del Id del CPU
     * @return String correspondiente al ID
     */
    public String getCPUId() {
        return this.CPUId;
    }

    /**
     * Getter del estado del CPU
     * @return true si el CPU esta on, de lo contrario false
     */
    public boolean isOn() {
        return this.isOn;
    }

    /**
     * Getter del monitor del CPU
     * @return objeto Monitos correspondiente al CPU
     */
    public Monitor getMonitor() {
        return this.monitor;
    }
}
