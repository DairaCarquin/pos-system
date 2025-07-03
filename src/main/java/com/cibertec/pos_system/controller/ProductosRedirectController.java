package com.cibertec.pos_system.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ProductosRedirectController {

    @GetMapping("/productos")
    public String redirectToProductoLista() {
        return "redirect:/producto/lista";
    }
} 