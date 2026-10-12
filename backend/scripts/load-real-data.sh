#!/bin/bash

echo "🦉 Loading real POI data from OpenStreetMap..."

# Database connection
DB_HOST="db.clamldjsgwixexashtmm.supabase.co"
DB_NAME="postgres"
DB_USER="postgres"
DB_PASS="4JYz_!2hQsCi$uS"

# Function to query Overpass API
query_osm() {
    local query="$1"
    curl -s -X POST "http://overpass-api.de/api/interpreter" -d "data=$query"
}

# Function to insert POI
insert_poi() {
    local town_id=$1
    local name=$2
    local category=$3
    local lat=$4
    local lon=$5
    local description=$6
    
    PGPASSWORD=$DB_PASS psql -h $DB_HOST -U $DB_USER -d $DB_NAME -c "
        INSERT INTO pois (town_id, name, category, location, description, price_range, rating, amenities, dietary_options, tags)
        VALUES (
            $town_id,
            '$name',
            '$category',
            ST_SetSRID(ST_MakePoint($lon, $lat), 4326),
            '$description',
            2,
            4.5,
            '[]'::jsonb,
            '[]'::jsonb,
            '[]'::jsonb
        )
        ON CONFLICT DO NOTHING;
    " 2>&1 | grep -v "INSERT"
}

echo "📍 Loading hotels in Asheville..."
query='[out:json];node["tourism"="hotel"](around:10000,35.5951,-82.5515);out;'
result=$(query_osm "$query")
echo "$result" | jq -r '.elements[] | select(.tags.name != null) | "\(.tags.name)|\(.lat)|\(.lon)"' | head -10 | while IFS='|' read -r name lat lon; do
    insert_poi 1 "$name" "hotel" "$lat" "$lon" "Hotel in Asheville"
    echo "  ✓ $name"
done

echo "📍 Loading restaurants in Asheville..."
query='[out:json];node["amenity"="restaurant"](around:10000,35.5951,-82.5515);out;'
result=$(query_osm "$query")
echo "$result" | jq -r '.elements[] | select(.tags.name != null) | "\(.tags.name)|\(.lat)|\(.lon)"' | head -10 | while IFS='|' read -r name lat lon; do
    insert_poi 1 "$name" "restaurant" "$lat" "$lon" "Restaurant in Asheville"
    echo "  ✓ $name"
done

echo "📍 Loading attractions in Asheville..."
query='[out:json];node["tourism"="attraction"](around:10000,35.5951,-82.5515);out;'
result=$(query_osm "$query")
echo "$result" | jq -r '.elements[] | select(.tags.name != null) | "\(.tags.name)|\(.lat)|\(.lon)"' | head -10 | while IFS='|' read -r name lat lon; do
    insert_poi 1 "$name" "activity_outdoor" "$lat" "$lon" "Attraction in Asheville"
    echo "  ✓ $name"
done

echo ""
echo "✅ Real data loaded! Check your app to see the new POIs."
