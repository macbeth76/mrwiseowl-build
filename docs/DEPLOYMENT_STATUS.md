# Deployment Status

## Production URLs

**Frontend**: https://frontend-k4g28iok6-wise-owl-techs-projects.vercel.app
**Backend**: https://explicit-tiffani-wiseowltech-07037a1a.koyeb.app

## Infrastructure

### Frontend (Vercel)
- **Platform**: Vercel
- **Framework**: React + TypeScript + Vite
- **Auto-deploy**: Yes (from main branch)
- **Build Command**: `npm run build`
- **Output Directory**: `dist`

### Backend (Koyeb)
- **Platform**: Koyeb
- **Framework**: Spring Boot 3.2.0
- **Auto-deploy**: Yes (from main branch)
- **Build**: Docker (from backend directory)
- **Port**: 8080

### Database (Supabase)
- **Platform**: Supabase
- **Type**: PostgreSQL 17 + PostGIS
- **Connection**: Via JDBC
- **Migrations**: Flyway

## Features Deployed

✅ User authentication (JWT)
✅ Town search (4 towns: Asheville, Gatlinburg, Pigeon Forge, Boone)
✅ POI system (44 real locations, 8 categories)
✅ Interactive map (Leaflet + OpenStreetMap)
✅ Category filtering
✅ Search functionality
✅ Itinerary builder
✅ Save/load itineraries
✅ AI chat (Groq + Llama 3.1-8b-instant)
✅ Mobile responsive design
✅ Performance caching

## API Endpoints

### Towns
- `GET /api/towns/search?q={query}` - Search towns
- `GET /api/towns/{id}` - Get town by ID

### POIs
- `GET /api/pois?townId={id}` - Get POIs for town
- `GET /api/pois?townId={id}&category={cat}` - Filter by category
- `GET /api/pois/{id}` - Get POI by ID

### Authentication
- `POST /api/auth/register` - Register new user
- `POST /api/auth/login` - Login user

### Itineraries
- `POST /api/itineraries` - Save itinerary (requires auth)
- `GET /api/itineraries` - List user's itineraries (requires auth)
- `DELETE /api/itineraries/{id}` - Delete itinerary (requires auth)

### AI Chat
- `POST /api/chat` - Send message to AI

## Environment Variables

### Backend
- `SUPABASE_URL` - Database connection URL
- `SUPABASE_USER` - Database username
- `SUPABASE_PASSWORD` - Database password
- `JWT_SECRET` - JWT signing key
- `GROQ_API_KEY` - Groq AI API key

### Frontend
- `VITE_API_URL` - Backend API URL

## Monitoring

- **Backend Health**: `GET /actuator/health`
- **Uptime**: Check via health endpoint
- **Logs**: Available in Koyeb dashboard

## Known Limitations (Free Tier)

- **Koyeb**: 512MB RAM, may sleep after inactivity
- **Supabase**: 500MB database, 2GB bandwidth/month
- **Groq**: 14,400 requests/day
- **Vercel**: 100GB bandwidth/month

## Deployment Process

1. Push to main branch
2. Koyeb auto-builds backend Docker image
3. Vercel auto-builds frontend
4. Both deploy automatically
5. Database migrations run on backend startup

## Rollback Procedure

1. Revert commit on main branch
2. Push to trigger redeploy
3. Or manually deploy previous version in platform dashboard

## Next Steps

- [ ] Add custom domain (mrwiseowl.wiseowltech.net)
- [ ] Set up monitoring alerts
- [ ] Configure CDN for static assets
- [ ] Add error tracking (Sentry)
- [ ] Implement rate limiting
