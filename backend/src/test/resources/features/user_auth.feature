Feature: User Authentication
  As a user
  I want to register and login
  So that I can save my itineraries

  Scenario: Register new user
    When I register with email "test@example.com" and password "password123"
    Then I should receive a JWT token
    And the token should be valid

  Scenario: Login existing user
    Given a user exists with email "existing@example.com" and password "password123"
    When I login with email "existing@example.com" and password "password123"
    Then I should receive a JWT token
    And the token should be valid

  Scenario: Login with wrong password
    Given a user exists with email "user@example.com" and password "correct123"
    When I login with email "user@example.com" and password "wrong123"
    Then I should receive an authentication error
