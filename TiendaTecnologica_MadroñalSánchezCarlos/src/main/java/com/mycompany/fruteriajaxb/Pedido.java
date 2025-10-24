package com.mycompany.fruteriajaxb;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Pedido {
    private List<ProductoPedido> productos = new ArrayList<>();
    private LocalDate fecha;
    private double total;

    public Pedido() {
        this.fecha = LocalDate.now();
    }

    public void agregarProducto(ProductoPedido p) {
        productos.add(p);
        total += p.getSubtotal();
    }

    public List<ProductoPedido> getProductos() { return productos; }
    public LocalDate getFecha() { return fecha; }
    public double getTotal() { return total; }
}