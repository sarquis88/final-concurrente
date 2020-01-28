import java.util.Arrays;
import java.util.LinkedList;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Monitor {

    private ReentrantLock mutex;
    private LinkedList<Condition> waitingQueue;
    private RedDePetri RdP;

    private LinkedList<LinkedList<Integer>> debugMarcas = new LinkedList<>();
    private LinkedList<Integer> debugTransiciones = new LinkedList<>();

    /**
     * Constructor de clase
     * @param red red de petri asociada al monitor
     */
    public Monitor(RedDePetri red){
        this.RdP = red;
        this.mutex = new ReentrantLock(true);

        this.waitingQueue = new LinkedList<>();
        for(int i = 0; i < RdP.getTransiciones().length; i++)
            this.waitingQueue.add(mutex.newCondition());
    }

    /**
     * Entrada al monitor y disparo de transicion
     * @param transicion transicion a disparar
     */
    public void entrar(int transicion) throws InterruptedException {

        mutex.lock();

        while (!RdP.isSensibilizada(transicion))
            waitingQueue.get(transicion).await();

        this.RdP.disparar(transicion);

        if(Main.isPrintMarcado()) {
            System.out.println("\nTransicion: " + transicion);
            printMarcaActual();
        }
    }

    /**
     * Salida del monitor y avisar a todos los hilos
     */
    public void salir() {
        signalAll();
        mutex.unlock();
    }

    /**
     * Despierta a todos los hilos suspendidos al azar
     */
    private void signalAll() {
        for(Condition condition : this.waitingQueue)
            condition.signalAll();
    }

    /**
     * Impresion de marca actual
     */
    public void printMarcaActual() {
        System.out.println(Arrays.toString(this.RdP.getMarcaActual()));
    }

    /**
     * Impresion de todas las transiciones disparadas y variacion de marcado
     */
    public void printRegistro() {
        System.out.println("\n");

        for(int i=0; i < this.debugTransiciones.size(); i++) {
            System.out.println(this.debugTransiciones.get(i));
            System.out.println(this.debugMarcas.get(i));
            System.out.println("\n");
        }
    }

    // DEBUG ============================================================
    /*
    this.debugTransiciones.add(transicion);
    LinkedList<Integer> aux = new LinkedList<>();
    for(int i = 0; i < this.RdP.getMarcaActual().length; i++)
        aux.add(this.RdP.getMarcaActual()[i]);
    this.debugMarcas.add(aux);
    */
    // REGISTRO DE MOVIMIENTOS EN RED ===================================

    // DEBUG ============================================================
    /*
    System.out.println(transicion);
    printMarcaActual();
    System.out.println("");
    */
    // PLOTEO DE MOVIMIENTOS EN RED =====================================
}