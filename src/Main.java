public class Main {

    private static final int CANTIDADPROCESOS = 1000;
    private static final int ARRIVALRATE = 3;
    
    public static void main(String[] args) throws InterruptedException {

    	Thread.currentThread().setName("Main");
                                //  0, 1, 2, 3, 4, 5, 6, 7, 8, 9,10,11,12,13,14,15
        int[] marcadoInicial = {    1, 0, 0, 0, 0, 1, 0, 0, 1, 1, 0, 0, 0, 0, 0, 1  };

        int[][] incidenciaFrontward = { // TRANSICIONES     0, 1, 2, 3, 4, 5, 6, 7, 8, 9,10,11,12,13,14
                                                        {   1, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0    }, // 0  P
                                                        {   0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0    }, // 1  L
                                                        {   1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0    }, // 2  A
                                                        {   1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0    }, // 3  Z
                                                        {   0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0    }, // 4  A
                                                        {   0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0    }, // 5  S
                                                        {   0, 0, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0    }, // 6
                                                        {   0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0    }, // 7
                                                        {   0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0    }, // 8
                                                        {   0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0    }, // 9
                                                        {   0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0    }, // 10
                                                        {   0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0    }, // 11
                                                        {   0, 0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0    }, // 12
                                                        {   0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 0, 0, 0, 0    }, // 13
                                                        {   0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0    }, // 14
                                                        {   0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1    }, // 15
        };

        int[][] incidenciaBackward = {  // TRANSICIONES     0, 1, 2, 3, 4, 5, 6, 7, 8, 9,10,11,12,13,14
                                                        {   0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0     }, // 0  P
                                                        {   1, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0     }, // 1  L
                                                        {   0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0     }, // 2  A
                                                        {   0, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0     }, // 3  Z
                                                        {   0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0     }, // 4  A
                                                        {   0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0     }, // 5  S
                                                        {   0, 0, 0, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0     }, // 6
                                                        {   0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0     }, // 7
                                                        {   0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0     }, // 8
                                                        {   0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0     }, // 9
                                                        {   0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0     }, // 10
                                                        {   0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0     }, // 11
                                                        {   0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 0, 0, 0, 0, 0     }, // 12
                                                        {   0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 0, 0, 0     }, // 13
                                                        {   0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1     }, // 14
                                                        {   0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0     }, // 15
        };

        RedDePetri redDePetri = new RedDePetri(marcadoInicial, incidenciaFrontward, incidenciaBackward);
        Politicas politicas = new Politicas(incidenciaBackward.length);
        Monitor monitor = new Monitor(redDePetri);

        CPUBuffer cpuBufferA = new CPUBuffer();
        CPUBuffer cpuBufferB = new CPUBuffer();

        CPUPower cpuPowerA = new CPUPower(monitor, "A", cpuBufferA);
        CPUProcessing cpuProcessingA = new CPUProcessing(cpuPowerA);
        CPUPower cpuPowerB = new CPUPower(monitor, "B", cpuBufferB);
        CPUProcessing cpuProcessingB = new CPUProcessing(cpuPowerB);

        GeneradorProcesos generadorProcesos = new GeneradorProcesos(monitor, CANTIDADPROCESOS,
                ARRIVALRATE, cpuBufferA, cpuBufferB);


        generadorProcesos.start();

        cpuPowerA.start();
        cpuPowerB.start();

        cpuProcessingA.start();
        cpuProcessingB.start();

    }
}


