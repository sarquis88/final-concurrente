import java.util.LinkedList;

public class ProcessBuffer {

    private LinkedList<Process> buffer;

    /**
     * Constructor de clase
     */
    public ProcessBuffer() {
        this.buffer = new LinkedList<>();
    }

    /**
     * Agregar nuevo Process al buffer
     * @param Process objeto Process a añadir
     */
    public void addProceso(Process Process) {
        this.buffer.add(Process);
    }

    /**
     * Procesar el ultimo elemento de la lista
     * @return proceso procesado
     */
    public Process procesar() {
        return this.buffer.pollFirst();
    }

    /**
     * Getter del tamaño del buffer
     * @return int relativo al size del buffer
     */
    public int getSize() {
        return this.buffer.size();
    }
}