Feature: POI Search
  As a traveler
  I want to search for points of interest
  So that I can plan activities in a town

  Scenario: Get POIs for a town
    Given a town exists with ID 1
    When I get POIs for town ID 1
    Then I should see at least 1 POI
    And each POI should have a name and category

  Scenario: Filter POIs by category
    Given a town exists with ID 1
    When I get POIs for town ID 1 and category "hotel"
    Then all POIs should have category "hotel"

  Scenario: Get POI by ID
    Given a POI exists with ID 1
    When I get POI by ID 1
    Then I should receive the POI details
    And the POI should have coordinates
