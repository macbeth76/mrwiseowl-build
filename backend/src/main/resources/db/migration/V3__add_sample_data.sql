-- Add more towns
INSERT INTO towns (name, country, state, location, population) VALUES
('Gatlinburg', 'USA', 'Tennessee', ST_SetSRID(ST_MakePoint(-83.5102, 35.7143), 4326), 3944),
('Pigeon Forge', 'USA', 'Tennessee', ST_SetSRID(ST_MakePoint(-83.5544, 35.7884), 4326), 6347),
('Boone', 'USA', 'North Carolina', ST_SetSRID(ST_MakePoint(-81.6745, 36.2168), 4326), 19205);

-- Gatlinburg POIs
INSERT INTO pois (town_id, name, category, location, address, description, price_range, rating, amenities, dietary_options, tags) VALUES
(2, 'Gatlinburg SkyLift Park', 'activity_outdoor', ST_SetSRID(ST_MakePoint(-83.5102, 35.7143), 4326), '765 Parkway, Gatlinburg, TN 37738', 'Scenic chairlift with mountain views and SkyBridge', 3, 4.6, '["parking", "gift_shop", "restrooms"]', '[]', '["family_friendly", "scenic"]'),
(2, 'Ripley''s Aquarium', 'activity_indoor', ST_SetSRID(ST_MakePoint(-83.5185, 35.7109), 4326), '88 River Rd, Gatlinburg, TN 37738', 'World-class aquarium with underwater tunnel', 3, 4.7, '["parking", "wheelchair_accessible", "gift_shop"]', '[]', '["family_friendly", "educational"]'),
(2, 'Anakeesta', 'activity_outdoor', ST_SetSRID(ST_MakePoint(-83.5130, 35.7120), 4326), '576 Parkway, Gatlinburg, TN 37738', 'Mountain adventure park with ziplines and treetop walks', 4, 4.5, '["parking", "restaurant", "gift_shop"]', '["vegetarian", "gluten_free"]', '["adventure", "family_friendly"]'),
(2, 'Smoky Mountain Brewery', 'brewery', ST_SetSRID(ST_MakePoint(-83.5140, 35.7100), 4326), '1004 Parkway, Gatlinburg, TN 37738', 'Craft brewery with mountain views', 2, 4.3, '["outdoor_seating", "live_music"]', '["vegetarian", "gluten_free"]', '["casual_dining"]'),
(2, 'The Park Vista Hotel', 'hotel', ST_SetSRID(ST_MakePoint(-83.5095, 35.7150), 4326), '705 Cherokee Orchard Rd, Gatlinburg, TN 37738', 'Luxury hotel with mountain views', 4, 4.4, '["pool", "spa", "restaurant", "wifi"]', '[]', '["luxury", "romantic"]'),
(2, 'Gatlinburg RV Park', 'campground_rv', ST_SetSRID(ST_MakePoint(-83.5200, 35.7050), 4326), '4229 E Parkway, Gatlinburg, TN 37738', 'Full hookup RV sites near downtown', 2, 4.2, '["full_hookup", "wifi", "laundry"]', '[]', '[]'),
(2, 'Cades Cove Campground', 'campground_tent', ST_SetSRID(ST_MakePoint(-83.7800, 35.5950), 4326), 'Cades Cove Loop Rd, Townsend, TN 37882', 'Tent camping in Great Smoky Mountains', 1, 4.5, '["restrooms", "fire_pits"]', '[]', '["nature", "hiking"]'),
(2, 'Sugarlands Visitor Center', 'historic', ST_SetSRID(ST_MakePoint(-83.5560, 35.6800), 4326), '1420 Fighting Creek Gap Rd, Gatlinburg, TN 37738', 'National park visitor center and museum', 1, 4.6, '["parking", "restrooms", "gift_shop"]', '[]', '["educational", "family_friendly"]');

