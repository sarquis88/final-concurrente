public class Main {

    private static final int CANTIDADPROCESOS = 100;
    private static final int ARRIVALRATE = 10;
    private static final int SERVICERATE = 3;
    private static final int STANDBYDELAY = 30;

    private static final boolean PRINTMARCADO = false;

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
        cpuPowerA = new CPUPower(monitor, cpuBufferA, STANDBYDELAY, "A");
        cpuProcessingA = new CPUProcessing(cpuPowerA, SERVICERATE, "A");

        CPUBuffer cpuBufferB = new CPUBuffer();
        cpuPowerB = new CPUPower(monitor, cpuBufferB, STANDBYDELAY, "B");
        cpuProcessingB = new CPUProcessing(cpuPowerB, SERVICERATE, "B");

        CPUGenerator cpuGenerator = new CPUGenerator(monitor, CANTIDADPROCESOS, ARRIVALRATE, cpuBufferA, cpuBufferB);

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

        double tiempo = (fin - inicio) / 1000.00;
        System.out.println(Colors.BLUE_BOLD + "\n--> TIEMPO: " + tiempo + " [seg]" + Colors.RESET);
        System.out.println(Colors.BLUE_BOLD + "--> TIEMPO EN OFF DE CPU A: " + cpuPowerA.getTiempoSleep() + " [seg]" + Colors.RESET);
        System.out.println(Colors.BLUE_BOLD + "--> TIEMPO EN OFF DE CPU B: " + cpuPowerB.getTiempoSleep() + " [seg]" + Colors.RESET);
        System.out.println(Colors.BLUE_BOLD + "\n--> TRANSICIONES DISPARADAS: " + redDePetri.getTransicionesDisparadas() + Colors.RESET);
        System.out.println(Colors.BLUE_BOLD + "\n--> PROCESOS TERMINADOS POR CPU A: " + cpuProcessingA.getProcesados() + Colors.RESET);
        System.out.println(Colors.BLUE_BOLD + "--> PROCESOS TERMINADOS POR CPU B: " + cpuProcessingB.getProcesados() + Colors.RESET);

        System.exit(0);
    }
}


