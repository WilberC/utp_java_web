-- Seed data for UTP Web Development Project
-- This migration populates all tables with realistic sample data in Spanish
-- IMPORTANT: This migration will only run if no users exist in the database

-- Check if seed data already exists (users table is empty)
DO $$
BEGIN
    -- If users already exist, skip seeding
    IF EXISTS (SELECT 1 FROM users LIMIT 1) THEN
        RAISE NOTICE 'Seed data already exists, skipping V7__seed_data migration';
        RETURN;
    END IF;
END $$;

-- Seed users table
INSERT INTO users (name, email, password, address, phone, role, status, email_verified, phone_verified) VALUES
('Usuario Administrador', 'admin@utp.edu.pe', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', 'Av. Universitaria 1801, San Miguel, Lima', '+51 999 123 456', 'ADMIN', 'ACTIVE', true, true),
('Juan Pérez', 'juan.perez@email.com', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', 'Jr. Lima 123, Miraflores, Lima', '+51 999 234 567', 'CUSTOMER', 'ACTIVE', true, true),
('María García', 'maria.garcia@email.com', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', 'Av. Arequipa 456, San Isidro, Lima', '+51 999 345 678', 'CUSTOMER', 'ACTIVE', true, false),
('Carlos López', 'carlos.lopez@email.com', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', 'Jr. Tacna 789, Centro de Lima', '+51 999 456 789', 'CUSTOMER', 'ACTIVE', false, true),
('Ana Rodríguez', 'ana.rodriguez@email.com', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', 'Av. Javier Prado 321, San Borja, Lima', '+51 999 567 890', 'CUSTOMER', 'ACTIVE', true, true),
('Luis Martínez', 'luis.martinez@email.com', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', 'Jr. Huancavelica 654, Barranco, Lima', '+51 999 678 901', 'CUSTOMER', 'INACTIVE', false, false),
('Carmen Silva', 'carmen.silva@email.com', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', 'Av. La Marina 987, San Miguel, Lima', '+51 999 789 012', 'CUSTOMER', 'ACTIVE', true, true),
('Roberto Torres', 'roberto.torres@email.com', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', 'Jr. Callao 147, Callao', '+51 999 890 123', 'CUSTOMER', 'ACTIVE', true, false),
('Patricia Vargas', 'patricia.vargas@email.com', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', 'Av. Brasil 258, Pueblo Libre, Lima', '+51 999 901 234', 'CUSTOMER', 'SUSPENDED', false, true),
('Fernando Ruiz', 'fernando.ruiz@email.com', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', 'Jr. Huaraz 369, Jesús María, Lima', '+51 999 012 345', 'CUSTOMER', 'ACTIVE', true, true);

-- Seed categories table
INSERT INTO categories (name) VALUES
('Electrónicos'),
('Libros'),
('Ropa'),
('Hogar y Jardín'),
('Deportes y Aire Libre'),
('Juguetes y Juegos'),
('Salud y Belleza'),
('Automotriz'),
('Alimentos y Bebidas'),
('Suministros de Oficina');

-- Seed products table
INSERT INTO products (name, description, price, stock, category_id, barcode, image_url, status, low_stock_threshold, is_featured) VALUES
('Laptop HP Pavilion 15', '15.6" Full HD, Intel Core i5, 8GB RAM, 512GB SSD', 2999.99, 25, 1, '1234567890123', '/images/laptop-hp.jpg', 'ACTIVE', 5, true),
('iPhone 15 Pro', '6.1" Super Retina XDR, chip A17 Pro, 128GB de almacenamiento', 3999.99, 15, 1, '1234567890124', '/images/iphone-15-pro.jpg', 'ACTIVE', 3, true),
('Samsung Galaxy S24', '6.2" Dynamic AMOLED, Snapdragon 8 Gen 3, 256GB', 3499.99, 20, 1, '1234567890125', '/images/samsung-s24.jpg', 'ACTIVE', 5, false),
('Audífonos Bluetooth Inalámbricos', 'Cancelación de ruido, 30 horas de batería', 299.99, 50, 1, '1234567890126', '/images/headphones.jpg', 'ACTIVE', 10, false),
('El Libro del Código Limpio', 'Robert C. Martin - Guía de desarrollo de software profesional', 89.99, 30, 2, '1234567890127', '/images/clean-code.jpg', 'ACTIVE', 5, true),
('Libro de Patrones de Diseño', 'Gang of Four - Patrones de diseño de software esenciales', 79.99, 25, 2, '1234567890128', '/images/design-patterns.jpg', 'ACTIVE', 5, false),
('Spring Boot en Acción', 'Craig Walls - Guía completa de Spring Boot', 69.99, 20, 2, '1234567890129', '/images/spring-boot.jpg', 'ACTIVE', 5, true),
('Camiseta de Algodón para Hombre', '100% algodón, ajuste cómodo, varios colores', 29.99, 100, 3, '1234567890130', '/images/mens-tshirt.jpg', 'ACTIVE', 20, false),
('Jeans para Mujer', 'Denim de alta calidad, ajuste moderno, color azul', 89.99, 60, 3, '1234567890131', '/images/womens-jeans.jpg', 'ACTIVE', 15, false),
('Zapatillas para Correr', 'Ligeras, transpirables, perfectas para carreras diarias', 199.99, 40, 5, '1234567890132', '/images/running-shoes.jpg', 'ACTIVE', 10, true),
('Set de Herramientas de Jardín', 'Conjunto completo de herramientas esenciales para jardinería', 149.99, 30, 4, '1234567890133', '/images/garden-tools.jpg', 'ACTIVE', 8, false),
('Mat de Yoga', 'Material antideslizante y ecológico, incluye correa de transporte', 49.99, 80, 5, '1234567890134', '/images/yoga-mat.jpg', 'ACTIVE', 15, false),
('Juego de Mesa: Monopolio', 'Clásico juego de compraventa de propiedades para diversión familiar', 39.99, 25, 6, '1234567890135', '/images/monopoly.jpg', 'ACTIVE', 5, false),
('Set de Cuidado de la Piel', 'Rutina completa de cuidado facial, ingredientes naturales', 129.99, 35, 7, '1234567890136', '/images/skincare-set.jpg', 'ACTIVE', 10, true),
('Ambientador para Auto', 'Fragancia duradera, fácil de instalar', 19.99, 120, 8, '1234567890137', '/images/air-freshener.jpg', 'ACTIVE', 25, false),
('Granos de Café Orgánico', 'Calidad premium, origen único, paquete de 1kg', 59.99, 45, 9, '1234567890138', '/images/coffee-beans.jpg', 'ACTIVE', 10, false),
('Mouse Inalámbrico', 'Diseño ergonómico, conexión 2.4GHz, batería de 12 meses', 39.99, 70, 10, '1234567890139', '/images/wireless-mouse.jpg', 'ACTIVE', 15, false),
('Lámpara de Escritorio', 'Iluminación LED, brillo ajustable, puerto USB de carga', 79.99, 55, 10, '1234567890140', '/images/desk-lamp.jpg', 'ACTIVE', 12, false),
('Botella de Agua', 'Acero inoxidable, capacidad 1L, mantiene bebidas frías por 24h', 34.99, 90, 5, '1234567890141', '/images/water-bottle.jpg', 'ACTIVE', 20, false),
('Mochila', 'Material duradero, múltiples compartimentos, funda para laptop', 89.99, 40, 3, '1234567890142', '/images/backpack.jpg', 'ACTIVE', 10, true);

-- Seed orders table
INSERT INTO orders (order_date, status, total_amount, user_id, payment_method, payment_status, shipping_address, billing_address, notes, estimated_delivery_date) VALUES
('2024-01-15 10:30:00', 'DELIVERED', 3299.98, 2, 'CREDIT_CARD', 'COMPLETED', 'Jr. Lima 123, Miraflores, Lima', 'Jr. Lima 123, Miraflores, Lima', 'Por favor entregar en horario laboral', '2024-01-17 14:00:00'),
('2024-01-16 14:20:00', 'SHIPPED', 159.98, 3, 'BANK_TRANSFER', 'COMPLETED', 'Av. Arequipa 456, San Isidro, Lima', 'Av. Arequipa 456, San Isidro, Lima', 'Regalo de cumpleaños', '2024-01-18 16:00:00'),
('2024-01-17 09:15:00', 'PROCESSING', 299.99, 4, 'CREDIT_CARD', 'PROCESSING', 'Jr. Tacna 789, Centro de Lima', 'Jr. Tacna 789, Centro de Lima', NULL, '2024-01-19 10:00:00'),
('2024-01-18 16:45:00', 'PENDING', 189.97, 5, 'CASH_ON_DELIVERY', 'PENDING', 'Av. Javier Prado 321, San Borja, Lima', 'Av. Javier Prado 321, San Borja, Lima', 'Llamar antes de la entrega', '2024-01-20 15:00:00'),
('2024-01-19 11:30:00', 'DELIVERED', 3999.99, 2, 'CREDIT_CARD', 'COMPLETED', 'Jr. Lima 123, Miraflores, Lima', 'Jr. Lima 123, Miraflores, Lima', NULL, '2024-01-21 12:00:00'),
('2024-01-20 13:20:00', 'CANCELLED', 449.97, 7, 'CREDIT_CARD', 'REFUNDED', 'Av. La Marina 987, San Miguel, Lima', 'Av. La Marina 987, San Miguel, Lima', 'Cliente solicitó cancelación', NULL),
('2024-01-21 08:45:00', 'SHIPPED', 129.98, 8, 'BANK_TRANSFER', 'COMPLETED', 'Jr. Callao 147, Callao', 'Jr. Callao 147, Callao', NULL, '2024-01-23 14:00:00'),
('2024-01-22 15:10:00', 'PROCESSING', 79.99, 10, 'CREDIT_CARD', 'PROCESSING', 'Jr. Huaraz 369, Jesús María, Lima', 'Jr. Huaraz 369, Jesús María, Lima', 'Entrega express solicitada', '2024-01-24 09:00:00'),
('2024-01-23 12:00:00', 'PENDING', 259.97, 3, 'CASH_ON_DELIVERY', 'PENDING', 'Av. Arequipa 456, San Isidro, Lima', 'Av. Arequipa 456, San Isidro, Lima', NULL, '2024-01-25 16:00:00'),
('2024-01-24 10:30:00', 'DELIVERED', 149.99, 5, 'CREDIT_CARD', 'COMPLETED', 'Av. Javier Prado 321, San Borja, Lima', 'Av. Javier Prado 321, San Borja, Lima', NULL, '2024-01-26 11:00:00');

-- Seed order_items table
INSERT INTO order_items (quantity, subtotal, order_id, product_id) VALUES
(1, 2999.99, 1, 1),
(1, 299.99, 1, 4),
(1, 89.99, 2, 5),
(1, 69.99, 2, 7),
(1, 299.99, 3, 4),
(1, 29.99, 4, 8),
(1, 89.99, 4, 9),
(1, 69.99, 4, 10),
(1, 3999.99, 5, 2),
(1, 199.99, 6, 11),
(1, 129.99, 6, 14),
(1, 119.98, 6, 12),
(1, 89.99, 7, 5),
(1, 39.99, 7, 10),
(1, 79.99, 8, 6),
(1, 89.99, 9, 5),
(1, 69.99, 9, 7),
(1, 99.99, 9, 10),
(1, 149.99, 10, 11);

-- Seed payment_transactions table
INSERT INTO payment_transactions (order_id, amount, currency, payment_method, status, transaction_id, error_message) VALUES
(1, 3299.98, 'PEN', 'CREDIT_CARD', 'COMPLETED', 'TXN_001_20240115', NULL),
(2, 159.98, 'PEN', 'BANK_TRANSFER', 'COMPLETED', 'TXN_002_20240116', NULL),
(3, 299.99, 'PEN', 'CREDIT_CARD', 'PROCESSING', 'TXN_003_20240117', NULL),
(5, 3999.99, 'PEN', 'CREDIT_CARD', 'COMPLETED', 'TXN_005_20240119', NULL),
(6, 449.97, 'PEN', 'CREDIT_CARD', 'REFUNDED', 'TXN_006_20240120', 'Cliente solicitó reembolso'),
(7, 129.98, 'PEN', 'BANK_TRANSFER', 'COMPLETED', 'TXN_007_20240121', NULL),
(8, 79.99, 'PEN', 'CREDIT_CARD', 'PROCESSING', 'TXN_008_20240122', NULL),
(10, 149.99, 'PEN', 'CREDIT_CARD', 'COMPLETED', 'TXN_010_20240124', NULL);

-- Seed email_templates table
INSERT INTO email_templates (code, subject, body) VALUES
('WELCOME_EMAIL', '¡Bienvenido a la Tienda UTP Web Development!', 'Estimado/a {{userName}},\n\n¡Bienvenido a nuestra tienda en línea! Nos emociona tenerte como cliente.\n\nSaludos cordiales,\nEquipo UTP Web Development'),
('ORDER_CONFIRMATION', 'Confirmación de Pedido - Pedido #{{orderId}}', 'Estimado/a {{userName}},\n\n¡Gracias por tu pedido! Tu pedido ha sido confirmado y está siendo procesado.\n\nDetalles del Pedido:\nNúmero de Pedido: {{orderId}}\nMonto Total: {{totalAmount}}\n\nTe notificaremos cuando tu pedido sea enviado.\n\nSaludos cordiales,\nEquipo UTP Web Development'),
('ORDER_SHIPPED', 'Tu Pedido Ha Sido Enviado - Pedido #{{orderId}}', 'Estimado/a {{userName}},\n\n¡Excelentes noticias! Tu pedido ha sido enviado y está en camino hacia ti.\n\nNúmero de Pedido: {{orderId}}\nFecha de Entrega Estimada: {{estimatedDelivery}}\n\nRastrea tu pedido: {{trackingUrl}}\n\nSaludos cordiales,\nEquipo UTP Web Development'),
('PASSWORD_RESET', 'Solicitud de Restablecimiento de Contraseña', 'Estimado/a {{userName}},\n\nHas solicitado restablecer tu contraseña. Haz clic en el enlace de abajo para establecer una nueva contraseña:\n\n{{resetLink}}\n\nEste enlace expirará en 24 horas.\n\nSi no solicitaste esto, por favor ignora este correo.\n\nSaludos cordiales,\nEquipo UTP Web Development'),
('PROMOTIONAL_OFFER', '¡Oferta Especial Solo para Ti!', 'Estimado/a {{userName}},\n\n¡Tenemos una oferta especial solo para ti!\n\n{{offerDescription}}\n\nUsa el código: {{promoCode}}\n\nVálido hasta: {{expiryDate}}\n\nSaludos cordiales,\nEquipo UTP Web Development');

-- Seed notifications table
INSERT INTO notifications (user_id, type, status, title, content, metadata, sent_at, read_at) VALUES
(2, 'EMAIL', 'SENT', 'Pedido Confirmado', 'Tu pedido #1 ha sido confirmado', '{"orderId": 1, "totalAmount": 3299.98}', '2024-01-15 10:35:00', '2024-01-15 11:00:00'),
(2, 'EMAIL', 'SENT', 'Pedido Enviado', 'Tu pedido #1 ha sido enviado', '{"orderId": 1, "trackingNumber": "TRK001234567"}', '2024-01-17 09:00:00', '2024-01-17 10:30:00'),
(2, 'EMAIL', 'SENT', 'Pedido Entregado', 'Tu pedido #1 ha sido entregado', '{"orderId": 1}', '2024-01-17 14:30:00', '2024-01-17 15:00:00'),
(3, 'EMAIL', 'SENT', 'Pedido Confirmado', 'Tu pedido #2 ha sido confirmado', '{"orderId": 2, "totalAmount": 159.98}', '2024-01-16 14:25:00', '2024-01-16 15:00:00'),
(3, 'EMAIL', 'SENT', 'Pedido Enviado', 'Tu pedido #2 ha sido enviado', '{"orderId": 2, "trackingNumber": "TRK001234568"}', '2024-01-18 10:00:00', '2024-01-18 11:00:00'),
(4, 'EMAIL', 'SENT', 'Pedido Confirmado', 'Tu pedido #3 ha sido confirmado', '{"orderId": 3, "totalAmount": 299.99}', '2024-01-17 09:20:00', '2024-01-17 10:00:00'),
(5, 'EMAIL', 'SENT', 'Pedido Confirmado', 'Tu pedido #4 ha sido confirmado', '{"orderId": 4, "totalAmount": 189.97}', '2024-01-18 16:50:00', '2024-01-18 17:00:00'),
(2, 'EMAIL', 'SENT', 'Pedido Confirmado', 'Tu pedido #5 ha sido confirmado', '{"orderId": 5, "totalAmount": 3999.99}', '2024-01-19 11:35:00', '2024-01-19 12:00:00'),
(7, 'EMAIL', 'SENT', 'Pedido Cancelado', 'Tu pedido #6 ha sido cancelado', '{"orderId": 6, "refundAmount": 449.97}', '2024-01-20 13:25:00', '2024-01-20 14:00:00'),
(8, 'EMAIL', 'SENT', 'Pedido Confirmado', 'Tu pedido #7 ha sido confirmado', '{"orderId": 7, "totalAmount": 129.98}', '2024-01-21 08:50:00', '2024-01-21 09:00:00'),
(10, 'EMAIL', 'SENT', 'Pedido Confirmado', 'Tu pedido #8 ha sido confirmado', '{"orderId": 8, "totalAmount": 79.99}', '2024-01-22 15:15:00', NULL),
(3, 'EMAIL', 'SENT', 'Pedido Confirmado', 'Tu pedido #9 ha sido confirmado', '{"orderId": 9, "totalAmount": 259.97}', '2024-01-23 12:05:00', NULL),
(5, 'EMAIL', 'SENT', 'Pedido Confirmado', 'Tu pedido #10 ha sido confirmado', '{"orderId": 10, "totalAmount": 149.99}', '2024-01-24 10:35:00', '2024-01-24 11:00:00'),
(5, 'EMAIL', 'SENT', 'Pedido Enviado', 'Tu pedido #10 ha sido enviado', '{"orderId": 10, "trackingNumber": "TRK001234569"}', '2024-01-25 09:00:00', '2024-01-25 10:00:00'),
(5, 'EMAIL', 'SENT', 'Pedido Entregado', 'Tu pedido #10 ha sido entregado', '{"orderId": 10}', '2024-01-26 11:30:00', '2024-01-26 12:00:00');

-- Seed user_notification_preferences table
INSERT INTO user_notification_preferences (user_id, email_enabled, sms_enabled, push_enabled, order_updates, promotions, security_alerts) VALUES
(1, true, false, false, true, true, true),
(2, true, true, false, true, true, true),
(3, true, false, true, true, false, true),
(4, true, true, true, true, true, true),
(5, true, false, false, true, true, false),
(6, false, false, false, false, false, false),
(7, true, true, false, true, true, true),
(8, true, false, true, true, false, true),
(9, false, true, false, false, false, true),
(10, true, true, true, true, true, true);

-- Seed report_refresh_log table
INSERT INTO report_refresh_log (refresh_type, started_at, completed_at, status) VALUES
('DAILY_SALES', '2024-01-15 00:05:00', '2024-01-15 00:05:30', 'COMPLETED'),
('PRODUCT_SALES', '2024-01-15 00:06:00', '2024-01-15 00:06:45', 'COMPLETED'),
('CUSTOMER_ANALYTICS', '2024-01-15 00:07:00', '2024-01-15 00:07:20', 'COMPLETED'),
('DAILY_SALES', '2024-01-16 00:05:00', '2024-01-16 00:05:25', 'COMPLETED'),
('PRODUCT_SALES', '2024-01-16 00:06:00', '2024-01-16 00:06:40', 'COMPLETED'),
('CUSTOMER_ANALYTICS', '2024-01-16 00:07:00', '2024-01-16 00:07:15', 'COMPLETED'),
('DAILY_SALES', '2024-01-17 00:05:00', '2024-01-17 00:05:35', 'COMPLETED'),
('PRODUCT_SALES', '2024-01-17 00:06:00', '2024-01-17 00:06:50', 'COMPLETED'),
('CUSTOMER_ANALYTICS', '2024-01-17 00:07:00', '2024-01-17 00:07:25', 'COMPLETED'),
('DAILY_SALES', '2024-01-18 00:05:00', '2024-01-18 00:05:30', 'COMPLETED'),
('PRODUCT_SALES', '2024-01-18 00:06:00', '2024-01-18 00:06:45', 'COMPLETED'),
('CUSTOMER_ANALYTICS', '2024-01-18 00:07:00', '2024-01-18 00:07:20', 'COMPLETED'),
('DAILY_SALES', '2024-01-19 00:05:00', '2024-01-19 00:05:25', 'COMPLETED'),
('PRODUCT_SALES', '2024-01-19 00:06:00', '2024-01-19 00:06:40', 'COMPLETED'),
('CUSTOMER_ANALYTICS', '2024-01-19 00:07:00', '2024-01-19 00:07:15', 'COMPLETED'),
('DAILY_SALES', '2024-01-20 00:05:00', '2024-01-20 00:05:30', 'COMPLETED'),
('PRODUCT_SALES', '2024-01-20 00:06:00', '2024-01-20 00:06:45', 'COMPLETED'),
('CUSTOMER_ANALYTICS', '2024-01-20 00:07:00', '2024-01-20 00:07:20', 'COMPLETED'),
('DAILY_SALES', '2024-01-21 00:05:00', '2024-01-21 00:05:25', 'COMPLETED'),
('PRODUCT_SALES', '2024-01-21 00:06:00', '2024-01-21 00:06:40', 'COMPLETED'),
('CUSTOMER_ANALYTICS', '2024-01-21 00:07:00', '2024-01-21 00:07:15', 'COMPLETED'),
('DAILY_SALES', '2024-01-22 00:05:00', '2024-01-22 00:05:30', 'COMPLETED'),
('PRODUCT_SALES', '2024-01-22 00:06:00', '2024-01-22 00:06:45', 'COMPLETED'),
('CUSTOMER_ANALYTICS', '2024-01-22 00:07:00', '2024-01-22 00:07:20', 'COMPLETED'),
('DAILY_SALES', '2024-01-23 00:05:00', '2024-01-23 00:05:25', 'COMPLETED'),
('PRODUCT_SALES', '2024-01-23 00:06:00', '2024-01-23 00:06:40', 'COMPLETED'),
('CUSTOMER_ANALYTICS', '2024-01-23 00:07:00', '2024-01-23 00:07:15', 'COMPLETED'),
('DAILY_SALES', '2024-01-24 00:05:00', '2024-01-24 00:05:30', 'COMPLETED'),
('PRODUCT_SALES', '2024-01-24 00:06:00', '2024-01-24 00:06:45', 'COMPLETED'),
('CUSTOMER_ANALYTICS', '2024-01-24 00:07:00', '2024-01-24 00:07:20', 'COMPLETED');

-- Refresh materialized views to include seed data
SELECT refresh_report_views(); 