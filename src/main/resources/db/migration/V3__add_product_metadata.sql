-- Add product metadata
ALTER TABLE products
    ADD COLUMN barcode VARCHAR(50) UNIQUE,
    ADD COLUMN image_url VARCHAR(255),
    ADD COLUMN status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE' CHECK (status IN ('ACTIVE', 'INACTIVE', 'DISCONTINUED')),
    ADD COLUMN created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    ADD COLUMN updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    ADD COLUMN low_stock_threshold INTEGER NOT NULL DEFAULT 10,
    ADD COLUMN is_featured BOOLEAN NOT NULL DEFAULT FALSE;

-- Create trigger for products table
CREATE TRIGGER update_products_updated_at
    BEFORE UPDATE ON products
    FOR EACH ROW
    EXECUTE FUNCTION update_updated_at_column();

-- Add indexes for product status and search
CREATE INDEX idx_products_status ON products(status);
CREATE INDEX idx_products_barcode ON products(barcode);
CREATE INDEX idx_products_featured ON products(is_featured);
CREATE INDEX idx_products_stock ON products(stock) WHERE stock <= low_stock_threshold;

-- Add full text search capability
ALTER TABLE products
    ADD COLUMN search_vector tsvector
    GENERATED ALWAYS AS (
        setweight(to_tsvector('spanish', coalesce(name, '')), 'A') ||
        setweight(to_tsvector('spanish', coalesce(description, '')), 'B')
    ) STORED;

CREATE INDEX idx_products_search ON products USING GIN (search_vector); 