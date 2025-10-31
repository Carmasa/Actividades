import java.util.*;

public class Ejercicio3GrafoRutas {
    private Map<String, List<Ejercicio3Arista>> grafo = new HashMap<>();
    private static final double PENALIZACION_TRANSPORTE = 5.0;

    public void agregarArista(String origen, String destino, Ejercicio3ModoTransporte modo, double tiempo) {
        grafo.computeIfAbsent(origen, k -> new ArrayList<>()).add(new Ejercicio3Arista(destino, modo, tiempo));
    }

    public Ejercicio3Ruta buscarRuta(String origen, String destino,
                           Set<Ejercicio3ModoTransporte> modosProhibidos,
                           boolean minimizarTransbordos) {
        if (!grafo.containsKey(origen) || !grafo.containsKey(destino)) {
            return new Ejercicio3Ruta();
        }

        // Dijkstra modificado
        Map<String, Double> dist = new HashMap<>();
        Map<String, String> prevNodo = new HashMap<>();
        Map<String, Ejercicio3ModoTransporte> prevModo = new HashMap<>();
        Map<String, Integer> transbordos = new HashMap<>();

        PriorityQueue<NodoDist> cola = new PriorityQueue<>(Comparator.comparingDouble(n -> n.dist));
        dist.put(origen, 0.0);
        transbordos.put(origen, 0);
        cola.offer(new NodoDist(origen, 0.0, null));

        while (!cola.isEmpty()) {
            NodoDist actual = cola.poll();
            String u = actual.nodo;
            double distU = actual.dist;

            if (distU > dist.getOrDefault(u, Double.MAX_VALUE)) continue;
            if (u.equals(destino)) break;

            for (Ejercicio3Arista arista : grafo.getOrDefault(u, Collections.emptyList())) {
                if (modosProhibidos.contains(arista.modo)) continue;

                double peso = arista.tiempo;
                int transActual = transbordos.getOrDefault(u, 0);
                Ejercicio3ModoTransporte modoAnterior = prevModo.get(u);

                if (minimizarTransbordos && modoAnterior != null && modoAnterior != arista.modo) {
                    peso += PENALIZACION_TRANSPORTE;
                }

                double nuevaDist = distU + peso;
                if (nuevaDist < dist.getOrDefault(arista.destino, Double.MAX_VALUE)) {
                    dist.put(arista.destino, nuevaDist);
                    prevNodo.put(arista.destino, u);
                    prevModo.put(arista.destino, arista.modo);
                    transbordos.put(arista.destino, modoAnterior != null && modoAnterior != arista.modo ?
                            transActual + 1 : transActual);
                    cola.offer(new NodoDist(arista.destino, nuevaDist, arista.modo));
                }
            }
        }

        Ejercicio3Ruta ruta = new Ejercicio3Ruta();
        if (!dist.containsKey(destino)) return ruta;

        List<String> camino = new ArrayList<>();
        List<Ejercicio3ModoTransporte> modos = new ArrayList<>();
        String paso = destino;
        while (!paso.equals(origen)) {
            camino.add(0, paso);
            modos.add(0, prevModo.get(paso));
            paso = prevNodo.get(paso);
        }
        camino.add(0, origen);

        ruta.nodos.add(origen);
        Ejercicio3ModoTransporte modoAnt = null;
        for (int i = 0; i < modos.size(); i++) {
            String sig = camino.get(i + 1);
            Ejercicio3ModoTransporte m = modos.get(i);
            double t = obtenerTiempo(origen.equals(camino.get(i)) ? origen : camino.get(i), sig, m);
            ruta.addPaso(sig, m, t);
            modoAnt = m;
        }

        return ruta;
    }

    private double obtenerTiempo(String origen, String destino, Ejercicio3ModoTransporte modo) {
        for (Ejercicio3Arista a : grafo.getOrDefault(origen, Collections.emptyList())) {
            if (a.destino.equals(destino) && a.modo == modo) {
                return a.tiempo;
            }
        }
        return 0.0;
    }

    private static class NodoDist {
        String nodo;
        double dist;
        Ejercicio3ModoTransporte modo;

        NodoDist(String nodo, double dist, Ejercicio3ModoTransporte modo) {
            this.nodo = nodo;
            this.dist = dist;
            this.modo = modo;
        }
    }

    // Ejemplo
    public void cargarGrafoEjemplo() {
        // Zonas: A (Casa), B (Estación Metro), C (Centro), D (Trabajo), E (Ilerna)
        agregarArista("A", "B", Ejercicio3ModoTransporte.PEATON, 10);
        agregarArista("A", "C", Ejercicio3ModoTransporte.BICI, 15);
        agregarArista("B", "C", Ejercicio3ModoTransporte.METRO, 5);
        agregarArista("C", "D", Ejercicio3ModoTransporte.AUTOBUS, 12);
        agregarArista("C", "E", Ejercicio3ModoTransporte.PEATON, 8);
        agregarArista("E", "D", Ejercicio3ModoTransporte.BICI, 10);
        agregarArista("B", "D", Ejercicio3ModoTransporte.AUTOBUS, 20);
        // Algunas rutas unidireccionales
        agregarArista("D", "C", Ejercicio3ModoTransporte.PEATON, 14);
    }
}