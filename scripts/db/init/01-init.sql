-- Web Editor Database Initialization Script
-- This script runs automatically when PostgreSQL container starts for the first time

-- Enable required extensions
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- Create schema if needed
-- CREATE SCHEMA IF NOT EXISTS webeditor;

-- Grant privileges
GRANT ALL PRIVILEGES ON DATABASE webeditor TO webeditor;

-- Tables will be created automatically by JPA (ddl-auto: update)
-- This is just for additional initialization if needed

-- Example: Create indexes for better performance
-- CREATE INDEX IF NOT EXISTS idx_users_username ON users(username);
-- CREATE INDEX IF NOT EXISTS idx_users_email ON users(email);
-- CREATE INDEX IF NOT EXISTS idx_contents_author_id ON contents(author_id);
-- CREATE INDEX IF NOT EXISTS idx_contents_status ON contents(status);
-- CREATE INDEX IF NOT EXISTS idx_contents_created_at ON contents(created_at);
