public class Main {

    private static final int CANTIDADPROCESOS = 500;       // cantidad de procesos a generar

    private static final double ARRIVALRATEMIN = 2.00;      // tiempo minimo entre generacion de procesos
    private static final double ARRIVALRATEMAX = 6.00;     // tiempo maximo entre generacion de procesos

    private static final double SERVICERATEMIN = 6.00;      // tiempo minimo de procesamiento
    private static final double SERVICERATEMAX = 10.00;     // tiempo maximo de procesamiento

    private static final double STANDBYDELAYMIN = 30;       // tiempo minimo de encendido
    private static final double STANDBYDELAYMAX = 35;       // tiempo maximo de encendido

    private static final boolean PRINTMARCADO = false;      // true: se imprimen los disparos y los marcados de forma dinamica

    private static long inicio;
    private static long fin;

    private static Thread[] threads = {null, null, null, null, null};

    private static RedDePetri redDePetri;
    private static CPUProcessing cpuProcessingA;
    private static CPUProcessing cpuProcessingB;
    private static CPUPower cpuPowerA;
    private static CPUPower cpuPowerB;

    public static void main(String[] args) {

        Thread.currentThread().setName("Main");

        //                          0, 1, 2, 3, 4, 5, 6, 7, 8, 9,10,11,12,13,14,15
        int[] marcadoInicial = {    1, 0, 1, 0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 1   };

        int[][] incidenciaFrontward = { // TRANSICIONES     0, 1, 2, 3, 4, 5, 6, 7, 8, 9,10,11,12,13,14
                                                        {   0, 1, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0  }, // 0  P
                                                        {   1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0  }, // 1  L
                                                        {   0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0  }, // 2  A
                                                        {   0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0  }, // 3  Z
                                                        {   0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0  }, // 4  A
                                                        {   0, 0, 0, 1, 0, 1, 0, 1, 0, 0, 0, 0, 0, 0, 0  }, // 5  S
                                                        {   0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0  }, // 6
                                                        {   0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0  }, // 7
                                                        {   0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0  }, // 8
                                                        {   0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0  }, // 9
                                                        {   0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0  }, // 10
                                                        {   0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0  }, // 11
                                                        {   0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 1, 0, 1  }, // 12
                                                        {   0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0  }, // 13
                                                        {   0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0  }, // 14
                                                        {   0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0  }, // 15
        };

        int[][] incidenciaBackward = { // TRANSICIONES      0, 1, 2, 3, 4, 5, 6, 7, 8, 9,10,11,12,13,14
                                                        {   1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0  }, // 0  P
                                                        {   0, 1, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0  }, // 1  L
                                                        {   0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0  }, // 2  A
                                                        {   0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0  }, // 3  Z
                                                        {   0, 0, 1, 1, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0  }, // 4  A
                                                        {   0, 0, 0, 0, 1, 1, 0, 1, 0, 0, 0, 0, 0, 0, 0  }, // 5  S
                                                        {   0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0  }, // 6
                                                        {   0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0  }, // 7
                                                        {   0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0  }, // 8
                                                        {   0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0  }, // 9
                                                        {   0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0  }, // 10
                                                        {   0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 1  }, // 11
                                                        {   0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 0, 1  }, // 12
                                                        {   0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0  }, // 13
                                                        {   0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0  }, // 14
                                                        {   0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0  }, // 15
        };

        redDePetri = new RedDePetri(marcadoInicial, incidenciaFrontward, incidenciaBackward);
        Monitor monitor = new Monitor(redDePetri);

        CPUBuffer cpuBufferA = new CPUBuffer();
        cpuPowerA = new CPUPower(monitor, cpuBufferA, STANDBYDELAYMAX, STANDBYDELAYMIN, "A");
        cpuProcessingA = new CPUProcessing(cpuPowerA, SERVICERATEMAX, SERVICERATEMIN, "A");

        CPUBuffer cpuBufferB = new CPUBuffer();
        cpuPowerB = new CPUPower(monitor, cpuBufferB, STANDBYDELAYMAX, STANDBYDELAYMIN, "B");
        cpuProcessingB = new CPUProcessing(cpuPowerB, SERVICERATEMAX, SERVICERATEMIN, "B");

        CPUGenerator cpuGenerator = new CPUGenerator(monitor, CANTIDADPROCESOS, ARRIVALRATEMAX, ARRIVALRATEMIN, cpuBufferA, cpuBufferB);

        threads[0] = cpuGenerator;
        threads[1] = cpuPowerA;
        threads[2] = cpuProcessingA;
        threads[3] = cpuPowerB;
        threads[4] = cpuProcessingB;

        for(Thread thread : threads)
            thread.start();
    }

    public static void setInicio() {
        inicio = System.currentTimeMillis();
    }

    public static void setFin(String interrupter) {
        fin = System.currentTimeMillis();

        if(interrupter.equalsIgnoreCase("A"))
            threads[4].interrupt();
        else if(interrupter.equalsIgnoreCase("B"))
            threads[2].interrupt();
    }

    public static int getCantidadProcesos() {
        return CANTIDADPROCESOS;
    }

    public static boolean isPrintMarcado() {
        return PRINTMARCADO;
    }

    public static void exit() {
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        double tiempoEjecucion = (fin - inicio) / 1000.00;

        double tiempoSleepA = cpuPowerA.getTiempoSleep();
        double tiempoSleepB = cpuPowerB.getTiempoSleep();
        double tiempoSleepRelA = (tiempoSleepA / tiempoEjecucion) * 100.00;
        double tiempoSleepRelB = (tiempoSleepB / tiempoEjecucion) * 100.00;

        System.out.println(Colors.BLUE_BOLD + "\n--> TIEMPO: " + String.format("%.2f", tiempoEjecucion) + " [seg]" + Colors.RESET);
        System.out.println(Colors.BLUE_BOLD + "--> TIEMPO EN OFF DE CPU A: " + String.format("%.2f", tiempoSleepA) + " [seg] (%" + String.format("%.2f", tiempoSleepRelA) + ")" + Colors.RESET);
        System.out.println(Colors.BLUE_BOLD + "--> TIEMPO EN OFF DE CPU B: " + String.format("%.2f", tiempoSleepB) + " [seg] (%" + String.format("%.2f", tiempoSleepRelB) + ")" + Colors.RESET);
        System.out.println(Colors.BLUE_BOLD + "\n--> TRANSICIONES DISPARADAS: " + redDePetri.getTransicionesDisparadas() + Colors.RESET);
        System.out.println(Colors.BLUE_BOLD + "\n--> PROCESOS TERMINADOS POR CPU A: " + cpuProcessingA.getProcesados() + Colors.RESET);
        System.out.println(Colors.BLUE_BOLD + "--> PROCESOS TERMINADOS POR CPU B: " + cpuProcessingB.getProcesados() + Colors.RESET);

        System.exit(0);
    }
}


