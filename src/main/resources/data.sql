-- ==============================================================
-- DEVELOPMENT ENVIRONMENT
-- Carta tus tablas iniciales y data de prueba base aquí
-- ==============================================================

-- CREACION DE ROLES (primero)
INSERT INTO roles (role_id, nombre) VALUES
(1, 'USUARIOS'),
(2, 'ROLES'),
(3, 'PRODUCTOS'),
(4, 'CATEGORIAS'),
(5, 'LOCALES'),
(6, 'COMPRAS'),
(7, 'MEDIOS_DE_PAGO'),
(8, 'PROVEEDORES'),
(9, 'CLIENTES'),
(10, 'CAJA'),
(11, 'ADMIN');

-- CREACION DE USUARIOS (segundo)
INSERT INTO users (user_id, enabled, password, username) VALUES
(1, true, '$2a$10$DHmKRktydQQlVnrRwVI65edNihJKmsgTwVdsoJVofv2e6rjcpDGri', 'administrador'),
(2, true, '$2a$10$DHmKRktydQQlVnrRwVI65edNihJKmsgTwVdsoJVofv2e6rjcpDGri', 'supervisor'),
(3, true, '$2a$10$DHmKRktydQQlVnrRwVI65edNihJKmsgTwVdsoJVofv2e6rjcpDGri', 'cajero'),
(4, true, '$2a$10$DHmKRktydQQlVnrRwVI65edNihJKmsgTwVdsoJVofv2e6rjcpDGri', 'soporte');

-- ASIGNACIÓN DE ROLES A USUARIOS (tercero)
INSERT INTO users_roles (user_id, role_id) VALUES
(1, 11),
(2, 3),
(2, 4),
(2, 5),
(2, 6),
(2, 7),
(2, 8),
(2, 9),
(2, 10),
(3, 10),
(4, 3),
(4, 4),
(4, 5),
(4, 7);

-- LOCALES (cuarto)
INSERT INTO locales (nombre, direccion, telefono, email, horario, activo) VALUES
('Chafa fea Callao', 'Av. Sáenz Peña 150, Callao', '987654321', 'callao@chafafea.com', 'L-D 9am-8pm', true),
('Chafa fea Piura', 'Av. Grau 300, Piura', '912345678', 'piura@chafafea.com', 'L-D 10am-7pm', true),
('Chafa fea Talara', 'Av. Bolognesi 450, Talara, Piura', '935678123', 'talara@chafafea.com', 'L-D 8am-9pm', true),
('Chafa fea San Borja', 'Av. Aviación 2500, San Borja, Lima', '945612378', 'sanborja@chafafea.com', 'L-D 8am-8pm', true),
('Chafa fea Salaverry', 'Av. Salaverry 1200, Jesús María, Lima', '932165498', 'salayverry@chafafea.com', 'L-D 9am-6pm', true),
('Chafa fea San Miguel', 'Av. La Marina 2000, San Miguel, Lima', '931234567', 'sanmiguel@chafafea.com', 'L-D 8am-10pm', true),
('Chafa fea Miraflores', 'Av. Benavides 1135, Miraflores, Lima', '934567890', 'miraflores@chafafea.com', 'L-D 7am-11pm', true),
('Chafa fea Arequipa', 'Av. Ejército 709, Cayma, Arequipa', '936789012', 'arequipa@chafafea.com', 'L-D 8am-9pm', true);

-- MEDIOS DE PAGO (quinto)
INSERT INTO medios_pago (id, nombre, descripcion, tipo, activo) VALUES
(1, 'Efectivo', 'Pago en efectivo', 'EFECTIVO', true),
(2, 'Tarjeta', 'Pago con tarjeta de crédito o débito', 'TARJETA', true);

-- CAJAS (sexto - después de usuarios y locales)
INSERT INTO cajas (codigo, nombre, fecha_creacion, usuario_id, local_id) VALUES
('CA01', 'Caja 01', '2024-01-15 08:00:00', 1, 1),
('PI01', 'Caja 01', '2024-01-20 09:30:00', 1, 2),  
('TA01', 'Caja 01', '2024-02-01 10:15:00', 1, 3);


