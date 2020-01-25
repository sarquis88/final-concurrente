import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Monitor {

    private ReentrantLock mutex;
    private Condition waitingQueue;
    private RedDePetri RdP;

    /**
     * Constructor de clase
     * @param red red de petri asociada al monitor
     */
    public Monitor(RedDePetri red){
        this.RdP = red;
        this.mutex = new ReentrantLock(true);
        this.waitingQueue = mutex.newCondition();
    }

    /**
     * Entrada al monitor y disparo de transicion
     * @param transicion transicion a disparar
     * @return true si la transicion se disparo, de lo contrario false
     */
    public boolean disparo(int transicion) throws InterruptedException {
        boolean exito;
        try {
            mutex.lock();

            while (!RdP.isSensibilizada(transicion))
                waitingQueue.await();

            exito = this.RdP.disparar(transicion);
            waitingQueue.signalAll();
        }
        finally {
            mutex.unlock();
        }
        return exito;
    }
}
