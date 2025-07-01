package com.cibertec.pos_system.puntos.task;

// --- Importaciones necesarias ---
import com.cibertec.pos_system.entity.CajaVentaEntity;
import com.cibertec.pos_system.puntos.entity.VentaProcesadaPuntos;
import com.cibertec.pos_system.puntos.repository.PuntosCajaVentaRepository;
import com.cibertec.pos_system.puntos.repository.VentaProcesadaPuntosRepository;
import com.cibertec.pos_system.puntos.service.PuntosService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Anotación @Component:
 * Le dice a Spring: "Oye, quiero que crees y gestiones un objeto (un 'bean') de esta clase
 * cuando la aplicación inicie". Esto es necesario para que Spring pueda encontrar y ejecutar la tarea.
 */
@Component
@RequiredArgsConstructor // Crea un constructor con los campos 'final' (mejor que @Autowired)
@Slf4j // Una utilidad de Lombok para poder escribir logs (ej. log.info(...))
public class ProcesadorPuntosTask {

    // --- Dependencias que la tarea necesita para trabajar ---
    // Spring se las "inyectará" automáticamente gracias a @RequiredArgsConstructor
    private final PuntosCajaVentaRepository puntosCajaVentaRepository;
    private final VentaProcesadaPuntosRepository ventaProcesadaRepository;
    private final PuntosService puntosService;

    /**
     * Anotación @Scheduled:
     * ¡Esta es la magia! Convierte un método normal en una tarea automática.
     * - fixedRate = 60000: Le dice a Spring "Ejecuta este método cada 60,000 milisegundos (es decir, cada minuto)".
     * El tiempo empieza a contar desde que termina la ejecución anterior.
     *
     * Anotación @Transactional:
     * Asegura que todas las operaciones con la base de datos dentro de este método
     * se completen con éxito, o si algo falla, no se guarde ningún cambio.
     * Es vital para mantener la consistencia de los datos.
     */
    @Scheduled(fixedRate = 60000)
    @Transactional
    public void procesarVentasPendientes() {
        log.info("--- Tarea de Puntos: Iniciando búsqueda de ventas pendientes. ---");

        // 1. Llama a TU repositorio especializado para buscar las ventas que te interesan.
        List<CajaVentaEntity> ventasSinProcesar = puntosCajaVentaRepository.findVentasFinalizadasSinPuntosAsignados();

        // 2. Si no hay ventas nuevas, termina la tarea para no gastar recursos.
        if (ventasSinProcesar.isEmpty()) {
            log.info("--- Tarea de Puntos: No se encontraron ventas nuevas. Finalizando. ---");
            return;
        }

        log.info("--- Tarea de Puntos: Se encontraron {} ventas para procesar. ---", ventasSinProcesar.size());

        // 3. Itera sobre cada venta encontrada para procesarla una por una.
        for (CajaVentaEntity venta : ventasSinProcesar) {
            try {
                // Llama a tu servicio para que haga la lógica de negocio
                int puntosAsignados = puntosService.asignarPuntosParaVenta(venta);

                // Crea el registro en tu tabla de rastreo para no volver a procesar esta venta.
                VentaProcesadaPuntos registro = new VentaProcesadaPuntos();
                registro.setVentaId(venta.getId());
                registro.setFechaProcesamiento(LocalDateTime.now());
                registro.setPuntosAsignados(puntosAsignados);
                ventaProcesadaRepository.save(registro);

                log.info("Venta ID {} procesada. Cliente ID: {}. Puntos asignados: {}",
                        venta.getId(), venta.getCliente().getId(), puntosAsignados);

            } catch (Exception e) {
                // Si algo sale mal con una venta, registra el error y continúa con la siguiente.
                log.error("Error al procesar la venta ID {}: {}", venta.getId(), e.getMessage());
            }
        }
        log.info("--- Tarea de Puntos: Procesamiento completado. ---");
    }
}