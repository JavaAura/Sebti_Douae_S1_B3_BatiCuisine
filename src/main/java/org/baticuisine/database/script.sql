
CREATE TYPE STATUS AS ENUM ('INPROGRESS', 'COMPLETED', 'CANCELLED');

-- Create Client table
CREATE TABLE client (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    address VARCHAR(255),
    phone_number VARCHAR(50),
    is_professional BOOLEAN
);

-- Create Quote table
CREATE TABLE quote (
    id SERIAL PRIMARY KEY,
    estimated_amount DOUBLE PRECISION NOT NULL,
    issue_date DATE NOT NULL,
    validity_date DATE NOT NULL,
    is_accepted BOOLEAN
);

-- Create Project table
CREATE TABLE project (
    id SERIAL PRIMARY KEY,
    project_name VARCHAR(255) NOT NULL,
    profit_margin DOUBLE PRECISION,
    total_cost DOUBLE PRECISION,
    status STATUS NOT NULL,
    quote_id INTEGER,
    client_id INTEGER,
    FOREIGN KEY (quote_id) REFERENCES quote(id) ON DELETE CASCADE,
    FOREIGN KEY (client_id) REFERENCES client(id) ON DELETE CASCADE
);

-- Create Component base table (abstract table)
CREATE TABLE component (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    unit_cost DOUBLE PRECISION,
    quantity DOUBLE PRECISION,
    component_type VARCHAR(50),
    tax_rate DOUBLE PRECISION,
    project_id INTEGER,
    FOREIGN KEY (project_id) REFERENCES project(id) ON DELETE CASCADE);

-- Create Material table (inherits from Component)
CREATE TABLE material (

    transport_cost DOUBLE PRECISION,
    quality_coefficient DOUBLE PRECISION
) INHERITS (component);  -- Inherit from Component

-- Create Labor table (inherits from Component)
CREATE TABLE labor (

    hourly_rate DOUBLE PRECISION,
    work_hours DOUBLE PRECISION,
    worker_productivity DOUBLE PRECISION
) INHERITS (component);  -- Inherit from Component

