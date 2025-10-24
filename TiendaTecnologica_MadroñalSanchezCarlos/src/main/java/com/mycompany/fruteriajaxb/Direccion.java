package com.mycompany.fruteriajaxb;

public class Direccion {
    private String calle;
    private int numero;
    private String ciudad;
    private String pais;

    public Direccion(String calle, int numero, String ciudad, String pais) {
        this.calle = calle;
        this.numero = numero;
        this.ciudad = ciudad;
        this.pais = pais;
    }

    public String getCalle() { return calle; }
    public int getNumero() { return numero; }
    public String getCiudad() { return ciudad; }
    public String getPais() { return pais; }
}