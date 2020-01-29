import java.util.ArrayList;
import java.util.LinkedList;

public class Politicas {
    
    private LinkedList<Integer> prioridades;

    /**
     * Constructor de clase
     */
    public Politicas(int cantidadTransiciones) {
        this.prioridades = new LinkedList<>();

        for(int i = 0; i < cantidadTransiciones; i++)
            this.prioridades.add(i);                // prioridades por defecto
    }

    /**
     * Agregar prioridad a la transicion
     * @param transicion numero de transicion
     * @param prioridad numero de prioridad
     */
    public void addPrioridad(int transicion, int prioridad){
        this.prioridades.add(transicion, prioridad);
    }

    /**
     * Devuelve la transicion de mayor prioridad entre las sensibilizadas
     * MAYOR PRIORIDAD == 0
     * @param sensibilizadas lista de transiciones sensibilizadas
     * @return int correspondiente a la transicion con mayor prioridad
     */
    public int decidir(ArrayList<Integer> sensibilizadas) {
        int prior = sensibilizadas.get(0);
        for (int transicion : sensibilizadas) {
            if (prioridades.get(transicion) < prioridades.get(prior))
                prior = transicion;
        }
        return prior;
    }
}
