package com.cibertec.pos_system.controller;

import com.cibertec.pos_system.entity.LocalEntity;
import com.cibertec.pos_system.service.LocalService;

import org.springframework.ui.Model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/local")
public class LocalController {

    @Autowired
    private LocalService localService;

    @GetMapping
    public String listarLocales(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Model model) {
        Page<LocalEntity> localesPage = localService.listarPaginado(page, size);
        model.addAttribute("listaLocales", localesPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", localesPage.getTotalPages());
        model.addAttribute("totalElements", localesPage.getTotalElements());
        model.addAttribute("size", size);
        return "local/listar";
    }

    @GetMapping("/nuevo")
    public String mostrarFormularioNuevoLocal(Model model) {
        model.addAttribute("local", new LocalEntity());
        return "local/formulario";
    }

    @PostMapping("/guardar")
    public String guardarLocal(@ModelAttribute LocalEntity local) {
        localService.crear(local);
        return "redirect:/local";
    }

    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditar(@PathVariable Long id, Model model) {
        LocalEntity local = localService.obtener(id).orElseThrow(() -> new RuntimeException("Local no encontrado"));
        model.addAttribute("local", local);
        return "local/formulario";
    }

    @PostMapping("/actualizar")
    public String actualizarLocal(@ModelAttribute LocalEntity local) {
        localService.actualizar(local.getId(), local);
        return "redirect:/local";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminarLocal(@PathVariable Long id) {
        localService.eliminar(id);
        return "redirect:/local";
    }
}