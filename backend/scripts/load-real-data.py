#!/usr/bin/env python3
"""
Load real POI data from OpenStreetMap using Overpass API
"""
import os
import requests
import json
import psycopg2
from typing import List, Dict

# Database connection
DB_CONFIG = {
    'host': 'db.clamldjsgwixexashtmm.supabase.co',
    'port': 5432,
    'database': 'postgres',
    'user': 'postgres',
    'password': os.environ.get('SUPABASE_DB_PASSWORD', ''),
}

# Overpass API endpoint
OVERPASS_URL = "http://overpass-api.de/api/interpreter"

# Towns to load data for
TOWNS = [
    {'id': 1, 'name': 'Asheville', 'lat': 35.5951, 'lon': -82.5515, 'radius': 10000},
    {'id': 2, 'name': 'Gatlinburg', 'lat': 35.7143, 'lon': -83.5102, 'radius': 10000},
    {'id': 3, 'name': 'Pigeon Forge', 'lat': 35.7884, 'lon': -83.5544, 'radius': 10000},
    {'id': 4, 'name': 'Boone', 'lat': 36.2168, 'lon': -81.6745, 'radius': 10000},
]

# OSM queries for different categories
QUERIES = {
    'hotel': """
        [out:json];
        (
          node["tourism"="hotel"](around:{radius},{lat},{lon});
          way["tourism"="hotel"](around:{radius},{lat},{lon});
        );
        out center;
    """,
    'campground_rv': """
        [out:json];
        (
          node["tourism"="caravan_site"](around:{radius},{lat},{lon});
          way["tourism"="caravan_site"](around:{radius},{lat},{lon});
        );
        out center;
    """,
    'campground_tent': """
        [out:json];
        (
          node["tourism"="camp_site"](around:{radius},{lat},{lon});
          way["tourism"="camp_site"](around:{radius},{lat},{lon});
        );
        out center;
    """,
    'brewery': """
        [out:json];
        (
          node["craft"="brewery"](around:{radius},{lat},{lon});
          way["craft"="brewery"](around:{radius},{lat},{lon});
        );
        out center;
    """,
    'restaurant': """
        [out:json];
        (
          node["amenity"="restaurant"](around:{radius},{lat},{lon});
          way["amenity"="restaurant"](around:{radius},{lat},{lon});
        );
        out center;
    """,
    'historic': """
        [out:json];
        (
          node["historic"](around:{radius},{lat},{lon});
          way["historic"](around:{radius},{lat},{lon});
        );
        out center;
    """,
    'activity_outdoor': """
        [out:json];
        (
          node["tourism"="attraction"](around:{radius},{lat},{lon});
          way["tourism"="attraction"](around:{radius},{lat},{lon});
          node["leisure"="park"](around:{radius},{lat},{lon});
        );
        out center;
    """,
}

def query_overpass(query: str) -> List[Dict]:
    """Query Overpass API"""
    try:
        response = requests.post(OVERPASS_URL, data={'data': query}, timeout=60)
        response.raise_for_status()
        data = response.json()
        return data.get('elements', [])
    except Exception as e:
        print(f"Error querying Overpass: {e}")
        return []

def extract_poi_data(element: Dict, category: str, town_id: int) -> Dict:
    """Extract POI data from OSM element"""
    tags = element.get('tags', {})
    
    # Get coordinates
    if 'lat' in element and 'lon' in element:
        lat, lon = element['lat'], element['lon']
    elif 'center' in element:
        lat, lon = element['center']['lat'], element['center']['lon']
    else:
        return None
    
    # Extract name
    name = tags.get('name', tags.get('operator', f"Unnamed {category}"))
    
    # Extract other details
    description = tags.get('description', '')
    phone = tags.get('phone', '')
    website = tags.get('website', '')
    address = tags.get('addr:full', '')
    
    # Estimate rating (random for now, would need external API)
    rating = 4.0 + (hash(name) % 10) / 10.0
    
    # Estimate price range
    price_range = 2
    
    # Extract amenities
    amenities = []
    if tags.get('internet_access') == 'yes':
        amenities.append('wifi')
    if tags.get('wheelchair') == 'yes':
        amenities.append('wheelchair_accessible')
    if tags.get('outdoor_seating') == 'yes':
        amenities.append('outdoor_seating')
    
    return {
        'town_id': town_id,
        'name': name,
        'category': category,
        'latitude': lat,
        'longitude': lon,
        'address': address,
        'phone': phone,
        'website': website,
        'description': description,
        'price_range': price_range,
        'rating': rating,
        'amenities': amenities,
    }

def insert_poi(conn, poi: Dict):
    """Insert POI into database"""
    cursor = conn.cursor()
    
    query = """
        INSERT INTO pois (
            town_id, name, category, location, address, phone, website,
            description, price_range, rating, amenities, dietary_options, tags
        ) VALUES (
            %s, %s, %s, ST_SetSRID(ST_MakePoint(%s, %s), 4326),
            %s, %s, %s, %s, %s, %s, %s, %s, %s
        )
        ON CONFLICT DO NOTHING
    """
    
    cursor.execute(query, (
        poi['town_id'],
        poi['name'],
        poi['category'],
        poi['longitude'],
        poi['latitude'],
        poi['address'],
        poi['phone'],
        poi['website'],
        poi['description'],
        poi['price_range'],
        poi['rating'],
        json.dumps(poi['amenities']),
        json.dumps([]),
        json.dumps([])
    ))
    
    conn.commit()
    cursor.close()

def main():
    print("🦉 Loading real POI data from OpenStreetMap...")
    
    # Connect to database
    conn = psycopg2.connect(**DB_CONFIG)
    
    total_loaded = 0
    
    for town in TOWNS:
        print(f"\n📍 Loading data for {town['name']}...")
        
        for category, query_template in QUERIES.items():
            query = query_template.format(
                radius=town['radius'],
                lat=town['lat'],
                lon=town['lon']
            )
            
            print(f"  Fetching {category}...")
            elements = query_overpass(query)
            
            count = 0
            for element in elements[:10]:  # Limit to 10 per category per town
                poi = extract_poi_data(element, category, town['id'])
                if poi and poi['name'] != f"Unnamed {category}":
                    try:
                        insert_poi(conn, poi)
                        count += 1
                        total_loaded += 1
                    except Exception as e:
                        print(f"    Error inserting {poi['name']}: {e}")
            
            print(f"    Loaded {count} {category} POIs")
    
    conn.close()
    print(f"\n✅ Total POIs loaded: {total_loaded}")

if __name__ == '__main__':
    main()
