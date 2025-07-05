package com.cibertec.pos_system.controller;

import com.cibertec.pos_system.entity.ProductoEntity;
import com.cibertec.pos_system.service.CategoriaService;
import com.cibertec.pos_system.service.FileUploadService;
import com.cibertec.pos_system.service.ProductoService;
import com.cibertec.pos_system.service.ProveedorService;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/producto")
public class ProductoWebController {

    private final ProductoService productoService;
    private final CategoriaService categoriaService;
    private final ProveedorService proveedorService;
    private final FileUploadService fileUploadService;

    public ProductoWebController(ProductoService productoService, 
                               CategoriaService categoriaService,
                               ProveedorService proveedorService,
                               FileUploadService fileUploadService) {
        this.productoService = productoService;
        this.categoriaService = categoriaService;
        this.proveedorService = proveedorService;
        this.fileUploadService = fileUploadService;
    }

    @GetMapping("/lista")
    public String listar(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Model model) {
        Page<ProductoEntity> productosPage = productoService.listarPaginado(page, size);
        model.addAttribute("productos", productosPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", productosPage.getTotalPages());
        model.addAttribute("totalElements", productosPage.getTotalElements());
        model.addAttribute("size", size);
        return "producto/lista";
    }

    @GetMapping("/nuevo")
    public String nuevo(Model model) {
        model.addAttribute("producto", new ProductoEntity());
        model.addAttribute("listaCategorias", categoriaService.listar());
        model.addAttribute("listaProveedores", proveedorService.listar());
        return "producto/formulario";
    }

    @PostMapping("/guardar")
    public String guardar(@ModelAttribute ProductoEntity producto, 
                         @RequestParam(value = "imagenFile", required = false) MultipartFile imagenFile) {
        try {
            if (imagenFile != null && !imagenFile.isEmpty()) {
                String filename = fileUploadService.uploadImage(imagenFile);
                producto.setImagen(filename);
            }
            productoService.crear(producto);
        } catch (Exception e) {
            // Log the error and continue without image
            System.err.println("Error uploading image: " + e.getMessage());
        }
        return "redirect:/producto/lista";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model) {
        ProductoEntity producto = productoService.buscarPorId(id);
        if (producto == null) {
            return "redirect:/producto/lista";
        }
        model.addAttribute("producto", producto);
        model.addAttribute("listaCategorias", categoriaService.listar());
        model.addAttribute("listaProveedores", proveedorService.listar());
        return "producto/formulario";
    }

    @PostMapping("/actualizar")
    public String actualizar(@ModelAttribute ProductoEntity producto,
                           @RequestParam(value = "imagenFile", required = false) MultipartFile imagenFile) {
        try {
            // Get the existing product to preserve the current image if no new one is uploaded
            ProductoEntity existingProducto = productoService.buscarPorId(producto.getId());
            if (existingProducto != null) {
                producto.setImagen(existingProducto.getImagen());
            }
            
            if (imagenFile != null && !imagenFile.isEmpty()) {
                // Delete old image if exists
                if (existingProducto != null && existingProducto.getImagen() != null) {
                    fileUploadService.deleteImage(existingProducto.getImagen());
                }
                
                // Upload new image
                String filename = fileUploadService.uploadImage(imagenFile);
                producto.setImagen(filename);
            }
            
            productoService.actualizar(producto.getId(), producto);
        } catch (Exception e) {
            // Log the error and continue without image
            System.err.println("Error uploading image: " + e.getMessage());
        }
        return "redirect:/producto/lista";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id) {
        ProductoEntity producto = productoService.buscarPorId(id);
        if (producto != null && producto.getImagen() != null) {
            fileUploadService.deleteImage(producto.getImagen());
        }
        productoService.eliminar(id);
        return "redirect:/producto/lista";
    }

    @GetMapping
    public String redirectToLista() {
        return "redirect:/producto/lista";
    }
} 