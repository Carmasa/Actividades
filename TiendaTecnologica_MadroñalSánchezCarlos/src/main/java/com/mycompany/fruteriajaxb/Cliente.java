
package com.mycompany.fruteriajaxb;

import java.util.ArrayList;
import java.util.List;

public class Cliente {
    private String nombre;
    private String email;
    private Direccion direccion;
    private List<Pedido> historialCompras = new ArrayList<>();

    public Cliente(String nombre, String email, Direccion direccion) {
        this.nombre = nombre;
        this.email = email;
        this.direccion = direccion;
    }

    public String getNombre() { return nombre; }
    public String getEmail() { return email; }
    public Direccion getDireccion() { return direccion; }
    public List<Pedido> getHistorialCompras() { return historialCompras; }

    public void agregarPedido(Pedido pedido) {
        historialCompras.add(pedido);
    }
}