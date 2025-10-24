/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.fruteriajaxb;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
/**
 *
 * @author zamar
 */

@XmlRootElement(name="fruta")
@XmlType(propOrder = {"nombre", "precio", "temporada", "oferta"})
@XmlAccessorType(XmlAccessType.FIELD) // Accede directamente a los campos
public class Fruta {
    @XmlElement(name="nombre")
    private String nombre;
    @XmlElement(name = "precio")
    private Double precio;
    @XmlElement(name = "temporada")
    private String temporada;
    @XmlElement(name = "oferta")
    private String oferta;
    
    public Fruta(){}

    public String getNombre() {
        return nombre;
    }
    
    public Double getPrecio() {
        return precio;
    }
        
    public String getTemporada() {
        return temporada;
    }
            
    public String getOferta() {
        return oferta;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setPrecio(Double precio) {
        this.precio = precio;
    }

    public void setTemporada(String temporada) {
        this.temporada = temporada;
    }
    
    public void setOferta(String oferta) {
        this.oferta = oferta;
    }
}
