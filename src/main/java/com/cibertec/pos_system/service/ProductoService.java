package com.cibertec.pos_system.service;
import com.cibertec.pos_system.entity.CategoriaEntity;
import com.cibertec.pos_system.entity.DescuentoEntity;
import com.cibertec.pos_system.entity.ProductoEntity;
import com.cibertec.pos_system.entity.PrecioDescuentoEntity;
import com.cibertec.pos_system.repository.CategoriaRepository;
import com.cibertec.pos_system.repository.PrecioDescuentoRepository;
import com.cibertec.pos_system.repository.ProductoRepository;
import com.cibertec.pos_system.service.DescuentoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ProductoService {
    private static final Logger log = LoggerFactory.getLogger(ProductoService.class);
    private final ProductoRepository productoRepository;
    private final CategoriaRepository categoriaRepository;
    private final PrecioDescuentoRepository precioDescuentoRepository;
    private final DescuentoService descuentoService;
    public ProductoService(
            ProductoRepository productoRepository,
            CategoriaRepository categoriaRepository,
            PrecioDescuentoRepository precioDescuentoRepository,
            DescuentoService descuentoService
    ) {
        this.productoRepository = productoRepository;
        this.categoriaRepository = categoriaRepository;
        this.precioDescuentoRepository = precioDescuentoRepository;
        this.descuentoService = descuentoService;
    }
    public List<ProductoEntity> listar() {
        return productoRepository.findAll();
    }
    public List<ProductoEntity> listarTodos() {
        return listar();
    }
    public List<ProductoEntity> obtenerProductosPorProveedor(Long proveedorId) {
        List<ProductoEntity> productos = productoRepository.findByProveedorId(proveedorId);
        log.info("Productos encontrados para proveedor ID {}: {}", proveedorId, productos.size());
        return productos;
    }
    public List<CategoriaEntity> listarCategorias() {
        return categoriaRepository.findAll();
    }
    public ProductoEntity buscarPorId(Long id) {
        return productoRepository.findById(id).orElse(null);
    }
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
  public List<ProductoEntity> listarSoloConDescuento() {
    sincronizarDescuentosVigentes(); // asegura que estén actualizados natm
    List<PrecioDescuentoEntity> lista = precioDescuentoRepository.findAll()
        .stream()
        .filter(PrecioDescuentoEntity::isVigente)
        .toList();
    List<ProductoEntity> resultado = new ArrayList<>();
    for (PrecioDescuentoEntity pd : lista) {
        ProductoEntity producto = pd.getProducto();
        producto.setPrecio(pd.getPrecioFinal());
        producto.setMontoDescuento(pd.getValorDescuento());
        producto.setDescuentoAplicado(pd.getDescuento());
        resultado.add(producto);
    }
    return resultado;
}
    public void sincronizarDescuentosVigentes() {
    List<ProductoEntity> productos = productoRepository.findAll();
    LocalDate hoy = LocalDate.now();
    for (ProductoEntity producto : productos) {
        DescuentoEntity descuento = descuentoService.obtenerDescuentoAplicable(producto);
        if (descuento != null) {
            // Verifica si ya existe un descuento vigente natm
            Optional<PrecioDescuentoEntity> existente = precioDescuentoRepository.findByProductoAndVigenteTrue(producto);
            if (existente.isEmpty()) {
                BigDecimal precioOriginal = producto.getPrecio();
                BigDecimal montoDescuento = BigDecimal.ZERO;
                BigDecimal precioFinal = precioOriginal;

                switch (descuento.getTipo()) {
                    case PORCENTAJE:
                        montoDescuento = precioOriginal.multiply(descuento.getValor())
                                .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
                        break;
                    case FIJO:
                        montoDescuento = descuento.getValor();
                        break;
                    case DOS_POR_UNO:
                        montoDescuento = precioOriginal.divide(BigDecimal.valueOf(2), 2, RoundingMode.HALF_UP);
                        break;
                }
                precioFinal = precioOriginal.subtract(montoDescuento);
                PrecioDescuentoEntity nuevo = PrecioDescuentoEntity.builder()
                        .producto(producto)
                        .descuento(descuento)
                        .precioFinal(precioFinal)
                        .valorDescuento(montoDescuento)
                        .fechaAplicacion(hoy)
                        .vigente(true)
                        .fechaInicio(descuento.getFechaInicio())
                        .fechaFin(descuento.getFechaFin())
                        .build();
                precioDescuentoRepository.save(nuevo);
            }
        }
    }
}
public List<ProductoEntity> buscarPorNombre(String nombre) {
    return productoRepository.findByNombreContainingIgnoreCase(nombre);
}

    public List<ProductoEntity> listarPorProveedor(Long proveedorId) {
        return productoRepository.findByProveedorId(proveedorId);
    }
    
    public List<ProductoEntity> listarPorCategoria(Long categoriaId) {
        return productoRepository.findByCategoriaId(categoriaId);

        
    }
}
      

      
    


