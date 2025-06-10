# Requisitos del Sistema - Mini Market Online

## Requisitos Funcionales

### Gestión de Usuarios
1. El sistema debe permitir el registro de nuevos usuarios (clientes) con sus datos personales (nombre, email, contraseña, dirección, teléfono).
2. El sistema debe permitir el inicio de sesión de usuarios registrados.
3. El sistema debe permitir a los usuarios modificar sus datos personales.
4. El sistema debe permitir a los usuarios cambiar su contraseña.
5. El sistema debe permitir a los administradores gestionar usuarios (crear, editar, desactivar).

### Gestión de Productos
6. El sistema debe permitir a los administradores crear nuevos productos con su información (nombre, descripción, precio, stock, categoría).
7. El sistema debe permitir a los administradores actualizar la información de los productos.
8. El sistema debe permitir a los administradores gestionar el inventario (actualizar stock).
9. El sistema debe permitir a los usuarios buscar productos por nombre, categoría o rango de precios.
10. El sistema debe mostrar el estado de disponibilidad de los productos (en stock, agotado, bajo stock).

### Gestión de Categorías
11. El sistema debe permitir a los administradores crear y gestionar categorías de productos.
12. El sistema debe permitir a los usuarios filtrar productos por categoría.
13. El sistema debe mostrar una navegación jerárquica de categorías.

### Gestión de Pedidos
14. El sistema debe permitir a los usuarios agregar productos a un carrito de compras.
15. El sistema debe permitir a los usuarios modificar la cantidad de productos en el carrito.
16. El sistema debe permitir a los usuarios realizar pedidos desde el carrito.
17. El sistema debe permitir a los usuarios ver el historial de sus pedidos.
18. El sistema debe permitir a los administradores gestionar el estado de los pedidos (pendiente, procesando, enviado, entregado, cancelado).
19. El sistema debe enviar notificaciones por email sobre el estado de los pedidos.
20. El sistema debe generar facturas en PDF para los pedidos completados.

### Gestión de Pagos
21. El sistema debe permitir múltiples métodos de pago (tarjeta de crédito, transferencia bancaria).
22. El sistema debe validar y procesar pagos de forma segura.
23. El sistema debe mantener un registro de las transacciones.

### Reportes y Estadísticas
24. El sistema debe generar reportes de ventas diarias, semanales y mensuales.
25. El sistema debe mostrar estadísticas de productos más vendidos.
26. El sistema debe generar alertas de stock bajo.

## Requisitos No Funcionales

### Seguridad
1. El sistema debe implementar autenticación JWT para todas las operaciones sensibles.
2. El sistema debe encriptar las contraseñas de usuarios usando BCrypt.
3. El sistema debe implementar HTTPS para todas las comunicaciones.
4. El sistema debe validar y sanitizar todas las entradas de usuario.

### Rendimiento
5. El sistema debe manejar al menos 100 usuarios concurrentes.
6. El sistema debe tener un tiempo de respuesta menor a 2 segundos para operaciones comunes.
7. El sistema debe implementar caché para productos y categorías frecuentemente accedidos.

### Usabilidad
8. El sistema debe ser responsive y funcionar en dispositivos móviles y de escritorio.
9. El sistema debe seguir las directrices de accesibilidad WCAG 2.1.
10. El sistema debe proporcionar mensajes de error claros y en español.

### Mantenibilidad
11. El sistema debe seguir los principios SOLID y patrones de diseño.
12. El sistema debe tener una cobertura de pruebas superior al 80%.
13. El sistema debe usar control de versiones (Git) y seguir convenciones de código.

### Escalabilidad
14. El sistema debe estar preparado para escalar horizontalmente.
15. El sistema debe usar una base de datos que soporte alta concurrencia.

## Migraciones Necesarias

Para soportar estos requisitos, se necesitarán las siguientes migraciones adicionales:

1. V2__add_user_status.sql
   - Agregar campo de estado para usuarios (activo/inactivo)
   - Agregar campo de última sesión
   - Agregar campo de fecha de registro

2. V3__add_product_metadata.sql
   - Agregar campo de código de barras
   - Agregar campo de imagen
   - Agregar campo de estado (activo/inactivo)
   - Agregar campo de fecha de creación/actualización

3. V4__add_order_metadata.sql
   - Agregar campo de método de pago
   - Agregar campo de estado de pago
   - Agregar campo de dirección de envío
   - Agregar campo de notas del pedido

4. V5__add_notifications.sql
   - Crear tabla de notificaciones
   - Crear tabla de plantillas de email
   - Agregar campos de preferencias de notificación

5. V6__add_reports.sql
   - Crear vistas materializadas para reportes
   - Agregar índices para optimización de consultas
   - Agregar campos de metadatos para análisis 