package com.cibertec.pos_system.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.cibertec.pos_system.entity.CajaEntity;
import com.cibertec.pos_system.entity.CajaSesionEntity;
import com.cibertec.pos_system.service.CajaService;
import com.cibertec.pos_system.service.CajaSesionService;
import com.cibertec.pos_system.service.UsuarioService;

import jakarta.servlet.http.HttpSession;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/caja-sesion")
public class CajaSesionController {

    @Autowired
    private CajaSesionService sesionService;

    @Autowired
    private CajaService cajaService;

    @Autowired
    private  UsuarioService usuarioService;


    @GetMapping
    public String listarSesiones(Model model, HttpSession session) {
        List<CajaSesionEntity> sesiones = sesionService.listar();
        List<CajaEntity> cajas = cajaService.listar();

        Map<Long, CajaSesionEntity> sesionesActivas = new HashMap<>();
        for (CajaSesionEntity sesion : sesiones) {
            if ("ABIERTA".equals(sesion.getEstado())) {
                sesionesActivas.put(sesion.getCaja().getId(), sesion);
            }
        }

        model.addAttribute("sesiones", sesiones);
        model.addAttribute("cajas", cajas);
        model.addAttribute("sesionesActivas", sesionesActivas);
        model.addAttribute("usuarioLogueado", session.getAttribute("usuarioLogueado"));
        model.addAttribute("fechaActual", LocalDateTime.now());
        model.addAttribute("contentPage", "Mantenimiento/CajaSesionEntity.jsp");

        return "layout";
    }

    @PostMapping("/abrir")
    public String abrirCaja(@RequestParam Long cajaId, @RequestParam Long usuarioId,
                            @RequestParam double montoInicial) {

        CajaSesionEntity sesion = new CajaSesionEntity();
        sesion.setCaja(cajaService.obtener(cajaId).orElseThrow());
        sesion.setUsuario(usuarioService.obtener(usuarioId).orElseThrow());
        sesion.setFechaApertura(LocalDateTime.now());
        sesion.setMontoInicial(montoInicial);
        sesion.setEstado("ABIERTA");

        sesionService.guardar(sesion);
        return "redirect:/caja-sesion";
    }

    @PostMapping("/cerrar")
    public String cerrarCaja(@RequestParam Long sesionId, @RequestParam double montoCierre) {
        CajaSesionEntity sesion = sesionService.obtener(sesionId).orElseThrow();
        sesion.setMontoCierre(montoCierre);
        sesion.setFechaCierre(LocalDateTime.now());
        sesion.setEstado("CERRADA");

        sesionService.guardar(sesion);
        return "redirect:/caja-sesion";
    }

    @PostMapping("/delete")
    public String eliminarCaja(@RequestParam Long id) {
        sesionService.eliminar(id);
        return "redirect:/caja-sesion";
    }
}