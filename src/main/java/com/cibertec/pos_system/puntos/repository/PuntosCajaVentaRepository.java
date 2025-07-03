package com.cibertec.pos_system.puntos.repository;

import com.cibertec.pos_system.entity.CajaVentaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PuntosCajaVentaRepository extends JpaRepository<CajaVentaEntity, Long> {
    /**
     * consulta personalizada. Busca ventas que están FINALIZADAS, 
     * tienen un cliente y aún no han sido procesadas para puntos.
     */
    @Query("SELECT v FROM CajaVentaEntity v WHERE v.estado = 'FINALIZADA' AND v.cliente IS NOT NULL AND v.id NOT IN (SELECT p.ventaId FROM VentaProcesadaPuntos p)")
    List<CajaVentaEntity> findVentasFinalizadasSinPuntosAsignados();

}