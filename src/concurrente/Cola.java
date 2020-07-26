package concurrente;

public class Cola {

	private int waiting;

	/**
	 * Constructor de clase
	 */
	Cola() {
		this.waiting = 0;
	}

	/**
	 * Envio de thread a dormir
	 */
	public synchronized void acquire() {
		this.waiting++;
		try {
			wait();
		} catch (InterruptedException e) {
			interruptedReaccion();
		}
	}

	/**
	 * Notificacion al thread para que se despierte
	 */
	public synchronized void release() {
		this.waiting--;
		this.notify();
	}

	/**
	 * Rutina de interrupcion vacia
	 */
	private void interruptedReaccion() {

	}

	/**
	 * Indica si hay un thread durmiendo en cola
	 * @return retorna int waiting, 1 ó 0
	 */
	public int getWaiting() {
		if( this.waiting > 0 )
			return 1;
		else
			return 0;
	}
}
