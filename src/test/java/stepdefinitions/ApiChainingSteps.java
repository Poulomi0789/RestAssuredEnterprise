package stepdefinitions;

import io.cucumber.java.en.*;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.module.jsv.JsonSchemaValidator;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class ApiChainingSteps {
    private Response response;
    private int newPostId;

    @Given("the base API is initialized")
    public void init() {
        // In a real project, pull this from a config file
        RestAssured.baseURI = "https://jsonplaceholder.typicode.com";
    }

    @When("I create a post with title {string} and body {string}")
    public void createPost(String title, String body) {
        Map<String, Object> payload = new HashMap<>();
        payload.put("title", title);
        payload.put("body", body);
        payload.put("userId", 1);

        response = given()
                .contentType(ContentType.JSON)
                .body(payload)
                .when()
                .post("/posts");

        // Extract ID for chaining
        if(response.statusCode() == 201) {
            newPostId = response.jsonPath().getInt("id");
            System.out.println("Created Post ID: " + newPostId);
        }
    }

    @Then("the status code should be {int}")
    public void verifyStatus(int code) {
        response.then().statusCode(code);
    }

    @Then("the response should match the schema {string}")
    public void verifySchema(String schemaPath) {
        // Make sure the file exists in src/test/resources/schemas/post-schema.json
        response.then().assertThat()
                .body(JsonSchemaValidator.matchesJsonSchemaInClasspath(schemaPath));
    }

    @When("I retrieve comments for the newly created post")
    public void getComments() {
        response = given()
                .queryParam("postId", newPostId)
                .when()
                .get("/comments");

        System.out.println("Chained comments response: " + response.asString());
    }
}