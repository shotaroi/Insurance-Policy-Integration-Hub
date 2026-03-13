-- Insurance Policy Integration Hub - Initial Schema
-- Policy table for storing insurance policy information

CREATE TABLE policy (
    id              BIGSERIAL PRIMARY KEY,
    policy_number   VARCHAR(50) NOT NULL UNIQUE,
    customer_id     VARCHAR(100) NOT NULL,
    policy_type     VARCHAR(20) NOT NULL,
    premium_amount  DECIMAL(19, 4) NOT NULL,
    start_date      DATE NOT NULL,
    status          VARCHAR(20) NOT NULL,
    created_at      TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at      TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Indexes for common query patterns
CREATE INDEX idx_policy_policy_number ON policy(policy_number);
CREATE INDEX idx_policy_customer_id ON policy(customer_id);
CREATE INDEX idx_policy_status ON policy(status);
CREATE INDEX idx_policy_created_at ON policy(created_at);

-- Constraint for valid policy types
ALTER TABLE policy ADD CONSTRAINT chk_policy_type
    CHECK (policy_type IN ('LIFE', 'HOME', 'CAR'));

-- Constraint for valid statuses
ALTER TABLE policy ADD CONSTRAINT chk_policy_status
    CHECK (status IN ('PENDING', 'ACTIVE', 'CANCELLED', 'EXPIRED'));
