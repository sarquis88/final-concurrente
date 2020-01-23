import java.util.ArrayList;
import java.util.concurrent.Semaphore;

public class Monitor {

    private Semaphore mutex = new Semaphore(1);
    private Semaforo[] VariablesDeCondicion;            //condiciones de sincronizacion de cada transicion
    private RedDePetri RdP;
    private Politicas politicas;

    /**
     * Constructor de clase
     * @param red red de petri asociada al monitor
     * @param politicas politicas asociadas al monitor
     */
    public Monitor(RedDePetri red, Politicas politicas){
        this.RdP = red;
        this.VariablesDeCondicion = new Semaforo[RdP.getTransiciones().length];
        this.politicas = politicas;

        for(int i = 0; i < this.VariablesDeCondicion.length; i++)
            this.VariablesDeCondicion[i] = new Semaforo(this.mutex);
    }

    /**
     * Entrada al monitor
     * Dispara transicion de entrada tomando el mutex y sin devolverlo
     * @param transicion transicion a disparar
     */
    public void entrar(int transicion) {
        try {
            mutex.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        while(!(RdP.isSensibilizada(transicion))){
            VariablesDeCondicion[transicion].delay();
        }
        this.RdP.disparar(transicion);
    }

    /**
     * Salida del monitor
     * Devuelve el mutex y desbloquea un hilo
     */
    public void salir(){
        desbloquearUno();
        mutex.release();
    }

    /**
     * Desbloquea un hilo de las colas de condicion cuya transicion esté sensibilizada
     */
    private void desbloquearUno(){
        ArrayList<Integer> desbloqueables = colasAndSensibilizadas();
        if(!desbloqueables.isEmpty())
            VariablesDeCondicion[politicas.decidir(desbloqueables)].resume();
    }

    /**
     * Devuelve una lista de las transiciones sensibilizadas que tienen hilos queriendo dispararlas
     * @return arraylist de enteros correspondiente a la lista de transiciones sensibilizadas
     */
    private ArrayList<Integer> colasAndSensibilizadas() {
        ArrayList<Integer> desbloqueables = new ArrayList<>();
        for(int i = 0; i < this.VariablesDeCondicion.length; i++) {
            if( !(VariablesDeCondicion[i].isEmpty()) && RdP.isSensibilizada(i))
                desbloqueables.add(i);
        }
        return desbloqueables;
    }
}
