package com.example.LigaMasterAPI.DAO;

import com.example.LigaMasterAPI.Models.Jugador;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface JugadorDAO extends JpaRepository<Jugador, Long> {

    // Buscar jugadores por posición
    List<Jugador> findByPosicion(String posicion);

    // Encontrar al jugador más caro de la liga
    @Query("SELECT j FROM Jugador j ORDER BY j.valorMercado DESC LIMIT 1")
    Optional<Jugador> findJugadorMasCaro();

    // Saber en qué equipos ha jugado un futbolista a lo largo de su carrera
    @Query("SELECT DISTINCT j.equipo FROM Jugador j WHERE j.id = :jugadorId AND j.equipo IS NOT NULL")
    List<Object> findEquiposPorJugador(@Param("jugadorId") Long jugadorId);
}
