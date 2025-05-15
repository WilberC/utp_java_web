#!/bin/bash
set -e

psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" --dbname "$POSTGRES_DB" <<-EOSQL
    -- Create the database if it doesn't exist
    SELECT 'CREATE DATABASE utp_web_dev'
    WHERE NOT EXISTS (SELECT FROM pg_database WHERE datname = 'utp_web_dev')\gexec

    -- Connect to the database
    \c utp_web_dev

    -- Create extensions if needed
    CREATE EXTENSION IF NOT EXISTS "uuid-ossp";
    CREATE EXTENSION IF NOT EXISTS "pgcrypto";

    -- Grant privileges
    GRANT ALL PRIVILEGES ON DATABASE utp_web_dev TO utp_user;
    GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA public TO utp_user;
    GRANT ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA public TO utp_user;
EOSQL 