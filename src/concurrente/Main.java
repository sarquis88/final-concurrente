package concurrente;

import java.io.*;

import static java.lang.Math.round;
import static java.lang.Thread.currentThread;

public class Main {

    private static final int CANTIDADPROCESOS = 100;          // cantidad de procesos a generar

    private static final long ARRIVALRATE = 10;             // tiempo promedio entre generacion de procesos
    private static final long SERVICERATE = 20;             // tiempo promedio de procesamiento
    private static final int FACTORA = 1;                   // factor de multiplicacion para serviceRate de A
    private static final int FACTORB = 1;                   // factor de multiplicacion para serviceRate de B
    private static final long STANDBYDELAY = 30;            // tiempo promedio de encendido

    private static final boolean DUALCORE = true;
    private static final boolean GARBAGECOLLECTION = true;

    private static final boolean LOGGING = true;            // logueo en consola
    private static final boolean REALBUFFER = true;         // creacion de objetos Process en ProcessBuffer

    private static final String invariantesFile = "./src/files/T-Invariantes.txt";

    private static long inicio;
    private static long fin;

    private static final Thread[] threads = { null, null, null, null, null, null, null };

    private static int[][] invariantes;

    private static RedDePetri redDePetri;
    private static CPUProcessing cpuProcessingA;
    private static CPUProcessing cpuProcessingB;

    public static void main(String[] args) {

        currentThread().setName("Main");

        int[] prioridades;
        int[] secuenciaCPUPowerA = { 2, 3,  4 };
        int[] secuenciaCPUPowerB = { 8, 9, 10 };
        int[] secuenciaCPUProcessingA = {  5,  6 };
        int[] secuenciaCPUProcessingB = { 11, 12 };
        int[] secuenciaProcessGenerator;
        int secuenciaGarbageCollectorA;
        int secuenciaGarbageCollectorB;
        /* #################################################################################### */
        // inicializacion secuencias de disparos, t-invariantes y path de red
        String petriNetFile;
        if (DUALCORE)
        {
            secuenciaProcessGenerator = new int[]{0, 1, 7};
            secuenciaGarbageCollectorA = 13;
            secuenciaGarbageCollectorB = 14;
            if (GARBAGECOLLECTION)
            {
                petriNetFile = "./src/files/petri-net.xml";
                invariantes = new int[][]{
                        {0, 7, 8, 9, 11, 12, 10},
                        {0, 7, 14, 11, 12, -1, -1},
                        {0, 1, 2, 3, 5, 6, 4},
                        {0, 1, 13, 5, 6, -1, -1}
                };
                // TRANSICIONES          0,  1, 2, 3, 4, 5, 6,  7, 8, 9,10,11,12, 13, 14
                prioridades = new int[]{10, 11, 6, 8, 4, 2, 0, 12, 7, 9, 5, 3, 1, 13, 14};
            } else
            {
                petriNetFile = "./src/files/petri-net-nogarbagecollector.xml";
                invariantes = new int[][]{
                        {0, 7, 8, 9, 11, 12, 10},
                        {0, 1, 2, 3, 5, 6, 4}
                };
                // TRANSICIONES          0,  1, 2, 3, 4, 5, 6,  7, 8, 9,10,11,12
                prioridades = new int[]{10, 11, 6, 8, 4, 2, 0, 12, 7, 9, 5, 3, 1};
            }
        } else
        {
            secuenciaProcessGenerator = new int[]{0, 1};
            secuenciaGarbageCollectorA = 7;
            if (GARBAGECOLLECTION)
            {
                petriNetFile = "./src/files/petri-net-mono.xml";
                invariantes = new int[][]{
                        {0, 1, 2, 3, 5, 6, 4},
                        {0, 1, 7, 5, 6, -1, -1}
                };
                // TRANSICIONES          0, 1, 2, 3, 4, 5, 6, 7
                prioridades = new int[]{5, 6, 3, 4, 2, 1, 0, 7};
            } else
            {
                petriNetFile = "./src/files/petri-net-mono-nogarbagecollector.xml";
                invariantes = new int[][]{
                        {0, 1, 2, 3, 5, 6, 4}
                };
                // TRANSICIONES          0, 1, 2, 3, 4, 5, 6
                prioridades = new int[]{5, 6, 3, 4, 2, 1, 0};
            }
        }
        /* #################################################################################### */
        // parseo de red de petri y creacion de matrices y marcado
        XMLParser xmlParser = new XMLParser(petriNetFile);
        xmlParser.setupParser();
        int[] marcadoInicial = xmlParser.getMarcado();
        int[][] incidenciaBackward = xmlParser.getIncidenciaBackward();
        int[][] incidenciaFrontward = xmlParser.getIncidenciaFrontward();
        int[][] matrizInhibidora = xmlParser.getMatrizInhibidora();
        /* #################################################################################### */
        // seteo de tiempos alpha y beta
        long[] timeStamp = new long[incidenciaBackward[0].length];
        long[] alfa = new long[incidenciaBackward[0].length];
        long[] beta = new long[incidenciaBackward[0].length];
        for (int i = 0; i < timeStamp.length; i++) {
            alfa[i] = 0;
            beta[i] = Long.MAX_VALUE;
            timeStamp[i] = 0;
        }
        alfa[0] = ARRIVALRATE;
        alfa[6] = SERVICERATE * FACTORA;
        alfa[3] = STANDBYDELAY;
        if ( DUALCORE )
        {
            alfa[12] = SERVICERATE * FACTORB;
            alfa[9] = STANDBYDELAY;
        }
        /* #################################################################################### */
        // creacion de monitor y red de petri
        Monitor monitor = new Monitor(new Politica(prioridades));
        redDePetri = new RedDePetri(marcadoInicial, incidenciaFrontward, incidenciaBackward, matrizInhibidora, monitor,
                timeStamp, alfa, beta, DUALCORE );
        monitor.setRedDePetri(redDePetri);
        /* #################################################################################### */
        // creacion de buffers
        ProcessBuffer[] buffers = {null, null};
        if( REALBUFFER )
        {
            buffers[0] = new ProcessBuffer();
            buffers[1] = new ProcessBuffer();
        }
        /* #################################################################################### */
        // creacion de threads
        threads[0] = new ProcessGenerator(monitor, CANTIDADPROCESOS, buffers, secuenciaProcessGenerator);
        if( DUALCORE )
        {
            CPUPower cpuPowerA = new CPUPower(monitor, "A", secuenciaCPUPowerA);
            threads[1] = cpuPowerA;
            cpuProcessingA = new CPUProcessing(cpuPowerA, "A", buffers[0], secuenciaCPUProcessingA);
            threads[2] = cpuProcessingA;
            CPUPower cpuPowerB = new CPUPower(monitor, "B", secuenciaCPUPowerB);
            threads[3] = cpuPowerB;
            cpuProcessingB = new CPUProcessing(cpuPowerB, "B", buffers[1], secuenciaCPUProcessingB);
            threads[4] = cpuProcessingB;

            if(GARBAGECOLLECTION) {
                CPUGarbageCollector CPUGarbageCollectorA = new CPUGarbageCollector(monitor, "A", secuenciaGarbageCollectorA);
                CPUGarbageCollector CPUGarbageCollectorB = new CPUGarbageCollector(monitor, "B", secuenciaGarbageCollectorB);
                threads[5] = CPUGarbageCollectorA;
                threads[6] = CPUGarbageCollectorB;
            }
        }
        else
        {
            CPUPower cpuPowerA = new CPUPower(monitor, "A", secuenciaCPUPowerA);
            threads[1] = cpuPowerA;
            cpuProcessingA = new CPUProcessing(cpuPowerA, "A", buffers[0], secuenciaCPUProcessingA);
            threads[2] = cpuProcessingA;
            if(GARBAGECOLLECTION) {
                CPUGarbageCollector CPUGarbageCollectorA = new CPUGarbageCollector(monitor, "A", secuenciaGarbageCollectorA);
                threads[5] = CPUGarbageCollectorA;
            }
        }
        /* #################################################################################### */
        // inicializacion de threads
        for(Thread thread : threads) {
            if(thread != null)
                thread.start();
        }
        /* #################################################################################### */
    }

