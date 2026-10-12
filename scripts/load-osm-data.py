#!/usr/bin/env python3
"""
Load POI data from OpenStreetMap and generate SQL migration
Usage: python load-osm-data.py "Charlotte" 35.1 -80.9 35.3 -80.7
"""

import sys
import requests
import json
from datetime import datetime

def get_pois(category_query, bbox, category_name):
    """Query OpenStreetMap Overpass API"""
    overpass_url = "https://overpass-api.de/api/interpreter"
    
    query = f"""
    [out:json][timeout:25];
    (
      node{category_query}({bbox[0]},{bbox[1]},{bbox[2]},{bbox[3]});
      way{category_query}({bbox[0]},{bbox[1]},{bbox[2]},{bbox[3]});
    );
    out center;
    """
    
    print(f"Querying {category_name}...")
    response = requests.post(overpass_url, data=query)
    return response.json()

def generate_sql(town_name, town_data, pois_data, migration_number):
    """Generate SQL migration file"""
    
    sql = f"""-- Add {town_name} and POIs
-- Generated: {datetime.now().strftime('%Y-%m-%d %H:%M:%S')}

-- Add town
INSERT INTO towns (name, state, country, latitude, longitude, population)
VALUES ('{town_name}', '{town_data['state']}', 'USA', {town_data['lat']}, {town_data['lon']}, {town_data.get('population', 0)});

-- Get town ID for reference
-- town_id will be auto-generated

-- Add POIs
"""
    
    for category, pois in pois_data.items():
        sql += f"\n-- {category.upper()}\n"
        for poi in pois['elements'][:20]:  # Limit to 20 per category
            tags = poi.get('tags', {})
            name = tags.get('name', 'Unknown').replace("'", "''")
            
            # Get coordinates
            if 'lat' in poi:
                lat, lon = poi['lat'], poi['lon']
            elif 'center' in poi:
                lat, lon = poi['center']['lat'], poi['center']['lon']
            else:
                continue
            
            description = tags.get('description', '').replace("'", "''")
            
            sql += f"""INSERT INTO pois (name, category, latitude, longitude, town_id, description)
SELECT '{name}', '{category}', {lat}, {lon}, id, '{description}'
FROM towns WHERE name = '{town_name}' LIMIT 1;
"""
    
    # Write to file
    filename = f"../backend/src/main/resources/db/migration/V{migration_number}__add_{town_name.lower().replace(' ', '_')}_data.sql"
    with open(filename, 'w') as f:
        f.write(sql)
    
    print(f"\n✅ Generated: {filename}")
    return filename

def main():
    if len(sys.argv) < 6:
        print("Usage: python load-osm-data.py <town_name> <lat_min> <lon_min> <lat_max> <lon_max> [state] [migration_number]")
        print("Example: python load-osm-data.py Charlotte 35.1 -80.9 35.3 -80.7 'North Carolina' 8")
        sys.exit(1)
    
    town_name = sys.argv[1]
    bbox = (float(sys.argv[2]), float(sys.argv[3]), float(sys.argv[4]), float(sys.argv[5]))
    state = sys.argv[6] if len(sys.argv) > 6 else 'Unknown'
    migration_number = sys.argv[7] if len(sys.argv) > 7 else '8'
    
    # Town center
    town_data = {
        'state': state,
        'lat': (bbox[0] + bbox[2]) / 2,
        'lon': (bbox[1] + bbox[3]) / 2,
        'population': 0
    }
    
    # Categories to query
    categories = {
        'hotel': '["tourism"="hotel"]',
        'restaurant': '["amenity"="restaurant"]',
        'brewery': '["craft"="brewery"]',
        'campground_rv': '["tourism"="caravan_site"]',
        'campground_tent': '["tourism"="camp_site"]',
        'activity_outdoor': '["tourism"="attraction"]["outdoor"="yes"]',
        'activity_indoor': '["tourism"="museum"]',
        'historic': '["historic"]'
    }
    
    pois_data = {}
    for category, query in categories.items():
        try:
            data = get_pois(query, bbox, category)
            pois_data[category] = data
            print(f"  Found {len(data.get('elements', []))} {category}")
        except Exception as e:
            print(f"  Error querying {category}: {e}")
    
    # Generate SQL
    filename = generate_sql(town_name, town_data, pois_data, migration_number)
    
    print(f"\n📊 Summary:")
    total = sum(len(data.get('elements', [])) for data in pois_data.values())
    print(f"  Total POIs: {total}")
    print(f"  Migration: V{migration_number}")
    print(f"\n🚀 Next steps:")
    print(f"  1. Review: {filename}")
    print(f"  2. git add {filename}")
    print(f"  3. git commit -m 'feat: add {town_name} POIs'")
    print(f"  4. git push")

if __name__ == '__main__':
    main()
