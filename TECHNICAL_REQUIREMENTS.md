# MyConcierge - Technical Requirements

## API Requirements

### External APIs

#### Map Provider
**Option 1: Mapbox**
- Mapbox GL JS for rendering
- Geocoding API for search
- Directions API for navigation
- Static Images API for thumbnails

**Option 2: Leaflet + OpenStreetMap**
- Leaflet.js for rendering
- Nominatim for geocoding
- OSRM for routing
- Free tier available

#### POI Data Sources
- **Google Places API**: Hotels, activities, reviews
- **OpenStreetMap Overpass API**: Historic sites, campgrounds
- **Recreation.gov API**: Campground availability
- **Yelp Fusion API**: Reviews and ratings

#### Transit Data Sources
- **Google Transit API (GTFS)**: Real-time transit data
- **OpenStreetMap**: Transit infrastructure
- **City-specific APIs**: MTA (NYC), WMATA (DC), MBTA (Boston), etc.
- **Transit.land**: Multi-city transit aggregator

#### AI Services
- **AWS Bedrock**: Claude 3 (Sonnet/Haiku) for chatbot
- **Alternative**: OpenAI GPT-4 Turbo or GPT-3.5
- **Fallback**: Llama 3 on AWS SageMaker

### Internal API Endpoints

#### AI Chat
```
POST /api/chat
Body: { message, conversationId, townId }
Response: { reply, actions: { showPOIs, updateFilters, centerMap } }
```

#### Town Search
```
GET /api/towns/search?q={query}
Response: [{ id, name, lat, lng, country, state }]
```

#### Get POIs by Town
```
GET /api/pois?townId={id}&layers={hotels,activity_outdoor,activity_indoor,campground_rv,campground_tent,historic}
Response: { hotels: [], activity_outdoor: [], activity_indoor: [], campground_rv: [], campground_tent: [], historic: [] }
```

#### Get Transit Stops
```
GET /api/transit/stops?townId={id}&types={subway,bus,train}&radius={miles}
Response: { subway: [], bus: [], train: [] }
```

#### Get Transit Routes
```
GET /api/transit/routes?townId={id}&stopId={id}
Response: [{ route_id, name, type, color, next_arrivals: [] }]
```

#### Get Transit Directions
```
POST /api/transit/directions
Body: { from: {lat, lng}, to: {lat, lng}, townId }
Response: { routes: [{ steps, duration, transfers, fare }] }
```

#### Search Cities by Criteria
```
POST /api/cities/search
Body: {
  homeLocation: {lat, lng, address},
  dates: {start, end},
  maxDistance: miles,
  requirements: {
    rvFullHookup: true,
    nearDowntown: true,
    interests: ["breweries", "historic"],
    events: ["fireworks"]
  }
}
Response: [{ 
  town, 
  matchScore, 
  distance, 
  driveTime,
  availableRVSites: count,
  eventsCount,
  breweriesCount,
  historicSitesCount
}]
```

#### Get Events by Date Range
```
GET /api/events?townId={id}&startDate={date}&endDate={date}&types={fireworks,festival}
Response: [{ id, name, type, date, location, description }]
```

#### Create Trip Plan
```
POST /api/trips
Body: {
  userId,
  townId,
  dates: {start, end},
  homeLocation,
  preferences,
  selectedPOIs: [poi_ids]
}
Response: { tripId, itinerary, estimatedCosts }
```

#### Family Profile Management
```
POST /api/family/create
Body: { userId, familyName }
Response: { familyGroupId }

POST /api/family/{groupId}/members
Body: { 
  name, 
  relationship, 
  ageRange, 
  userId (optional),
  preferences: {
    interests: [],
    dietary: [],
    activityLevel,
    mobility
  }
}
Response: { memberId }

GET /api/family/{groupId}/members
Response: [{ id, name, relationship, ageRange, preferences }]

POST /api/family/members/{memberId}/preferences
Body: { category, preferenceType, importance, notes }
Response: { preferenceId }

GET /api/family/{groupId}/trip-suggestions?townId={id}
Response: { 
  balancedItinerary: [],
  perMemberActivities: {},
  compromiseSuggestions: []
}
```

