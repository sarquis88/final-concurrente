import java.util.ArrayList;
import java.util.LinkedList;

public class Main {

	private static final int PRODUCTORES = 5;
    private static final int CONSUMIDORES = 8;
    private static final int DISPAROSPRODUCTORES = 10000;
    private static final int DISPAROSCONSUMIDORES = 6250;
    private static final int TIEMPOPRODUCCION = 0;      // ambos tiempos en milisegundos
    private static final int TIEMPOCONSUMICION = 0;
    
    public static void main(String[] args) throws InterruptedException {

        int[] marcadoInicial = {0,10,0,5,8,0,0,15,0,0};
        int[][] incidencia = {                          // TRANSICIONES
                                                        { 1,-1, 0, 0, 0, 0, 0, 0}, // P
                                                        { 0, 0, 0, 0,-1, 0, 0, 1}, // L
                                                        { 0, 0, 0, 0, 0, 1,-1, 0}, // A
                                                        {-1, 1, 0, 0,-1, 1, 0, 0}, // Z
                                                        { 0, 0,-1, 1, 0, 0,-1, 1}, // A
                                                        { 0, 0, 1,-1, 0, 0, 0, 0}, // S
                                                        { 0, 0, 0, 0, 1,-1, 0, 0},  
                                                        {-1, 0, 0, 1, 0, 0, 0, 0},  
                                                        { 0, 1,-1, 0, 0, 0, 0, 0},  
                                                        { 0, 0, 0, 0, 0, 0, 1,-1}   
                                                                                    };

        Politicas politicas = new Politicas(incidencia.length
        );

        Productor[] productores = new Productor[PRODUCTORES];
        Consumidor[] consumidores = new Consumidor[CONSUMIDORES];

        Monitor monitor = new Monitor(new RedDePetri(marcadoInicial, incidencia), politicas);

        ArrayList<LinkedList<Object>> buffers = new ArrayList<>();
        buffers.add(new LinkedList<>());
        buffers.add(new LinkedList<>());

        long inicio = System.currentTimeMillis();

        for (int i = 0; i < PRODUCTORES; i++) {
			productores[i] = new Productor(monitor, DISPAROSPRODUCTORES, buffers);
			productores[i].start();
		}
        for (int i = 0; i < CONSUMIDORES; i++) {
			consumidores[i] = new Consumidor(monitor, DISPAROSCONSUMIDORES, buffers);
			consumidores[i].start();
		}

		for (int i = 0; i < PRODUCTORES; i++)
			productores[i].join();
		for (int i = 0; i < CONSUMIDORES; i++)
			consumidores[i].join();

        long fin = System.currentTimeMillis();
        double tiempo = (fin - inicio) / (1000.00);

		System.out.println("\nFIN DE EJECUCION");
		System.out.println("TIEMPO: " + tiempo + " SEGUNDOS");
    }

    static public int getTiempoproduccion() {
        return TIEMPOPRODUCCION;
    }

    static public int getTiempoconsumicion() {
        return TIEMPOCONSUMICION;
    }
}


