import java.util.ArrayList;
import java.util.HashMap;

public class Politicas {
    
    private HashMap<Integer, Integer> prioridades;

    /**
     * Constructor de clase
     */
    public Politicas(int cantidadPrioridades) {
        this.prioridades = new HashMap<>(cantidadPrioridades);

        for(int i = 0; i < cantidadPrioridades; i++)
            addPrioridad(i , 0);                // prioridades por defecto

        // addPrioridad(6, 1);      // prioridad de produccion en buffer 1
        // addPrioridad(0, 1);      // prioridad de consumicion en buffer 0
        // asi, se deberia notar mas productor en buffer 0
    }

    /**
     * Agregar prioridad a la politica
     * @param transicion numero de transicion
     * @param prioridad numero de prioridad
     */
    public void addPrioridad(int transicion, int prioridad){
        this.prioridades.put(transicion, prioridad);
    }

    /**
     * Devuelve la transicion de mayor prioridad entre las sensibilizadas
     * @param sensibilizadas lista de transiciones sensibilizadas
     * @return int correspondiente a la transicion con mayor prioridad
     */
    public int decidir(ArrayList<Integer> sensibilizadas) {
        int eleccion = sensibilizadas.get(0);
        for (int transicion : sensibilizadas) {
            if (prioridades.get(transicion) > prioridades.get(eleccion))
                eleccion = transicion;
        }
        return eleccion;
    }
}
