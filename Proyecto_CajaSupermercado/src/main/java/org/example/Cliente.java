package org.example;

/**
 * Representa un cliente en la simulación del supermercado.
 */
public class Cliente {
    private static long contadorId = 0;
    private static final Object bloqueoId = new Object();
    
    private final long id;
    private final int numArticulos;
    private final long tiempoLlegada;
    private long tiempoInicioServicio;
    private long tiempoSalida;
    
    /**
     * Crea un nuevo cliente con un ID único y un número aleatorio de artículos.
     * @param minArticulos número mínimo de artículos
     * @param maxArticulos número máximo de artículos
     */
    public Cliente(int minArticulos, int maxArticulos) {
        synchronized (bloqueoId) {
            this.id = ++contadorId;
        }
        this.numArticulos = minArticulos + (int)(Math.random() * (maxArticulos - minArticulos + 1));
        this.tiempoLlegada = System.currentTimeMillis();
        this.tiempoInicioServicio = 0;
        this.tiempoSalida = 0;
    }
    
    public long obtenerId() {
        return id;
    }
    
    public int obtenerNumArticulos() {
        return numArticulos;
    }
    
    public long obtenerTiempoLlegada() {
        return tiempoLlegada;
    }
    
    public long obtenerTiempoInicioServicio(long l) {
        return tiempoInicioServicio;
    }
    
    public void establecerTiempoInicioServicio(long tiempoInicioServicio) {
        this.tiempoInicioServicio = tiempoInicioServicio;
    }
    
    public long obtenerTiempoSalida() {
        return tiempoSalida;
    }
    
    public void establecerTiempoSalida(long tiempoSalida) {
        this.tiempoSalida = tiempoSalida;
    }
    
    /**
     * Devuelve el tiempo de espera (tiempo desde llegada hasta inicio de servicio) en milisegundos.
     */
    public long obtenerTiempoEspera() {
        if (tiempoInicioServicio == 0) {
            return 0;
        }
        return tiempoInicioServicio - tiempoLlegada;
    }
    
    /**
     * Devuelve el tiempo de servicio (tiempo desde inicio de servicio hasta salida) en milisegundos.
     */
    public long obtenerTiempoServicio() {
        if (tiempoInicioServicio == 0 || tiempoSalida == 0) {
            return 0;
        }
        return tiempoSalida - tiempoInicioServicio;
    }
    
    @Override
    public String toString() {
        return String.format("Cliente#%d (artículos: %d)", id, numArticulos);
    }
}
