# Mr Wise Owl 🦉

**Your wise guide to every adventure**

AI-powered travel concierge for RV travelers and families. Find the perfect destination with personalized recommendations, interactive maps, and smart trip planning.

## 🌟 Features

- **Smart City Search**: Find cities matching your exact criteria (RV sites, breweries, events, etc.)
- **Interactive Maps**: 7 layers - Hotels, Activities, Campgrounds (RV/Tent), Historic Sites, Transit, Events, Dining
- **AI Chatbot**: Powered by Llama 3 - Get personalized recommendations
- **Family Profiles**: Set preferences for each family member (including dietary restrictions)
- **Trip Planning**: Multi-day itineraries balanced for everyone
- **SSR for SEO**: Server-side rendered pages for better search rankings

## 🚀 Tech Stack

**Frontend**: React + TypeScript + Tailwind CSS + Leaflet.js
**Backend**: Spring Boot 3.x + Gradle + Thymeleaf + HTMX
**Database**: PostgreSQL + PostGIS (Supabase)
**AI**: Groq (Llama 3)
**Maps**: OpenStreetMap (100% free)
**Hosting**: Vercel + Koyeb (Free tiers)

## 📋 Project Structure

```
mrwiseowl/
├── docs/                    # Kiro specification files
│   ├── PROJECT_OVERVIEW.md
│   ├── FEATURES.md
│   ├── TECHNICAL_REQUIREMENTS.md
│   ├── UI_UX_SPEC.md
│   ├── AI_CHATBOT_SPEC.md
│   ├── DESIGN_PATTERNS.md
│   ├── BOOTSTRAP_STRATEGY.md
│   └── TECH_COMPARISON.md
├── backend/                 # Spring Boot application
└── frontend/                # React application
```

## 🎯 Development Roadmap

### Phase 1: Free MVP (Weeks 1-10) - $0 Budget
- [x] Kiro spec files created
- [x] Steering documentation (.kiro/steering/)
- [x] Infrastructure setup (Vercel, Koyeb, Supabase)
- [x] GitHub repository with SSH key configuration
- [x] Spring Boot 3.2.0 + Gradle backend
- [x] PostgreSQL + PostGIS database
- [x] Town search with geospatial queries
- [x] POI system with 8 categories (44+ real locations)
- [x] Interactive map with Leaflet.js
- [x] AI chatbot integration (Groq/Llama 3.1-8b-instant)
- [x] User authentication (JWT + BCrypt)
- [x] Itinerary builder
- [x] Deployed to Koyeb + Vercel (auto-deploy from GitHub)
- [ ] SSR with Thymeleaf (optional)
- [ ] Public launch with custom domain

**Goal**: 100 active users, validate concept

**Current Status**: MVP Complete - 22/25 tasks done (88%) - Production deployed

### Phase 2: Freemium (Months 4-6)
- [ ] Family profiles
- [ ] Premium features
- [ ] Monetization ($9.99/month)

**Goal**: 1,000 users, $500/month revenue

### Phase 3: Scale (Months 7-12)
- [ ] Mobile apps
- [ ] Business tier
- [ ] API access

**Goal**: 5,000 users, $3,000/month revenue

## 🛠️ Getting Started

### Prerequisites
- Java 17+
- Node.js 18+
- PostgreSQL 14+ with PostGIS
- Gradle

### Backend Setup
```bash
cd backend
./gradlew bootRun
```

### Frontend Setup
```bash
cd frontend
npm install
npm run dev
```

### Environment Variables
```bash
# Backend (application.yml)
SUPABASE_URL=your_supabase_url
SUPABASE_KEY=your_supabase_key
GROQ_API_KEY=your_groq_key

# Frontend (.env)
VITE_API_URL=http://localhost:8080
VITE_MAPBOX_TOKEN=not_needed_for_osm
```

## 🧪 Testing

### BDD Tests (Cucumber)
```bash
./gradlew test
```

### Feature Files
Located in `backend/src/test/resources/features/`

Example:
```gherkin
Feature: Search cities by criteria
  Scenario: Find RV-friendly cities with breweries
    Given I am traveling from "Garner, NC"
    When I search for cities with RV sites and breweries
    Then I should see at least 3 matching cities
```

## 🎨 Design Patterns

This project demonstrates 10 key design patterns:
- Strategy Pattern (search algorithms)
- Factory Pattern (POI creation)
- Builder Pattern (search criteria)
- Repository Pattern (data access)
- Observer Pattern (real-time updates)
- Decorator Pattern (POI enrichment)
- Chain of Responsibility (preference filtering)
- Template Method (trip planning)
- Adapter Pattern (external APIs)
- Facade Pattern (complex operations)

See [DESIGN_PATTERNS.md](docs/DESIGN_PATTERNS.md) for details.

## 💰 Zero Budget Strategy

**Total Cost: $0/month** (Phase 1)

All services use free tiers:
- Vercel (frontend hosting)
- Koyeb (backend hosting)
- Supabase (PostgreSQL + PostGIS)
- OpenStreetMap (maps, no API key needed)
- Groq (AI chatbot - 14,400 requests/day free)
- Recreation.gov API (campground data)

See [BOOTSTRAP_STRATEGY.md](docs/BOOTSTRAP_STRATEGY.md) for full details.

## 📚 Documentation

- [Project Overview](docs/PROJECT_OVERVIEW.md) - Tech stack, architecture, phases
- [Features](docs/FEATURES.md) - Complete feature specifications
- [Technical Requirements](docs/TECHNICAL_REQUIREMENTS.md) - APIs, database, infrastructure
- [UI/UX Spec](docs/UI_UX_SPEC.md) - Design system, components, accessibility
- [AI Chatbot](docs/AI_CHATBOT_SPEC.md) - Conversational AI specifications
- [Design Patterns](docs/DESIGN_PATTERNS.md) - Architecture patterns used
- [Bootstrap Strategy](docs/BOOTSTRAP_STRATEGY.md) - Zero-budget launch plan
- [Tech Comparison](docs/TECH_COMPARISON.md) - Spring Boot vs Micronaut, Maven vs Gradle

## 🤝 Contributing

This is a startup project. Contributions welcome once MVP is launched!

## 📄 License

Proprietary - All rights reserved

## 🦉 About

**Mr Wise Owl** is built by [Wise Owl Technologies](https://wiseowltech.net)

**Domain**: https://app.mrwiseowl.wiseowltech.net
**Mission**: Make travel planning simple, personalized, and accessible for everyone

---

**Built with ❤️ using Spring Boot, React, and zero budget** 🚀
