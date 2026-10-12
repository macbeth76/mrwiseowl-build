-- Enable PostGIS extension
CREATE EXTENSION IF NOT EXISTS postgis;

-- Towns table
CREATE TABLE towns (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    country VARCHAR(100),
    state VARCHAR(100),
    location GEOGRAPHY(POINT, 4326),
    population INTEGER,
    created_at TIMESTAMP DEFAULT NOW()
);

CREATE INDEX idx_towns_location ON towns USING GIST(location);
CREATE INDEX idx_towns_name ON towns(name);

-- POIs (Points of Interest)
CREATE TABLE pois (
    id BIGSERIAL PRIMARY KEY,
    town_id BIGINT REFERENCES towns(id),
    name VARCHAR(255) NOT NULL,
    category VARCHAR(50), -- hotel, activity_outdoor, activity_indoor, campground_rv, campground_tent, historic, brewery, restaurant, bar
    location GEOGRAPHY(POINT, 4326),
    address TEXT,
    phone VARCHAR(50),
    website VARCHAR(500),
    description TEXT,
    price_range INTEGER, -- 1-4 ($-$$$$)
    rating DECIMAL(2,1),
    photos JSONB,
    amenities JSONB,
    hours JSONB,
    cuisine_type VARCHAR(100),
    dietary_options JSONB, -- ["gluten_free", "vegan", "vegetarian", "dairy_free", "nut_free", "shellfish_free", "halal", "kosher"]
    tags JSONB, -- ["romantic", "couples_friendly", "date_night", "anniversary_special"]
    external_id VARCHAR(255),
    external_source VARCHAR(50),
    created_at TIMESTAMP DEFAULT NOW(),
    updated_at TIMESTAMP DEFAULT NOW()
);

CREATE INDEX idx_pois_location ON pois USING GIST(location);
CREATE INDEX idx_pois_category ON pois(category);
CREATE INDEX idx_pois_town ON pois(town_id);
CREATE INDEX idx_pois_tags ON pois USING GIN(tags);
CREATE INDEX idx_pois_dietary ON pois USING GIN(dietary_options);

-- Users
CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    email VARCHAR(255) UNIQUE NOT NULL,
    password_hash VARCHAR(255),
    name VARCHAR(255),
    created_at TIMESTAMP DEFAULT NOW()
);

-- Family groups
CREATE TABLE family_groups (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255),
    primary_user_id BIGINT REFERENCES users(id),
    created_at TIMESTAMP DEFAULT NOW()
);

-- Family members
CREATE TABLE family_members (
    id BIGSERIAL PRIMARY KEY,
    family_group_id BIGINT REFERENCES family_groups(id),
    user_id BIGINT REFERENCES users(id),
    name VARCHAR(255) NOT NULL,
    relationship VARCHAR(50), -- husband, wife, child, teen
    age_range VARCHAR(20), -- adult, teen, child_6_12, child_0_5
    preferences JSONB,
    created_at TIMESTAMP DEFAULT NOW()
);

CREATE INDEX idx_family_members_group ON family_members(family_group_id);

-- Saved locations
CREATE TABLE saved_locations (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT REFERENCES users(id),
    poi_id BIGINT REFERENCES pois(id),
    notes TEXT,
    created_at TIMESTAMP DEFAULT NOW(),
    UNIQUE(user_id, poi_id)
);

-- Transit stops
CREATE TABLE transit_stops (
    id BIGSERIAL PRIMARY KEY,
    town_id BIGINT REFERENCES towns(id),
    name VARCHAR(255) NOT NULL,
    type VARCHAR(50), -- subway, bus, train
    location GEOGRAPHY(POINT, 4326),
    routes JSONB,
    accessibility JSONB,
    external_id VARCHAR(255),
    created_at TIMESTAMP DEFAULT NOW()
);

CREATE INDEX idx_transit_stops_location ON transit_stops USING GIST(location);
CREATE INDEX idx_transit_stops_type ON transit_stops(type);

-- Events
CREATE TABLE events (
    id BIGSERIAL PRIMARY KEY,
    town_id BIGINT REFERENCES towns(id),
    name VARCHAR(255) NOT NULL,
    type VARCHAR(50), -- fireworks, festival, concert, holiday
    location GEOGRAPHY(POINT, 4326),
    venue_name VARCHAR(255),
    start_date TIMESTAMP,
    end_date TIMESTAMP,
    description TEXT,
    ticket_url VARCHAR(500),
    price_range INTEGER,
    photos JSONB,
    created_at TIMESTAMP DEFAULT NOW()
);

CREATE INDEX idx_events_dates ON events(start_date, end_date);
CREATE INDEX idx_events_town ON events(town_id);
CREATE INDEX idx_events_type ON events(type);

-- Trips
CREATE TABLE trips (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT REFERENCES users(id),
    family_group_id BIGINT REFERENCES family_groups(id),
    town_id BIGINT REFERENCES towns(id),
    start_date DATE,
    end_date DATE,
    home_location GEOGRAPHY(POINT, 4326),
    trip_purpose VARCHAR(50), -- anniversary, family, solo, business, romantic_getaway
    preferences JSONB,
    itinerary JSONB,
    created_at TIMESTAMP DEFAULT NOW()
);

-- AI Conversations
CREATE TABLE conversations (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id BIGINT REFERENCES users(id),
    town_id BIGINT REFERENCES towns(id),
    created_at TIMESTAMP DEFAULT NOW(),
    updated_at TIMESTAMP DEFAULT NOW()
);

CREATE TABLE messages (
    id BIGSERIAL PRIMARY KEY,
    conversation_id UUID REFERENCES conversations(id),
    role VARCHAR(20), -- 'user' or 'assistant'
    content TEXT,
    metadata JSONB,
    created_at TIMESTAMP DEFAULT NOW()
);
