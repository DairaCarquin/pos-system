package com.cibertec.pos_system.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.cibertec.pos_system.entity.VentaWeb;
import com.cibertec.pos_system.repository.VentaWebRepository;

import jakarta.persistence.EntityNotFoundException;

public class VentaWebService {

    @Autowired
    private VentaWebRepository repository;

    public List<VentaWeb> findAll(){
        return repository.findAll();
    }

    public VentaWeb findbyId(long id){
        return repository.findById(id).orElseThrow( () -> new EntityNotFoundException("venta not found") );
    }
    public VentaWeb save(VentaWeb venta){
        return repository.save(venta);
    }
    
    public VentaWeb update(long id, VentaWeb venta){
        
        boolean isExit = repository.existsById(id);

        if(isExit){
            VentaWeb temp = new VentaWeb();

            temp.setId(id);
            temp.setNumeroComprobante(venta.getNumeroComprobante());
            temp.setFecha(venta.getFecha());
            temp.setCliente(venta.getCliente());
            temp.setSubtotal(venta.getSubtotal());
            temp.setImpuestos(venta.getImpuestos());
            temp.setTotal(venta.getTotal());
            temp.setMetodoPago(venta.getMetodoPago());
            temp.setEstado(venta.getEstado());
            temp.setDetalles(venta.getDetalles());
            

            return repository.save(temp);

        }

        return null;

    }
    
}
