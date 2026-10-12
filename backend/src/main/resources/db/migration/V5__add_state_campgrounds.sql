-- Real State Park Campgrounds in NC and TN

-- North Carolina State Parks near Asheville and Boone
INSERT INTO pois (town_id, name, category, location, address, description, price_range, rating, amenities, dietary_options, tags) VALUES
-- Near Asheville
(1, 'Lake James State Park', 'campground_rv', ST_SetSRID(ST_MakePoint(-81.9000, 35.7300), 4326), '6883 NC-126, Nebo, NC 28761', 'RV camping with full hookups, lake access, hiking trails', 2, 4.6, '["full_hookup", "water", "electric", "dump_station", "showers", "restrooms", "boat_launch", "hiking"]', '[]', '["state_park", "lake"]'),
(1, 'Mount Mitchell State Park', 'campground_tent', ST_SetSRID(ST_MakePoint(-82.2652, 35.7648), 4326), '2388 State Highway 128, Burnsville, NC 28714', 'Tent camping at highest peak east of Mississippi', 1, 4.7, '["restrooms", "hiking", "scenic_views"]', '[]', '["state_park", "mountain", "scenic"]'),
(1, 'South Mountains State Park', 'campground_rv', ST_SetSRID(ST_MakePoint(-81.6200, 35.6100), 4326), '3001 South Mountains Park Ave, Connelly Springs, NC 28612', 'RV and tent camping with waterfall trails', 2, 4.5, '["electric", "water", "restrooms", "showers", "hiking", "fishing"]', '[]', '["state_park", "waterfall"]'),

-- Near Boone
(4, 'Grandfather Mountain State Park', 'campground_tent', ST_SetSRID(ST_MakePoint(-81.8125, 36.1095), 4326), '2050 Blowing Rock Hwy, Linville, NC 28646', 'Backcountry camping near Mile High Swinging Bridge', 1, 4.8, '["hiking", "scenic_views", "restrooms"]', '[]', '["state_park", "mountain", "scenic"]'),
(4, 'Elk Knob State Park', 'campground_tent', ST_SetSRID(ST_MakePoint(-81.6500, 36.3500), 4326), '5564 Meat Camp Rd, Todd, NC 28684', 'Primitive camping with mountain views', 1, 4.4, '["hiking", "restrooms"]', '[]', '["state_park", "mountain"]'),
(4, 'New River State Park', 'campground_rv', ST_SetSRID(ST_MakePoint(-81.5000, 36.4500), 4326), '358 New River State Park Rd, Laurel Springs, NC 28644', 'RV camping along scenic New River', 2, 4.6, '["electric", "water", "restrooms", "showers", "canoe_access", "fishing"]', '[]', '["state_park", "river"]'),

-- Tennessee State Parks near Gatlinburg and Pigeon Forge
(2, 'Cades Cove Campground', 'campground_rv', ST_SetSRID(ST_MakePoint(-83.7800, 35.5950), 4326), 'Cades Cove Loop Rd, Townsend, TN 37882', 'Great Smoky Mountains NP campground with RV sites', 2, 4.7, '["electric", "water", "dump_station", "restrooms", "showers", "hiking", "wildlife"]', '[]', '["national_park", "wildlife"]'),
(2, 'Elkmont Campground', 'campground_tent', ST_SetSRID(ST_MakePoint(-83.5800, 35.6600), 4326), '434 Elkmont Rd, Gatlinburg, TN 37738', 'Tent camping in Great Smoky Mountains', 1, 4.6, '["restrooms", "hiking", "fishing", "river_access"]', '[]', '["national_park", "river"]'),
(2, 'Smokemont Campground', 'campground_rv', ST_SetSRID(ST_MakePoint(-83.3100, 35.5600), 4326), '1786 Newfound Gap Rd, Cherokee, NC 28719', 'RV camping with mountain stream access', 2, 4.5, '["electric", "water", "restrooms", "hiking", "fishing"]', '[]', '["national_park", "stream"]'),

(3, 'Greenbrier Campground', 'campground_tent', ST_SetSRID(ST_MakePoint(-83.4200, 35.7500), 4326), 'Greenbrier Rd, Gatlinburg, TN 37738', 'Primitive camping near Greenbrier entrance', 1, 4.4, '["restrooms", "hiking", "river_access"]', '[]', '["national_park"]'),
(3, 'Big Creek Campground', 'campground_tent', ST_SetSRID(ST_MakePoint(-83.1100, 35.7600), 4326), 'Big Creek Rd, Waterville, NC 28787', 'Remote tent camping with creek access', 1, 4.5, '["restrooms", "hiking", "fishing"]', '[]', '["national_park", "remote"]'),

-- Additional RV Parks
(1, 'Asheville Bear Creek RV Park', 'campground_rv', ST_SetSRID(ST_MakePoint(-82.6500, 35.5200), 4326), '81 S Bear Creek Rd, Asheville, NC 28806', 'Full service RV resort near Asheville', 3, 4.5, '["full_hookup", "wifi", "pool", "hot_tub", "laundry", "playground"]', '[]', '["resort", "family_friendly"]'),
(2, 'Twin Creek RV Resort', 'campground_rv', ST_SetSRID(ST_MakePoint(-83.5300, 35.7000), 4326), '1202 E Pkwy, Gatlinburg, TN 37738', 'Premium RV resort near downtown Gatlinburg', 3, 4.6, '["full_hookup", "wifi", "pool", "cable_tv", "laundry"]', '[]', '["resort", "luxury"]'),
(3, 'Riveredge RV Park', 'campground_rv', ST_SetSRID(ST_MakePoint(-83.5600, 35.7900), 4326), '4220 Huskey St, Pigeon Forge, TN 37863', 'Riverside RV camping near attractions', 2, 4.4, '["full_hookup", "wifi", "river_access", "laundry"]', '[]', '["river"]'),
(4, 'Boone KOA Holiday', 'campground_rv', ST_SetSRID(ST_MakePoint(-81.7000, 36.2300), 4326), '123 Harmony Mountain Ln, Boone, NC 28607', 'Full service KOA with mountain views', 3, 4.5, '["full_hookup", "wifi", "pool", "hot_tub", "playground", "dog_park"]', '[]', '["koa", "family_friendly"]');