    /**
     * Inicio del clock que cuenta la duracion del programa
     */
    public static void setInicio() {
        inicio = System.currentTimeMillis();
    }

    /**
     * Fin del clock que cuenta la duracion del programa;
     */
    public static void setFin() {
        fin = System.currentTimeMillis();
    }

    /**
     * Getter de cantidad de procesos
     * @return marco CANTIDADPROCESOS
     */
    public static int getCantidadProcesos() {
        return CANTIDADPROCESOS;
    }

    /**
     * Getter de logging
     * @return marco LOGGING
     */
    public static boolean isLoggingActivated() {
        return LOGGING;
    }

    /**
     * Metodo llamado por CPUProcessing al procesar ultimo proceso
     */
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
        InvarianteTest invarianteTest = new InvarianteTest(invariantesFile);

        if(redDePetri.getIsPInvariantesCorrecto())
            pInvariantes = Colors.GREEN_BOLD + "CORRECTO" + Colors.RESET;
        else
            pInvariantes = Colors.RED_BOLD + "INCORRECTO" + Colors.RESET;

        if(invarianteTest.checkInvariantes(invariantes))
            tInvariantes = Colors.GREEN_BOLD + "CORRECTO" + Colors.RESET;
        else
            tInvariantes = Colors.RED_BOLD + "INCORRECTO" + Colors.RESET;

        System.out.println(Colors.BLUE_BOLD + "\n--> TIEMPO: " + String.format("%.2f", tiempoEjecucion) + " [seg]" + Colors.RESET);
        if( LOGGING )
        {
            System.out.println(Colors.BLUE_BOLD + "\n--> PROCESOS TERMINADOS POR CPU A: " + cpuProcessingA.getProcesados() + Colors.RESET);
            if( DUALCORE )
                System.out.println(Colors.BLUE_BOLD + "--> PROCESOS TERMINADOS POR CPU B: " + cpuProcessingB.getProcesados() + Colors.RESET);
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