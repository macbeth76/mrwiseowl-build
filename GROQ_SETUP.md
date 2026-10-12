# Groq AI Setup

## Get Free API Key

1. Go to https://console.groq.com/
2. Sign up for free account
3. Navigate to API Keys section
4. Create new API key
5. Copy the key

## Free Tier Limits
- 14,400 requests per day
- 30 requests per minute
- Perfect for MVP!

## Add to Koyeb

1. Go to Koyeb dashboard
2. Select your mrwiseowl app
3. Go to Settings → Environment Variables
4. Add: `GROQ_API_KEY` = `your_key_here`
5. Redeploy

## Test Chat

Once deployed, the chat will use Llama 3.1 to:
- Answer travel questions
- Recommend places based on preferences
- Provide information about POIs
- Help plan itineraries

## Model Used
- **llama-3.1-8b-instant** - Fast, efficient, perfect for chat
