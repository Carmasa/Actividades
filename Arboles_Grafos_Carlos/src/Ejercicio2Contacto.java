import java.util.Objects;

public class Ejercicio2Contacto implements Comparable<Ejercicio2Contacto> {
    public String nombre;
    public String apellidos;
    public String telefono;
    public String email;

    public Ejercicio2Contacto(String nombre, String apellidos, String telefono, String email) {
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.telefono = telefono;
        this.email = email;
    }

    @Override
    public int compareTo(Ejercicio2Contacto otro) {
        int compApell = this.apellidos.compareToIgnoreCase(otro.apellidos);
        if (compApell != 0)
            return compApell;
        return this.nombre.compareToIgnoreCase(otro.nombre);
    }

    @Override
    public String toString() {
        return String.format("Contacto{apellidos='%s', nombre='%s', teléfono='%s', email='%s'}",
                apellidos, nombre, telefono, email != null ? email : "—");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ejercicio2Contacto contacto = (Ejercicio2Contacto) o;
        return Objects.equals(telefono, contacto.telefono);
    }

    @Override
    public int hashCode() {
        return Objects.hash(telefono);
    }
}