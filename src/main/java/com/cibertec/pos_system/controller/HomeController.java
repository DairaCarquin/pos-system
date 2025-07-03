package com.cibertec.pos_system.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    @GetMapping("/")
    public String home() {
        // Redirige a la página de usuarios. Cambia "/user" por la ruta que prefieras.
        return "redirect:/user";
    }
}
