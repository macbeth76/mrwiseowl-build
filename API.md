# Mr Wise Owl - API Documentation

## Base URL
- **Local**: `http://localhost:8080`
- **Production**: `https://[your-railway-url].railway.app`

## Authentication
Currently public (JWT auth coming in Phase 2)

---

## Health Check

### GET /api/health
Check if the API is running

**Response**:
```json
{
  "status": "UP",
  "service": "Mr Wise Owl API",
  "timestamp": "2024-11-29T17:30:00",
  "message": "🦉 Your wise guide to every adventure"
}
```

---

## Towns API

### GET /api/towns/search
Search for towns by name

**Parameters**:
- `q` (optional): Search query

**Example**:
```bash
curl "http://localhost:8080/api/towns/search?q=Asheville"
```

**Response**:
```json
[
  {
    "id": 1,
    "name": "Asheville",
    "country": "USA",
    "state": "North Carolina",
    "latitude": 35.5951,
    "longitude": -82.5515,
    "population": 94589
  }
]
```

### GET /api/towns/{id}
Get town by ID

**Example**:
```bash
curl "http://localhost:8080/api/towns/1"
```

### GET /api/towns/nearby
Find towns near a location

**Parameters**:
- `lat`: Latitude
- `lng`: Longitude
- `radius`: Radius in kilometers (default: 50)

**Example**:
```bash
curl "http://localhost:8080/api/towns/nearby?lat=35.5&lng=-82.5&radius=100"
```

---

## POIs (Points of Interest) API

### GET /api/pois
Get POIs by town and optional filters

**Parameters**:
- `townId` (required): Town ID
- `category` (optional): Single category filter
- `categories` (optional): Multiple categories (comma-separated)

**Categories**:
- `hotel`
- `activity_outdoor`
- `activity_indoor`
- `campground_rv`
- `campground_tent`
- `historic`
- `brewery`
- `restaurant`
- `bar`

**Examples**:
```bash
# All POIs in a town
curl "http://localhost:8080/api/pois?townId=1"

# Hotels only
curl "http://localhost:8080/api/pois?townId=1&category=hotel"

# Multiple categories
curl "http://localhost:8080/api/pois?townId=1&categories=hotel,brewery,restaurant"
```

**Response**:
```json
[
  {
    "id": 1,
    "townId": 1,
    "name": "Biltmore Estate",
    "category": "historic",
    "latitude": 35.5407,
    "longitude": -82.5526,
    "address": "1 Lodge St, Asheville, NC 28803",
    "description": "America's largest home with beautiful gardens",
    "priceRange": 4,
    "rating": 4.7,
    "amenities": ["parking", "tours", "restaurant"],
    "dietaryOptions": ["gluten_free", "vegetarian"],
    "tags": ["romantic", "anniversary_special"]
  }
]
```

### GET /api/pois/{id}
Get POI by ID

**Example**:
```bash
curl "http://localhost:8080/api/pois/1"
```

### GET /api/pois/nearby
Find POIs near a location

**Parameters**:
- `lat`: Latitude
- `lng`: Longitude
- `radius`: Radius in kilometers (default: 5)
- `category` (optional): Filter by category

**Example**:
```bash
# All POIs within 5km
curl "http://localhost:8080/api/pois/nearby?lat=35.5951&lng=-82.5515&radius=5"

# Only breweries within 10km
curl "http://localhost:8080/api/pois/nearby?lat=35.5951&lng=-82.5515&radius=10&category=brewery"
```

### GET /api/pois/dietary
Find POIs with specific dietary options

**Parameters**:
- `option`: Dietary option to filter by

**Dietary Options**:
- `gluten_free`
- `vegan`
- `vegetarian`
- `dairy_free`
- `nut_free`
- `shellfish_free`
- `halal`
- `kosher`

**Example**:
```bash
curl "http://localhost:8080/api/pois/dietary?option=gluten_free"
```

---

## Data Models

### Town
```json
{
  "id": 1,
  "name": "Asheville",
  "country": "USA",
  "state": "North Carolina",
  "latitude": 35.5951,
  "longitude": -82.5515,
  "population": 94589
}
```

### POI
```json
{
  "id": 1,
  "townId": 1,
  "name": "Wicked Weed Brewing",
  "category": "brewery",
  "latitude": 35.5951,
  "longitude": -82.5515,
  "address": "91 Biltmore Ave, Asheville, NC 28801",
  "phone": "(828) 575-9599",
  "website": "https://wickedweedbrewing.com",
  "description": "Popular craft brewery in downtown",
  "priceRange": 2,
  "rating": 4.5,
  "photos": ["url1", "url2"],
  "amenities": ["outdoor_seating", "live_music", "wifi"],
  "hours": {
    "monday": "11:00-22:00",
    "tuesday": "11:00-22:00"
  },
  "cuisineType": "American",
  "dietaryOptions": ["gluten_free", "vegan"],
  "tags": ["dog_friendly", "live_music"]
}
```

---

## Error Responses

### 404 Not Found
```json
{
  "timestamp": "2024-11-29T17:30:00",
  "status": 404,
  "error": "Not Found",
  "message": "Town not found with id: 999",
  "path": "/api/towns/999"
}
```

### 400 Bad Request
```json
{
  "timestamp": "2024-11-29T17:30:00",
  "status": 400,
  "error": "Bad Request",
  "message": "Required parameter 'townId' is missing",
  "path": "/api/pois"
}
```

---

## Rate Limiting
Currently no rate limiting (free tier)

---

## Coming Soon

### AI Chat API
```
POST /api/chat
Body: { message, conversationId, townId }
```

### User Authentication
```
POST /api/auth/login
POST /api/auth/register
```

### Family Profiles
```
GET /api/family/{groupId}/members
POST /api/family/{groupId}/members
```

---

## Testing

### Test with curl
```bash
# Health check
curl http://localhost:8080/api/health

# Search towns
curl "http://localhost:8080/api/towns/search?q=Asheville"

# Get POIs
curl "http://localhost:8080/api/pois?townId=1&category=brewery"
```

### Test with httpie
```bash
http GET localhost:8080/api/health
http GET "localhost:8080/api/towns/search?q=Asheville"
http GET "localhost:8080/api/pois?townId=1"
```

---

**Built with ❤️ by Mr Wise Owl** 🦉