#### Get POI Details
```
GET /api/pois/{id}
Response: { id, name, category, lat, lng, details, photos, reviews }
```

#### Filter POIs
```
POST /api/pois/filter
Body: { townId, layers, priceRange, distance, rating, amenities }
Response: { filtered POIs }
```

#### Save Location
```
POST /api/users/{userId}/saved
Body: { poiId }
Response: { success, savedId }
```

## Database Schema

### Tables

#### towns
```sql
CREATE TABLE towns (
  id SERIAL PRIMARY KEY,
  name VARCHAR(255) NOT NULL,
  country VARCHAR(100),
  state VARCHAR(100),
  location GEOGRAPHY(POINT, 4326),
  population INTEGER,
  created_at TIMESTAMP DEFAULT NOW()
);

CREATE INDEX idx_towns_location ON towns USING GIST(location);
```

#### pois (Points of Interest)
```sql
CREATE TABLE pois (
  id SERIAL PRIMARY KEY,
  town_id INTEGER REFERENCES towns(id),
  name VARCHAR(255) NOT NULL,
  category VARCHAR(50), -- hotel, activity_outdoor, activity_indoor, campground_rv, campground_tent, historic, brewery, restaurant, bar
  location GEOGRAPHY(POINT, 4326),
  address TEXT,
  phone VARCHAR(50),
  website VARCHAR(500),
  description TEXT,
  price_range INTEGER, -- 1-4 ($-$$$$)
  rating DECIMAL(2,1),
  photos JSONB,
  amenities JSONB,
  hours JSONB,
  cuisine_type VARCHAR(100), -- for restaurants
  dietary_options JSONB, -- ["gluten_free", "vegan", "vegetarian", "dairy_free", "nut_free", "shellfish_free", "halal", "kosher"]
  tags JSONB, -- ["romantic", "couples_friendly", "date_night", "anniversary_special"]
  external_id VARCHAR(255),
  external_source VARCHAR(50),
  created_at TIMESTAMP DEFAULT NOW(),
  updated_at TIMESTAMP DEFAULT NOW()
);

CREATE INDEX idx_pois_location ON pois USING GIST(location);
CREATE INDEX idx_pois_category ON pois(category);
CREATE INDEX idx_pois_town ON pois(town_id);
CREATE INDEX idx_pois_tags ON pois USING GIN(tags);
CREATE INDEX idx_pois_dietary ON pois USING GIN(dietary_options);
```

#### users
```sql
CREATE TABLE users (
  id SERIAL PRIMARY KEY,
  email VARCHAR(255) UNIQUE NOT NULL,
  password_hash VARCHAR(255),
  name VARCHAR(255),
  created_at TIMESTAMP DEFAULT NOW()
);
```

#### family_groups
```sql
CREATE TABLE family_groups (
  id SERIAL PRIMARY KEY,
  name VARCHAR(255), -- "Smith Family"
  primary_user_id INTEGER REFERENCES users(id),
  created_at TIMESTAMP DEFAULT NOW()
);
```

#### family_members
```sql
CREATE TABLE family_members (
  id SERIAL PRIMARY KEY,
  family_group_id INTEGER REFERENCES family_groups(id),
  user_id INTEGER REFERENCES users(id), -- null for kids without accounts
  name VARCHAR(255) NOT NULL,
  relationship VARCHAR(50), -- husband, wife, child, teen
  age_range VARCHAR(20), -- adult, teen, child_6_12, child_0_5
  preferences JSONB, -- {interests, dietary, mobility, activity_level}
  created_at TIMESTAMP DEFAULT NOW()
);

CREATE INDEX idx_family_members_group ON family_members(family_group_id);
```

#### member_preferences
```sql
CREATE TABLE member_preferences (
  id SERIAL PRIMARY KEY,
  family_member_id INTEGER REFERENCES family_members(id),
  category VARCHAR(50), -- activities, food, accommodation, interests
  preference_type VARCHAR(100), -- outdoor_activities, museums, breweries, playgrounds
  importance VARCHAR(20), -- must_have, nice_to_have, avoid
  notes TEXT,
  created_at TIMESTAMP DEFAULT NOW()
);
```

