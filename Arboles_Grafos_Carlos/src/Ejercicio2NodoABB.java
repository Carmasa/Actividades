public class Ejercicio2NodoABB {
    public Ejercicio2Contacto contacto;
    public Ejercicio2NodoABB izq, der;

    public Ejercicio2NodoABB(Ejercicio2Contacto contacto) {
        this.contacto = contacto;
        this.izq = null;
        this.der = null;
    }
}