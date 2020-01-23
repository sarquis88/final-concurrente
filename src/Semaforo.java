import java.util.concurrent.Semaphore;

public class Semaforo {

    private Semaphore mutex;
    private Semaphore condicion;
    private int bloqueados;

    /**
     * Constructor de clase
     * @param mutex mutex del monitor
     */
    public Semaforo(Semaphore mutex) {
        this.mutex = mutex;
        condicion = new Semaphore(0, true);
        bloqueados = 0;
    }

    /**
     * Bloquea un hilo y devuelve el mutex
     */
    public void delay() {
        bloqueados++;
        mutex.release();
        try {
            condicion.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        try {
            mutex.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Desbloquea un hilo
     * Si no hay bloqueados no hace nada
     */
    public void resume() {
        if(bloqueados > 0) {
            bloqueados--;
            condicion.release();
        }
    }

    /**
     * Indica si la cola de bloqueados esta vacia
     * @return true si esta la cola vacia, de lo contrario false
     */
    public boolean isEmpty() {
        return bloqueados <= 0;
    }
}
