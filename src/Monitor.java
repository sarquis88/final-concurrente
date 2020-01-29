import java.util.Arrays;
import java.util.LinkedList;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Monitor {

    private ReentrantLock mutex;
    private LinkedList<Condition> waitingQueue;
    private RedDePetri RdP;

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
     * ATENCION: se debe liberar el mutex con salir() si o si despues de entrar()
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
}