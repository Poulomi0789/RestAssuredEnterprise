package clients;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import models.PostRequest; // Import your POJO

public class ApiChainingClient {

    public Response createPost(PostRequest payload) {
        return RestAssured.given()
                .contentType(ContentType.JSON)
                .body(payload) // RestAssured converts POJO to JSON here
                .when()
                .post("/posts");
    }

    public Response getCommentsForPost(int postId) {
        return RestAssured.given()
                .baseUri("https://jsonplaceholder.typicode.com")
                .queryParam("postId", postId)
                .when()
                .get("/comments");
    }

    public Response updatePost(int postId, PostRequest payload) {
        return RestAssured.given()
                .contentType(ContentType.JSON)
                .pathParam("id", postId)
                .body(payload)
                .when()
                .put("/posts/{id}");
    }
}