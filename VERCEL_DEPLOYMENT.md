# Vercel Deployment Guide

## Option 1: Deploy via Vercel Dashboard (Easiest)

### Step 1: Go to Vercel
1. Visit https://vercel.com/dashboard
2. Click **"Add New..."** → **"Project"**

### Step 2: Import Repository
1. Select **"Import Git Repository"**
2. Choose **wiseowltechnet/mrwiseowl**
3. Click **"Import"**

### Step 3: Configure Project
- **Framework Preset**: Vite
- **Root Directory**: `frontend`
- **Build Command**: `npm run build`
- **Output Directory**: `dist`

### Step 4: Add Environment Variables
Click **"Environment Variables"** and add:

```
VITE_API_URL=https://[your-railway-url].railway.app
```

To get your Railway URL:
1. Go to Railway dashboard
2. Click your mrwiseowl project
3. Go to Settings → Domains
4. Copy the generated domain

### Step 5: Deploy
1. Click **"Deploy"**
2. Wait 1-2 minutes
3. Vercel will give you a URL: `https://mrwiseowl.vercel.app`

### Step 6: Configure Custom Domain
1. In Vercel project settings, go to **"Domains"**
2. Add domain: `mrwiseowl.wiseowltech.net`
3. Vercel will provide DNS instructions:
   - Add CNAME record in your DNS:
     - Name: `mrwiseowl`
     - Value: `cname.vercel-dns.com`

---

## Option 2: Deploy via CLI

### Step 1: Install Vercel CLI
```bash
npm install -g vercel
```

### Step 2: Login
```bash
vercel login
```

### Step 3: Deploy
```bash
cd frontend
vercel
```

Follow the prompts:
- **Set up and deploy**: Yes
- **Which scope**: Your account
- **Link to existing project**: No
- **Project name**: mrwiseowl
- **Directory**: ./
- **Override settings**: No

### Step 4: Set Environment Variable
```bash
vercel env add VITE_API_URL production
# Enter: https://[your-railway-url].railway.app
```

### Step 5: Redeploy
```bash
vercel --prod
```

---

## Update Backend CORS

Your backend needs to allow requests from Vercel. Update `SecurityConfig.java`:

```java
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .authorizeHttpRequests(auth -> auth
                .anyRequest().permitAll()
            );
        
        return http.build();
    }
    
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of(
            "http://localhost:5173",
            "https://mrwiseowl.vercel.app",
            "https://mrwiseowl.wiseowltech.net"
        ));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
```

---

## Verify Deployment

### Test Frontend
```bash
curl https://mrwiseowl.vercel.app
```

### Test API Connection
Open browser console on your Vercel site and check:
- Network tab for API calls
- Console for any errors

---

## Automatic Deployments

Once connected, Vercel will:
- ✅ Auto-deploy on every push to `main`
- ✅ Create preview deployments for PRs
- ✅ Run builds automatically
- ✅ Invalidate CDN cache

---

## Custom Domain Setup

### In Your DNS Provider (wiseowltech.net)
Add CNAME record:
```
Type: CNAME
Name: mrwiseowl
Value: cname.vercel-dns.com
TTL: 3600
```

### In Vercel Dashboard
1. Go to Project Settings → Domains
2. Add: `mrwiseowl.wiseowltech.net`
3. Vercel will verify DNS
4. SSL certificate auto-generated

Wait 5-10 minutes for DNS propagation.

---

## URLs After Deployment

- **Frontend**: https://mrwiseowl.wiseowltech.net
- **Backend**: https://[your-railway-url].railway.app
- **GitHub**: https://github.com/wiseowltechnet/mrwiseowl

---

## Cost

**Vercel Free Tier**:
- ✅ 100GB bandwidth/month
- ✅ Unlimited deployments
- ✅ Automatic HTTPS
- ✅ Global CDN
- ✅ Custom domains

**Total Cost: Still $0/month** 💰

---

## Monitoring

### Vercel Analytics (Free)
1. Go to Project Settings → Analytics
2. Enable Web Analytics
3. View traffic, performance, and errors

### Check Deployment Status
```bash
vercel ls
```

---

## Troubleshooting

### Build fails on Vercel
- Check build logs in Vercel dashboard
- Verify `package.json` scripts
- Test build locally: `npm run build`

### API calls fail
- Check CORS configuration in backend
- Verify VITE_API_URL is set correctly
- Check Railway backend is running

### Custom domain not working
- Verify DNS records
- Wait for DNS propagation (up to 24 hours)
- Check Vercel domain status

---

**Ready to deploy!** 🚀
