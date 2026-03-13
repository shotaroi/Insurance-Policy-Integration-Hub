-- Trigger to auto-update updated_at on policy changes
CREATE OR REPLACE FUNCTION update_updated_at_column()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ language 'plpgsql';

CREATE TRIGGER update_policy_updated_at
    BEFORE UPDATE ON policy
    FOR EACH ROW
    EXECUTE PROCEDURE update_updated_at_column();
