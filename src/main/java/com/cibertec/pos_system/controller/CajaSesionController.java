package com.cibertec.pos_system.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.cibertec.pos_system.entity.CajaEntity;
import com.cibertec.pos_system.entity.CajaSesionEntity;
import com.cibertec.pos_system.entity.UsuarioEntity;
import com.cibertec.pos_system.repository.UsuarioRepository;
import com.cibertec.pos_system.service.CajaService;
import com.cibertec.pos_system.service.CajaSesionService;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@Controller
@RequestMapping("/caja-sesion")
public class CajaSesionController {

    @Autowired
    private CajaSesionService sesionService;

    @Autowired
    private CajaService cajaService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @GetMapping
    public String listarSesiones(@RequestParam(value = "q", required = false) String q, Model model) {
        List<CajaSesionEntity> sesiones;
        if (q != null && !q.isEmpty()) {
            sesiones = sesionService.buscarPorCualquierCampo(q);
        } else {
            sesiones = sesionService.listar();
        }
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
        model.addAttribute("fechaActual", LocalDateTime.now());

        return "caja/caja-sesion";
    }

    @PostMapping("/abrir")
    public String abrirCaja(
            @RequestParam Long cajaId,
            @RequestParam double montoInicial,
            RedirectAttributes redirectAttributes) {

        CajaSesionEntity sesion = new CajaSesionEntity();
        sesion.setCaja(cajaService.obtener(cajaId).orElseThrow());
        sesion.setFechaApertura(LocalDateTime.now());
        sesion.setMontoInicial(montoInicial);
        sesion.setEstado("ABIERTA");

        // Asignar usuario de apertura
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String usuarioActual = authentication.getName();
        UsuarioEntity usuarioSesion = usuarioRepository.getUserByUsername(usuarioActual);
        sesion.setUsuarioApertura(usuarioSesion);

        sesionService.guardar(sesion);
        redirectAttributes.addAttribute("aperturaExitosa", true);
        return "redirect:/caja";
    }

    @PostMapping("/cerrar")
    public String cerrarCaja(
            @RequestParam Long cajaId,
            @RequestParam double montoCierre,
            RedirectAttributes redirectAttributes) {

        CajaSesionEntity sesion = sesionService.obtenerSesionActivaPorCajaId(cajaId);
        if (sesion != null) {
            if (montoCierre < sesion.getMontoInicial()) {
                redirectAttributes.addFlashAttribute("errorCerrar", "Monto de cierre menor a monto inicial, no se puede cerrar la caja");
                return "redirect:/caja";
            }
            sesion.setMontoCierre(montoCierre);
            sesion.setFechaCierre(LocalDateTime.now());
            sesion.setEstado("CERRADA");

            // Asignar usuario de cierre
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String usuarioActual = authentication.getName();
            UsuarioEntity usuarioSesion = usuarioRepository.getUserByUsername(usuarioActual);
            sesion.setUsuarioCierre(usuarioSesion);

            sesionService.guardar(sesion);
            redirectAttributes.addFlashAttribute("cierreExitosa", true);
        }
        return "redirect:/caja";
    }

    @PostMapping("/delete")
    public String eliminarCaja(@RequestParam Long id) {
        sesionService.eliminar(id);
        return "redirect:/caja-sesion";
    }
}