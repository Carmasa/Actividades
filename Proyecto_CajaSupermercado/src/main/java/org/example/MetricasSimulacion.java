package org.example;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Rastrea estadísticas y métricas para la simulación del supermercado.
 */
public class MetricasSimulacion {
    private final AtomicInteger totalClientesGenerados = new AtomicInteger(0);
    private final AtomicInteger clientesAtendidos = new AtomicInteger(0);
    private final AtomicInteger clientesAbandonados = new AtomicInteger(0);
    private final AtomicLong tiempoEsperaTotal = new AtomicLong(0);
    private final AtomicLong tiempoServicioTotal = new AtomicLong(0);
    
    private final Map<Integer, Integer> clientesPorCaja = new ConcurrentHashMap<>();
    private final List<Cliente> clientesAtendidosList = Collections.synchronizedList(new ArrayList<>());
    private final List<Cliente> clientesAbandonadosList = Collections.synchronizedList(new ArrayList<>());
    
    private long tiempoInicioSimulacion;
    private long tiempoFinSimulacion;
    
    public void registrarClienteGenerado() {
        totalClientesGenerados.incrementAndGet();
    }
    
    public void registrarClienteAtendido(Cliente cliente, int numeroCaja) {
        clientesAtendidos.incrementAndGet();
        clientesAtendidosList.add(cliente);
        tiempoEsperaTotal.addAndGet(cliente.obtenerTiempoEspera());
        tiempoServicioTotal.addAndGet(cliente.obtenerTiempoServicio());
        
        clientesPorCaja.merge(numeroCaja, 1, Integer::sum);
    }
    
    public void registrarClienteAbandonado(Cliente cliente) {
        clientesAbandonados.incrementAndGet();
        clientesAbandonadosList.add(cliente);
    }
    
    public void establecerTiempoInicioSimulacion(long tiempoInicio) {
        this.tiempoInicioSimulacion = tiempoInicio;
    }
    
    public void establecerTiempoFinSimulacion(long tiempoFin) {
        this.tiempoFinSimulacion = tiempoFin;
    }
    
    public int obtenerTotalClientesGenerados() {
        return totalClientesGenerados.get();
    }
    
    public int obtenerClientesAtendidos() {
        return clientesAtendidos.get();
    }
    
    public int obtenerClientesAbandonados() {
        return clientesAbandonados.get();
    }
    
    public double obtenerTiempoEsperaPromedio() {
        int atendidos = clientesAtendidos.get();
        if (atendidos == 0) return 0;
        return tiempoEsperaTotal.get() / (double) atendidos;
    }
    
    public double obtenerTiempoServicioPromedio() {
        int atendidos = clientesAtendidos.get();
        if (atendidos == 0) return 0;
        return tiempoServicioTotal.get() / (double) atendidos;
    }
    
    public long obtenerTiempoTotalSimulacion() {
        return tiempoFinSimulacion - tiempoInicioSimulacion;
    }
    
    public Map<Integer, Integer> obtenerClientesPorCaja() {
        return new TreeMap<>(clientesPorCaja);
    }
    

    public void imprimirInformeFinal() {
        System.out.println("\n" + "=".repeat(50));
        System.out.println("-INFORME FINAL DE SIMULACIÓN");
        System.out.println("=".repeat(50));
        
        System.out.printf("Clientes generados: %d%n", obtenerTotalClientesGenerados());
        System.out.printf("Clientes atendidos: %d%n", obtenerClientesAtendidos());
        System.out.printf("Clientes abandonados: %d%n", obtenerClientesAbandonados());
        
        System.out.printf("Tiempo medio de espera: %.2f s%n", obtenerTiempoEsperaPromedio() / 1000.0);
        System.out.printf("Tiempo medio de atención: %.2f s%n", obtenerTiempoServicioPromedio() / 1000.0);
        System.out.printf("Tiempo total de simulación: %.2f s%n", obtenerTiempoTotalSimulacion() / 1000.0);
        
        System.out.println();
        System.out.println("-Clientes por Caja");
        Map<Integer, Integer> porCaja = obtenerClientesPorCaja();
        if (porCaja.isEmpty()) {
            System.out.println("Sin datos");
        } else {
            porCaja.forEach((caja, cantidad) ->
                System.out.printf("Caja %d -> %d clientes%n", caja, cantidad)
            );
        }
        
        System.out.println("=".repeat(50) + "\n");
    }
}
