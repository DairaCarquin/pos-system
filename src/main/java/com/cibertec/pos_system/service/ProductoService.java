package com.cibertec.pos_system.service;

import com.cibertec.pos_system.entity.ProductoEntity;
import com.cibertec.pos_system.repository.ProductoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductoService {

    private final ProductoRepository productoRepository;

    public ProductoService(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    // List of products
    public List<ProductoEntity> listar() {
        return productoRepository.findAll();
    }

    public ProductoEntity crear(ProductoEntity producto) {
        return productoRepository.save(producto);
    }

    public ProductoEntity actualizar(Long id, ProductoEntity producto) {
        producto.setId(id);
        return productoRepository.save(producto);
    }

    public void eliminar(Long id) {
        productoRepository.deleteById(id);
    }

    public Optional<ProductoEntity> obtener(Long id) {
        return productoRepository.findById(id);
    }
}
