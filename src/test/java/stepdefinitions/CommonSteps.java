package stepdefinitions;

import assertions.ResponseAssertions;
import io.cucumber.java.en.Then;
import io.restassured.response.Response;

public class CommonSteps extends BaseSteps {

    @Then("the status code should be {int}")
    public void verifyStatusCode(int expectedCode) {
        // Fetch the response specific to THIS thread
        Response currentResponse = getResponse();

        if (currentResponse == null) {
            throw new RuntimeException("The response is null! Ensure setResponse() was called in the previous step.");
        }
        ResponseAssertions.assertStatusCode(currentResponse, expectedCode);
    }
}