Feature: Town Search
  As a traveler
  I want to search for towns
  So that I can find destinations to visit

  Scenario: Search for all towns
    When I search for towns with no query
    Then I should see at least 1 town
    And each town should have a name and state

  Scenario: Search for specific town
    When I search for towns with query "Asheville"
    Then I should see at least 1 town
    And the first town name should contain "Asheville"

  Scenario: Get town by ID
    Given a town exists with ID 1
    When I get town by ID 1
    Then I should receive the town details
    And the town should have coordinates
