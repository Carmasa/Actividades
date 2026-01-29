package com.example.LigaMasterAPI.TaskController;

import com.example.LigaMasterAPI.DAO.JugadorDAO;
import com.example.LigaMasterAPI.Models.*;
import com.example.LigaMasterAPI.TaskRepository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/ojeador")
public class OjeadorController {

    @Autowired
    private JugadorDAO jugadorDAO;

    @Autowired
    private JugadorRepository jugadorRepository;

    @Autowired
    private EquipoRepository equipoRepository;

    @Autowired
    private EntrenadorRepository entrenadorRepository;

    @Autowired
    private PartidoRepository partidoRepository;

    @Autowired
    private ContratoRepository contratoRepository;

    // Página principal
    @GetMapping("/")
    public String inicio(Model model) {
        model.addAttribute("jugadores", jugadorRepository.findAll());
        model.addAttribute("equipos", equipoRepository.findAll());
        return "ojeador/index";
    }

    // Buscar jugadores por posición
    @GetMapping("/buscar-por-posicion")
    public String buscarPorPosicion(Model model) {
        model.addAttribute("posiciones", new String[]{"Portero", "Defensa", "Centrocampista", "Delantero"});
        return "ojeador/buscar-posicion";
    }

    @PostMapping("/buscar-por-posicion")
    public String buscarPorPosicionResultado(@RequestParam String posicion, Model model) {
        List<Jugador> jugadores = jugadorDAO.findByPosicion(posicion);
        model.addAttribute("posicion", posicion);
        model.addAttribute("jugadores", jugadores);
        model.addAttribute("posiciones", new String[]{"Portero", "Defensa", "Centrocampista", "Delantero"});
        return "ojeador/buscar-posicion";
    }

    // Encontrar al jugador más caro
    @GetMapping("/jugador-mas-caro")
    public String jugadorMasCaro(Model model) {
        Optional<Jugador> jugador = jugadorDAO.findJugadorMasCaro();
        if (jugador.isPresent()) {
            model.addAttribute("jugador", jugador.get());
        }
        return "ojeador/jugador-mas-caro";
    }

    // Historial de equipos por jugador
    @GetMapping("/historial-equipos")
    public String historialEquipos(Model model) {
        model.addAttribute("jugadores", jugadorRepository.findAll());
        return "ojeador/historial-equipos";
    }

    @PostMapping("/historial-equipos")
    public String historialEquiposResultado(@RequestParam Long jugadorId, Model model) {
        Optional<Jugador> jugador = jugadorRepository.findById(jugadorId);
        model.addAttribute("jugadores", jugadorRepository.findAll());

        if (jugador.isPresent()) {
            Jugador j = jugador.get();
            model.addAttribute("jugador", j);
            model.addAttribute("equipos", j.getContratos());
        }

        return "ojeador/historial-equipos";
    }

    // Registro de nuevo equipo
    @GetMapping("/registrar-equipo")
    public String formularioRegistroEquipo(Model model) {
        return "ojeador/registrar-equipo";
    }

    @PostMapping("/registrar-equipo")
    public String registrarEquipo(
            @RequestParam String nombre,
            @RequestParam String estadio,
            @RequestParam String ciudad,
            Model model) {

        try {
            Equipo equipo = new Equipo(nombre, estadio, ciudad);
            equipoRepository.save(equipo);
            model.addAttribute("mensaje", "Equipo registrado exitosamente");
        } catch (Exception e) {
            model.addAttribute("error", "Error al registrar el equipo: " + e.getMessage());
        }

        return "ojeador/registrar-equipo";
    }

    // Registro de nuevo jugador
    @GetMapping("/registrar-jugador")
    public String formularioRegistro(Model model) {
        model.addAttribute("equipos", equipoRepository.findAll());
        model.addAttribute("posiciones", new String[]{"Portero", "Defensa", "Centrocampista", "Delantero"});
        return "ojeador/registrar-jugador";
    }

    @PostMapping("/registrar-jugador")
    public String registrarJugador(
            @RequestParam String nombre,
            @RequestParam String posicion,
            @RequestParam Integer dorsal,
            @RequestParam BigDecimal valorMercado,
            @RequestParam Long equipoId,
            Model model) {

        try {
            Jugador jugador = new Jugador(nombre, posicion, dorsal, valorMercado);

            Optional<Equipo> equipo = equipoRepository.findById(equipoId);
            if (equipo.isPresent()) {
                jugador.setEquipo(equipo.get());
            }

            jugadorRepository.save(jugador);
            model.addAttribute("mensaje", "Jugador registrado exitosamente");
        } catch (Exception e) {
            model.addAttribute("error", "Error al registrar el jugador: " + e.getMessage());
        }

        model.addAttribute("equipos", equipoRepository.findAll());
        model.addAttribute("posiciones", new String[]{"Portero", "Defensa", "Centrocampista", "Delantero"});
        return "ojeador/registrar-jugador";
    }


    // Listar todos los jugadores
    @GetMapping("/listar")
    public String listarJugadores(Model model) {
        model.addAttribute("jugadores", jugadorRepository.findAll());
        return "ojeador/listar-jugadores";
    }
}

