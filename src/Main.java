import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import static java.lang.Math.round;
import static java.lang.Thread.currentThread;

public class Main {

    private static final int CANTIDADPROCESOS = 50;        // cantidad de procesos a generar

    private static final long ARRIVALRATE = 10;      // tiempo promedio entre generacion de procesos
    private static final long SERVICERATE = 15;     // tiempo promedio de procesamiento
    private static final long STANDBYDELAY = 30;       // tiempo promedio de encendido
    private static final boolean GARBAGECOLLECTION = true;
    private static final boolean LOGGING = true;
    private static final String invariantesFile = "./src/T-Invariantes.txt";

    private static long inicio;
    private static long fin;

    private static Thread[] threads = {null, null, null, null};

    private static int[][] invariantes;

    private static RedDePetri redDePetri;
    private static CPUProcessing cpuProcessing;

    public static void main(String[] args) {

        currentThread().setName("Main");

        //                          0, 1, 2, 3, 4, 5, 6, 7, 8
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
                                                        {   0, 0, 0, 0, 0, 0, 1, 0,  }  // 8
        };

        int[][] incidenciaBackward = { // TRANSICIONES      0, 1, 2, 3, 4, 5, 6, 7
                                                        {   1, 0, 0, 0, 0, 0, 0, 0,   }, // 0  P
                                                        {   0, 1, 0, 0, 0, 0, 0, 0,   }, // 1  L
                                                        {   0, 0, 1, 0, 0, 0, 0, 0,   }, // 2  A
                                                        {   0, 0, 0, 1, 0, 0, 0, 0,   }, // 3  Z
                                                        {   0, 0, 1, 1, 0, 0, 0, 1,   }, // 4  A
                                                        {   0, 0, 0, 0, 1, 1, 0, 1,   }, // 5  S
                                                        {   0, 0, 0, 0, 0, 1, 0, 0,   }, // 6
                                                        {   0, 0, 0, 0, 0, 0, 1, 0,   }, // 7
                                                        {   0, 0, 0, 0, 0, 1, 0, 0,   }, // 8
        };

        int[][] matrizInhibidora = { // PLAZAS              0, 1, 2, 3, 4, 5, 6, 7, 8
                                                        {   0, 0, 0, 0, 0, 0, 0, 0, 0  }, // 0  T
                                                        {   0, 0, 0, 0, 0, 0, 0, 0, 0  }, // 1  R
                                                        {   0, 0, 0, 0, 0, 0, 0, 0, 0  }, // 2  A
                                                        {   0, 0, 0, 0, 0, 0, 0, 0, 0  }, // 3  N
                                                        {   0, 0, 0, 0, 0, 0, 1, 1, 0  }, // 4  S
                                                        {   0, 0, 0, 0, 0, 0, 0, 0, 0  }, // 5  I
                                                        {   0, 0, 0, 0, 0, 0, 0, 0, 0  }, // 6  C
                                                        {   0, 0, 0, 0, 0, 0, 0, 0, 0  }, // 7  I
        };

        invariantes = new int[][]   {
                {   0,  1,  2,  3,  5,  6,  4 },
                {   0,  1,  7,  5,  6, -1, -1 }
        };

        long[] timeStamp = new long[incidenciaBackward[0].length];
        long[] alfa = new long[incidenciaBackward[0].length];
        long[] beta = new long[incidenciaBackward[0].length];
        for(int i = 0; i < timeStamp.length ; i++) {
            alfa[i] = 0;
            beta[i] = Long.MAX_VALUE;
            timeStamp[i] = 0;
        }

        alfa[0] = ARRIVALRATE;
        alfa[6] = SERVICERATE;
        alfa[3] = STANDBYDELAY;

        // TRANSICIONES         0, 1, 2, 3, 4, 5, 6, 7
        int[] prioridades = {   5,11, 6, 4, 2, 1, 0, 7  };

        Monitor monitor = new Monitor(new Politica(prioridades));
        redDePetri = new RedDePetri(marcadoInicial, incidenciaFrontward, incidenciaBackward, matrizInhibidora, monitor,
                timeStamp, alfa, beta);
        monitor.setRedDePetri(redDePetri);

        CPUPower cpuPower = new CPUPower(monitor);
        cpuProcessing = new CPUProcessing(cpuPower);

        ProcessGenerator processGenerator = new ProcessGenerator(monitor, CANTIDADPROCESOS);

        threads[0] = processGenerator;
        threads[1] = cpuPower;
        threads[2] = cpuProcessing;

        if(GARBAGECOLLECTION) {
            CPUGarbageCollector CPUGarbageCollector = new CPUGarbageCollector(monitor);
            threads[3] = CPUGarbageCollector;
        }

        for(Thread thread : threads) {
            if(thread != null)
                thread.start();
        }
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

    public static boolean isLoggingActivated() {
        return LOGGING;
    }

    public static void exit() {
        // se duerme el hilo para darle tiempo a los CPUs para que se apaguen
        // de lo contrario, el programa puede terminar con los CPUs en modo On
        try {
            Thread.sleep(round(STANDBYDELAY * 3));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        try {
            File TInvariantesFile = new File(invariantesFile);

            TInvariantesFile.delete();
            TInvariantesFile.createNewFile();

            BufferedWriter bufferedWriter;
            bufferedWriter = new BufferedWriter(new FileWriter(TInvariantesFile, false));
            bufferedWriter.write(redDePetri.getOrdenTransicionesDisparadas());
            bufferedWriter.flush();
            bufferedWriter.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        double tiempoEjecucion = (fin - inicio) / 1000.00;

        String pInvariantes, tInvariantes;
        if(redDePetri.getIsPInvariantesCorrecto())
            pInvariantes = Colors.GREEN_BOLD + "CORRECTO" + Colors.RESET;
        else
            pInvariantes = Colors.RED_BOLD + "INCORRECTO" + Colors.RESET;

        if(InvarianteTest.checkInvariantes(invariantesFile, invariantes))
            tInvariantes = Colors.GREEN_BOLD + "CORRECTO" + Colors.RESET;
        else
            tInvariantes = Colors.RED_BOLD + "INCORRECTO" + Colors.RESET;

        if( LOGGING )
        {
            System.out.println(Colors.BLUE_BOLD + "\n--> TIEMPO: " + String.format("%.2f", tiempoEjecucion) + " [seg]" + Colors.RESET);
            System.out.println(Colors.BLUE_BOLD + "\n--> PROCESOS TERMINADOS POR CPU A: " + cpuProcessing.getProcesados() + Colors.RESET);
            System.out.println(Colors.BLUE_BOLD + "\n--> ANALISIS DE P-INVARIANTES: " + pInvariantes + Colors.RESET);
            System.out.println(Colors.BLUE_BOLD + "--> ANALISIS DE T-INVARIANTES: " + tInvariantes + Colors.RESET);
        }

        System.exit(0);
    }
}

// doxygen

/*! \mainpage Trabajo Práctico III
 * Alumno: Tomas Sarquis\n\n
 * Matricula: 39884977\n\n
 * Repositorio: https://github.com/sarquis88/final-concurrente
 */