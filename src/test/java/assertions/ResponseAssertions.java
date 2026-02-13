package assertions;

import exceptions.FrameworkException;
import io.restassured.response.Response;
import org.junit.jupiter.api.Assertions;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

public class ResponseAssertions {

    /**
     * Verifies that the status code matches the expected value
     */
    public static void assertStatusCode(Response response, int expectedCode) {
        Assertions.assertEquals(expectedCode, response.getStatusCode(),
                "Status code mismatch!");
    }

    /**
     * Validates the response body against a JSON schema file in resources
     */
    public static void assertSchema(Response response, String schemaPath) {
        try {
            response.then().assertThat().body(matchesJsonSchemaInClasspath(schemaPath));
        } catch (Exception e) {
            throw new FrameworkException("Schema validation failed or Schema file not found at: " + schemaPath, e);
        }
    }

    /**
     * Verifies if a specific field in the JSON response is not null
     */
    public static void assertFieldNotNull(Response response, String jsonPath) {
        Object value = response.jsonPath().get(jsonPath);
        Assertions.assertNotNull(value, "Field " + jsonPath + " should not be null!");
    }

    public static void assertField(Response response, String path, Object expected) {
        if (response == null) {
            throw new RuntimeException("❌ Assertion Failed: The Response object is NULL. " +
                    "Check if 'setResponse()' was called in the @When step.");
        }
        Object actual = response.jsonPath().get(path);
        Assertions.assertEquals(expected, actual, "Field mismatch at: " + path);
    }
}