import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

import static java.lang.Math.round;
import static java.lang.Thread.currentThread;

public class Main {

    private static final int CANTIDADPROCESOS = 1000;        // cantidad de procesos a generar

    private static final double ARRIVALRATEAVG = 2.00;      // tiempo promedio entre generacion de procesos

    private static final double SERVICERATEAVG = 15.00;     // tiempo promedio de procesamiento
    private static final int FACTORA = 1;                   // factor de multiplicacion para serviceRate de A
    private static final int FACTORB = 1;                   // factor de multiplicacion para serviceRate de B

    private static final double STANDBYDELAYAVG = 30.00;       // tiempo promedio de encendido

    private static final boolean GARBAGECOLLECTION = true;

    private static long inicio;
    private static long fin;

    private static Thread[] threads = {null, null, null, null, null, null, null};

    private static RedDePetri redDePetri;
    private static CPUProcessing cpuProcessingA;
    private static CPUProcessing cpuProcessingB;
    private static CPUPower cpuPowerA;
    private static CPUPower cpuPowerB;

    public static void main(String[] args) {

        currentThread().setName("Main");

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

        int[][] matrizInhibidora = { // PLAZAS              0, 1, 2, 3, 4, 5, 6, 7, 8, 9,10,11,12,13,14,15
                                                        {   0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0  }, // 0  T
                                                        {   0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0  }, // 1  R
                                                        {   0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0  }, // 2  A
                                                        {   0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0  }, // 3  N
                                                        {   0, 0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0  }, // 4  S
                                                        {   0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0  }, // 5  I
                                                        {   0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0  }, // 6  C
                                                        {   0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0  }, // 7  I
                                                        {   0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0  }, // 8  O
                                                        {   0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0  }, // 9  N
                                                        {   0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0  }, // 10 E
                                                        {   0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 0  }, // 11 S
                                                        {   0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0  }, // 12
                                                        {   0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0  }, // 13
                                                        {   0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0  }, // 14
        };

        redDePetri = new RedDePetri(marcadoInicial, incidenciaFrontward, incidenciaBackward, matrizInhibidora);
        Monitor monitor = new Monitor(redDePetri);

        CPUBuffer cpuBufferA = new CPUBuffer();
        cpuPowerA = new CPUPower(monitor, STANDBYDELAYAVG, "A");
        cpuProcessingA = new CPUProcessing(cpuPowerA, cpuBufferA, SERVICERATEAVG * FACTORA, "A");

        CPUBuffer cpuBufferB = new CPUBuffer();
        cpuPowerB = new CPUPower(monitor, STANDBYDELAYAVG, "B");
        cpuProcessingB = new CPUProcessing(cpuPowerB, cpuBufferB, SERVICERATEAVG * FACTORB, "B");

        CPUGenerator cpuGenerator = new CPUGenerator(monitor, CANTIDADPROCESOS, ARRIVALRATEAVG, cpuBufferA, cpuBufferB);

        threads[0] = cpuGenerator;
        threads[1] = cpuPowerA;
        threads[2] = cpuProcessingA;
        threads[3] = cpuPowerB;
        threads[4] = cpuProcessingB;

        if(GARBAGECOLLECTION) {
            CPUGarbageCollector CPUGarbageCollectorA = new CPUGarbageCollector(monitor, SERVICERATEAVG * 1.5, "A");
            CPUGarbageCollector CPUGarbageCollectorB = new CPUGarbageCollector(monitor, SERVICERATEAVG * 1.5, "B");
            threads[5] = CPUGarbageCollectorA;
            threads[6] = CPUGarbageCollectorB;
        }

        for(Thread thread : threads) {
            if(thread != null)
                thread.start();
        }
    }

    public static void setInicio() {
        inicio = System.currentTimeMillis();
    }

