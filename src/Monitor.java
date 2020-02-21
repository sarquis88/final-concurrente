import java.util.LinkedList;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Monitor {

    private ReentrantLock mutex;
    private LinkedList<Condition> waitingQueue;
    private RedDePetri redDePetri;

    /**
     * Constructor de clase
     * @param red red de petri asociada al monitor
     */
    public Monitor(RedDePetri red){
        this.redDePetri = red;
        this.mutex = new ReentrantLock(true);

        this.waitingQueue = new LinkedList<>();
        for(int i = 0; i < redDePetri.getTransiciones().length; i++)
            this.waitingQueue.add(mutex.newCondition());
    }

    /**
     * Entrada al monitor y disparo de transicion
     * ATENCION: se debe liberar el mutex con salir() si o si despues de entrar()
     * @param transicion transicion a disparar
     */
    public void entrar(int transicion) throws InterruptedException {

        mutex.lock();

        while (redDePetri.isSensibilizada(transicion) == 0)
            waitingQueue.get(transicion).await();

        this.redDePetri.disparar(transicion);
    }

    /**
     * Salida del monitor y avisar a todos los hilos
     */
    public void salir() {
        signalAll();
        if(mutex.isLocked())
            mutex.unlock();
    }

    /**
     * Despierta a todos los hilos suspendidos al azar
     */
    private void signalAll() {
        for(Condition condition : this.waitingQueue)
            condition.signalAll();
    }
}