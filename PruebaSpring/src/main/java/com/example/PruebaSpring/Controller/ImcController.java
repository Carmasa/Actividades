// java
package com.example.PruebaSpring.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/miapp")
public class ImcController {

    @GetMapping("/imc")
    public String mostrarFormulario() {
        return "form";
    }

    @PostMapping("/imc")
    public String calcularImc(@RequestParam double peso, @RequestParam double altura, Model model) {
        double imc = peso / (altura * altura);
        model.addAttribute("peso", peso);
        model.addAttribute("altura", altura);
        model.addAttribute("imc", String.format("%.2f", imc));
        return "vista";
    }
}