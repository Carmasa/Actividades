package com.mycompany.fruteriajaxb;

public class ProductoPedido {
    private String nombre;
    private double precioUnitario;
    private int cantidad;

    public ProductoPedido(String nombre, double precioUnitario, int cantidad) {
        this.nombre = nombre;
        this.precioUnitario = precioUnitario;
        this.cantidad = cantidad;
    }

    public double getSubtotal() {
        return precioUnitario * cantidad;
    }

    public String getNombre() { return nombre; }
    public double getPrecioUnitario() { return precioUnitario; }
    public int getCantidad() { return cantidad; }
}