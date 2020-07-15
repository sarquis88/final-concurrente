package concurrente;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import static java.lang.Math.round;
import static java.lang.Thread.currentThread;

public class Main {

    private static final int CANTIDADPROCESOS = 1000;          // cantidad de procesos a generar

    private static final long ARRIVALRATE = 10;             // tiempo promedio entre generacion de procesos

    private static final long SERVICERATE = 15;             // tiempo promedio de procesamiento
    private static final int FACTORA = 1;                   // factor de multiplicacion para serviceRate de A
    private static final int FACTORB = 1;                   // factor de multiplicacion para serviceRate de B

    private static final long STANDBYDELAY = 30;            // tiempo promedio de encendido

    private static final boolean GARBAGECOLLECTION = false;
    private static final boolean LOGGING = true;
    private static final boolean REALBUFFER = true;

    private static final String invariantesFile = "./src/files/T-Invariantes.txt";
    private static final String petriNetFile = "./src/files/petri-net.xml";

    private static long inicio;
    private static long fin;

    private static Thread[] threads = {null, null, null, null, null, null, null};

    private static int[][] invariantes;

    private static RedDePetri redDePetri;
    private static CPUProcessing cpuProcessingA;
    private static CPUProcessing cpuProcessingB;

    public static void main(String[] args) {

        currentThread().setName("Main");

        XMLParser xmlParser = new XMLParser(petriNetFile);

        int[] marcadoInicial = xmlParser.getMarcado();
        int[][] incidenciaBackward = xmlParser.getIncidenciaBackward();
        int[][] incidenciaFrontward = xmlParser.getIncidenciaFrontward();
        int[][] matrizInhibidora = xmlParser.getMatrizInhibidora();

        if( GARBAGECOLLECTION ) {
            invariantes = new int[][]   {
                    {   0,  8,  9, 10, 12, 13, 11 },
                    {   0,  8, 14, 12, 13, -1, -1 },
                    {   0,  1,  2,  3,  5,  6,  4 },
                    {   0,  1,  7,  5,  6, -1, -1 }
            };
        }
        else    {
            invariantes = new int[][]   {
                    {   0,  8,  9, 10, 12, 13, 11 },
                    {   0,  1,  2,  3,  5,  6,  4 }
            };
        }


        long[] timeStamp = new long[incidenciaBackward[0].length];
        long[] alfa = new long[incidenciaBackward[0].length];
        long[] beta = new long[incidenciaBackward[0].length];
        for(int i = 0; i < timeStamp.length ; i++) {
            alfa[i] = 0;
            beta[i] = Long.MAX_VALUE;
            timeStamp[i] = 0;
        }

        alfa[0] = ARRIVALRATE;
        alfa[6] = SERVICERATE * FACTORA;
        alfa[13] = SERVICERATE * FACTORB;
        alfa[3] = STANDBYDELAY;
        alfa[10] = STANDBYDELAY;

        // TRANSICIONES         0, 1, 2, 3, 4, 5, 6, 7, 8, 9,10,11,12,13,14
        int[] prioridades = {  10,11, 6, 8, 4, 2, 0,13,12, 7, 9, 5, 3, 1,14     };

        Monitor monitor = new Monitor(new Politica(prioridades));
        redDePetri = new RedDePetri(marcadoInicial, incidenciaFrontward, incidenciaBackward, matrizInhibidora, monitor,
                timeStamp, alfa, beta);
        monitor.setRedDePetri(redDePetri);

        ProcessBuffer[] buffers = {null, null};
        if( REALBUFFER )
        {
            buffers[0] = new ProcessBuffer();
            buffers[1] = new ProcessBuffer();
        }

        CPUPower cpuPowerA = new CPUPower(monitor, "A");
        cpuProcessingA = new CPUProcessing(cpuPowerA, "A", buffers[0]);

        CPUPower cpuPowerB = new CPUPower(monitor, "B");
        cpuProcessingB = new CPUProcessing(cpuPowerB, "B", buffers[1]);

        ProcessGenerator processGenerator = new ProcessGenerator(monitor, CANTIDADPROCESOS, buffers);

        threads[0] = processGenerator;
        threads[1] = cpuPowerA;
        threads[2] = cpuProcessingA;
        threads[3] = cpuPowerB;
        threads[4] = cpuProcessingB;

        if(GARBAGECOLLECTION) {
            CPUGarbageCollector CPUGarbageCollectorA = new CPUGarbageCollector(monitor, "A");
            CPUGarbageCollector CPUGarbageCollectorB = new CPUGarbageCollector(monitor, "B");
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