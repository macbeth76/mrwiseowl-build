# Getting Started with Mr Wise Owl 🦉

## Quick Start (5 minutes)

### Prerequisites
- Java 17+
- Node.js 18+
- Git

### 1. Clone the Repository
```bash
git clone git@github.com:wiseowltechnet/mrwiseowl.git
cd mrwiseowl
```

### 2. Start Everything
```bash
./control.sh start
```

That's it! 🎉

- **Frontend**: http://localhost:5173
- **Backend**: http://localhost:8080
- **API Docs**: See API.md

### 3. Stop Everything
```bash
./control.sh stop
```

---

## What You'll See

### Frontend (http://localhost:5173)
- Interactive map of Asheville, NC
- 3 POI markers (Biltmore Estate, Wicked Weed Brewing, Asheville KOA)
- Click markers for details
- Responsive header with town info

### Backend (http://localhost:8080)
- REST API with 8 endpoints
- PostgreSQL + PostGIS database (Supabase)
- Geospatial queries
- Dietary filtering

---

## Control Script Commands

### All Services
```bash
./control.sh start      # Start backend + frontend
./control.sh stop       # Stop everything
./control.sh restart    # Restart everything
./control.sh status     # Show what's running
```

### Backend Only
```bash
./control.sh backend:start    # Start backend
./control.sh backend:stop     # Stop backend
./control.sh backend:restart  # Restart backend
./control.sh backend:logs     # View logs
./control.sh backend:test     # Test API endpoints
```

### Frontend Only
```bash
./control.sh frontend:start   # Start frontend
./control.sh frontend:stop    # Stop frontend
./control.sh frontend:logs    # View logs
```

### Docker
```bash
./control.sh docker:up        # Start PostgreSQL + backend
./control.sh docker:down      # Stop Docker services
./control.sh docker:logs      # View Docker logs
```

---

## Manual Setup (if control.sh doesn't work)

### Backend
```bash
cd backend
./gradlew build -x test
java -jar build/libs/mrwiseowl-0.0.1-SNAPSHOT.jar
```

### Frontend
```bash
cd frontend
npm install
npm run dev
```

---

## Testing the API

### Using control.sh
```bash
./control.sh backend:test
```

### Using curl
```bash
# Health check
curl http://localhost:8080/api/health

# Search towns
curl "http://localhost:8080/api/towns/search?q=Asheville"

# Get POIs
curl "http://localhost:8080/api/pois?townId=1"

# Get POIs by category
curl "http://localhost:8080/api/pois?townId=1&category=brewery"

# Find nearby POIs
curl "http://localhost:8080/api/pois/nearby?lat=35.5951&lng=-82.5515&radius=5"

# Dietary filtering
curl "http://localhost:8080/api/pois/dietary?option=gluten_free"
```

---

## Project Structure

```
mrwiseowl/
├── backend/                 # Spring Boot API
│   ├── src/main/java/
│   │   └── com/wiseowltech/mrwiseowl/
│   │       ├── controller/  # REST endpoints
│   │       ├── service/     # Business logic
│   │       ├── repository/  # Data access
│   │       ├── model/       # Entities
│   │       └── dto/         # API responses
│   └── build.gradle
├── frontend/                # React app
│   ├── src/
│   │   ├── components/      # Map, etc.
│   │   └── App.tsx
│   └── package.json
├── docs/                    # Kiro specs
├── control.sh              # Service manager
├── docker-compose.yml      # Local dev
└── README.md
```

---

## Environment Variables

### Backend (backend/src/main/resources/application.yml)
```yaml
DATABASE_URL: jdbc:postgresql://db.clamldjsgwixexashtmm.supabase.co:5432/postgres
DB_USER: postgres
DB_PASSWORD: 4JYz_!2hQsCi$uS
JWT_SECRET: your-secret-key
```

### Frontend (frontend/.env)
```
VITE_API_URL=http://localhost:8080
```

---

## Database

### Supabase (Production)
- **Host**: db.clamldjsgwixexashtmm.supabase.co
- **Database**: postgres
- **User**: postgres
- **Password**: 4JYz_!2hQsCi$uS
- **PostGIS**: Enabled

### Local (Docker)
```bash
./control.sh docker:up
```
- **Host**: localhost:5432
- **Database**: mrwiseowl
- **User**: postgres
- **Password**: localdev123

---

## Troubleshooting

### Backend won't start
```bash
# Check logs
./control.sh backend:logs

# Or manually
tail -f .pids/backend.log
```

### Frontend won't start
```bash
# Check logs
./control.sh frontend:logs

# Or reinstall dependencies
cd frontend
rm -rf node_modules
npm install
```

### Port already in use
```bash
# Kill process on port 8080
lsof -ti:8080 | xargs kill -9

# Kill process on port 5173
lsof -ti:5173 | xargs kill -9
```

### Database connection fails
- Verify Supabase credentials in application.yml
- Check internet connection
- Verify PostGIS extension is enabled

---

## Development Workflow

### 1. Start services
```bash
./control.sh start
```

### 2. Make changes
- Backend: Edit Java files in `backend/src/`
- Frontend: Edit React files in `frontend/src/`

### 3. Restart to see changes
```bash
# Backend changes
./control.sh backend:restart

# Frontend changes (auto-reloads)
# Just save the file
```

### 4. Test
```bash
./control.sh backend:test
```

### 5. Commit
```bash
git add .
git commit -m "Your changes"
git push origin main
```

---

## Deployment

### Backend (Railway)
- Auto-deploys from GitHub
- Push to main branch
- Railway builds and deploys automatically

### Frontend (Vercel - Coming Soon)
```bash
cd frontend
vercel
```

---

## Next Steps

### Add More Data
1. Go to Supabase dashboard
2. Run SQL to insert towns and POIs
3. Restart backend: `./control.sh backend:restart`

### Add Features
- See TODO list: 8/25 tasks complete (32%)
- Next: AI chatbot, user auth, more layers

### Deploy
- Backend: Already on Railway
- Frontend: Deploy to Vercel
- Domain: Configure mrwiseowl.wiseowltech.net

---

## Resources

- **API Documentation**: API.md
- **Deployment Guide**: DEPLOYMENT.md
- **Supabase Setup**: SUPABASE_SETUP.md
- **Session Summary**: SESSION_SUMMARY.md
- **Kiro Specs**: docs/

---

## Support

### Issues
- Check logs: `./control.sh backend:logs`
- Test API: `./control.sh backend:test`
- Check status: `./control.sh status`

### Questions
- See documentation in docs/
- Check API.md for endpoint details
- Review SESSION_SUMMARY.md for what's implemented

---

## Quick Reference

```bash
# Start everything
./control.sh start

# Check status
./control.sh status

# View logs
./control.sh backend:logs
./control.sh frontend:logs

# Test API
./control.sh backend:test

# Stop everything
./control.sh stop
```

**Happy coding! 🦉**
