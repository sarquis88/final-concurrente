import java.util.concurrent.locks.ReentrantLock;

public class Monitor {

    private ReentrantLock mutex;
    private Cola[] colas;
    private RedDePetri redDePetri;

    /**
     * Constructor de clase
     */
    public Monitor(){
        this.mutex = new ReentrantLock(true);
    }

    public void setRedDePetri(RedDePetri redDePetri, int cantidadTransiciones) {
        this.redDePetri = redDePetri;
        this.colas = new Cola[redDePetri.getTransiciones().length];
        for(int i = 0; i < colas.length; i++)
            this.colas[i] = new Cola();
    }

    /**
     * Intenta disparar la transicion dentro de la seccion critica.
     * @param transicion la transicion de la Red de Petri a disparar
     */
    public void disparar(int transicion){
        mutex.lock();

        boolean k = true;
        while(k) {
            if(redDePetri.disparoTemporal(transicion)) {
                signalAll();
                k = false;
            }
            else {
                mutex.unlock();
                this.colas[transicion].acquire();
                mutex.lock();
                k = true;
            }
        }
        mutex.unlock();
    }

    /**
     * No politica
     * Despierta a todos los hilos
     */
    private void signalAll() {
        for(Cola cola : this.colas)
            cola.release();
    }

    /**
     * Retorna el lock del monitor
     * @return ReentrantLock que funciona como mutex
     */
    public ReentrantLock getMutex() {
        return this.mutex;
    }
}