package com.cibertec.pos_system.service;

import com.cibertec.pos_system.entity.ClienteEntity;
import com.cibertec.pos_system.repository.ClienteRepository;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

@Service
public class ClienteService {

    private final ClienteRepository clienteRepository;

    public ClienteService(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    public List<ClienteEntity> listar() {
        return clienteRepository.findAll();
    }

    public ClienteEntity guardar(ClienteEntity cliente) {
        return clienteRepository.save(cliente);
    }

    public ClienteEntity actualizar(Long id, ClienteEntity cliente) {
        cliente.setId(id);
        return clienteRepository.save(cliente);
    }

    public void eliminar(Long id) {
        clienteRepository.deleteById(id);
    }

    public Optional<ClienteEntity> obtener(Long id) {
        return clienteRepository.findById(id);
    }

    public Page<ClienteEntity> buscarPorDni(String dni, Pageable pageable) {
        return clienteRepository.findByDniContaining(dni, pageable);
    }


    public Page<ClienteEntity> listarPaginado(Pageable pageable) {
        return clienteRepository.findAll(pageable); // ✅ Esto es correcto
    }



}

