public class Main {

    private static final int CANTIDADPROCESOS = 2000;
    private static final int ARRIVALRATE = 5;
    private static final int SERVICERATE = 6;
    private static final int STANDBYDELAY = 100;

    private static final boolean PRINTMARCADO = false;

    private static long inicio;
    private static long fin;
    private static Thread[] threads = {null, null, null};

    public static void main(String[] args) throws InterruptedException {

        Thread.currentThread().setName("Main");

        //  0, 1, 2, 3, 4, 5, 6, 7, 8
        int[] marcadoInicial = {    1, 0, 1, 0, 0, 0, 0, 0, 1   };

        int[][] incidenciaFrontward = { // TRANSICIONES     0, 1, 2, 3, 4, 5, 6, 7
                {   0, 1, 0, 0, 0, 0, 0, 0  }, // 0  P
                {   1, 0, 0, 0, 0, 0, 0, 0  }, // 1  L
                {   0, 0, 0, 0, 1, 0, 0, 0  }, // 2  A
                {   0, 0, 1, 0, 0, 0, 0, 0  }, // 3  Z
                {   0, 1, 1, 0, 0, 0, 0, 0  }, // 4  A
                {   0, 0, 0, 1, 0, 1, 0, 1  }, // 5  S
                {   0, 1, 0, 0, 0, 0, 0, 0  }, // 6
                {   0, 0, 0, 0, 0, 1, 0, 0  }, // 7
                {   0, 0, 0, 0, 0, 0, 1, 0, }, // 8
        };

        int[][] incidenciaBackward = { // TRANSICIONES      0, 1, 2, 3, 4, 5, 6, 7
                {   1, 0, 0, 0, 0, 0, 0, 0  }, // 0  P
                {   0, 1, 0, 0, 0, 0, 0, 0  }, // 1  L
                {   0, 0, 1, 0, 0, 0, 0, 0  }, // 2  A
                {   0, 0, 0, 1, 0, 0, 0, 0  }, // 3  Z
                {   0, 0, 1, 1, 0, 0, 0, 1  }, // 4  A
                {   0, 0, 0, 0, 1, 1, 0, 1  }, // 5  S
                {   0, 0, 0, 0, 0, 1, 0, 0  }, // 6
                {   0, 0, 0, 0, 0, 0, 1, 0  }, // 7
                {   0, 0, 0, 0, 0, 1, 0, 0, }, // 8
        };

        RedDePetri redDePetri = new RedDePetri(marcadoInicial, incidenciaFrontward, incidenciaBackward);
        Monitor monitor = new Monitor(redDePetri);

        CPUBuffer cpuBuffer = new CPUBuffer();
        CPUPower cpuPower = new CPUPower(monitor, cpuBuffer, STANDBYDELAY);
        CPUProcessing cpuProcessing = new CPUProcessing(cpuPower, SERVICERATE);
        CPUGenerator cpuGenerator = new CPUGenerator(monitor, CANTIDADPROCESOS, ARRIVALRATE, cpuBuffer);

        threads[0] = cpuGenerator;
        threads[1] = cpuPower;
        threads[2] = cpuProcessing;

        for(Thread thread : threads)
            thread.start();

        for(Thread thread : threads)
            thread.join();

        double tiempo = (fin - inicio) / 1000.00;
        System.out.println(Colors.BLUE_BOLD + "\n          --> TIEMPO: " + tiempo + " [seg] <--" + Colors.RESET);
        System.out.println(Colors.BLUE_BOLD + "      --> TRANSICIONES DISPARADAS: " + redDePetri.getTransicionesDisparadas() + " <--" + Colors.RESET);
    }

    public static void setInicio() {
        inicio = System.currentTimeMillis();
    }

    public static void setFin() {
        fin = System.currentTimeMillis();
    }

    public static int getCantidadProcesos() {
        return CANTIDADPROCESOS;
    }

    public static boolean isPrintMarcado() {
        return PRINTMARCADO;
    }
}


