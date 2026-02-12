package org.example;

/**
 * Punto de entrada principal para el Simulador Multihilo del Supermercado.
 * 
 * Este programa simula un supermercado con múltiples cajas atendiendo a clientes
 * de forma concurrente. Incluye tiempos de espera,
 * tiempos de servicio y flujo de clientes a través del sistema.
 */
public class Main {
    public static void main(String[] args) {
        // ============ PARÁMETROS DE CONFIGURACIÓN ============
        
        // Número de cajas abiertas en el supermercado
        int numCajas = 3;
        
        // Duración total de la simulación en milisegundos (15 segundos)
        long duracionSimulacion = 15000;
        
        // Capacidad máxima de la cola de clientes
        int maxClientesEnCola = 20;
        
        // Intervalo de llegada de clientes (en milisegundos)
        // Rango: 500-1500 ms (0.5 a 1.5 segundos)
        int intervaloMinLlegada = 500;
        int intervaloMaxLlegada = 1500;
        
        // Artículos por cliente
        // Rango: 5-30 artículos
        int articulosMin = 5;
        int articulosMax = 30;
        
        // Tiempo para procesar cada artículo (en milisegundos)
        // Por defecto: 100 ms por artículo (5 artículos = 500 ms)
        int tiempoMsArticulo = 100;
        
        // Número máximo de clientes a generar (-1 = ilimitado)
        int maxClientesAGenerar = -1;
        
        // ============ INICIAR SIMULACIÓN ============
        
        SimuladorSupermercado simulador = new SimuladorSupermercado(
            numCajas,
            duracionSimulacion,
            maxClientesEnCola,
            intervaloMinLlegada,
            intervaloMaxLlegada,
            articulosMin,
            articulosMax,
            tiempoMsArticulo,
            maxClientesAGenerar
        );
        
        // Ejecutar la simulación (espera hasta finalización)
        simulador.ejecutar();
    }
}