--CLIENTES
INSERT INTO clientes (dni, nombre, apellido, direccion, telefono, email, activo, tipo, puntos_acumulados) VALUES
('12345678', 'Juan', 'Pérez', 'Av. Lima 123', '999111222', 'juan.perez@email.com', 1, 'regular', 10),
('87654321', 'María', 'García', 'Calle Sol 456', '988222333', 'maria.garcia@email.com', 1, 'vip', 50),
('11223344', 'Carlos', 'Ramírez', 'Jr. Luna 789', '977333444', 'carlos.ramirez@email.com', 1, 'regular', 5),
('44332211', 'Ana', 'Torres', 'Av. Mar 321', '966444555', 'ana.torres@email.com', 1, 'vip', 80),
('55667788', 'Luis', 'Soto', 'Calle Río 654', '955555666', 'luis.soto@email.com', 1, 'regular', 0),
('88776655', 'Elena', 'Mendoza', 'Jr. Sol 987', '944666777', 'elena.mendoza@email.com', 1, 'vip', 120),
('99887766', 'Pedro', 'Vargas', 'Av. Paz 159', '933777888', 'pedro.vargas@email.com', 1, 'regular', 15),
('66778899', 'Lucía', 'Flores', 'Calle Luna 753', '922888999', 'lucia.flores@email.com', 1, 'vip', 200),
('33445566', 'Miguel', 'Castro', 'Jr. Estrella 852', '911999000', 'miguel.castro@email.com', 1, 'regular', 7),
('66554433', 'Rosa', 'Reyes', 'Av. Sol 951', '900000111', 'rosa.reyes@email.com', 1, 'vip', 60);


-- PROVEEDORES
INSERT INTO proveedores (ruc, razon_social, direccion, telefono, email, contacto, activo) VALUES
('20123456789', 'Proveedor A SAC', 'Av. Lima 123', '999111222', 'proveedora@email.com', 'Juan Pérez', true),
('20234567890', 'Distribuidora B EIRL', 'Jr. Arequipa 456', '988222333', 'distribuidorab@email.com', 'María López', true),
('20345678901', 'Importadora C SRL', 'Calle Cusco 789', '977333444', 'importadorac@email.com', 'Carlos Ruiz', true),
('20456789012', 'Mayorista D SAC', 'Av. Piura 321', '966444555', 'mayoristad@email.com', 'Ana Torres', true),
('20567890123', 'Proveedor E SAC', 'Jr. Tacna 654', '955555666', 'proveedore@email.com', 'Luis Gómez', true);

-- CATEGORIAS
INSERT INTO categorias (nombre, descripcion, activo) VALUES
('Abarrotes', 'Productos de abarrotes', true),
('Lácteos', 'Productos lácteos', true),
('Bebidas', 'Bebidas frías y calientes', true),
('Limpieza', 'Productos de limpieza', true),
('Panadería', 'Productos de panadería', true);

-- PRODUCTOS
-- Abarrotes (categoria_id = 1)
INSERT INTO productos (codigo, nombre, descripcion, precio, imagen, activo, stock_actual, categoria_id, proveedor_id) VALUES
('ABR001', 'Arroz', 'Arroz extra', 3.50, NULL, true, 100, 1, 1),
('ABR002', 'Azúcar', 'Azúcar rubia', 2.80, NULL, true, 80, 1, 1),
('ABR003', 'Fideos', 'Fideos largos', 2.20, NULL, true, 60, 1, 1),
('ABR004', 'Lentejas', 'Lentejas verdes', 4.00, NULL, true, 50, 1, 1),
('ABR005', 'Aceite', 'Aceite vegetal', 8.50, NULL, true, 40, 1, 1),
('ABR006', 'Sal', 'Sal fina', 1.00, NULL, true, 70, 1, 1),
('ABR007', 'Harina', 'Harina sin preparar', 3.00, NULL, true, 100, 1, 1),
('ABR008', 'Avena', 'Avena en hojuelas', 2.50, NULL, true, 100, 1, 1),
('ABR009', 'Maíz', 'Maíz mote', 2.80, NULL, true, 100, 1, 1),
('ABR010', 'Frejol', 'Frejol canario', 5.00, NULL, true, 100, 1, 1);

