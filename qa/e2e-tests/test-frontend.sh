#!/bin/bash

FRONTEND_URL="${FRONTEND_URL:-https://frontend-d2xv0d6gx-wise-owl-techs-projects.vercel.app}"
API_URL="${API_URL:-https://explicit-tiffani-wiseowltech-07037a1a.koyeb.app}"
REPORT_FILE="qa/reports/e2e-test-report-$(date +%Y%m%d-%H%M%S).txt"

echo "🌐 Mr Wise Owl E2E Tests" | tee "$REPORT_FILE"
echo "Frontend: $FRONTEND_URL" | tee -a "$REPORT_FILE"
echo "API: $API_URL" | tee -a "$REPORT_FILE"
echo "Started: $(date)" | tee -a "$REPORT_FILE"
echo "================================" | tee -a "$REPORT_FILE"

PASSED=0
FAILED=0

test_page_load() {
    local name="$1"
    local url="$2"
    local expected_status="${3:-200}"
    
    echo -n "Testing $name... " | tee -a "$REPORT_FILE"
    
    status=$(curl -s -o /dev/null -w "%{http_code}" "$url")
    
    if [ "$status" = "$expected_status" ]; then
        echo "✅ PASS (HTTP $status)" | tee -a "$REPORT_FILE"
        ((PASSED++))
    else
        echo "❌ FAIL (Expected $expected_status, got $status)" | tee -a "$REPORT_FILE"
        ((FAILED++))
    fi
}

test_page_content() {
    local name="$1"
    local url="$2"
    local search_text="$3"
    
    echo -n "Testing $name... " | tee -a "$REPORT_FILE"
    
    content=$(curl -s "$url")
    
    if echo "$content" | grep -q "$search_text"; then
        echo "✅ PASS (Found: $search_text)" | tee -a "$REPORT_FILE"
        ((PASSED++))
    else
        echo "❌ FAIL (Not found: $search_text)" | tee -a "$REPORT_FILE"
        ((FAILED++))
    fi
}

test_js_bundle() {
    local name="$1"
    
    echo -n "Testing $name... " | tee -a "$REPORT_FILE"
    
    # Get the HTML and extract JS bundle path
    html=$(curl -s "$FRONTEND_URL")
    js_file=$(echo "$html" | grep -o 'src="/assets/[^"]*\.js"' | head -1 | sed 's/src="//;s/"//')
    
    if [ -n "$js_file" ]; then
        status=$(curl -s -o /dev/null -w "%{http_code}" "$FRONTEND_URL$js_file")
        if [ "$status" = "200" ]; then
            echo "✅ PASS (Bundle loaded)" | tee -a "$REPORT_FILE"
            ((PASSED++))
        else
            echo "❌ FAIL (Bundle HTTP $status)" | tee -a "$REPORT_FILE"
            ((FAILED++))
        fi
    else
        echo "❌ FAIL (No JS bundle found)" | tee -a "$REPORT_FILE"
        ((FAILED++))
    fi
}

test_css_bundle() {
    local name="$1"
    
    echo -n "Testing $name... " | tee -a "$REPORT_FILE"
    
    # Get the HTML and extract CSS bundle path
    html=$(curl -s "$FRONTEND_URL")
    css_file=$(echo "$html" | grep -o 'href="/assets/[^"]*\.css"' | head -1 | sed 's/href="//;s/"//')
    
    if [ -n "$css_file" ]; then
        status=$(curl -s -o /dev/null -w "%{http_code}" "$FRONTEND_URL$css_file")
        if [ "$status" = "200" ]; then
            echo "✅ PASS (Styles loaded)" | tee -a "$REPORT_FILE"
            ((PASSED++))
        else
            echo "❌ FAIL (CSS HTTP $status)" | tee -a "$REPORT_FILE"
            ((FAILED++))
        fi
    else
        echo "❌ FAIL (No CSS bundle found)" | tee -a "$REPORT_FILE"
        ((FAILED++))
    fi
}

test_api_integration() {
    local name="$1"
    
    echo -n "Testing $name... " | tee -a "$REPORT_FILE"
    
    # Check if API is accessible from frontend's perspective
    api_status=$(curl -s -o /dev/null -w "%{http_code}" "$API_URL/api/health")
    
    if [ "$api_status" = "200" ]; then
        echo "✅ PASS (API reachable)" | tee -a "$REPORT_FILE"
        ((PASSED++))
    else
        echo "❌ FAIL (API HTTP $api_status)" | tee -a "$REPORT_FILE"
        ((FAILED++))
    fi
}

echo "" | tee -a "$REPORT_FILE"
echo "📄 Page Load Tests" | tee -a "$REPORT_FILE"
echo "-------------------" | tee -a "$REPORT_FILE"
test_page_load "Frontend Homepage" "$FRONTEND_URL" 200
test_page_content "Page Title" "$FRONTEND_URL" "frontend"
test_page_content "App Container" "$FRONTEND_URL" "root"

echo "" | tee -a "$REPORT_FILE"
echo "📦 Asset Loading" | tee -a "$REPORT_FILE"
echo "-------------------" | tee -a "$REPORT_FILE"
test_js_bundle "JavaScript Bundle"
test_css_bundle "CSS Bundle"

echo "" | tee -a "$REPORT_FILE"
echo "🔌 API Integration" | tee -a "$REPORT_FILE"
echo "-------------------" | tee -a "$REPORT_FILE"
test_api_integration "API Connectivity"

echo "" | tee -a "$REPORT_FILE"
echo "🗺️  Map & Data Tests" | tee -a "$REPORT_FILE"
echo "-------------------" | tee -a "$REPORT_FILE"
echo "Note: Full map interaction tests require browser automation (Playwright/Selenium)" | tee -a "$REPORT_FILE"
echo "Manual test checklist:" | tee -a "$REPORT_FILE"
echo "  - [ ] Map loads with Asheville centered" | tee -a "$REPORT_FILE"
echo "  - [ ] 3 POI markers visible (Biltmore, Wicked Weed, KOA)" | tee -a "$REPORT_FILE"
echo "  - [ ] Clicking markers shows popup with POI details" | tee -a "$REPORT_FILE"
echo "  - [ ] Map is interactive (zoom, pan)" | tee -a "$REPORT_FILE"

echo "" | tee -a "$REPORT_FILE"
echo "================================" | tee -a "$REPORT_FILE"
echo "Results: $PASSED passed, $FAILED failed" | tee -a "$REPORT_FILE"
echo "Completed: $(date)" | tee -a "$REPORT_FILE"

if [ $FAILED -gt 0 ]; then
    exit 1
fi
