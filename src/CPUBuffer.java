import java.util.LinkedList;

public class CPUBuffer {

    private LinkedList<CPUProcess> buffer;

    /**
     * Constructor de clase
     */
    public CPUBuffer() {
        this.buffer = new LinkedList<>();
    }

    /**
     * Agregar nuevo CPUProcess al buffer
     * @param CPUProcess objeto CPUProcess a añadir
     */
    public void addProceso(CPUProcess CPUProcess) {
        this.buffer.add(CPUProcess);
    }

    /**
     * Procesar el ultimo elemento de la lista
     * @return proceso procesado
     */
    public CPUProcess procesar() {
        return this.buffer.poll();
    }

    /**
     * Getter del tamaño del buffer
     * @return int relativo al size del buffer
     */
    public int getSize() {
        return this.buffer.size();
    }
}
