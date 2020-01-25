public class Main {

    private static final int CANTIDADPROCESOS = 500;
    private static final int ARRIVALRATE = 3;
    
    public static void main(String[] args) throws InterruptedException {

                                //  0, 1, 2, 3, 4, 5, 6, 7, 8, 9,10,11,12,13,14,15
        int[] marcadoInicial = {    1, 0, 0, 0, 0, 1, 0, 0, 1, 1, 0, 0, 0, 0, 0, 1  };

        int[][] incidencia = {          // TRANSICIONES     0, 1, 2, 3, 4, 5, 6, 7, 8, 9,10,11,12,13,14
                                                        {   1, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0,-1, 0, 0    }, // 0  P
                                                        {  -1, 0, 0, 0, 0, 0,-1, 0, 0, 0, 0, 0, 1, 0, 0    }, // 1  L
                                                        {   1, 0, 0, 0,-1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0    }, // 2  A
                                                        {   1, 0,-1,-1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0    }, // 3  Z
                                                        {   0, 1,-1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0    }, // 4  A
                                                        {   0,-1, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0    }, // 5  S
                                                        {   0, 0, 1, 0, 0,-1, 0, 0, 0, 0, 0, 0, 0, 0, 0    }, // 6
                                                        {   0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0,-1, 0    }, // 7
                                                        {   0, 0, 0, 0,-1, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0    }, // 8
                                                        {   0, 0, 0, 0, 0, 0, 0,-1, 0, 0, 0, 1, 0, 0, 0    }, // 9
                                                        {   0, 0, 0, 0, 0, 0, 1, 0, 0, 0,-1, 0, 0, 0, 0    }, // 10
                                                        {   0, 0, 0, 0, 0, 0, 0, 1,-1, 0, 0, 0, 0, 0, 0    }, // 11
                                                        {   0, 0, 0, 0, 0, 0, 1, 0,-1,-1, 0, 0, 0, 0, 0    }, // 12
                                                        {   0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0,-1, 0, 0, 0    }, // 13
                                                        {   0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0,-1    }, // 14
                                                        {   0, 0, 0, 0, 0, 0, 0, 0, 0, 0,-1, 0, 0, 0, 1    }, // 15
        };

        RedDePetri redDePetri = new RedDePetri(marcadoInicial, incidencia);
        Politicas politicas = new Politicas(incidencia.length);
        Monitor monitor = new Monitor(redDePetri, politicas);

        CPUBuffer cpuBufferA = new CPUBuffer();
        CPUBuffer cpuBufferB = new CPUBuffer();

        CPUPower cpuPowerA = new CPUPower(monitor, "A", cpuBufferA);
        CPUProcessing cpuProcessingA = new CPUProcessing(cpuPowerA);
        CPUPower cpuPowerB = new CPUPower(monitor, "B", cpuBufferB);
        CPUProcessing cpuProcessingB = new CPUProcessing(cpuPowerB);

        GeneradorProcesos generadorProcesos = new GeneradorProcesos(monitor, CANTIDADPROCESOS,
                ARRIVALRATE, cpuBufferA, cpuBufferB);

        cpuPowerA.start();
        cpuPowerB.start();

        cpuProcessingA.start();
        cpuProcessingB.start();

        generadorProcesos.start();
        generadorProcesos.join();
        cpuProcessingA.join();
        cpuProcessingB.join();
        cpuPowerA.join();
        cpuPowerB.join();

		System.out.println("\nFIN DE EJECUCION");
    }
}


