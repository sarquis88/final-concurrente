package concurrente;

public class CPU {

    private static boolean finished = false;

    /**
     * Pregunta si el CPU termino de procesar
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
