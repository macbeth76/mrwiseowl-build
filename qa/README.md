# Mr Wise Owl QA Testing

## Structure

```
qa/
├── api-tests/          # Backend API integration tests
├── e2e-tests/          # End-to-end browser tests
├── reports/            # Test reports and coverage
├── scripts/            # Test automation scripts
└── README.md
```

## Quick Start

```bash
# Run all tests
./qa/scripts/run-all-tests.sh

# Run API tests only
./qa/scripts/run-api-tests.sh

# Run E2E tests only
./qa/scripts/run-e2e-tests.sh
```

## Test Coverage

- **API Tests**: REST endpoints, database queries, error handling
- **E2E Tests**: User flows, map interactions, data loading
- **Performance Tests**: Load times, API response times
- **Security Tests**: CORS, authentication, input validation

## Tools

- **Backend**: JUnit 5, Mockito, REST Assured
- **Frontend**: Vitest, React Testing Library, Playwright
- **Reporting**: HTML reports, coverage reports
