-- Create payment method enum
CREATE TYPE payment_method AS ENUM ('CREDIT_CARD', 'BANK_TRANSFER', 'CASH_ON_DELIVERY');
CREATE TYPE payment_status AS ENUM ('PENDING', 'PROCESSING', 'COMPLETED', 'FAILED', 'REFUNDED');

-- Add order metadata
ALTER TABLE orders
    ADD COLUMN payment_method payment_method NOT NULL,
    ADD COLUMN payment_status payment_status NOT NULL DEFAULT 'PENDING',
    ADD COLUMN shipping_address TEXT NOT NULL,
    ADD COLUMN billing_address TEXT NOT NULL,
    ADD COLUMN notes TEXT,
    ADD COLUMN created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    ADD COLUMN updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    ADD COLUMN estimated_delivery_date TIMESTAMP,
    ADD COLUMN actual_delivery_date TIMESTAMP;

-- Create trigger for orders table
CREATE TRIGGER update_orders_updated_at
    BEFORE UPDATE ON orders
    FOR EACH ROW
    EXECUTE FUNCTION update_updated_at_column();

-- Create payment transactions table
CREATE TABLE payment_transactions (
    id BIGSERIAL PRIMARY KEY,
    order_id BIGINT NOT NULL REFERENCES orders(id),
    amount DECIMAL(10,2) NOT NULL CHECK (amount > 0),
    currency VARCHAR(3) NOT NULL DEFAULT 'PEN',
    payment_method payment_method NOT NULL,
    status payment_status NOT NULL,
    transaction_id VARCHAR(100),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    error_message TEXT
);

-- Create trigger for payment_transactions table
CREATE TRIGGER update_payment_transactions_updated_at
    BEFORE UPDATE ON payment_transactions
    FOR EACH ROW
    EXECUTE FUNCTION update_updated_at_column();

-- Add indexes
CREATE INDEX idx_orders_payment_status ON orders(payment_status);
CREATE INDEX idx_orders_created_at ON orders(created_at);
CREATE INDEX idx_payment_transactions_order ON payment_transactions(order_id);
CREATE INDEX idx_payment_transactions_status ON payment_transactions(status); 