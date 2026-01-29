package com.example.LigaMasterAPI.TaskRepository;

import com.example.LigaMasterAPI.Models.Jugador;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JugadorRepository extends JpaRepository<Jugador, Long> {
}
