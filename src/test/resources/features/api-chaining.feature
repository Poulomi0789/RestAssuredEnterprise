@smoke
Feature: API Chaining Demo

  Background:
    Given the base API is initialized
    # This step is not required, base API added to hook. Kept this step for learning purpose

  Scenario: Create a post and then add a comment to that specific post
    # 1. Create the Post (POST)
    When I create a post with title "RestAssured Chaining" and body "Testing is easy"
    Then the status code should be 201
    And the response should match the schema "schemas/post-schema.json"

    # 2. Get comments for the new post (GET)
    When I retrieve comments for the newly created post
    Then the status code should be 200