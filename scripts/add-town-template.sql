-- Template for adding new towns with PostGIS format
-- Replace TOWN_NAME, STATE, LONGITUDE, LATITUDE, POPULATION

-- Add Town
INSERT INTO towns (name, state, country, location, population)
VALUES ('TOWN_NAME', 'STATE', 'USA', ST_SetSRID(ST_MakePoint(LONGITUDE, LATITUDE), 4326)::geography, POPULATION);

-- Add POIs (examples)
INSERT INTO pois (name, category, location, town_id, description, rating, price_range)
SELECT 'Hotel Name', 'hotel', ST_SetSRID(ST_MakePoint(LONGITUDE, LATITUDE), 4326)::geography, id, 'Description', 4.5, 3
FROM towns WHERE name = 'TOWN_NAME' LIMIT 1;

INSERT INTO pois (name, category, location, town_id, description, rating, price_range)
SELECT 'Restaurant Name', 'restaurant', ST_SetSRID(ST_MakePoint(LONGITUDE, LATITUDE), 4326)::geography, id, 'Description', 4.4, 3
FROM towns WHERE name = 'TOWN_NAME' LIMIT 1;

INSERT INTO pois (name, category, location, town_id, description, rating, price_range)
SELECT 'Brewery Name', 'brewery', ST_SetSRID(ST_MakePoint(LONGITUDE, LATITUDE), 4326)::geography, id, 'Description', 4.3, 2
FROM towns WHERE name = 'TOWN_NAME' LIMIT 1;

INSERT INTO pois (name, category, location, town_id, description, rating, price_range)
SELECT 'Activity Name', 'activity_outdoor', ST_SetSRID(ST_MakePoint(LONGITUDE, LATITUDE), 4326)::geography, id, 'Description', 4.6, 2
FROM towns WHERE name = 'TOWN_NAME' LIMIT 1;

INSERT INTO pois (name, category, location, town_id, description, rating, price_range)
SELECT 'Historic Site', 'historic', ST_SetSRID(ST_MakePoint(LONGITUDE, LATITUDE), 4326)::geography, id, 'Description', 4.5, 1
FROM towns WHERE name = 'TOWN_NAME' LIMIT 1;

-- NOTE: PostGIS uses (LONGITUDE, LATITUDE) order - NOT (lat, lon)!
-- SRID 4326 is WGS84 (standard GPS coordinates)
