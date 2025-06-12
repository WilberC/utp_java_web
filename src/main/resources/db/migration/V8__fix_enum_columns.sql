-- Fix enum columns to work with JPA @Enumerated(EnumType.STRING)
-- This migration converts PostgreSQL enum types to VARCHAR to avoid type comparison issues

-- First, remove default values that depend on enum types
ALTER TABLE users ALTER COLUMN role DROP DEFAULT;
ALTER TABLE orders ALTER COLUMN status DROP DEFAULT;
ALTER TABLE orders ALTER COLUMN payment_status DROP DEFAULT;

-- Drop materialized views that depend on columns we're about to alter
DROP MATERIALIZED VIEW IF EXISTS mv_daily_sales;
DROP MATERIALIZED VIEW IF EXISTS mv_product_sales;
DROP MATERIALIZED VIEW IF EXISTS mv_customer_analytics;

-- Convert user_role enum to VARCHAR
ALTER TABLE users 
    ALTER COLUMN role TYPE VARCHAR(20) USING role::text;

-- Convert order_status enum to VARCHAR  
ALTER TABLE orders 
    ALTER COLUMN status TYPE VARCHAR(20) USING status::text;

-- Convert payment_method enum to VARCHAR in orders table
ALTER TABLE orders 
    ALTER COLUMN payment_method TYPE VARCHAR(20) USING payment_method::text;

-- Convert payment_status enum to VARCHAR in orders table
ALTER TABLE orders 
    ALTER COLUMN payment_status TYPE VARCHAR(20) USING payment_status::text;

-- Convert payment_method enum to VARCHAR in payment_transactions table
ALTER TABLE payment_transactions 
    ALTER COLUMN payment_method TYPE VARCHAR(20) USING payment_method::text;

-- Convert payment_status enum to VARCHAR in payment_transactions table
ALTER TABLE payment_transactions 
    ALTER COLUMN status TYPE VARCHAR(20) USING status::text;

-- Drop the enum types with CASCADE to handle any remaining dependencies
DROP TYPE IF EXISTS user_role CASCADE;
DROP TYPE IF EXISTS order_status CASCADE;
DROP TYPE IF EXISTS payment_method CASCADE;
DROP TYPE IF EXISTS payment_status CASCADE;

-- Add check constraints to ensure valid values
ALTER TABLE users 
    ADD CONSTRAINT chk_users_role 
    CHECK (role IN ('ADMIN', 'CUSTOMER'));

ALTER TABLE orders 
    ADD CONSTRAINT chk_orders_status 
    CHECK (status IN ('PENDING', 'PROCESSING', 'SHIPPED', 'DELIVERED', 'CANCELLED'));

ALTER TABLE orders 
    ADD CONSTRAINT chk_orders_payment_method 
    CHECK (payment_method IN ('CREDIT_CARD', 'BANK_TRANSFER', 'CASH_ON_DELIVERY'));

ALTER TABLE orders 
    ADD CONSTRAINT chk_orders_payment_status 
    CHECK (payment_status IN ('PENDING', 'PROCESSING', 'COMPLETED', 'FAILED', 'REFUNDED'));

ALTER TABLE payment_transactions 
    ADD CONSTRAINT chk_payment_transactions_payment_method 
    CHECK (payment_method IN ('CREDIT_CARD', 'BANK_TRANSFER', 'CASH_ON_DELIVERY'));

ALTER TABLE payment_transactions 
    ADD CONSTRAINT chk_payment_transactions_status 
    CHECK (status IN ('PENDING', 'PROCESSING', 'COMPLETED', 'FAILED', 'REFUNDED'));

-- Recreate materialized views with the new VARCHAR columns
CREATE MATERIALIZED VIEW mv_daily_sales AS
SELECT
    DATE_TRUNC('day', o.created_at) as sale_date,
    COUNT(DISTINCT o.id) as total_orders,
    SUM(o.total_amount) as total_sales,
    AVG(o.total_amount) as average_order_value,
    COUNT(DISTINCT o.user_id) as unique_customers
FROM orders o
WHERE o.status != 'CANCELLED'
GROUP BY DATE_TRUNC('day', o.created_at)
WITH DATA;

CREATE MATERIALIZED VIEW mv_product_sales AS
SELECT
    p.id as product_id,
    p.name as product_name,
    c.name as category_name,
    COUNT(oi.id) as total_orders,
    SUM(oi.quantity) as total_quantity,
    SUM(oi.subtotal) as total_revenue,
    AVG(oi.quantity) as average_quantity_per_order
FROM products p
JOIN categories c ON p.category_id = c.id
JOIN order_items oi ON p.id = oi.product_id
JOIN orders o ON oi.order_id = o.id
WHERE o.status != 'CANCELLED'
GROUP BY p.id, p.name, c.name
WITH DATA;

CREATE MATERIALIZED VIEW mv_customer_analytics AS
SELECT
    u.id as user_id,
    u.email,
    COUNT(DISTINCT o.id) as total_orders,
    SUM(o.total_amount) as total_spent,
    AVG(o.total_amount) as average_order_value,
    MAX(o.created_at) as last_order_date,
    MIN(o.created_at) as first_order_date
FROM users u
LEFT JOIN orders o ON u.id = o.user_id
WHERE o.status != 'CANCELLED'
GROUP BY u.id, u.email
WITH DATA;

-- Recreate indexes for materialized views
CREATE UNIQUE INDEX idx_mv_daily_sales_date ON mv_daily_sales(sale_date);
CREATE UNIQUE INDEX idx_mv_product_sales_product ON mv_product_sales(product_id);
CREATE UNIQUE INDEX idx_mv_customer_analytics_user ON mv_customer_analytics(user_id); 