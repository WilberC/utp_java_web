-- Create materialized view for daily sales
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

-- Create materialized view for product sales
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

-- Create materialized view for customer analytics
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

-- Create function to refresh materialized views
CREATE OR REPLACE FUNCTION refresh_report_views()
RETURNS void AS $$
BEGIN
    REFRESH MATERIALIZED VIEW CONCURRENTLY mv_daily_sales;
    REFRESH MATERIALIZED VIEW CONCURRENTLY mv_product_sales;
    REFRESH MATERIALIZED VIEW CONCURRENTLY mv_customer_analytics;
END;
$$ LANGUAGE plpgsql;

-- Create indexes for materialized views
CREATE UNIQUE INDEX idx_mv_daily_sales_date ON mv_daily_sales(sale_date);
CREATE UNIQUE INDEX idx_mv_product_sales_product ON mv_product_sales(product_id);
CREATE UNIQUE INDEX idx_mv_customer_analytics_user ON mv_customer_analytics(user_id);

-- Create table for scheduled report refreshes
CREATE TABLE report_refresh_log (
    id BIGSERIAL PRIMARY KEY,
    refresh_type VARCHAR(50) NOT NULL,
    started_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    completed_at TIMESTAMP,
    status VARCHAR(20) NOT NULL DEFAULT 'RUNNING' CHECK (status IN ('RUNNING', 'COMPLETED', 'FAILED')),
    error_message TEXT
);

-- Create index for report refresh log
CREATE INDEX idx_report_refresh_log_status ON report_refresh_log(status);
CREATE INDEX idx_report_refresh_log_started_at ON report_refresh_log(started_at); 