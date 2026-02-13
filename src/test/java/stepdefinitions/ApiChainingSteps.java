package stepdefinitions;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import io.restassured.module.jsv.JsonSchemaValidator;
import models.PostRequest;

public class ApiChainingSteps extends BaseSteps {

    // IMPORTANT: Ensure there is NO "private Response response;" declaration here anymore!

    @Given("the base API is initialized")
    public void init() {
        // Since we are setting BaseURI in the Client constructor now,
        // this can be used for logging or specific setup.
        System.out.println("Initializing Scenario on Thread: " + Thread.currentThread().getId());
    }

    @When("I create a post with title {string} and body {string}")
    public void createPost(String title, String body) {
        PostRequest payload = new PostRequest(title, body, 1);

        // FIX: Use setResponse() so CommonSteps can see it via ThreadLocal
        setResponse(client.createPost(payload));

        // Extract ID for chaining and store in context
        int generatedId = getResponse().jsonPath().getInt("id");
        scenarioContext.setContext("POST_ID", generatedId);
    }

    @When("I retrieve comments for the newly created post")
    public void getComments() {
        // Retrieve ID from the thread-safe ScenarioContext
        Object postIdObj = scenarioContext.getContext("POST_ID");

        if (postIdObj == null) {
            throw new RuntimeException("POST_ID not found in context! Ensure 'createPost' ran successfully.");
        }

        int postId = (int) postIdObj;
        setResponse(client.getCommentsForPost(postId));
    }

    @Then("the response should match the schema {string}")
    public void verifySchema(String schemaPath) {
        // FIX: Use getResponse()
        getResponse().then().assertThat()
                .body(JsonSchemaValidator.matchesJsonSchemaInClasspath(schemaPath));
    }
}