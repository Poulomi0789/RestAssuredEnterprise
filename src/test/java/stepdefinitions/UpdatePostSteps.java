package stepdefinitions;

import assertions.ResponseAssertions;
import clients.ApiChainingClient;
import io.cucumber.java.en.*;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import models.PostRequest;
import utils.ScenarioContext;

import static org.hamcrest.Matchers.*;

public class UpdatePostSteps extends BaseSteps{

    //    private int newPostId;
    private ApiChainingClient client = new ApiChainingClient();
    // 1. Initialize the context
    private ScenarioContext scenarioContext = new ScenarioContext();

    @Given("I have a post with ID {int}")
    public void setPostId(int id) {
        scenarioContext.setContext("UPDATE_POST_ID", id);
    }

    @When("I update the post with title {string} and body {string}")
    public void updatePost(String title, String body) {
        int id = (int) scenarioContext.getContext("UPDATE_POST_ID");
        PostRequest updatePayload = new PostRequest(title, body, 1);
        setResponse(client.updatePost(id, updatePayload));
    }

    @Then("the response field {string} should be {string}")
    public void verifyStringField(String fieldPath, String expectedValue) {
        // Pull the response from context before asserting
        Response res = (Response) scenarioContext.getContext("LATEST_RESPONSE");
        ResponseAssertions.assertField(getResponse(), fieldPath, expectedValue);
    }

    @Then("the response field {string} should be the integer {int}")
    public void verifyIntField(String fieldPath, int expectedValue) {
        Response currentResponse = getResponse();
        ResponseAssertions.assertField(currentResponse, fieldPath, expectedValue);
    }
    @Then("the update status code should be {int}")
    public void verifyUpdateStatus(int code) {
        Response currentResponse = getResponse();
        ResponseAssertions.assertStatusCode(currentResponse, code);
    }
}