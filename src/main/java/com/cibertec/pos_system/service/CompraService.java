package com.cibertec.pos_system.service;

import com.cibertec.pos_system.entity.CompraEntity;

import java.util.List;

public interface CompraService {
    List<CompraEntity> listarComprasPorCliente(String dni);
    List<CompraEntity> listarComprasPorClienteId(Long clienteId);
    CompraEntity registrarCompra(CompraEntity compra);
    long contarComprasPorCliente(String dni);
}