-- Lácteos (categoria_id = 2)
INSERT INTO productos (codigo, nombre, descripcion, precio, imagen, activo, stock_actual, categoria_id, proveedor_id) VALUES
('LAC001', 'Leche Entera', 'Leche fresca', 4.20, NULL, true, 60, 2, 1),
('LAC002', 'Yogurt', 'Yogurt natural', 2.50, NULL, true, 50, 2, 1),
('LAC003', 'Queso Fresco', 'Queso de vaca', 8.00, NULL, true, 40, 2, 1),
('LAC004', 'Mantequilla', 'Mantequilla sin sal', 6.50, NULL, true, 30, 2, 1),
('LAC005', 'Leche Evaporada', 'Leche en lata', 3.00, NULL, true, 70, 2, 1),
('LAC006', 'Queso Edam', 'Queso bola', 12.00, NULL, true, 20, 2, 1),
('LAC007', 'Queso Parmesano', 'Queso rallado', 10.00, NULL, true, 15, 2, 1),
('LAC008', 'Leche Deslactosada', 'Leche sin lactosa', 4.80, NULL, true, 25, 2, 1),
('LAC009', 'Yogurt Griego', 'Yogurt espeso', 3.50, NULL, true, 18, 2, 1),
('LAC010', 'Queso Mozzarella', 'Queso para pizza', 9.00, NULL, true, 22, 2, 1);

-- Bebidas (categoria_id = 3)
INSERT INTO productos (codigo, nombre, descripcion, precio, imagen, activo, stock_actual, categoria_id, proveedor_id) VALUES
('BEB001', 'Agua Mineral', 'Agua sin gas', 1.50, NULL, true, 100, 3, 1),
('BEB002', 'Gaseosa Cola', 'Bebida gaseosa', 3.00, NULL, true, 80, 3, 1),
('BEB003', 'Jugo de Naranja', 'Jugo natural', 2.80, NULL, true, 60, 3, 1),
('BEB004', 'Cerveza', 'Cerveza rubia', 5.00, NULL, true, 50, 3, 1),
('BEB005', 'Vino Tinto', 'Vino de mesa', 15.00, NULL, true, 20, 3, 1),
('BEB006', 'Energizante', 'Bebida energética', 4.00, NULL, true, 30, 3, 1),
('BEB007', 'Té Helado', 'Té frío', 2.20, NULL, true, 25, 3, 1),
('BEB008', 'Café', 'Café instantáneo', 3.50, NULL, true, 40, 3, 1),
('BEB009', 'Jugo de Piña', 'Jugo natural', 2.80, NULL, true, 35, 3, 1),
('BEB010', 'Agua con Gas', 'Agua gasificada', 2.00, NULL, true, 28, 3, 1);

-- Limpieza (categoria_id = 4)
INSERT INTO productos (codigo, nombre, descripcion, precio, imagen, activo, stock_actual, categoria_id, proveedor_id) VALUES
('LIM001', 'Detergente', 'Detergente en polvo', 6.00, NULL, true, 40, 4, 1),
('LIM002', 'Jabón', 'Jabón de lavar', 2.50, NULL, true, 35, 4, 1),
('LIM003', 'Lejía', 'Lejía líquida', 3.00, NULL, true, 30, 4, 1),
('LIM004', 'Esponja', 'Esponja multiuso', 1.20, NULL, true, 50, 4, 1),
('LIM005', 'Limpiavidrios', 'Spray limpiavidrios', 5.00, NULL, true, 20, 4, 1),
('LIM006', 'Desinfectante', 'Desinfectante líquido', 4.50, NULL, true, 25, 4, 1),
('LIM007', 'Trapeador', 'Trapeador absorbente', 8.00, NULL, true, 15, 4, 1),
('LIM008', 'Escoba', 'Escoba de paja', 7.00, NULL, true, 18, 4, 1),
('LIM009', 'Guantes', 'Guantes de limpieza', 3.50, NULL, true, 22, 4, 1),
('LIM010', 'Paño', 'Paño de cocina', 2.00, NULL, true, 27, 4, 1);

-- Panadería (categoria_id = 5)
INSERT INTO productos (codigo, nombre, descripcion, precio, imagen, activo, stock_actual, categoria_id, proveedor_id) VALUES
('PAN001', 'Pan Francés', 'Pan tradicional', 0.30, NULL, true, 200, 5, 1),
('PAN002', 'Pan Integral', 'Pan con fibra', 0.50, NULL, true, 150, 5, 1),
('PAN003', 'Pan de Molde', 'Pan para sandwich', 3.00, NULL, true, 60, 5, 1),
('PAN004', 'Croissant', 'Pan hojaldrado', 1.20, NULL, true, 40, 5, 1),
('PAN005', 'Baguette', 'Pan largo', 2.00, NULL, true, 30, 5, 1),
('PAN006', 'Pan Dulce', 'Pan con pasas', 1.00, NULL, true, 50, 5, 1),
('PAN007', 'Rosca', 'Rosca de pan', 1.50, NULL, true, 25, 5, 1),
('PAN008', 'Pan Ciabatta', 'Pan italiano', 2.50, NULL, true, 20, 5, 1),
('PAN009', 'Pan Brioche', 'Pan suave', 2.00, NULL, true, 18, 5, 1),
('PAN010', 'Pan de Yema', 'Pan dulce tradicional', 1.80, NULL, true, 22, 5, 1);