#### saved_locations
```sql
CREATE TABLE saved_locations (
  id SERIAL PRIMARY KEY,
  user_id INTEGER REFERENCES users(id),
  poi_id INTEGER REFERENCES pois(id),
  notes TEXT,
  created_at TIMESTAMP DEFAULT NOW(),
  UNIQUE(user_id, poi_id)
);
```

#### conversations
```sql
CREATE TABLE conversations (
  id UUID PRIMARY KEY,
  user_id INTEGER REFERENCES users(id),
  town_id INTEGER REFERENCES towns(id),
  created_at TIMESTAMP DEFAULT NOW(),
  updated_at TIMESTAMP DEFAULT NOW()
);
```

#### messages
```sql
CREATE TABLE messages (
  id SERIAL PRIMARY KEY,
  conversation_id UUID REFERENCES conversations(id),
  role VARCHAR(20), -- 'user' or 'assistant'
  content TEXT,
  metadata JSONB, -- function calls, POI references
  created_at TIMESTAMP DEFAULT NOW()
);
```

#### transit_stops
```sql
CREATE TABLE transit_stops (
  id SERIAL PRIMARY KEY,
  town_id INTEGER REFERENCES towns(id),
  name VARCHAR(255) NOT NULL,
  type VARCHAR(50), -- subway, bus, train
  location GEOGRAPHY(POINT, 4326),
  routes JSONB, -- [{route_id, route_name, route_color}]
  accessibility JSONB, -- {elevator, ramp, audio_announcements}
  external_id VARCHAR(255),
  created_at TIMESTAMP DEFAULT NOW()
);

CREATE INDEX idx_transit_stops_location ON transit_stops USING GIST(location);
CREATE INDEX idx_transit_stops_type ON transit_stops(type);
```

#### transit_routes
```sql
CREATE TABLE transit_routes (
  id SERIAL PRIMARY KEY,
  town_id INTEGER REFERENCES towns(id),
  route_name VARCHAR(255),
  route_number VARCHAR(50),
  type VARCHAR(50), -- subway, bus, train
  color VARCHAR(7), -- hex color
  path GEOGRAPHY(LINESTRING, 4326),
  stops JSONB, -- array of stop_ids in order
  schedule JSONB,
  created_at TIMESTAMP DEFAULT NOW()
);
```

#### events
```sql
CREATE TABLE events (
  id SERIAL PRIMARY KEY,
  town_id INTEGER REFERENCES towns(id),
  name VARCHAR(255) NOT NULL,
  type VARCHAR(50), -- fireworks, festival, concert, holiday
  location GEOGRAPHY(POINT, 4326),
  venue_name VARCHAR(255),
  start_date TIMESTAMP,
  end_date TIMESTAMP,
  description TEXT,
  ticket_url VARCHAR(500),
  price_range INTEGER,
  photos JSONB,
  created_at TIMESTAMP DEFAULT NOW()
);

CREATE INDEX idx_events_dates ON events(start_date, end_date);
CREATE INDEX idx_events_town ON events(town_id);
CREATE INDEX idx_events_type ON events(type);
```

#### trips
```sql
CREATE TABLE trips (
  id SERIAL PRIMARY KEY,
  user_id INTEGER REFERENCES users(id),
  family_group_id INTEGER REFERENCES family_groups(id),
  town_id INTEGER REFERENCES towns(id),
  start_date DATE,
  end_date DATE,
  home_location GEOGRAPHY(POINT, 4326),
  trip_purpose VARCHAR(50), -- anniversary, family, solo, business, romantic_getaway
  preferences JSONB, -- {accommodation, interests, budget}
  itinerary JSONB, -- day-by-day plans with member assignments
  created_at TIMESTAMP DEFAULT NOW()
);
```

## Infrastructure Requirements

