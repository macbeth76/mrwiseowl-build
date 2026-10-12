#!/bin/bash
# Check Koyeb deployment status

API_KEY=$(cat .koyeb/api-key.txt)
SERVICE_ID="explicit-tiffani-wiseowltech-07037a1a"

echo "📊 Checking Koyeb service status..."
echo ""

curl -s "https://app.koyeb.com/v1/services/${SERVICE_ID}" \
  -H "Authorization: Bearer ${API_KEY}" | jq '.service | {name, status, version}'

echo ""
echo "🔗 Backend URL: https://explicit-tiffani-wiseowltech-07037a1a.koyeb.app"
