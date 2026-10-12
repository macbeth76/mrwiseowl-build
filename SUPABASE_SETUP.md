# Supabase Setup Instructions

## Step 1: Create New Project

1. Go to https://supabase.com/dashboard
2. Click "New Project"
3. Fill in:
   - **Name**: mrwiseowl
   - **Database Password**: /3dZxmJs$_ryEdB
   - **Region**: Choose closest to you (e.g., East US)
4. Click "Create new project" (takes ~2 minutes)

## Step 2: Enable PostGIS Extension

1. In your project dashboard, go to **Database** → **Extensions**
2. Search for "postgis"
3. Click **Enable** on the PostGIS extension
4. Wait for confirmation

## Step 3: Get Connection Details

1. Go to **Settings** → **Database**
2. Copy these values:

```
Host: db.[your-project-ref].supabase.co
Database name: postgres
Port: 5432
User: postgres
Password: /3dZxmJs$_ryEdB
```

3. Connection string format:
```
postgresql://postgres:/3dZxmJs$_ryEdB@db.[your-project-ref].supabase.co:5432/postgres
```

## Step 4: Update application.yml

Replace `[your-project-ref]` in backend/src/main/resources/application.yml:

```yaml
spring:
  datasource:
    url: jdbc:postgresql://db.[your-project-ref].supabase.co:5432/postgres
    username: postgres
    password: /3dZxmJs$_ryEdB
```

## Step 5: Run Migrations

From the backend directory:

```bash
cd backend
./gradlew flywayMigrate
```

This will:
- ✅ Enable PostGIS extension
- ✅ Create all tables (towns, pois, users, etc.)
- ✅ Insert sample data (Asheville, NC)

## Step 6: Verify Setup

Run the Spring Boot app:

```bash
./gradlew bootRun
```

Test the health endpoint:
```bash
curl http://localhost:8080/api/health
```

Expected response:
```json
{
  "status": "UP",
  "service": "Mr Wise Owl API",
  "timestamp": "2024-11-29T...",
  "message": "🦉 Your wise guide to every adventure"
}
```

## Troubleshooting

### Connection refused
- Check if Supabase project is fully initialized
- Verify connection string has correct project ref
- Check firewall/network settings

### PostGIS not found
- Make sure PostGIS extension is enabled in Supabase dashboard
- Wait a few minutes after enabling

### Migration fails
- Check database password is correct
- Verify user has CREATE permissions
- Check Flyway logs for specific error

## Next Steps

Once setup is complete:
1. ✅ Database is ready
2. ✅ PostGIS enabled
3. ✅ Sample data loaded
4. Ready to build features! 🚀
