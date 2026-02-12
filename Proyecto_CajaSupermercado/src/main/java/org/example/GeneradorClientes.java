package org.example;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Genera clientes a intervalos aleatorios y los añade a la cola.
 * Se ejecuta como un hilo productor.
 */
public class GeneradorClientes extends Thread {
    private final BlockingQueue<Cliente> cola;
    private final MetricasSimulacion metricas;
    private final long duracionSimulacion; // en milisegundos
    private final int intervaloMinMs;
    private final int intervaloMaxMs;
    private final int articulosMin;
    private final int articulosMax;
    private final int maxClientes;
    
    private final AtomicBoolean enEjecucion = new AtomicBoolean(true);
    private long tiempoInicio;
    
    /**
     * Crea un generador de clientes.
     * @param cola la cola donde añadir clientes
     * @param metricas el rastreador de métricas
     * @param duracionSimulacion duración total de la simulación en milisegundos
     * @param intervaloMinMs intervalo mínimo entre llegadas de clientes
     * @param intervaloMaxMs intervalo máximo entre llegadas de clientes
     * @param articulosMin artículos mínimos por cliente
     * @param articulosMax artículos máximos por cliente
     * @param maxClientes número máximo de clientes a generar (-1 para ilimitado)
     */
    public GeneradorClientes(BlockingQueue<Cliente> cola, MetricasSimulacion metricas,
                           long duracionSimulacion, int intervaloMinMs, int intervaloMaxMs,
                           int articulosMin, int articulosMax, int maxClientes) {
        this.cola = cola;
        this.metricas = metricas;
        this.duracionSimulacion = duracionSimulacion;
        this.intervaloMinMs = intervaloMinMs;
        this.intervaloMaxMs = intervaloMaxMs;
        this.articulosMin = articulosMin;
        this.articulosMax = articulosMax;
        this.maxClientes = maxClientes;
        this.setName("GeneradorClientes");
    }
    
    @Override
    public void run() {
        tiempoInicio = System.currentTimeMillis();
        int clientesGenerados = 0;
        
        while (enEjecucion.get()) {
            long tiempoTranscurrido = System.currentTimeMillis() - tiempoInicio;
            
            // Verificar si el tiempo de simulación se ha excedido
            if (tiempoTranscurrido > duracionSimulacion) {
                break;
            }
            
            // Verificar si se alcanzó el máximo de clientes
            if (maxClientes > 0 && clientesGenerados >= maxClientes) {
                break;
            }
            
            try {
                // Generar intervalo aleatorio
                int intervalo = intervaloMinMs + 
                    (int)(Math.random() * (intervaloMaxMs - intervaloMinMs + 1));
                
                Thread.sleep(intervalo);
                
                // Crear y añadir cliente
                Cliente cliente = new Cliente(articulosMin, articulosMax);
                
                boolean anadido = cola.offer(cliente);
                
                if (anadido) {
                    metricas.registrarClienteGenerado();
                    clientesGenerados++;
                    System.out.printf("[Generador] %s generado. Total: %d%n", 
                        cliente, clientesGenerados);
                } else {
                    // La cola está llena, cliente abandona
                    metricas.registrarClienteAbandonado(cliente);
                    System.out.printf("[Generador] Cola llena. %s abandona el supermercado%n", 
                        cliente);
                }
                
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
        
        enEjecucion.set(false);
        System.out.println("[Generador] Generador finalizado. Clientes creados: " + clientesGenerados);
    }
    
    public void detenerGenerador() {
        enEjecucion.set(false);
    }
}
