package com.cibertec.pos_system.service;

import com.cibertec.pos_system.entity.ProductoEntity;
import com.cibertec.pos_system.repository.ProductoRepository;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;
@Service
public class ProductoService {

    private static final Logger log = LoggerFactory.getLogger(ProductoService.class);

    private final ProductoRepository productoRepository;

    public ProductoService(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

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

    public List<ProductoEntity> obtenerProductosPorProveedor(Long proveedorId) {
        List<ProductoEntity> productos = productoRepository.findByProveedorId(proveedorId);

        log.info("Productos encontrados para proveedor ID {}: {}", proveedorId, productos.size());

        for (ProductoEntity p : productos) {
            log.info("Producto -> ID: {}, Nombre: {}", p.getId(), p.getNombre());
        }

        return productos;
    }
}
