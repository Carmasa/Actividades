package org.example;

import java.util.*;
import java.util.concurrent.*;

/**
 * Orquesta la simulación del supermercado con clientes, cajas y métricas.
 */
public class SimuladorSupermercado {
    
    // Parámetros de configuración
    private final int numCajas;
    private final long duracionSimulacion; // milisegundos
    private final int maxClientesEnCola;
    private final int intervaloMinLlegadaCliente; // milisegundos
    private final int intervaloMaxLlegadaCliente; // milisegundos
    private final int articulosMinCliente;
    private final int articulosMaxCliente;
    private final int tiempoMsArticulo;
    private final int maxClientesAGenerar; // -1 para ilimitado
    
    // Componentes de la simulación
    private final BlockingQueue<Cliente> colClientes;
    private final MetricasSimulacion metricas;
    private final List<Caja> cajas;
    private GeneradorClientes generador;
    
    /**
     * Crea un simulador del supermercado con la configuración especificada.
     */
    public SimuladorSupermercado(int numCajas, long duracionSimulacion, 
                               int maxClientesEnCola, int intervaloMinLlegadaCliente,
                               int intervaloMaxLlegadaCliente, int articulosMinCliente,
                               int articulosMaxCliente, int tiempoMsArticulo,
                               int maxClientesAGenerar) {
        this.numCajas = numCajas;
        this.duracionSimulacion = duracionSimulacion;
        this.maxClientesEnCola = maxClientesEnCola;
        this.intervaloMinLlegadaCliente = intervaloMinLlegadaCliente;
        this.intervaloMaxLlegadaCliente = intervaloMaxLlegadaCliente;
        this.articulosMinCliente = articulosMinCliente;
        this.articulosMaxCliente = articulosMaxCliente;
        this.tiempoMsArticulo = tiempoMsArticulo;
        this.maxClientesAGenerar = maxClientesAGenerar;
        
        this.colClientes = new LinkedBlockingQueue<>(maxClientesEnCola);
        this.metricas = new MetricasSimulacion();
        this.cajas = new ArrayList<>();
    }
    
    /**
     * Inicia la simulación.
     */
    public void ejecutar() {
        long tiempoInicio = System.currentTimeMillis();
        metricas.establecerTiempoInicioSimulacion(tiempoInicio);
        
        System.out.println("\n" + "=".repeat(50));
        System.out.println("INICIANDO SIMULACIÓN DE SUPERMERCADO");
        System.out.println("=".repeat(50));
        System.out.printf("Cajas: %d, Duración: %.1f s, Capacidad cola: %d%n", 
            numCajas, duracionSimulacion / 1000.0, maxClientesEnCola);
        System.out.println("=".repeat(50) + "\n");
        
        // Crear e iniciar hilos de cajas
        for (int i = 1; i <= numCajas; i++) {
            Caja caja = new Caja(i, colClientes, metricas, tiempoMsArticulo);
            cajas.add(caja);
            caja.start();
        }
        
        // Crear e iniciar hilo generador de clientes
        generador = new GeneradorClientes(colClientes, metricas, duracionSimulacion,
            intervaloMinLlegadaCliente, intervaloMaxLlegadaCliente,
            articulosMinCliente, articulosMaxCliente, maxClientesAGenerar);
        generador.start();
        
        // Esperar a que el generador finalice
        try {
            generador.join();
            System.out.println("[Principal] Generador finalizó, esperando a que se vacíe la cola...\n");
            
            // Dar tiempo a las cajas para procesar los clientes restantes
            Thread.sleep(2000);
            
            // Cerrar todas las cajas
            for (Caja caja : cajas) {
                caja.cerrarCaja();
            }
            
            // Esperar a que todas las cajas finalicen
            for (Caja caja : cajas) {
                caja.join();
            }
            
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("¡Simulación interrumpida!");
        }
        
        long tiempoFin = System.currentTimeMillis();
        metricas.establecerTiempoFinSimulacion(tiempoFin);
        
        // Imprimir informe final
        metricas.imprimirInformeFinal();
    }
    
    /**
     * Devuelve el rastreador de métricas.
     */
    public MetricasSimulacion obtenerMetricas() {
        return metricas;
    }
}
