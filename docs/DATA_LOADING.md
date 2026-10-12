# Loading Real Data into Mr Wise Owl

## Current Data
- **Towns**: 4 (Asheville, Gatlinburg, Pigeon Forge, Boone)
- **POIs**: 44+ locations
- **Categories**: 8 types

## Methods to Load More Data

### 1. OpenStreetMap Overpass API (Recommended)

**Query hotels in Asheville:**
```bash
curl -X POST "https://overpass-api.de/api/interpreter" \
  -d '[out:json];
  node["tourism"="hotel"](35.5,-82.6,35.7,-82.5);
  out body;'
```

**Query restaurants:**
```bash
curl -X POST "https://overpass-api.de/api/interpreter" \
  -d '[out:json];
  node["amenity"="restaurant"](35.5,-82.6,35.7,-82.5);
  out body;'
```

**Query breweries:**
```bash
curl -X POST "https://overpass-api.de/api/interpreter" \
  -d '[out:json];
  node["craft"="brewery"](35.5,-82.6,35.7,-82.5);
  out body;'
```

### 2. Overpass Turbo (Visual Tool)

1. Go to https://overpass-turbo.eu/
2. Enter query:
```
[out:json];
(
  node["tourism"="hotel"](35.5,-82.6,35.7,-82.5);
  node["amenity"="restaurant"](35.5,-82.6,35.7,-82.5);
  node["craft"="brewery"](35.5,-82.6,35.7,-82.5);
);
out body;
```
3. Click "Run"
4. Export as GeoJSON

### 3. Automated Script

Create a data loading script:

```bash
#!/bin/bash
# Load POIs for a town

TOWN_NAME="Asheville"
LAT_MIN=35.5
LAT_MAX=35.7
LON_MIN=-82.6
LON_MAX=-82.5

# Hotels
curl -X POST "https://overpass-api.de/api/interpreter" \
  -d "[out:json];node[\"tourism\"=\"hotel\"]($LAT_MIN,$LON_MIN,$LAT_MAX,$LON_MAX);out body;" \
  > hotels_${TOWN_NAME}.json

# Restaurants
curl -X POST "https://overpass-api.de/api/interpreter" \
  -d "[out:json];node[\"amenity\"=\"restaurant\"]($LAT_MIN,$LON_MIN,$LAT_MAX,$LON_MAX);out body;" \
  > restaurants_${TOWN_NAME}.json

# Breweries
curl -X POST "https://overpass-api.de/api/interpreter" \
  -d "[out:json];node[\"craft\"=\"brewery\"]($LAT_MIN,$LON_MIN,$LAT_MAX,$LON_MAX);out body;" \
  > breweries_${TOWN_NAME}.json

echo "Data downloaded for $TOWN_NAME"
```

### 4. Python Script (More Powerful)

```python
import requests
import json

def get_pois(category, bbox):
    """
    Get POIs from OpenStreetMap
    bbox: (lat_min, lon_min, lat_max, lon_max)
    """
    overpass_url = "https://overpass-api.de/api/interpreter"
    
    queries = {
        'hotel': 'node["tourism"="hotel"]',
        'restaurant': 'node["amenity"="restaurant"]',
        'brewery': 'node["craft"="brewery"]',
        'campground': 'node["tourism"="camp_site"]',
        'attraction': 'node["tourism"="attraction"]',
        'museum': 'node["tourism"="museum"]',
        'park': 'node["leisure"="park"]'
    }
    
    query = f"""
    [out:json];
    {queries[category]}({bbox[0]},{bbox[1]},{bbox[2]},{bbox[3]});
    out body;
    """
    
    response = requests.post(overpass_url, data=query)
    return response.json()

# Example usage
bbox = (35.5, -82.6, 35.7, -82.5)  # Asheville
hotels = get_pois('hotel', bbox)

# Convert to SQL
for element in hotels['elements']:
    name = element['tags'].get('name', 'Unknown')
    lat = element['lat']
    lon = element['lon']
    print(f"INSERT INTO pois (name, category, latitude, longitude, town_id) VALUES ('{name}', 'hotel', {lat}, {lon}, 1);")
```

### 5. Recreation.gov API (Campgrounds)

```bash
# Get campgrounds near coordinates
curl "https://ridb.recreation.gov/api/v1/facilities?latitude=35.5951&longitude=-82.5515&radius=50" \
  -H "apikey: YOUR_API_KEY"
```

### 6. Google Places API (Paid, but accurate)

```bash
# Nearby search
curl "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=35.5951,-82.5515&radius=5000&type=restaurant&key=YOUR_API_KEY"
```

## Recommended Workflow

### Step 1: Choose Towns
Add more towns to expand coverage:
- **NC**: Charlotte, Raleigh, Wilmington, Outer Banks
- **TN**: Nashville, Chattanooga, Knoxville
- **SC**: Charleston, Myrtle Beach
- **VA**: Virginia Beach, Shenandoah

### Step 2: Get Bounding Boxes
Use https://boundingbox.klokantech.com/ to get coordinates

### Step 3: Run Overpass Queries
```bash
# For each town
./scripts/load-osm-data.sh "Charlotte" 35.1 -80.9 35.3 -80.7
```

### Step 4: Convert to SQL Migration
Create new migration file:
```sql
-- V8__add_charlotte_data.sql
INSERT INTO towns (name, state, country, latitude, longitude, population)
VALUES ('Charlotte', 'North Carolina', 'USA', 35.2271, -80.8431, 874579);

INSERT INTO pois (name, category, latitude, longitude, town_id, description)
VALUES 
  ('Hilton Charlotte', 'hotel', 35.2271, -80.8431, 5, 'Downtown hotel'),
  ('Brewery 99', 'brewery', 35.2280, -80.8420, 5, 'Craft brewery');
```

### Step 5: Deploy
```bash
git add backend/src/main/resources/db/migration/V8__add_charlotte_data.sql
git commit -m "feat: add Charlotte POIs"
git push
```

## Quick Start Script

I can create a script that:
1. Takes a town name and coordinates
2. Queries OpenStreetMap for all categories
3. Generates SQL migration file
4. Commits and deploys

Would you like me to create this automated data loader?

## Data Quality Tips

1. **Verify Names**: OSM data can have inconsistent naming
2. **Check Coordinates**: Some POIs may be misplaced
3. **Add Descriptions**: Enhance with additional info
4. **Set Ratings**: Add placeholder ratings (3.5-4.5)
5. **Add Amenities**: Include parking, wifi, etc.

## Rate Limits

- **Overpass API**: ~10,000 queries/day (free)
- **Recreation.gov**: 1,000 requests/day (free)
- **Google Places**: $200 credit/month (paid)

## Next Steps

1. Choose 5-10 new towns
2. Run Overpass queries
3. Create migration V8
4. Deploy and test

Want me to create the automated loader script?
