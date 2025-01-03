-- Create necessary schemas
CREATE SCHEMA IF NOT EXISTS core;
CREATE SCHEMA IF NOT EXISTS audit;

-- Enable required PostgreSQL extensions
CREATE EXTENSION IF NOT EXISTS "pgcrypto";
CREATE EXTENSION IF NOT EXISTS "pg_trgm";
CREATE EXTENSION IF NOT EXISTS "btree_gin";
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";
CREATE EXTENSION IF NOT EXISTS "hstore";

-- Create functions for timestamp management
CREATE OR REPLACE FUNCTION core.update_updated_at()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- Set up proper permissions
GRANT USAGE ON SCHEMA public TO intellisiem;
GRANT USAGE ON SCHEMA core TO intellisiem;
GRANT USAGE ON SCHEMA audit TO intellisiem;

-- Grant schema privileges
GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA public TO intellisiem;
GRANT ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA public TO intellisiem;
GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA core TO intellisiem;
GRANT ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA core TO intellisiem;
GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA audit TO intellisiem;
GRANT ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA audit TO intellisiem;

-- Set default privileges
ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT ALL ON TABLES TO intellisiem;
ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT ALL ON SEQUENCES TO intellisiem;
ALTER DEFAULT PRIVILEGES IN SCHEMA core GRANT ALL ON TABLES TO intellisiem;
ALTER DEFAULT PRIVILEGES IN SCHEMA core GRANT ALL ON SEQUENCES TO intellisiem;
ALTER DEFAULT PRIVILEGES IN SCHEMA audit GRANT ALL ON TABLES TO intellisiem;
ALTER DEFAULT PRIVILEGES IN SCHEMA audit GRANT ALL ON SEQUENCES TO intellisiem;