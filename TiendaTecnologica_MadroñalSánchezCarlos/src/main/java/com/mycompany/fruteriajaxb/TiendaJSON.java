package com.mycompany.fruteriajaxb;

import java.util.ArrayList;
import java.util.List;

public class TiendaJSON {
    private String nombre;
    private List<Fruta> frutas;
    private List<Verdura> verduras;
    private List<Cliente> usuarios;

    public TiendaJSON(Fruteria fruteria) {
        this.nombre = fruteria.getNombre();
        this.frutas = fruteria.getFrutas();
        this.verduras = fruteria.getVerduras();
        this.usuarios = new ArrayList<>();
    }

    public void agregarCliente(Cliente cliente) {
        usuarios.add(cliente);
    }
}