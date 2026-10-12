-- V11: Create family profiles and preferences tables

-- Family groups table (couples or families)
CREATE TABLE family_groups (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    type VARCHAR(20) NOT NULL CHECK (type IN ('couple', 'family')),
    primary_user_id BIGINT REFERENCES users(id) ON DELETE CASCADE,
    description TEXT,
    created_at TIMESTAMP DEFAULT NOW(),
    updated_at TIMESTAMP DEFAULT NOW()
);

-- Family members table (adults and children)
CREATE TABLE family_members (
    id BIGSERIAL PRIMARY KEY,
    family_group_id BIGINT REFERENCES family_groups(id) ON DELETE CASCADE,
    user_id BIGINT REFERENCES users(id),
    name VARCHAR(255) NOT NULL,
    relationship VARCHAR(50) NOT NULL,
    age_range VARCHAR(20),
    date_of_birth DATE,
    avatar_url VARCHAR(500),
    gender VARCHAR(20),
    created_at TIMESTAMP DEFAULT NOW()
);

-- Member preferences table
CREATE TABLE member_preferences (
    id BIGSERIAL PRIMARY KEY,
    family_member_id BIGINT REFERENCES family_members(id) ON DELETE CASCADE,
    category VARCHAR(50) NOT NULL,
    preference_key VARCHAR(100) NOT NULL,
    preference_value VARCHAR(255),
    importance VARCHAR(20) DEFAULT 'medium',
    notes TEXT,
    created_at TIMESTAMP DEFAULT NOW()
);

-- Indexes for performance
CREATE INDEX idx_family_groups_primary_user ON family_groups(primary_user_id);
CREATE INDEX idx_family_groups_type ON family_groups(type);
CREATE INDEX idx_family_members_group ON family_members(family_group_id);
CREATE INDEX idx_family_members_user ON family_members(user_id);
CREATE INDEX idx_member_preferences_member ON member_preferences(family_member_id);
CREATE INDEX idx_member_preferences_category ON member_preferences(category, preference_key);

-- Add trigger for updated_at
CREATE OR REPLACE FUNCTION update_family_groups_updated_at()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = NOW();
    RETURN NEW;
END;
$$ language 'plpgsql';

CREATE TRIGGER trigger_family_groups_updated_at
    BEFORE UPDATE ON family_groups
    FOR EACH ROW
    EXECUTE FUNCTION update_family_groups_updated_at();
