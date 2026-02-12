package com.example.plataformasaas.models;

import jakarta.persistence.*;
import org.hibernate.envers.Audited;

@Entity
@Table(name = "perfiles")
@Audited
public class Perfil {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;
    private String apellidos;
    private String pais;

    @OneToOne(mappedBy = "perfil")
    private Usuario usuario;

    public Perfil() {
    }

    public Perfil(Long id, String nombre, String apellidos, String pais, Usuario usuario) {
        this.id = id;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.pais = pais;
        this.usuario = usuario;
    }

    public static PerfilBuilder builder() {
        return new PerfilBuilder();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getPais() {
        return pais;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public static class PerfilBuilder {
        private Long id;
        private String nombre;
        private String apellidos;
        private String pais;
        private Usuario usuario;

        public PerfilBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public PerfilBuilder nombre(String nombre) {
            this.nombre = nombre;
            return this;
        }

        public PerfilBuilder apellidos(String apellidos) {
            this.apellidos = apellidos;
            return this;
        }

        public PerfilBuilder pais(String pais) {
            this.pais = pais;
            return this;
        }

        public PerfilBuilder usuario(Usuario usuario) {
            this.usuario = usuario;
            return this;
        }

        public Perfil build() {
            return new Perfil(id, nombre, apellidos, pais, usuario);
        }
    }
}
