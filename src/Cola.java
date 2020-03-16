public class Cola {
	
	Cola(){

	}
	
	public synchronized void acquire() {
		try{
			wait();
		}
		catch(InterruptedException e){
			interruptedReaccion();
		}
	}
	
	public synchronized void release() {
		this.notify();
	}

	private void interruptedReaccion() {

	}
}
