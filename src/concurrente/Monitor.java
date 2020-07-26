package concurrente;

import java.util.concurrent.locks.ReentrantLock;

public class Monitor {

    private final ReentrantLock mutex;
    private Cola[] colas;
    private RedDePetri redDePetri;
    private final Politica politica;

    /**
     * Constructor de clase
     * Si o si se debe llamar al metodo setRedDePetri antes de usar
     */
    public Monitor(Politica politica) {
        this.politica = politica;
        this.mutex = new ReentrantLock(true);
    }

    /**
     * Seteo de red de Petri del monitor
     * @param redDePetri objeto red de petri
     */
    public void setRedDePetri(RedDePetri redDePetri) {
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
        while(k)
        {

            k = redDePetri.disparoTemporal(transicion);

            if( k )
            {
                despertar();
                k = false;
            }
            else
            {
                mutex.unlock();
                this.colas[transicion].acquire();
                mutex.lock();
                k = true;
            }
        }
        mutex.unlock();
    }

    /**
     * Despierta una transicion que esté durmiendo y sensibilizada
     */
    private void despertar() {
        int[] sensibilizadas = this.redDePetri.getTransiciones();
        int[] listas = new int[sensibilizadas.length];

        for(int i = 0; i < sensibilizadas.length; i++)
            listas[i] = sensibilizadas[i] * this.colas[i].getWaiting();

        int aDespertar = this.politica.getTransicionPrioritaria(listas);

        if(aDespertar >= 0)
            this.colas[aDespertar].release();
    }

    /**
     * Retorna el lock del monitor
     * @return ReentrantLock que funciona como mutex
     */
    public ReentrantLock getMutex() {
        return this.mutex;
    }
}