package stepdefinitions;

import io.cucumber.java.en.*;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import static org.hamcrest.Matchers.*;

public class UserSteps {
    Response response;

    @Given("I set base URI")
    public void i_set_base_uri() {
        RestAssured.baseURI = "https://reqres.in/api";
    }

    @When("I send GET request to {string}")
    public void i_send_get_request_to(String path) {
        response = RestAssured.get(path);
    }

    @Then("response status should be {int}")
    public void response_status_should_be(Integer statusCode) {
        response.then().statusCode(statusCode);
    }

    @Then("response should contain {string}")
    public void response_should_contain(String name) {
        response.then().body("data.first_name", equalTo(name));
    }
}