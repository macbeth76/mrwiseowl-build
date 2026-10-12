# Mr Wise Owl - Project Overview

## Purpose
AI-powered travel concierge that helps visitors explore towns through an interactive map interface with multiple layers showing hotels, activities, campgrounds, historic sites, transit, events, and dining options. Specialized for RV travelers and families with personalized recommendations.

## Brand Identity
**Name**: Mr Wise Owl 🦉
**Domain**: wiseowltech.net
**Subdomain**: mrwiseowl.wiseowltech.net
**Tagline**: "Your wise guide to every adventure"
**Mission**: Make travel planning simple, personalized, and accessible for families and RV travelers

## Target Users
- Tourists visiting new towns
- Local residents exploring their area
- Travel planners researching destinations

## Core Value Proposition
Single map interface to discover all points of interest in a town without switching between multiple apps or websites.

## Tech Stack

### Frontend
- **Framework**: React 18+ with TypeScript
- **Map Library**: Leaflet.js + OpenStreetMap (100% free)
- **UI Components**: Tailwind CSS
- **State Management**: Zustand
- **Build Tool**: Vite
- **Hosting**: Vercel (Free tier)

### Backend
- **Language**: Java 17+
- **Framework**: Spring Boot 3.x
- **Build Tool**: Gradle
- **API**: RESTful with Spring Web
- **SSR**: Thymeleaf + HTMX
- **Database Access**: Spring Data JPA + Hibernate Spatial
- **Security**: Spring Security with JWT
- **Testing**: Cucumber (BDD), JUnit 5, Mockito
- **Hosting**: Railway.app or Render.com (Free tier)

### Database
- **Primary**: PostgreSQL 14+ with PostGIS extension
- **Hosting**: Supabase (Free tier - 500MB)
- **Cache**: Spring Cache (in-memory)

### External APIs (Free Tiers)
- **Maps**: OpenStreetMap + Nominatim (geocoding)
- **AI Chatbot**: Groq (Llama 3 - 14,400 requests/day free)
- **POI Data**: OpenStreetMap Overpass API
- **Campgrounds**: Recreation.gov API (free)
- **Transit**: GTFS feeds (free, city-specific)

### Infrastructure (All Free)
- **Frontend Hosting**: Vercel
- **Backend Hosting**: Railway.app
- **Database**: Supabase
- **CDN**: Vercel Edge (included)
- **Domain**: wiseowltech.net (already owned)
- **SSL**: Automatic (Vercel/Railway)
- **Monitoring**: UptimeRobot (free tier)

## Architecture

### High-Level Components
1. **Web Application** - React SPA with map interface
2. **API Server** - Handles data queries and user requests
3. **Database** - Stores location data with geospatial indexing
4. **External APIs** - Third-party data sources (Google Places, OpenStreetMap)

### Data Flow
```
User Input (Town Search) 
  → API Request 
  → Database Query (geospatial) 
  → Response with POI data 
  → Map Rendering with Layers
```

## Project Structure
```
mrwiseowl/
├── docs/                    # Kiro spec files
│   ├── PROJECT_OVERVIEW.md
│   ├── FEATURES.md
│   ├── TECHNICAL_REQUIREMENTS.md
│   ├── UI_UX_SPEC.md
│   ├── AI_CHATBOT_SPEC.md
│   ├── DESIGN_PATTERNS.md
│   ├── BOOTSTRAP_STRATEGY.md
│   └── TECH_COMPARISON.md
├── backend/                 # Spring Boot application
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/wiseowltech/mrwiseowl/
│   │   │   │   ├── controller/
│   │   │   │   ├── service/
│   │   │   │   ├── repository/
│   │   │   │   ├── model/
│   │   │   │   ├── dto/
│   │   │   │   ├── config/
│   │   │   │   └── MrWiseOwlApplication.java
│   │   │   └── resources/
│   │   │       ├── application.yml
│   │   │       ├── templates/        # Thymeleaf SSR
│   │   │       └── db/migration/     # Flyway
│   │   └── test/
│   │       ├── java/com/wiseowltech/mrwiseowl/
│   │       │   ├── bdd/steps/
│   │       │   └── unit/
│   │       └── resources/features/   # Cucumber BDD
│   ├── build.gradle
│   └── settings.gradle
├── frontend/                # React application
│   ├── src/
│   │   ├── components/
│   │   ├── pages/
│   │   ├── services/
│   │   └── utils/
│   ├── public/
│   └── package.json
└── README.md
```

## Development Phases

### Phase 1: Free MVP (Weeks 1-10) - $0 Budget
**Goal**: Validate concept with 100 active users

**Features**:
- Town search (OpenStreetMap)
- Interactive map with 3 layers (Hotels, Activities, Campgrounds)
- Basic AI chatbot (Groq/Llama 3)
- User authentication (Supabase)
- Save favorite locations
- SSR for SEO

**Infrastructure**: All free tier services

### Phase 2: Freemium Launch (Months 4-6) - ~$25/month
**Goal**: 1,000 users, 50 paying subscribers ($500/month revenue)

**New Features**:
- Family profiles with preferences
- Dietary restriction filters
- Transit layer
- Events layer
- Trip planning
- Premium AI features

**Monetization**:
- Free tier: 5 searches/month, basic chat
- Premium: $9.99/month - unlimited features

### Phase 3: Scale & Grow (Months 7-12) - ~$100/month
**Goal**: 5,000 users, 200 premium, 10 business ($3,000/month revenue)

**New Features**:
- Mobile apps (React Native)
- Advanced trip optimization
- Real-time transit updates
- White-label for RV parks
- API access for partners

**Business Tier**: $49/month for RV parks and tourism boards

## Success Metrics

### Phase 1 (MVP)
- 100 active users
- 500 searches/month
- 50 saved locations
- 10 beta testers providing feedback

### Phase 2 (Freemium)
- 1,000 active users
- 5,000 searches/month
- 50 premium subscribers
- $500 monthly revenue

### Phase 3 (Scale)
- 5,000 active users
- 25,000 searches/month
- 200 premium + 10 business subscribers
- $3,000 monthly revenue
- Break-even and profitable