-- Pigeon Forge POIs
INSERT INTO pois (town_id, name, category, location, address, description, price_range, rating, amenities, dietary_options, tags) VALUES
(3, 'Dollywood', 'activity_outdoor', ST_SetSRID(ST_MakePoint(-83.5300, 35.7950), 4326), '2700 Dollywood Parks Blvd, Pigeon Forge, TN 37863', 'Famous theme park with rides and shows', 4, 4.7, '["parking", "restaurant", "wheelchair_accessible"]', '["vegetarian", "gluten_free", "vegan"]', '["family_friendly", "entertainment"]'),
(3, 'The Island', 'activity_outdoor', ST_SetSRID(ST_MakePoint(-83.5544, 35.7884), 4326), '131 The Island Dr, Pigeon Forge, TN 37863', 'Entertainment complex with Great Smoky Mountain Wheel', 2, 4.4, '["parking", "restaurant", "shopping"]', '["vegetarian", "gluten_free"]', '["family_friendly", "shopping"]'),
(3, 'Titanic Museum', 'historic', ST_SetSRID(ST_MakePoint(-83.5600, 35.7900), 4326), '2134 Parkway, Pigeon Forge, TN 37863', 'Interactive Titanic experience', 3, 4.5, '["parking", "gift_shop"]', '[]', '["educational", "family_friendly"]'),
(3, 'WonderWorks', 'activity_indoor', ST_SetSRID(ST_MakePoint(-83.5580, 35.7920), 4326), '100 Music Rd, Pigeon Forge, TN 37863', 'Upside-down attraction with interactive exhibits', 3, 4.3, '["parking", "arcade"]', '[]', '["family_friendly", "educational"]'),
(3, 'Margaritaville Island Hotel', 'hotel', ST_SetSRID(ST_MakePoint(-83.5550, 35.7890), 4326), '131 The Island Dr, Pigeon Forge, TN 37863', 'Tropical-themed resort hotel', 3, 4.5, '["pool", "restaurant", "spa", "wifi"]', '[]', '["family_friendly"]'),
(3, 'Pigeon Forge KOA', 'campground_rv', ST_SetSRID(ST_MakePoint(-83.5700, 35.8000), 4326), '3122 Wears Valley Rd, Sevierville, TN 37862', 'Full service RV resort', 3, 4.4, '["full_hookup", "pool", "wifi", "playground"]', '[]', '["family_friendly"]');

-- Boone POIs
INSERT INTO pois (town_id, name, category, location, address, description, price_range, rating, amenities, dietary_options, tags) VALUES
(4, 'Grandfather Mountain', 'activity_outdoor', ST_SetSRID(ST_MakePoint(-81.8125, 36.1095), 4326), '2050 Blowing Rock Hwy, Linville, NC 28646', 'Nature park with Mile High Swinging Bridge', 3, 4.8, '["parking", "hiking", "wildlife"]', '[]', '["scenic", "adventure"]'),
(4, 'Tweetsie Railroad', 'activity_outdoor', ST_SetSRID(ST_MakePoint(-81.7200, 36.1500), 4326), '300 Tweetsie Railroad Ln, Blowing Rock, NC 28605', 'Wild West theme park with steam train', 3, 4.5, '["parking", "restaurant"]', '["vegetarian"]', '["family_friendly", "historic"]'),
(4, 'Appalachian State University', 'historic', ST_SetSRID(ST_MakePoint(-81.6745, 36.2168), 4326), '287 Rivers St, Boone, NC 28608', 'Historic university campus', 1, 4.6, '["parking", "tours"]', '[]', '["educational"]'),
(4, 'Appalachian Mountain Brewery', 'brewery', ST_SetSRID(ST_MakePoint(-81.6750, 36.2170), 4326), '163 Boone Creek Dr, Boone, NC 28607', 'Local craft brewery with outdoor seating', 2, 4.6, '["outdoor_seating", "live_music", "food_trucks"]', '["vegetarian", "vegan", "gluten_free"]', '["casual_dining"]'),
(4, 'The Inn at Ragged Gardens', 'hotel', ST_SetSRID(ST_MakePoint(-81.6740, 36.2160), 4326), '203 Sunset Dr, Blowing Rock, NC 28605', 'Historic bed and breakfast', 3, 4.7, '["wifi", "breakfast", "garden"]', '[]', '["romantic", "historic"]'),
(4, 'Boone KOA', 'campground_rv', ST_SetSRID(ST_MakePoint(-81.7000, 36.2300), 4326), '123 Harmony Mountain Ln, Boone, NC 28607', 'Mountain RV camping', 2, 4.3, '["full_hookup", "wifi", "pool"]', '[]', '[]'),
(4, 'Julian Price Memorial Park', 'campground_tent', ST_SetSRID(ST_MakePoint(-81.7500, 36.1400), 4326), 'Blue Ridge Parkway MP 297, Blowing Rock, NC 28605', 'Tent camping on Blue Ridge Parkway', 1, 4.6, '["restrooms", "lake", "hiking"]', '[]', '["nature", "scenic"]');
