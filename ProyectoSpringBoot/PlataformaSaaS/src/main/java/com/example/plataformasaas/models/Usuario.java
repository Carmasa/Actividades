package com.example.plataformasaas.models;

import jakarta.persistence.*;
import org.hibernate.envers.Audited;
import java.util.List;

@Entity
@Table(name = "usuarios")
@Audited
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "perfil_id", referencedColumnName = "id")
    private Perfil perfil;

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL)
    private List<Suscripcion> suscripciones;

    @Enumerated(EnumType.STRING)
    private Rol rol;

    public Usuario() {
    }

    public Usuario(Long id, String email, String password, Perfil perfil, List<Suscripcion> suscripciones, Rol rol) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.perfil = perfil;
        this.suscripciones = suscripciones;
        this.rol = rol;
    }

    public static UsuarioBuilder builder() {
        return new UsuarioBuilder();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Perfil getPerfil() {
        return perfil;
    }

    public void setPerfil(Perfil perfil) {
        this.perfil = perfil;
    }

    public List<Suscripcion> getSuscripciones() {
        return suscripciones;
    }

    public void setSuscripciones(List<Suscripcion> suscripciones) {
        this.suscripciones = suscripciones;
    }

    public Rol getRol() {
        return rol;
    }

    public void setRol(Rol rol) {
        this.rol = rol;
    }

    public static class UsuarioBuilder {
        private Long id;
        private String email;
        private String password;
        private Perfil perfil;
        private List<Suscripcion> suscripciones;
        private Rol rol;

        public UsuarioBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public UsuarioBuilder email(String email) {
            this.email = email;
            return this;
        }

        public UsuarioBuilder password(String password) {
            this.password = password;
            return this;
        }

        public UsuarioBuilder perfil(Perfil perfil) {
            this.perfil = perfil;
            return this;
        }

        public UsuarioBuilder suscripciones(List<Suscripcion> suscripciones) {
            this.suscripciones = suscripciones;
            return this;
        }

        public UsuarioBuilder rol(Rol rol) {
            this.rol = rol;
            return this;
        }

        public Usuario build() {
            return new Usuario(id, email, password, perfil, suscripciones, rol);
        }
    }
}
