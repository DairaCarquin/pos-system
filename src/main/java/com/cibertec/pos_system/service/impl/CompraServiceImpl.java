package com.cibertec.pos_system.service.impl;

import com.cibertec.pos_system.service.CompraService;
import com.cibertec.pos_system.entity.CompraEntity;
import com.cibertec.pos_system.repository.CompraRepository;
import com.cibertec.pos_system.service.CompraService;
import org.springframework.stereotype.Service; // <-- Este es el que faltaba

import java.time.LocalDate;
import java.util.List;

@Service
public class CompraServiceImpl implements CompraService {

    private final CompraRepository compraRepository;

    public CompraServiceImpl(CompraRepository compraRepository) {
        this.compraRepository = compraRepository;
    }

    @Override
    public List<CompraEntity> listarComprasPorCliente(String dni) {
        return compraRepository.findByClienteDni(dni);
    }
    @Override
    public List<CompraEntity> listarComprasPorClienteId(Long clienteId) {
        return compraRepository.findByClienteId(clienteId);
    }

    @Override
    public CompraEntity registrarCompra(CompraEntity compra) {
        long comprasRealizadas = compraRepository.contarComprasPorCliente(compra.getCliente().getDni());

        if ((comprasRealizadas + 1) % 10 == 0) {
            compra.setTotal(compra.getTotal() * 0.40); // 60% de descuento
        }

        compra.setFecha(LocalDate.now());
        return compraRepository.save(compra);
    }

    @Override
    public long contarComprasPorCliente(String dni) {
        return compraRepository.contarComprasPorCliente(dni);
    }
}
