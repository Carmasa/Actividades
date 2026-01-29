package com.example.LigaMasterAPI.TaskRepository;

import com.example.LigaMasterAPI.Models.Partido;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PartidoRepository extends JpaRepository<Partido, Long> {
}
