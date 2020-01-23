import java.util.ArrayList;
import java.util.LinkedList;

public class Productor extends Thread {

    private int cantidadAProducir;
    private Monitor monitor;
    private ArrayList<LinkedList<Object>> listaDeBuffers;
    private int id;
    private int tiempoDormir;

    private static int idGlobal = 0;

    /**
     * Constructor de clase
     * @param monitor monitor del productor
     * @param cantidadAProducir cantidad de productos a producir por cada productor
     * @param buffers buffers posibles en donde producir
     */
    public Productor(Monitor monitor, int cantidadAProducir, ArrayList<LinkedList<Object>> buffers) {
        this.monitor = monitor;
        this.cantidadAProducir = cantidadAProducir;
        this.listaDeBuffers = buffers;
        this.tiempoDormir = Main.getTiempoproduccion();
        this.id = idGlobal;
        idGlobal++;

        System.out.println("CREACION:       PRODUCTOR " + this.id);
    }

    /**
     * Produccion en buffer0
     * @param object objeto a producir
     */
    private void producirEnBuffer0(Object object){
        producirEnBuffer(object, listaDeBuffers.get(0), 2, 3, 0);
    }

    /**
     * Produccion en buffer1
     * @param object objeto a producir
     */
    private void producirEnBuffer1(Object object){
        producirEnBuffer(object, listaDeBuffers.get(1), 6, 7, 1);
    }

    /**
     * Accion de producir
     * Ingreso a monitor, disparo de red, salida de monitor
     * @param objeto objeto a producir
     * @param buffer buffer donde se guardara el producto
     * @param comienzoProduccion transicion de inicio
     * @param finProduccion transicion de final
     * @param bufferId id de buffer (solo sirve para logear)
     */
    private void producirEnBuffer(Object objeto, LinkedList<Object> buffer, int comienzoProduccion,
                                  int finProduccion, int bufferId) {

        try {
            monitor.entrar(comienzoProduccion);
            monitor.salir();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // si la ejecucion llego hasta aca ya tengo el hueco para producir y actualice el estado
        dormir();

        try {
            monitor.entrar(finProduccion);
            buffer.add(objeto);
            System.out.println("PRODUCCION:             BUFFER " + bufferId + " - TAMAÑO " + buffer.size());
            monitor.salir();
        } catch (Exception e) {
            e.printStackTrace();
        }

        //si la ejecucion llego hasta aca ya agregue un producto y actualice el estado
    }

    /**
     * Dormida del Thread durante tiempoDormir ms
     */
    private void dormir() {
        try {
            sleep(tiempoDormir);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Accion del Thread al iniciar
     */
    @Override
    public void run() {
        for (int i = 0; i < cantidadAProducir; i++) {
            if (i % 2 == 0)
                this.producirEnBuffer0(new Object());
            else
                this.producirEnBuffer1(new Object());
        }
        System.out.println("FIN:                    PRODUCTOR " + this.id);
    }
}