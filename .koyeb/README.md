# Koyeb Deployment

## Scripts

### Deploy
Trigger a new deployment:
```bash
./.koyeb/deploy.sh
```

### Status
Check deployment status:
```bash
./.koyeb/status.sh
```

## Manual Deployment

Using Koyeb CLI:
```bash
koyeb service redeploy explicit-tiffani-wiseowltech-07037a1a
```

## Configuration

- **Service ID**: explicit-tiffani-wiseowltech-07037a1a
- **API Key**: Stored in `.koyeb/api-key.txt` (gitignored)
- **Backend URL**: https://explicit-tiffani-wiseowltech-07037a1a.koyeb.app

## Environment Variables

Set in Koyeb dashboard:
- `SUPABASE_URL`
- `SUPABASE_USER`
- `SUPABASE_PASSWORD`
- `JWT_SECRET`
- `GROQ_API_KEY`
- `PORT=8080`
