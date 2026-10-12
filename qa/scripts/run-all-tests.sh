#!/bin/bash

echo "🦉 Mr Wise Owl - Running All Tests"
echo "===================================="

# Run API tests
echo ""
echo "📡 Running API Tests..."
./qa/api-tests/test-api.sh
API_EXIT=$?

# Run E2E tests
echo ""
echo "🌐 Running E2E Tests..."
./qa/e2e-tests/test-frontend.sh
E2E_EXIT=$?

# Summary
echo ""
echo "===================================="
echo "📊 Test Summary"
echo "===================================="
echo "API Tests: $([ $API_EXIT -eq 0 ] && echo '✅ PASSED' || echo '❌ FAILED')"
echo "E2E Tests: $([ $E2E_EXIT -eq 0 ] && echo '✅ PASSED' || echo '❌ FAILED')"

# Exit with failure if any tests failed
if [ $API_EXIT -ne 0 ] || [ $E2E_EXIT -ne 0 ]; then
    echo ""
    echo "❌ Some tests failed"
    exit 1
fi

echo ""
echo "✅ All tests passed!"

# Generate HTML report
echo ""
echo "📊 Generating HTML report..."
./qa/scripts/generate-report.sh

exit 0
