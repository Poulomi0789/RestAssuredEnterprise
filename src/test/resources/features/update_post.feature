@regression
Feature: Update a post

  Scenario: Update the title and body of post ID 1
    Given I have a post with ID 1
    When I update the post with title "Updated Title" and body "Updated body content"
    Then the update status code should be 200
    And the response field "title" should be "Updated Title"
    And the response field "body" should be "Updated body content"