    public static void setFin(String interrupter) {
        fin = System.currentTimeMillis();

        if(GARBAGECOLLECTION) {
            threads[5].interrupt();
            threads[6].interrupt();
        }

        if(interrupter.equalsIgnoreCase("A"))
            threads[4].interrupt();
        else if(interrupter.equalsIgnoreCase("B"))
            threads[2].interrupt();
    }

    public static int getCantidadProcesos() {
        return CANTIDADPROCESOS;
    }

    public static void exit() {
        // se duerme el hilo para darle tiempo a los CPUs para que se apaguen
        // de lo contrario, el programa puede terminar con los CPUs en modo On
        try {
            Thread.sleep(round(STANDBYDELAYAVG * 3));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        double tiempoEstimado = ((CANTIDADPROCESOS * SERVICERATEAVG) / 2) / 1000;
        double tiempoEjecucion = (fin - inicio) / 1000.00;
        double tiempoSleepA = cpuPowerA.getTiempoSleep();
        double tiempoSleepB = cpuPowerB.getTiempoSleep();
        double tiempoSleepRelA = (tiempoSleepA / tiempoEjecucion) * 100.00;
        double tiempoSleepRelB = (tiempoSleepB / tiempoEjecucion) * 100.00;

        String pInvariantes;
        if(redDePetri.getIsPInvariantesCorrecto())
            pInvariantes = Colors.GREEN_BOLD + "CORRECTO" + Colors.RESET;
        else
            pInvariantes = Colors.RED_BOLD + "INCORRECTO" + Colors.RESET;

        System.out.println(Colors.BLUE_BOLD + "\n--> TIEMPO: " + String.format("%.2f", tiempoEjecucion) + " [seg]" + Colors.RESET);
        System.out.println(Colors.BLUE_BOLD + "--> TIEMPO ESTIMADO: " + String.format("%.2f", tiempoEstimado) + " [seg]" + Colors.RESET);
        System.out.println(Colors.BLUE_BOLD + "--> TIEMPO EN OFF DE CPU A: " + String.format("%.2f", tiempoSleepA) + " [seg] (%" + String.format("%.2f", tiempoSleepRelA) + ")" + Colors.RESET);
        System.out.println(Colors.BLUE_BOLD + "--> TIEMPO EN OFF DE CPU B: " + String.format("%.2f", tiempoSleepB) + " [seg] (%" + String.format("%.2f", tiempoSleepRelB) + ")" + Colors.RESET);
        System.out.println(Colors.BLUE_BOLD + "\n--> TRANSICIONES DISPARADAS: " + redDePetri.getCantidadTransicionesDisparadas() + Colors.RESET);
        System.out.println(Colors.BLUE_BOLD + "\n--> PROCESOS TERMINADOS POR CPU A: " + cpuProcessingA.getProcesados() + Colors.RESET);
        System.out.println(Colors.BLUE_BOLD + "--> PROCESOS TERMINADOS POR CPU B: " + cpuProcessingB.getProcesados() + Colors.RESET);
        System.out.println(Colors.BLUE_BOLD + "\n--> ANALISIS DE P-INVARIANTES: " + pInvariantes + Colors.RESET);
        System.out.println("\n");

        try {
            File TInvariantesFile = new File("./src/T-Invariantes.txt");

            TInvariantesFile.delete();
            TInvariantesFile.createNewFile();

            BufferedWriter bufferedWriter;
            bufferedWriter = new BufferedWriter(new FileWriter(TInvariantesFile, false));
            bufferedWriter.write(redDePetri.getOrdenTransicionesDisparadas());
            bufferedWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.exit(0);
    }

    /**
     * Dormida del Thread durante un tiempo promedio de timeAvg
     */
    public static void dormir(double timeAvg) throws InterruptedException {
        Random random = new Random();

        double timeMin = timeAvg - timeAvg * 0.3;
        double timeMax = timeAvg + timeAvg * 0.3;

        double sleepTimeDouble = timeMin + (timeMin - timeMax) * random.nextDouble();
        long sleepTime = round(sleepTimeDouble);
        Thread.sleep(sleepTime);
    }
}