### Hosting
- **Frontend**: Vercel or AWS S3 + CloudFront
- **Backend**: AWS EC2 (t3.medium) or ECS
- **Database**: AWS RDS PostgreSQL with PostGIS

### Performance Requirements
- API response time: < 100ms (p95)
- Map initial load: < 2 seconds
- POI marker rendering: < 500ms for 1000 markers
- Database queries: < 50ms with geospatial indexing

### Scalability
- Support 1000 concurrent users (MVP)
- Handle 10,000 POIs per town
- Cache frequently accessed towns
- CDN for static assets

### Security
- HTTPS only
- JWT authentication with 1-hour expiration
- Rate limiting: 100 requests/minute per IP
- Input validation and sanitization
- SQL injection prevention (parameterized queries)
- XSS protection

## Technology Stack Details

### Frontend Dependencies
```json
{
  "react": "^18.2.0",
  "typescript": "^5.0.0",
  "mapbox-gl": "^2.15.0",
  "axios": "^1.4.0",
  "@mui/material": "^5.14.0",
  "zustand": "^4.4.0",
  "react-router-dom": "^6.15.0"
}
```

### Backend Dependencies
```xml
<!-- pom.xml -->
<dependencies>
    <!-- Spring Boot -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-data-jpa</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-security</artifactId>
    </dependency>
    
    <!-- Database -->
    <dependency>
        <groupId>org.postgresql</groupId>
        <artifactId>postgresql</artifactId>
    </dependency>
    <dependency>
        <groupId>org.hibernate</groupId>
        <artifactId>hibernate-spatial</artifactId>
    </dependency>
    
    <!-- JWT -->
    <dependency>
        <groupId>io.jsonwebtoken</groupId>
        <artifactId>jjwt</artifactId>
        <version>0.9.1</version>
    </dependency>
    
    <!-- Testing -->
    <dependency>
        <groupId>io.cucumber</groupId>
        <artifactId>cucumber-java</artifactId>
        <version>7.14.0</version>
        <scope>test</scope>
    </dependency>
    <dependency>
        <groupId>io.cucumber</groupId>
        <artifactId>cucumber-spring</artifactId>
        <version>7.14.0</version>
        <scope>test</scope>
    </dependency>
    <dependency>
        <groupId>io.cucumber</groupId>
        <artifactId>cucumber-junit-platform-engine</artifactId>
        <version>7.14.0</version>
        <scope>test</scope>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-test</artifactId>
        <scope>test</scope>
    </dependency>
</dependencies>
```

## Development Environment

### Required Tools
- Node.js 18+
- PostgreSQL 14+ with PostGIS extension
- Git
- Docker (optional, for local DB)

### Environment Variables
```
# Frontend
VITE_API_URL=http://localhost:3000
VITE_MAPBOX_TOKEN=your_token

# Backend
DATABASE_URL=postgresql://postgres:${DB_PASSWORD}@localhost:5432/myconcierge
JWT_SECRET=your_secret
MAPBOX_API_KEY=your_key
GOOGLE_PLACES_API_KEY=your_key
PORT=3000
```

## Testing Requirements

### Unit Tests
- 80%+ code coverage
- Jest for backend
- React Testing Library for frontend

### Integration Tests
- API endpoint testing
- Database query testing
- External API mocking

### E2E Tests
- Playwright or Cypress
- Critical user flows:
  - Search town → View map → Toggle layers → View POI details

## Monitoring and Logging

### Metrics to Track
- API response times
- Error rates
- User sessions
- Popular towns/POIs
- Map interactions

### Logging
- Winston or Pino for structured logging
- CloudWatch or Datadog for aggregation
- Error tracking with Sentry

## Deployment

### CI/CD Pipeline
- GitHub Actions or AWS CodePipeline
- Automated testing on PR
- Staging environment for QA
- Production deployment on merge to main

### Deployment Steps
1. Run tests
2. Build frontend (Vite)
3. Build backend (TypeScript compilation)
4. Run database migrations
5. Deploy to staging
6. Smoke tests
7. Deploy to production
8. Health check verification