-- DESCUENTOS
-- DESCUENTO 1: 15% en toda la categoría Bebidas
INSERT INTO descuentos (nombre, tipo, valor, activo, fecha_inicio, fecha_fin, condicion_minima_precio, categoria_id, producto_id) 
VALUES ('Descuento 15% en Bebidas', 'PORCENTAJE', 15.00, true, '2024-12-01', '2025-12-31', NULL, 3, NULL);

-- DESCUENTO 2: S/2 de descuento fijo en Gaseosa Cola
INSERT INTO descuentos (nombre, tipo, valor, activo, fecha_inicio, fecha_fin, condicion_minima_precio, categoria_id, producto_id) 
VALUES ('S/2 OFF en Gaseosa Cola', 'FIJO', 2.00, true, '2024-12-01', '2025-12-31', NULL, NULL, 12);

-- DESCUENTO 3: 2x1 en Pan Francés
INSERT INTO descuentos (nombre, tipo, valor, activo, fecha_inicio, fecha_fin, condicion_minima_precio, categoria_id, producto_id) 
VALUES ('2x1 en Pan Francés', 'DOS_POR_UNO', 0.00, true, '2024-12-01', '2025-12-31', NULL, NULL, 41);

-- DESCUENTO 4: 20% en Lácteos para compras mayores a S/20
INSERT INTO descuentos (nombre, tipo, valor, activo, fecha_inicio, fecha_fin, condicion_minima_precio, categoria_id, producto_id) 
VALUES ('20% OFF Lácteos - Compra mínima S/20', 'PORCENTAJE', 20.00, true, '2025-12-01', '2025-01-31', 20.00, 2, NULL);

-- DESCUENTO 5: S/1 OFF en Aceite vegetal
INSERT INTO descuentos (nombre, tipo, valor, activo, fecha_inicio, fecha_fin, condicion_minima_precio, categoria_id, producto_id) 
VALUES ('S/1 OFF en Aceite Vegetal', 'FIJO', 1.00, true, '2024-12-01', '2025-12-31', NULL, NULL, 5);

-- DESCUENTO 6: 2x1 en Yogurt Natural
INSERT INTO descuentos (nombre, tipo, valor, activo, fecha_inicio, fecha_fin, condicion_minima_precio, categoria_id, producto_id) 
VALUES ('2x1 en Yogurt Natural', 'DOS_POR_UNO', 0.00, true, '2024-12-01', '2025-12-31', NULL, NULL, 12);

-- DESCUENTO 7: 10% en toda la categoría Abarrotes
INSERT INTO descuentos (nombre, tipo, valor, activo, fecha_inicio, fecha_fin, condicion_minima_precio, categoria_id, producto_id) 
VALUES ('10% OFF en Abarrotes', 'PORCENTAJE', 10.00, true, '2024-12-01', '2025-12-31', NULL, 1, NULL);

-- DESCUENTO 8: S/3 OFF en Detergente
INSERT INTO descuentos (nombre, tipo, valor, activo, fecha_inicio, fecha_fin, condicion_minima_precio, categoria_id, producto_id) 
VALUES ('S/3 OFF en Detergente', 'FIJO', 3.00, true, '2024-12-01', '2025-12-31', NULL, NULL, 31);

-- DESCUENTO 9: Black Friday 30% en Panadería (FUTURO)
INSERT INTO descuentos (nombre, tipo, valor, activo, fecha_inicio, fecha_fin, condicion_minima_precio, categoria_id, producto_id) 
VALUES ('Black Friday - 30% OFF Panadería', 'PORCENTAJE', 30.00, true, '2024-12-15', '2025-12-31', NULL, 5, NULL);

-- DESCUENTO 10: 2x1 en Cerveza (fin de semana)
INSERT INTO descuentos (nombre, tipo, valor, activo, fecha_inicio, fecha_fin, condicion_minima_precio, categoria_id, producto_id) 
VALUES ('2x1 Cerveza - Fin de Semana', 'DOS_POR_UNO', 0.00, true, '2025-12-01', '2024-12-31', NULL, NULL, 14);
