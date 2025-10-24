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

@XmlRootElement(name="verdura")
@XmlType(propOrder = {"nombre", "precio", "tipo"})
@XmlAccessorType(XmlAccessType.FIELD) // Accede directamente a los campos
public class Verdura {
    @XmlElement(name="nombre")
    private String nombre;
    @XmlElement(name = "precio")
    private Double precio;
    @XmlElement(name = "tipo")
    private String tipo;

    
    public Verdura(){}

    public String getNombre() {
        return nombre;
    }
    
    public Double getPrecio() {
        return precio;
    }
        
    public String getTipo() {
        return tipo;
    }
            
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setPrecio(Double  precio) {
        this.precio = precio;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
    
}
