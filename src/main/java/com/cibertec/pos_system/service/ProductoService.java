package com.cibertec.pos_system.service;

import com.cibertec.pos_system.entity.CategoriaEntity; //importado por Nicole-precios
import com.cibertec.pos_system.entity.ProductoEntity;
import com.cibertec.pos_system.entity.PrecioDescuentoEntity;//imp natm
import com.cibertec.pos_system.repository.CategoriaRepository;//imp natm
import com.cibertec.pos_system.repository.PrecioDescuentoRepository;//imp natm
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
    private final CategoriaRepository categoriaRepository;//imp natm
    private final PrecioDescuentoRepository precioDescuentoRepository;//imp natm
    
    public ProductoService(ProductoRepository productoRepository,CategoriaRepository categoriaRepository,
                           PrecioDescuentoRepository precioDescuentoRepository) {
        this.productoRepository = productoRepository;
             this.categoriaRepository = categoriaRepository;//natm
        this.precioDescuentoRepository = precioDescuentoRepository;//natm
    }

    
    public List<ProductoEntity> listar() {
        return productoRepository.findAll();
    }
    public List<ProductoEntity> listarTodos() {
        return listar(); //natm
    }

     // Productos con descuento vigente según precio_descuento natm
    public List<ProductoEntity> listarConDescuentosAplicados() {
        return precioDescuentoRepository.findProductosConDescuentoVigente();
    }

    //natm
    public List<ProductoEntity> obtenerProductosPorProveedor(Long proveedorId) {
        List<ProductoEntity> productos = productoRepository.findByProveedorId(proveedorId);
        log.info("Productos encontrados para proveedor ID {}: {}", proveedorId, productos.size());
        return productos;
    }
    //natm
      public List<CategoriaEntity> listarCategorias() {
        return categoriaRepository.findAll();
    }
    //natm
      public ProductoEntity buscarPorId(Long id) {
        return productoRepository.findById(id).orElse(null);
    }
    //natm
    public CategoriaEntity buscarCategoriaPorId(Long id) {
        return categoriaRepository.findById(id).orElse(null);
    }


    public ProductoEntity crear(ProductoEntity producto) {
        return productoRepository.save(producto);
    }

    public ProductoEntity actualizar(Long id, ProductoEntity nuevoProducto) {
        productoRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado"));

        nuevoProducto.setId(id);
        return productoRepository.save(nuevoProducto);
    }

    public void eliminar(Long id) {
        productoRepository.deleteById(id);
    }
    
    public Optional<ProductoEntity> obtener(Long id) {
        return productoRepository.findById(id);
    }
  
    public List<ProductoEntity> listarPorProveedor(Long proveedorId) {
        return productoRepository.findByProveedorId(proveedorId);
    }
    
    public List<ProductoEntity> listarPorCategoria(Long categoriaId) {
        return productoRepository.findByCategoriaId(categoriaId);
    }
}
      
    


