package com.cibertec.pos_system.controller;

import com.cibertec.pos_system.entity.ClienteEntity;
import com.cibertec.pos_system.service.ClienteService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

/*@RestController
@RequestMapping("/clientes")
public class ClienteController {

    private final ClienteService clienteService;

    public ClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    @GetMapping
    public List<ClienteEntity> listar() {
        return clienteService.listar();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClienteEntity> obtener(@PathVariable Long id) {
        return clienteService.obtener(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ClienteEntity guardar(@RequestBody ClienteEntity cliente) {
        return clienteService.guardar(cliente);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClienteEntity> actualizar(@PathVariable Long id, @RequestBody ClienteEntity cliente) {
        if (!clienteService.obtener(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(clienteService.actualizar(id, cliente));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        if (!clienteService.obtener(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        clienteService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}

 */
