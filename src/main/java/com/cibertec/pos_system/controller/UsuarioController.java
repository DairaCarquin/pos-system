package com.cibertec.pos_system.controller;

import com.cibertec.pos_system.entity.UsuarioEntity;
import com.cibertec.pos_system.service.UsuarioService;

import org.springframework.ui.Model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/user")
public class UsuarioController {

    @Autowired
    private UsuarioService userService;

    @GetMapping
    public String listarUsuarios(Model model) {
        List<UsuarioEntity> users = userService.obtenerTodas();
        model.addAttribute("listaUsers",users);
        return "user/listar";
    }

    @GetMapping("/nuevo")
    public String mostrarFormularioNuevoUsuario(Model model) {
        model.addAttribute("user", new UsuarioEntity());
        model.addAttribute("accion","/user/nuevo");
        return "user/formulario";
    }

    @PostMapping("/nuevo")
    public String guardarNuevoUsuario(@ModelAttribute UsuarioEntity user) {
        userService.crearUser(user);
        return "redirect:/user";
    }

    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditar(@PathVariable Long id, @ModelAttribute UsuarioEntity user, Model model) {
        model.addAttribute("user",user);
        model.addAttribute("accion","/user/editar/"+id);
        return "user/formulario";
    }

    @PostMapping("/editar/{id}")
    public String actualizarUsuario(@PathVariable Long id,@ModelAttribute UsuarioEntity user){
        userService.actualizarUser(id,user);
        return "redirect:/user";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminarUsuario(@PathVariable Long id){
        userService.eliminarUser(id);
        return "redirect:/user";
    }
}
