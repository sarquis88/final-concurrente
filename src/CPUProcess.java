public class CPUProcess {

    private static int idGlobal = 1;

    private int idLocal;

    /**
     * Constructor de clase
     */
    public CPUProcess() {
        this.idLocal = idGlobal;
        idGlobal++;
    }

    /**
     * Getter de id del thread
     * @return int perteneciente al id del thread
     */
    public int getIdLocal() {
        return this.idLocal;
    }
}
