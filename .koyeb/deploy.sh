#!/bin/bash
# Koyeb Deployment Script

API_KEY=$(cat .koyeb/api-key.txt)
SERVICE_ID="explicit-tiffani-wiseowltech-07037a1a"

echo "🚀 Triggering Koyeb deployment..."

curl -X POST "https://app.koyeb.com/v1/services/${SERVICE_ID}/redeploy" \
  -H "Authorization: Bearer ${API_KEY}" \
  -H "Content-Type: application/json"

echo ""
echo "✅ Deployment triggered!"
echo "📊 Check status: https://app.koyeb.com/services/${SERVICE_ID}"
