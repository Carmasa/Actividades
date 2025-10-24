/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.fruteriajaxb;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlType;
/**
 *
 * @author zamar
 */

@XmlRootElement(name="fruteria")
@XmlType(propOrder = {"nombre", "frutas", "verduras"})
@XmlAccessorType(XmlAccessType.FIELD)
public class Fruteria {
    @XmlElement(name="nombre")
    private String nombre;
    
    @XmlElementWrapper(name = "frutas")
    @XmlElement(name = "fruta")
    private ArrayList<Fruta> frutas = new ArrayList();
    
    @XmlElementWrapper(name = "verduras")
    @XmlElement(name = "verdura")
    private ArrayList<Verdura> verduras = new ArrayList();

    public Fruteria(){}

    public String getNombre() {
        return nombre;
    }
        
    public ArrayList<Fruta> getFrutas() {
        return frutas;
    }
    
    public ArrayList<Verdura> getVerduras() {
        return verduras;
    }
    
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
        
    public void setFrutas(ArrayList<Fruta> frutas) {
        this.frutas = frutas;
    }
    
    public void setVerduras(ArrayList<Verdura> verduras) {
        this.verduras = verduras;
    }
}
