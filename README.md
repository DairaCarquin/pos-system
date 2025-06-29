# pos-system
Sistema de Punto de Venta - Proyecto POS

# 🌀 Módulo: Sistema de Puntos - Sistema POS

Este módulo forma parte del sistema de ventas desarrollado para comercios, y tiene como objetivo premiar a los clientes según sus compras mediante un sistema de puntos. Fue pensado para que funcione de forma clara, lógica y adaptable incluso si otros módulos del sistema aún no están terminados.

---

## 🧭 ¿Para qué sirve?

El módulo de **Sistema de Puntos** permite:

- Asignar puntos automáticamente a los clientes según sus compras.
- Diferenciar entre clientes comunes y VIP.
- Darle al cliente la opción de rechazar los puntos si así lo prefiere.
- Guardar los puntos acumulados por cliente.
- Consultar cuántos puntos ha acumulado cada cliente desde su perfil.

Este sistema busca incentivar la fidelidad de los clientes premiando sus compras frecuentes.

---

## 🛠️ ¿Qué incluye?

A continuación, te explicamos cada parte desarrollada:

### 1. Registrar Venta

👉 **¿Dónde está?**  
Ruta: [`/ventas/nueva`](http://localhost:8010/ventas/nueva)  
Desde aquí se puede registrar una nueva venta. El formulario permite:

- Elegir un cliente.
- Seleccionar productos (simulados por ahora).
- Definir cantidades.
- Indicar si el cliente acepta recibir puntos.

✅ Los puntos se asignan automáticamente al guardar la venta, según la cantidad de productos comprados y el tipo de cliente.

---

### 2. Ver todas las Ventas

👉 **¿Dónde está?**  
Ruta: [`/ventas/listar`](http://localhost:8010/ventas/listar)  
Aquí puedes consultar las ventas registradas. La vista muestra:

- Fecha, cliente, total y si se asignaron puntos.
- Un acceso directo al perfil del cliente.

---

### 3. Ver Perfil del Cliente

👉 **¿Dónde está?**  
Ruta: [`/cliente/{id}`] (por ejemplo: `/cliente/1`)  
Desde esta vista se puede ver:

- Información básica del cliente.
- Cuántos puntos tiene acumulados.

📌 Puedes ver la lista completa de clientes en [`/cliente`](http://localhost:8010/cliente).

---

### 4. Vista General: "Precios y Descuentos"

👉 **¿Dónde está?**  
Ruta: [`/ventas/preciosDescuentos`](http://localhost:8010/ventas/preciosDescuentos)  
Esta sección agrupa distintas funciones relacionadas a:

- Gestión de precios
- Promociones
- Sistema de puntos (el que desarrollamos)
- Reportes (a futuro)

Aquí se centraliza todo lo que ayuda a definir incentivos para el cliente.

---

## 🔍 ¿Cómo funciona el cálculo de puntos?

La lógica es muy simple:

| Tipo de cliente | Puntos por cada producto |
|-----------------|--------------------------|
| Regular         | 1 punto                  |
| VIP             | 2 puntos                 |

Además, si el cliente decide **no** recibir puntos, el sistema lo respeta y no asigna nada.

---

## 🧪 ¿Funciona aunque otros módulos no estén listos?

✅ ¡Sí! Este módulo está diseñado para funcionar de forma independiente.  
Aunque los módulos de productos o compras aún no estén implementados completamente, puedes hacer pruebas reales simulando ventas y viendo cómo se asignan los puntos correctamente.

---

## 📁 Archivos importantes

| Archivo o vista                         | Propósito principal                          |
|----------------------------------------|----------------------------------------------|
| `VentaController.java`                 | Controla las rutas de ventas y vista general |
| `VentaService.java`                    | Lógica de puntos y registro de venta         |
| `ClienteEntity.java`                   | Define tipo de cliente y puntos acumulados   |
| `ventas/venta-form.html`              | Formulario para registrar ventas             |
| `ventas/ventas-lista.html`            | Lista de ventas registradas                  |
| `cliente/lista.html`                  | Vista de todos los clientes                  |
| `cliente/perfil.html`                 | Perfil individual del cliente                |
| `ventas/preciosDescuentos.html`       | Pantalla de funciones del módulo             |

---

## 👥 ¿Para quién está hecho esto?

Este módulo fue diseñado pensando en:

- Comercios que desean fidelizar clientes con un sistema de puntos.
- Administradores que quieren tener control claro sobre promociones y beneficios.
- Equipos técnicos que necesitan integrar funciones sin afectar lo ya construido.

---

## ✨ Próximos pasos

Este módulo ya está funcional, pero está preparado para crecer con el sistema:

- Integrar con el módulo de productos reales.
- Implementar descuentos automáticos según promociones.
- Exportar reportes de puntos y ventas.

---

🧑‍💻 Desarrollado con amor y lógica modular.  
Si tienes dudas o mejoras, no dudes en escribirme 💬.
🐙

## Esquema visual de archivos

![Image](https://github.com/user-attachments/assets/750526b7-cb8c-44d5-bb7f-135fe8885023)
