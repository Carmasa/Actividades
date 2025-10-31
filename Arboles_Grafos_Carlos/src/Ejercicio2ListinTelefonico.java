import java.util.*;

public class Ejercicio2ListinTelefonico {
    private Ejercicio2NodoABB raiz = null;
    private Map<String, Ejercicio2Contacto> indiceTelefono = new HashMap<>();

    // --- ALTA ---
    public boolean alta(Ejercicio2Contacto contacto) {
        if (indiceTelefono.containsKey(contacto.telefono)) {
            System.out.println("Teléfono '" + contacto.telefono + "' ya existe.");
            return false;
        }
        raiz = insertar(raiz, contacto);
        indiceTelefono.put(contacto.telefono, contacto);
        return true;
    }

    private Ejercicio2NodoABB insertar(Ejercicio2NodoABB nodo, Ejercicio2Contacto contacto) {
        if (nodo == null)
            return new Ejercicio2NodoABB(contacto);
        int comparar = contacto.compareTo(nodo.contacto);
        if (comparar < 0) {
            nodo.izq = insertar(nodo.izq, contacto);
        } else if (comparar > 0) {
            nodo.der = insertar(nodo.der, contacto);
        } else {
            System.out.println("Contacto ya existe.");
            return nodo;
        }
        return nodo;
    }

    public boolean bajaPorTelefono(String telefono) {
        Ejercicio2Contacto contacto = indiceTelefono.get(telefono);
        if (contacto == null) {
            System.out.println("Teléfono no encontrado.");
            return false;
        }
        raiz = eliminar(raiz, contacto);
        indiceTelefono.remove(telefono);
        return true;
    }

    private Ejercicio2NodoABB buscarNodo(Ejercicio2NodoABB nodo, Ejercicio2Contacto clave) {
        if (nodo == null) return null;
        int comparar = clave.compareTo(nodo.contacto);
        if (comparar == 0) return nodo;
        if (comparar < 0) return buscarNodo(nodo.izq, clave);
        return buscarNodo(nodo.der, clave);
    }

    private Ejercicio2NodoABB eliminar(Ejercicio2NodoABB nodo, Ejercicio2Contacto contacto) {
        if (nodo == null) return null;
        int comparar = contacto.compareTo(nodo.contacto);
        if (comparar < 0) {
            nodo.izq = eliminar(nodo.izq, contacto);
        } else if (comparar > 0) {
            nodo.der = eliminar(nodo.der, contacto);
        } else {
            if (nodo.izq == null) return nodo.der;
            if (nodo.der == null) return nodo.izq;
            Ejercicio2NodoABB sucesor = minimo(nodo.der);
            nodo.contacto = sucesor.contacto;
            nodo.der = eliminar(nodo.der, sucesor.contacto);
        }
        return nodo;
    }

    private Ejercicio2NodoABB minimo(Ejercicio2NodoABB nodo) {
        while (nodo.izq != null) nodo = nodo.izq;
        return nodo;
    }

    public boolean modificar(String telefono, String nuevoNombre, String nuevosApellidos,
                             String nuevoTelefono, String nuevoEmail) {
        if (!indiceTelefono.containsKey(telefono)) {
            System.out.println("Teléfono no encontrado.");
            return false;
        }
        if (!telefono.equals(nuevoTelefono) && indiceTelefono.containsKey(nuevoTelefono)) {
            System.out.println("Nuevo teléfono ya existe.");
            return false;
        }

        Ejercicio2Contacto antiguo = indiceTelefono.get(telefono);
        raiz = eliminar(raiz, antiguo);
        indiceTelefono.remove(telefono);

        Ejercicio2Contacto nuevo = new Ejercicio2Contacto(nuevoNombre, nuevosApellidos, nuevoTelefono, nuevoEmail);
        raiz = insertar(raiz, nuevo);
        indiceTelefono.put(nuevoTelefono, nuevo);
        return true;
    }

    public Ejercicio2Contacto buscarPorTelefono(String telefono) {
        return indiceTelefono.get(telefono);
    }

    public Ejercicio2Contacto buscarPorNombre(String apellidos, String nombre) {
        Ejercicio2Contacto clave = new Ejercicio2Contacto(nombre, apellidos, "", "");
        Ejercicio2NodoABB nodo = buscarNodo(raiz, clave);

        if (nodo != null) {
            return nodo.contacto;
        } else {
            return null;
        }
    }

    public List<Ejercicio2Contacto> buscarPorPrefijo(String prefijo) {
        List<Ejercicio2Contacto> resultados = new ArrayList<>();
        buscarPrefijoRec(raiz, prefijo.toLowerCase(), resultados);
        return resultados;
    }

    private void buscarPrefijoRec(Ejercicio2NodoABB nodo, String prefijo, List<Ejercicio2Contacto> resultados) {
        if (nodo == null) return;
        if (nodo.contacto.apellidos.toLowerCase().startsWith(prefijo)) {
            resultados.add(nodo.contacto);
        }
        buscarPrefijoRec(nodo.izq, prefijo, resultados);
        buscarPrefijoRec(nodo.der, prefijo, resultados);
    }

    public List<Ejercicio2Contacto> buscarPorRango(String apInicio, String apFin) {
        List<Ejercicio2Contacto> resultados = new ArrayList<>();
        buscarRangoRec(raiz, apInicio, apFin, resultados);
        return resultados;
    }

    private void buscarRangoRec(Ejercicio2NodoABB nodo, String inicio, String fin, List<Ejercicio2Contacto> resultado) {
        if (nodo == null) return;
        String apellido = nodo.contacto.apellidos;
        int compInicio = apellido.compareToIgnoreCase(inicio);
        int compFin = apellido.compareToIgnoreCase(fin);
        if (compInicio >= 0 && compFin <= 0) {
            resultado.add(nodo.contacto);
        }
        if (compInicio > 0) buscarRangoRec(nodo.izq, inicio, fin, resultado);
        if (compFin < 0) buscarRangoRec(nodo.der, inicio, fin, resultado);
    }

    public void mostrarInorden() {
        if (raiz == null) {
            System.out.println("Listín vacío.");
            return;
        }
        System.out.println("Listín telefónico:");
        inorden(raiz);
    }

    private void inorden(Ejercicio2NodoABB nodo) {
        if (nodo != null) {
            inorden(nodo.izq);
            System.out.println(nodo.contacto);
            inorden(nodo.der);
        }
    }
}