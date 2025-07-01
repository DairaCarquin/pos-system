-- ==============================================================
-- DEVELOPMENT ENVIRONMENT
-- Carta tus tablas iniciales y data de prueba base aquí
-- ==============================================================

-- CREACION DE ROLES
insert into roles (role_id, nombre)
values (1, 'USUARIOS'),
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

-- CREACION DE USUARIOS
insert into users (user_id, enabled, password, username)
values
    (1, 1,'$2a$10$DHmKRktydQQlVnrRwVI65edNihJKmsgTwVdsoJVofv2e6rjcpDGri', 'administrador'),
    (2, 1,'$2a$10$DHmKRktydQQlVnrRwVI65edNihJKmsgTwVdsoJVofv2e6rjcpDGri', 'supervisor'),
    (3, 1,'$2a$10$DHmKRktydQQlVnrRwVI65edNihJKmsgTwVdsoJVofv2e6rjcpDGri', 'cajero'),
    (4, 1,'$2a$10$DHmKRktydQQlVnrRwVI65edNihJKmsgTwVdsoJVofv2e6rjcpDGri', 'soporte');


-- ASIGNACIÓN DE ROLES A USUARIOS
insert into users_roles (user_id, role_id)
values
    (1,11),
    (2,3),
    (2,4),
    (2,5),
    (2,6),
    (2,7),
    (2,8),
    (2,9),
    (2,10),
    (3,10),
    (4,3),
    (4,4),
    (4,5),
    (4,7);

-- CAJAS
INSERT INTO locales (nombre, direccion, telefono, email, horario, activo) VALUES
('Chafa fea Callao', 'Av. Sáenz Peña 150, Callao', '987654321', 'callao@chafafea.com', 'L-D 9am-8pm', TRUE),
('Chafa fea Piura', 'Av. Grau 300, Piura', '912345678', 'piura@chafafea.com', 'L-D 10am-7pm', TRUE),
('Chafa fea Talara', 'Av. Bolognesi 450, Talara, Piura', '935678123', 'talara@chafafea.com', 'L-D 8am-9pm', TRUE),
('Chafa fea San Borja', 'Av. Aviación 2500, San Borja, Lima', '945612378', 'sanborja@chafafea.com', 'L-D 8am-8pm', TRUE),
('Chafa fea Salaverry', 'Av. Salaverry 1200, Jesús María, Lima', '932165498', 'salayverry@chafafea.com', 'L-D 9am-6pm', TRUE),
('Chafa fea San Miguel', 'Av. La Marina 2000, San Miguel, Lima', '931234567', 'sanmiguel@chafafea.com', 'L-D 8am-10pm', TRUE),
('Chafa fea Miraflores', 'Av. Benavides 1135, Miraflores, Lima', '934567890', 'miraflores@chafafea.com', 'L-D 7am-11pm', TRUE),
('Chafa fea Arequipa', 'Av. Ejército 709, Cayma, Arequipa', '936789012', 'arequipa@chafafea.com', 'L-D 8am-9pm', TRUE);

-- medios_pago
INSERT INTO medios_pago (id, nombre, descripcion, tipo, activo) VALUES
(1, 'Efectivo', 'Pago en efectivo', 'EFECTIVO', 1),
(2, 'Tarjeta', 'Pago con tarjeta de crédito o débito', 'TARJETA', 1);
