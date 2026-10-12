#!/bin/bash

API_URL="${API_URL:-https://explicit-tiffani-wiseowltech-07037a1a.koyeb.app}"
REPORT_FILE="qa/reports/api-test-report-$(date +%Y%m%d-%H%M%S).txt"

echo "🧪 Mr Wise Owl API Tests" | tee "$REPORT_FILE"
echo "API URL: $API_URL" | tee -a "$REPORT_FILE"
echo "Started: $(date)" | tee -a "$REPORT_FILE"
echo "================================" | tee -a "$REPORT_FILE"

PASSED=0
FAILED=0

test_endpoint() {
    local name="$1"
    local endpoint="$2"
    local expected_status="${3:-200}"
    
    echo -n "Testing $name... " | tee -a "$REPORT_FILE"
    
    status=$(curl -s -o /dev/null -w "%{http_code}" "$API_URL$endpoint")
    
    if [ "$status" = "$expected_status" ]; then
        echo "✅ PASS (HTTP $status)" | tee -a "$REPORT_FILE"
        ((PASSED++))
    else
        echo "❌ FAIL (Expected $expected_status, got $status)" | tee -a "$REPORT_FILE"
        ((FAILED++))
    fi
}

test_json_response() {
    local name="$1"
    local endpoint="$2"
    local jq_filter="$3"
    
    echo -n "Testing $name... " | tee -a "$REPORT_FILE"
    
    response=$(curl -s "$API_URL$endpoint")
    result=$(echo "$response" | jq -r "$jq_filter" 2>/dev/null)
    
    if [ -n "$result" ] && [ "$result" != "null" ]; then
        echo "✅ PASS ($result)" | tee -a "$REPORT_FILE"
        ((PASSED++))
    else
        echo "❌ FAIL (Invalid response)" | tee -a "$REPORT_FILE"
        echo "Response: $response" >> "$REPORT_FILE"
        ((FAILED++))
    fi
}

test_cors() {
    local name="$1"
    local origin="$2"
    
    echo -n "Testing $name... " | tee -a "$REPORT_FILE"
    
    cors_header=$(curl -s -I -X OPTIONS \
        -H "Origin: $origin" \
        -H "Access-Control-Request-Method: GET" \
        "$API_URL/api/health" | grep -i "access-control-allow-origin")
    
    if [ -n "$cors_header" ]; then
        echo "✅ PASS" | tee -a "$REPORT_FILE"
        ((PASSED++))
    else
        echo "❌ FAIL (No CORS header)" | tee -a "$REPORT_FILE"
        ((FAILED++))
    fi
}

echo "" | tee -a "$REPORT_FILE"
echo "🏥 Health Checks" | tee -a "$REPORT_FILE"
echo "----------------" | tee -a "$REPORT_FILE"
test_endpoint "Health Check" "/api/health" 200
test_json_response "Health Status" "/api/health" '.status'
test_json_response "Service Name" "/api/health" '.service'

echo "" | tee -a "$REPORT_FILE"
echo "🏙️  Town Endpoints" | tee -a "$REPORT_FILE"
echo "----------------" | tee -a "$REPORT_FILE"
test_endpoint "Get Town by ID" "/api/towns/1" 200
test_json_response "Town Name" "/api/towns/1" '.name'
test_json_response "Town Latitude" "/api/towns/1" '.latitude'
test_json_response "Town Longitude" "/api/towns/1" '.longitude'
test_json_response "Town State" "/api/towns/1" '.state'
test_endpoint "Search Towns (empty)" "/api/towns/search" 200
test_endpoint "Search Towns (query)" "/api/towns/search?q=Asheville" 200

echo "" | tee -a "$REPORT_FILE"
echo "📍 POI Endpoints" | tee -a "$REPORT_FILE"
echo "----------------" | tee -a "$REPORT_FILE"
test_endpoint "Get POIs by Town" "/api/pois?townId=1" 200
test_json_response "POI Count" "/api/pois?townId=1" 'length'
test_json_response "First POI Name" "/api/pois?townId=1" '.[0].name'
test_json_response "First POI Category" "/api/pois?townId=1" '.[0].category'
test_endpoint "Get POIs by Category" "/api/pois?category=historic" 200

echo "" | tee -a "$REPORT_FILE"
echo "❌ Error Handling" | tee -a "$REPORT_FILE"
echo "----------------" | tee -a "$REPORT_FILE"
test_endpoint "Invalid Town ID" "/api/towns/99999" 404
test_endpoint "Invalid POI Query" "/api/pois?townId=99999" 200

echo "" | tee -a "$REPORT_FILE"
echo "🔒 CORS Tests" | tee -a "$REPORT_FILE"
echo "----------------" | tee -a "$REPORT_FILE"
test_cors "CORS for Vercel" "https://frontend-test.vercel.app"
test_cors "CORS for localhost" "http://localhost:5173"

echo "" | tee -a "$REPORT_FILE"
echo "================================" | tee -a "$REPORT_FILE"
echo "Results: $PASSED passed, $FAILED failed" | tee -a "$REPORT_FILE"
echo "Completed: $(date)" | tee -a "$REPORT_FILE"

if [ $FAILED -gt 0 ]; then
    exit 1
fi
