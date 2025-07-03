package com.cibertec.pos_system.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ProveedorRedirectController {

    @GetMapping("/proveedor")
    public String redirectToProveedoresLista() {
        return "redirect:/vista/proveedores/lista";
    }
} 