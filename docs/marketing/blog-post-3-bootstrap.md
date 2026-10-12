# Building a Travel App with $0 Budget: A Technical Case Study

**Published**: December 2024

Can you build and launch a production app without spending a dime? Yes. Here's how we built Mr Wise Owl using only free-tier services.

## The Challenge

Build a full-stack travel planning app with:
- User authentication
- Interactive maps
- AI chatbot
- Database with geospatial queries
- Mobile-responsive UI
- Production deployment

**Budget**: $0/month

## The Stack

### Frontend: Vercel (Free)
- **Framework**: React + TypeScript + Vite
- **Hosting**: Vercel free tier
- **Bandwidth**: 100GB/month
- **Auto-deploy**: From GitHub

**Cost**: $0

### Backend: Koyeb (Free)
- **Framework**: Spring Boot 3.2
- **Hosting**: Koyeb free tier
- **Resources**: 512MB RAM
- **Auto-deploy**: Docker from GitHub

**Cost**: $0

### Database: Supabase (Free)
- **Type**: PostgreSQL 17 + PostGIS
- **Storage**: 500MB
- **Bandwidth**: 2GB/month
- **Features**: Geospatial queries, JSONB

**Cost**: $0

### Maps: OpenStreetMap (Free)
- **Tiles**: Free, no API key needed
- **Library**: Leaflet.js
- **Data**: OpenStreetMap contributors

**Cost**: $0

### AI: Groq (Free)
- **Model**: Llama 3.1-8b-instant
- **Requests**: 14,400/day free
- **Speed**: 500+ tokens/second

**Cost**: $0

**Total Monthly Cost**: $0

## Technical Decisions

### Why Spring Boot?
- Mature ecosystem
- Built-in security
- Easy database integration
- Production-ready

### Why React?
- Component reusability
- Large community
- TypeScript support
- Fast development

### Why PostgreSQL + PostGIS?
- Geospatial queries (find nearby POIs)
- JSONB for flexible data
- Free on Supabase
- Industry standard

### Why Groq?
- Fastest inference (500+ tokens/sec)
- Generous free tier
- Open-source models
- No vendor lock-in

## Architecture

```
┌─────────────┐
│   Vercel    │  React Frontend
│  (Frontend) │  
└──────┬──────┘
       │ HTTPS
       ▼
┌─────────────┐
│   Koyeb     │  Spring Boot API
│  (Backend)  │  + JWT Auth
└──────┬──────┘
       │ JDBC
       ▼
┌─────────────┐
│  Supabase   │  PostgreSQL
│ (Database)  │  + PostGIS
└─────────────┘
       │
       ▼
┌─────────────┐
│    Groq     │  Llama 3.1
│    (AI)     │  Inference
└─────────────┘
```

## Key Features Implemented

### 1. Geospatial Queries
```sql
SELECT * FROM pois 
WHERE ST_DWithin(
  location::geography,
  ST_MakePoint(lng, lat)::geography,
  radius_meters
);
```

### 2. JWT Authentication
- BCrypt password hashing
- 24-hour token expiration
- Secure HTTP-only cookies

### 3. Caching
- Spring Cache for towns/POIs
- Reduces database load
- Faster response times

### 4. Mobile Responsive
- CSS media queries
- Touch-friendly (44px targets)
- Vertical layout on mobile

## Deployment Pipeline

1. Push to GitHub main branch
2. Koyeb builds Docker image
3. Vercel builds React app
4. Both auto-deploy
5. Flyway runs migrations

**Deploy time**: ~2 minutes

## Performance Metrics

- **Page Load**: <2 seconds
- **API Response**: <200ms (cached)
- **Database Queries**: <50ms
- **AI Response**: <1 second

## Limitations (Free Tier)

### Koyeb
- 512MB RAM (may sleep after inactivity)
- Single instance (no load balancing)

### Supabase
- 500MB storage (enough for 10K+ POIs)
- 2GB bandwidth/month

### Groq
- 14,400 requests/day (10 req/min)
- Rate limiting on free tier

## Scaling Strategy

When we hit limits:
1. **Koyeb**: Upgrade to $5/month (2GB RAM)
2. **Supabase**: Upgrade to $25/month (8GB storage)
3. **Groq**: Self-host Llama 3.1 on AWS

**Estimated cost at 1,000 users**: ~$50/month

## Lessons Learned

### What Worked
✅ Free tiers are surprisingly generous
✅ Modern frameworks are production-ready
✅ Open-source AI is viable
✅ Auto-deploy saves hours

### What Didn't
❌ Cold starts on free tier (Koyeb sleeps)
❌ Limited database storage
❌ No custom domain on free tier
❌ Rate limiting on AI requests

## Open Source

The entire codebase is on GitHub:
👉 **[github.com/wiseowltechnet/mrwiseowl](https://github.com/wiseowltechnet/mrwiseowl)**

## Try It

See the result of $0 budget:
👉 **[Launch Mr Wise Owl](https://frontend-k4g28iok6-wise-owl-techs-projects.vercel.app)**

## Conclusion

You don't need VC funding to build and launch a real product. Free tiers + smart architecture = viable MVP.

**Total development time**: 10 weeks  
**Total cost**: $0  
**Lines of code**: ~5,000  
**Features**: Production-ready

---

**Tech Stack Summary**:
- Frontend: React + TypeScript + Vite
- Backend: Spring Boot 3.2 + Gradle
- Database: PostgreSQL 17 + PostGIS
- AI: Groq + Llama 3.1
- Hosting: Vercel + Koyeb + Supabase
- Maps: OpenStreetMap + Leaflet.js

**Keywords**: zero budget startup, free tier hosting, Spring Boot deployment, React production, PostgreSQL PostGIS, Groq AI, bootstrapped SaaS, MVP development, serverless architecture
