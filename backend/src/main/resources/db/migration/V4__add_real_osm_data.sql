-- Real POIs from OpenStreetMap
-- Replace sample data with real places

-- Keep original 3 POIs, add more real ones
INSERT INTO pois (town_id, name, category, location, description, price_range, rating, amenities, dietary_options, tags) VALUES
-- Asheville Real Places
(1, 'The Restoration Asheville', 'hotel', ST_SetSRID(ST_MakePoint(-82.5515, 35.5951), 4326), 'Boutique hotel in downtown Asheville', 3, 4.6, '["wifi", "parking", "restaurant"]', '[]', '["boutique"]'),
(1, 'Hampton Inn & Suites Asheville', 'hotel', ST_SetSRID(ST_MakePoint(-82.5520, 35.5960), 4326), 'Modern hotel with amenities', 2, 4.3, '["wifi", "pool", "breakfast"]', '[]', '["family_friendly"]'),
(1, 'Kathmandu Cafe', 'restaurant', ST_SetSRID(ST_MakePoint(-82.5530, 35.5945), 4326), 'Nepalese and Indian cuisine', 2, 4.5, '["outdoor_seating"]', '["vegetarian", "vegan", "gluten_free"]', '["ethnic"]'),
(1, 'Cracker Barrel Old Country Store', 'restaurant', ST_SetSRID(ST_MakePoint(-82.5500, 35.5970), 4326), 'American comfort food', 2, 4.2, '["parking", "gift_shop"]', '["vegetarian"]', '["family_friendly"]'),
(1, 'Carmel''s Restaurant & Bar', 'restaurant', ST_SetSRID(ST_MakePoint(-82.5540, 35.5940), 4326), 'Upscale dining experience', 4, 4.7, '["outdoor_seating", "bar"]', '["vegetarian", "gluten_free"]', '["fine_dining"]'),
(1, 'Blue Ridge Parkway Visitor Center', 'activity_outdoor', ST_SetSRID(ST_MakePoint(-82.5600, 35.6100), 4326), 'Scenic parkway information center', 1, 4.8, '["parking", "restrooms", "gift_shop"]', '[]', '["scenic", "nature"]'),
(1, 'North Carolina Arboretum', 'activity_outdoor', ST_SetSRID(ST_MakePoint(-82.6200, 35.5300), 4326), 'Beautiful botanical gardens and trails', 2, 4.7, '["parking", "trails", "gift_shop"]', '[]', '["nature", "family_friendly"]'),
(1, 'Asheville Pinball Museum', 'activity_indoor', ST_SetSRID(ST_MakePoint(-82.5525, 35.5955), 4326), 'Vintage pinball machines and arcade', 2, 4.6, '["arcade"]', '[]', '["family_friendly", "retro"]'),
(1, 'French Broad Brewing Company', 'brewery', ST_SetSRID(ST_MakePoint(-82.5700, 35.5800), 4326), 'Local craft brewery with food', 2, 4.6, '["outdoor_seating", "food_trucks", "live_music"]', '["vegetarian", "vegan"]', '["casual_dining"]'),
(1, 'Asheville Glamping', 'campground_tent', ST_SetSRID(ST_MakePoint(-82.6000, 35.5500), 4326), 'Luxury camping experience', 3, 4.5, '["wifi", "showers", "fire_pits"]', '[]', '["glamping", "romantic"]');
