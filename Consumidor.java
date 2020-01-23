import java.util.ArrayList;
import java.util.LinkedList;

public class Consumidor extends Thread {

    private int cantidadAConsumir;
    private Monitor monitor;
    private ArrayList<LinkedList<Object>> listaDeBuffers;
    private int id;
    private int tiempoDormir;

    private static int idGlobal = 0;

    /**
     * Constructor de clase
     * @param monitor monitor del consumidor
     * @param cantidadAConsumir cantidad a consumir del consumidor
     * @param buffers lista de buffers posibles en donde consumir
     */
    public Consumidor(Monitor monitor, int cantidadAConsumir, ArrayList<LinkedList<Object>> buffers) {
        this.monitor = monitor;
        this.cantidadAConsumir = cantidadAConsumir;
        this.listaDeBuffers = buffers;
        this.tiempoDormir = Main.getTiempoconsumicion();
        this.id = idGlobal;
        idGlobal++;

        System.out.println("CREACION:       CONSUMIDOR " + this.id);
    }

    /**
     * Consumicion en buffer 0
     */
    private void consumirEnBuffer0() {
        consumirEnBuffer(listaDeBuffers.get(0), 0, 1, 0);
    }

    /**
     * Consumicion en buffer 1
     */
    private void consumirEnBuffer1() {
        consumirEnBuffer( listaDeBuffers.get(1), 4, 5, 1);
    }

    /**
     * Accion de consumir
     * @param buffer buffer en el cual consumir
     * @param comienzoConsumicion transicion de inicio
     * @param finConsumicion transicion de final
     * @param bufferId id del buffer (sirve solo para el log)
     */
    private void consumirEnBuffer(LinkedList<Object> buffer, int comienzoConsumicion, int finConsumicion, int bufferId) {
        try {
            monitor.entrar(comienzoConsumicion);
            monitor.salir();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // si la ejecucion llega hasta aca, ya se tiene el producto para consumir y se actualiza el estado
        dormir();

        try {
            monitor.entrar(finConsumicion);
            buffer.poll();                      // agrega un hueco al buffer
            System.out.println("CONSUMICION:            BUFFER " + bufferId + " - TAMAÑO " + buffer.size());
            monitor.salir();

        } catch (Exception e) {
            e.printStackTrace();
        }

        // si la ejecucion llega hasta aca ya se agrego un hueco y se actualizo el estado
    }

    /**
     * Dormida del thread durante tiempoDormir ms
     */
    private void dormir() {
        try {
            sleep(tiempoDormir);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Accion del Thread
     */
    @Override
    public void run() {
        for (int i = 0; i < cantidadAConsumir; i++) {
            if (i % 2 == 0)
                this.consumirEnBuffer0();
            else
                this.consumirEnBuffer1();
        }
        System.out.println("FIN:                    CONSUMIDOR " + this.id);
    }
}



