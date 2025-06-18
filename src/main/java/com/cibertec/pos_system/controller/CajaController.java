package com.cibertec.pos_system.controller;

import com.cibertec.pos_system.entity.CajaEntity;
import com.cibertec.pos_system.entity.CajaSesionEntity;
import com.cibertec.pos_system.entity.UsuarioEntity;
import com.cibertec.pos_system.repository.UsuarioRepository;
import com.cibertec.pos_system.service.CajaSesionService;
import com.cibertec.pos_system.service.impl.CajaServiceInterface;
import com.cibertec.pos_system.service.impl.LocalServiceInterface;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/caja")
public class CajaController {

    private final CajaServiceInterface cajaService;
    private final LocalServiceInterface localService;
    private final CajaSesionService cajaSesionService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    public CajaController(
            CajaServiceInterface cajaService,
            LocalServiceInterface localService,
            CajaSesionService cajaSesionService) {
        this.cajaService = cajaService;
        this.localService = localService;
        this.cajaSesionService = cajaSesionService;
    }

    @GetMapping
    public String listar(@RequestParam(value = "q", required = false) String q, Model model) {
        List<CajaEntity> cajas;
        if (q != null && !q.isEmpty()) {
            cajas = cajaService.buscarPorCualquierCampo(q);
        } else {
            cajas = cajaService.listar();
        }
        List<CajaSesionEntity> sesiones = cajaSesionService.listar();

        Map<Long, CajaSesionEntity> sesionesActivas = new HashMap<>();
        Map<Long, Integer> sesionesPorCaja = new HashMap<>();

        for (CajaSesionEntity sesion : sesiones) {
            Long cajaId = sesion.getCaja().getId();
            if ("ABIERTA".equals(sesion.getEstado())) {
                sesionesActivas.put(cajaId, sesion);
            }
            sesionesPorCaja.put(cajaId, sesionesPorCaja.getOrDefault(cajaId, 0) + 1);
        }

        model.addAttribute("cajas", cajas);
        model.addAttribute("locales", localService.listar());
        model.addAttribute("sesionesActivas", sesionesActivas);
        model.addAttribute("sesionesPorCaja", sesionesPorCaja);

        return "caja/cajas";
    }

    @GetMapping("/nuevo")
    public String mostrarFormularioNuevaCaja(Model model) {
        model.addAttribute("caja", new CajaEntity());
        model.addAttribute("locales", localService.listar());
        model.addAttribute("accion", "/caja/nuevo");
        return "caja/formulario";
    }

    @PostMapping("/nuevo")
    public String guardarNuevaCaja(@ModelAttribute CajaEntity caja) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String usuarioActual = authentication.getName();
        UsuarioEntity usuarioSesion = usuarioRepository.getUserByUsername(usuarioActual);
        caja.setUsuario(usuarioSesion);
        caja.setFechaCreacion(LocalDateTime.now());
        cajaService.crear(caja);
        return "redirect:/caja";
    }

    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditar(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        List<CajaSesionEntity> sesiones = cajaSesionService.listar();
        boolean cajaAbierta = sesiones.stream()
                .anyMatch(s -> s.getCaja().getId().equals(id) && "ABIERTA".equals(s.getEstado()));
        if (cajaAbierta) {
            redirectAttributes.addFlashAttribute("errorEditar", "No se puede editar una caja abierta.");
            return "redirect:/caja";
        }
        CajaEntity caja = cajaService.obtener(id).orElseThrow(() -> new RuntimeException("Caja no encontrada"));
        model.addAttribute("caja", caja);
        model.addAttribute("locales", localService.listar());
        model.addAttribute("accion", "/caja/editar/" + id);
        return "caja/formulario";
    }

    @PostMapping("/editar/{id}")
    public String editarCaja(@PathVariable Long id, @ModelAttribute CajaEntity caja, RedirectAttributes redirectAttributes) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String usuarioActual = authentication.getName();
        UsuarioEntity usuarioSesion = usuarioRepository.getUserByUsername(usuarioActual);

        caja.setUsuarioActualizacion(usuarioSesion);
        caja.setFechaActualizacion(LocalDateTime.now());

        cajaService.actualizar(id, caja);
        redirectAttributes.addFlashAttribute("mensaje", "Caja actualizada correctamente.");
        return "redirect:/caja";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminarCaja(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        List<CajaSesionEntity> sesiones = cajaSesionService.listar();
        boolean cajaAbierta = sesiones.stream()
                .anyMatch(s -> s.getCaja().getId().equals(id) && "ABIERTA".equals(s.getEstado()));
        if (cajaAbierta) {
            redirectAttributes.addFlashAttribute("errorEliminar", "No se puede eliminar una caja abierta.");
            return "redirect:/caja";
        }
        // Eliminar todas las sesiones asociadas a la caja antes de eliminar la caja
        List<CajaSesionEntity> sesionesDeCaja = cajaSesionService.listarPorCaja(id);
        for (CajaSesionEntity sesion : sesionesDeCaja) {
            cajaSesionService.eliminar(sesion.getId());
        }
        cajaService.eliminar(id);
        return "redirect:/caja";
    }

    // Métodos API REST (opcional, si los necesitas)
    @PostMapping
    @ResponseBody
    public CajaEntity crear(@RequestBody CajaEntity caja) {
        caja.setFechaCreacion(LocalDateTime.now());
        return cajaService.crear(caja);
    }

    @PutMapping("/{id}")
    @ResponseBody
    public CajaEntity actualizar(@PathVariable Long id, @RequestBody CajaEntity caja) {
        return cajaService.actualizar(id, caja);
    }

    @DeleteMapping("/{id}")
    @ResponseBody
    public void eliminar(@PathVariable Long id) {
        cajaService.eliminar(id);
    }

    @GetMapping("/{id}")
    @ResponseBody
    public CajaEntity obtener(@PathVariable Long id) {
        return cajaService.obtener(id)
                .orElseThrow(() -> new RuntimeException("Caja no encontrada"));
    }
}