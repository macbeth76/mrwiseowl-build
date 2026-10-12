# Mr Wise Owl - Deployment Guide

## Railway Deployment

### Step 1: Push to GitHub

```bash
git push origin main
```

### Step 2: Deploy to Railway

1. Go to https://railway.app/dashboard
2. Click **"New Project"**
3. Select **"Deploy from GitHub repo"**
4. Choose **mrwiseowl** repository
5. Railway will auto-detect the configuration

### Step 3: Set Environment Variables

In Railway dashboard, go to **Variables** and add:

```
DATABASE_URL=jdbc:postgresql://db.clamldjsgwixexashtmm.supabase.co:5432/postgres
DB_USER=postgres
DB_PASSWORD=4JYz_!2hQsCi$uS
JWT_SECRET=your-production-secret-key-change-this
GROQ_API_KEY=your-groq-api-key
```

### Step 4: Configure Domain

1. In Railway, go to **Settings** → **Domains**
2. Click **"Generate Domain"** (gets you a railway.app domain)
3. Or add custom domain: **mrwiseowl.wiseowltech.net**

For custom domain:
- Add CNAME record in your DNS:
  - Name: `mrwiseowl`
  - Value: `[your-app].railway.app`

### Step 5: Deploy

Railway will automatically:
- Build the Spring Boot app
- Run Flyway migrations
- Start the application
- Expose on port 8080

### Health Check

Once deployed, test:
```bash
curl https://mrwiseowl-production.up.railway.app/api/health
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

---

## Vercel Deployment (Frontend - Later)

### Step 1: Create Frontend

```bash
cd frontend
npm create vite@latest . -- --template react-ts
npm install
```

### Step 2: Deploy to Vercel

```bash
npm install -g vercel
vercel
```

Follow prompts:
- Project name: **mrwiseowl**
- Framework: **Vite**
- Build command: `npm run build`
- Output directory: `dist`

### Step 3: Configure Domain

In Vercel dashboard:
- Add domain: **mrwiseowl.wiseowltech.net**
- Vercel will provide DNS instructions

---

## Environment Variables Summary

### Railway (Backend)
```
DATABASE_URL=jdbc:postgresql://db.clamldjsgwixexashtmm.supabase.co:5432/postgres
DB_USER=postgres
DB_PASSWORD=4JYz_!2hQsCi$uS
JWT_SECRET=[generate-secure-key]
GROQ_API_KEY=[your-key]
PORT=8080
```

### Vercel (Frontend)
```
VITE_API_URL=https://mrwiseowl-production.up.railway.app
```

---

## Monitoring

### Railway Logs
```bash
railway logs
```

Or view in dashboard: **Deployments** → **Logs**

### Health Checks

Railway automatically monitors `/api/health` endpoint.

---

## Troubleshooting

### Build fails
- Check Gradle version compatibility
- Verify Java 17 is used
- Check build logs in Railway dashboard

### Database connection fails
- Verify Supabase credentials
- Check DATABASE_URL format
- Ensure Supabase allows Railway IP

### App crashes on startup
- Check environment variables are set
- Verify Flyway migrations succeeded
- Check application logs

---

## Cost Tracking

### Free Tier Limits

**Railway**:
- $5 credit/month
- 500 hours execution
- 100GB bandwidth

**Supabase**:
- 500MB database
- Unlimited API requests

**Vercel**:
- 100GB bandwidth
- Unlimited deployments

**Total: $0/month** (within free tiers) ✅

---

## Next Steps

1. ✅ Deploy backend to Railway
2. ⏳ Build React frontend
3. ⏳ Deploy frontend to Vercel
4. ⏳ Configure custom domain
5. ⏳ Set up monitoring

**Ready to launch Mr Wise Owl!** 🦉🚀
