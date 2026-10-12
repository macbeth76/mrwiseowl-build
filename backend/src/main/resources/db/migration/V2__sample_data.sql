-- Insert sample town (Asheville, NC)
INSERT INTO towns (name, country, state, location, population) VALUES
('Asheville', 'USA', 'North Carolina', ST_SetSRID(ST_MakePoint(-82.5515, 35.5951), 4326)::geography, 94589);

-- Get the town ID for reference
DO $$
DECLARE
    asheville_id BIGINT;
BEGIN
    SELECT id INTO asheville_id FROM towns WHERE name = 'Asheville';
    
    -- Insert sample POIs
    INSERT INTO pois (town_id, name, category, location, address, description, price_range, rating, amenities, dietary_options, tags) VALUES
    (asheville_id, 'Biltmore Estate', 'historic', ST_SetSRID(ST_MakePoint(-82.5526, 35.5407), 4326)::geography, '1 Lodge St, Asheville, NC 28803', 'America''s largest home with beautiful gardens', 4, 4.7, '["parking", "tours", "restaurant"]'::jsonb, '["gluten_free", "vegetarian"]'::jsonb, '["romantic", "anniversary_special"]'::jsonb),
    
    (asheville_id, 'Wicked Weed Brewing', 'brewery', ST_SetSRID(ST_MakePoint(-82.5515, 35.5951), 4326)::geography, '91 Biltmore Ave, Asheville, NC 28801', 'Popular craft brewery in downtown', 2, 4.5, '["outdoor_seating", "live_music"]'::jsonb, '["gluten_free", "vegan"]'::jsonb, '[]'::jsonb),
    
    (asheville_id, 'Asheville KOA', 'campground_rv', ST_SetSRID(ST_MakePoint(-82.6015, 35.5351), 4326)::geography, '309 Wiggins Rd, Candler, NC 28715', 'Full hookup RV sites near downtown', 2, 4.3, '["full_hookup", "wifi", "pool", "hot_tub"]'::jsonb, '[]'::jsonb, '[]'::jsonb);
END $$;
