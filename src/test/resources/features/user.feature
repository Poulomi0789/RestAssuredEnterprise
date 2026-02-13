@sanity
Feature: User API Test

  Scenario: Get user details
    Given I set base URI
    When I send GET request to "/users/2"
    Then response status should be 200
    And response should contain "Janet"
