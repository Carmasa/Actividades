package org.example.jdbc;

public class Coche {
    private Integer id;
    private String modelo;
    private double cv;

    public Coche (Integer id,String modelo,double cv) {
        this.id=id;
        this.modelo=modelo;
        this.cv=cv;
    }

    public Integer getId() {
        return id;
    }

    public String getModelo() {
        return modelo;
    }

    public double getCv() {
        return cv;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setCv(double cv) {
        this.cv = cv;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }
}
