package org.example;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Representa una caja que atiende clientes de la cola.
 * Se ejecuta como un hilo consumidor.
 */
public class Caja extends Thread {
    private final int numeroCaja;
    private final BlockingQueue<Cliente> cola;
    private final MetricasSimulacion metricas;
    private final int tiempoMsArticulo; // tiempo para procesar un artículo
    
    private final AtomicBoolean enEjecucion = new AtomicBoolean(true);
    private int clientesAtendidos = 0;
    
    /**
     * Crea una caja.
     * @param numeroCaja identificador único de la caja
     * @param cola la cola de donde obtener clientes
     * @param metricas el rastreador de métricas
     * @param tiempoMsArticulo tiempo para procesar cada artículo (en milisegundos)
     */
    public Caja(int numeroCaja, BlockingQueue<Cliente> cola, 
                   MetricasSimulacion metricas, int tiempoMsArticulo) {
        this.numeroCaja = numeroCaja;
        this.cola = cola;
        this.metricas = metricas;
        this.tiempoMsArticulo = tiempoMsArticulo;
        this.setName("Caja-" + numeroCaja);
    }
    
    @Override
    public void run() {
        while (enEjecucion.get()) {
            try {
                // Intentar obtener un cliente de la cola (esperar máximo 2 segundos)
                Cliente cliente = cola.poll(2, TimeUnit.SECONDS);
                
                if (cliente == null) {
                    // No hay cliente disponible, continuar esperando
                    continue;
                }
                
                // Registrar tiempo de inicio de servicio
                cliente.establecerTiempoInicioServicio(System.currentTimeMillis());
                System.out.printf("[Caja %d] Atendiendo %s%n", numeroCaja, cliente);
                
                // Procesar el cliente (el tiempo depende del número de artículos)
                long tiempoServicio = (long) cliente.obtenerNumArticulos() * tiempoMsArticulo;
                Thread.sleep(tiempoServicio);
                
                // Registrar tiempo de salida
                cliente.establecerTiempoSalida(System.currentTimeMillis());
                
                // Registrar métricas
                metricas.registrarClienteAtendido(cliente, numeroCaja);
                clientesAtendidos++;
                
                System.out.printf("[Caja %d] %s servido. Tiempo: %.2f s%n", 
                    numeroCaja, cliente, cliente.obtenerTiempoServicio() / 1000.0);
                
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
        
        System.out.printf("[Caja %d] Cerrada. Total de clientes atendidos: %d%n", 
            numeroCaja, clientesAtendidos);
    }
    
    public void cerrarCaja() {
        enEjecucion.set(false);
    }
    
    public int obtenerClientesAtendidos() {
        return clientesAtendidos;
    }
}
