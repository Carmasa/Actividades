public class Ejercicio3Arista {
    public String destino;
    public Ejercicio3ModoTransporte modo;
    public double tiempo; // en minutos

    public Ejercicio3Arista(String destino, Ejercicio3ModoTransporte modo, double tiempo) {
        this.destino = destino;
        this.modo = modo;
        this.tiempo = tiempo;
    }

    @Override
    public String toString() {
        return String.format("[%s → %s, %s, %.1f min]", destino, modo, tiempo);
    }
}