#!/bin/bash

REPORT_DIR="qa/reports"
HTML_REPORT="$REPORT_DIR/test-report-$(date +%Y%m%d-%H%M%S).html"

# Get latest test reports
LATEST_API=$(ls -t $REPORT_DIR/api-test-report-*.txt 2>/dev/null | head -1)
LATEST_E2E=$(ls -t $REPORT_DIR/e2e-test-report-*.txt 2>/dev/null | head -1)

# Extract test results
API_PASSED=$(grep "Results:" "$LATEST_API" | grep -o '[0-9]* passed' | grep -o '[0-9]*')
API_FAILED=$(grep "Results:" "$LATEST_API" | grep -o '[0-9]* failed' | grep -o '[0-9]*')
E2E_PASSED=$(grep "Results:" "$LATEST_E2E" | grep -o '[0-9]* passed' | grep -o '[0-9]*')
E2E_FAILED=$(grep "Results:" "$LATEST_E2E" | grep -o '[0-9]* failed' | grep -o '[0-9]*')

TOTAL_PASSED=$((API_PASSED + E2E_PASSED))
TOTAL_FAILED=$((API_FAILED + E2E_FAILED))
TOTAL_TESTS=$((TOTAL_PASSED + TOTAL_FAILED))

# Generate HTML
cat > "$HTML_REPORT" << EOF
<!DOCTYPE html>
<html>
<head>
    <title>Mr Wise Owl Test Report</title>
    <style>
        body { font-family: Arial, sans-serif; margin: 20px; background: #f5f5f5; }
        .container { max-width: 1200px; margin: 0 auto; background: white; padding: 20px; border-radius: 8px; }
        h1 { color: #2c3e50; border-bottom: 3px solid #3498db; padding-bottom: 10px; }
        .summary { display: flex; gap: 20px; margin: 20px 0; }
        .card { flex: 1; padding: 20px; border-radius: 8px; text-align: center; }
        .passed { background: #d4edda; color: #155724; }
        .failed { background: #f8d7da; color: #721c24; }
        .total { background: #d1ecf1; color: #0c5460; }
        .card h2 { margin: 0; font-size: 48px; }
        .card p { margin: 5px 0 0 0; font-size: 14px; }
        .section { margin: 30px 0; }
        .section h3 { color: #34495e; border-left: 4px solid #3498db; padding-left: 10px; }
        pre { background: #f8f9fa; padding: 15px; border-radius: 4px; overflow-x: auto; }
        .pass { color: #28a745; }
        .fail { color: #dc3545; }
        .timestamp { color: #6c757d; font-size: 14px; }
    </style>
</head>
<body>
    <div class="container">
        <h1>🦉 Mr Wise Owl Test Report</h1>
        <p class="timestamp">Generated: $(date)</p>
        
        <div class="summary">
            <div class="card total">
                <h2>$TOTAL_TESTS</h2>
                <p>Total Tests</p>
            </div>
            <div class="card passed">
                <h2>$TOTAL_PASSED</h2>
                <p>Passed</p>
            </div>
            <div class="card failed">
                <h2>$TOTAL_FAILED</h2>
                <p>Failed</p>
            </div>
        </div>

        <div class="section">
            <h3>📡 API Tests ($API_PASSED/$((API_PASSED + API_FAILED)))</h3>
            <pre>$(cat "$LATEST_API" | sed 's/✅/<span class="pass">✅<\/span>/g' | sed 's/❌/<span class="fail">❌<\/span>/g')</pre>
        </div>

        <div class="section">
            <h3>🌐 E2E Tests ($E2E_PASSED/$((E2E_PASSED + E2E_FAILED)))</h3>
            <pre>$(cat "$LATEST_E2E" | sed 's/✅/<span class="pass">✅<\/span>/g' | sed 's/❌/<span class="fail">❌<\/span>/g')</pre>
        </div>
    </div>
</body>
</html>
EOF

echo "📊 HTML report generated: $HTML_REPORT"
echo "Open with: open $HTML_REPORT"
