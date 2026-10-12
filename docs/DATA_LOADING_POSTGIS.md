# Loading Data with PostGIS

## Important: Schema Uses PostGIS Geography

The `towns` and `pois` tables use PostGIS `GEOGRAPHY(POINT, 4326)` type, NOT separate latitude/longitude columns.

## Correct Format

```sql
-- Towns
INSERT INTO towns (name, state, country, location, population)
VALUES ('Charlotte', 'North Carolina', 'USA', 
    ST_SetSRID(ST_MakePoint(-80.8431, 35.2271), 4326)::geography, 
    874579);

-- POIs
INSERT INTO pois (name, category, location, town_id, description, rating, price_range)
SELECT 'Hotel Name', 'hotel', 
    ST_SetSRID(ST_MakePoint(-80.8427, 35.2255), 4326)::geography, 
    id, 'Description', 4.5, 3
FROM towns WHERE name = 'Charlotte' LIMIT 1;
```

## Key Points

1. **Order**: `ST_MakePoint(LONGITUDE, LATITUDE)` - longitude first!
2. **SRID**: Always use 4326 (WGS84 standard GPS)
3. **Cast**: Must cast to `::geography` type
4. **No separate columns**: Don't use `latitude, longitude` columns

## Quick Reference

```sql
-- Template
ST_SetSRID(ST_MakePoint(lon, lat), 4326)::geography

-- Example coordinates
-- Charlotte: -80.8431, 35.2271
-- Format: ST_SetSRID(ST_MakePoint(-80.8431, 35.2271), 4326)::geography
```

## Using the Template

1. Copy `scripts/add-town-template.sql`
2. Replace placeholders:
   - TOWN_NAME
   - STATE  
   - LONGITUDE (negative for West)
   - LATITUDE
   - POPULATION
3. Save as `V8__add_townname.sql`
4. Test locally first!

## Testing Locally

```bash
cd backend
./gradlew bootRun
# Check logs for migration success
```

## Common Mistakes

❌ Wrong: `latitude, longitude` columns
❌ Wrong: `ST_MakePoint(lat, lon)` - wrong order!
❌ Wrong: Missing `::geography` cast
❌ Wrong: Wrong SRID (not 4326)

✅ Correct: `ST_SetSRID(ST_MakePoint(lon, lat), 4326)::geography`
