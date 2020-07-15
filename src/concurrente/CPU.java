package concurrente;/*
 * CLASE USADA PARA QUE LOS THREADS concurrente.CPUProcessing, concurrente.CPUPower y concurrente.CPUGarbageCollector
 * TERMINEN UNA VEZ QUE SE ACABEN LOS PROCESOS
 */

public class CPU {

    private static boolean finished = false;

    /**
     * Pregunta si el concurrente.CPU termino de procesar
     * @return true o false, dependiendo el caso
     */
    public static boolean isFinished() {
        return finished;
    }

    /**
     * Ejecutado al terminar de procesar
     */
    public static void finish() {
        finished = true;
    }
}
