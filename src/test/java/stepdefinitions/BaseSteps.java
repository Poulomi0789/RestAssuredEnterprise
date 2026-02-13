package stepdefinitions;

import clients.ApiChainingClient;
import io.restassured.response.Response;
import utils.ScenarioContext;

public class BaseSteps {

    // ThreadLocal ensures each thread (Scenario) has its own isolated copy
    private static ThreadLocal<Response> responseThreadLocal = new ThreadLocal<>();
    protected static ScenarioContext scenarioContext = new ScenarioContext();
    protected ApiChainingClient client = new ApiChainingClient();

    // Helper methods to get/set the response safely
    protected void setResponse(Response res) {
        responseThreadLocal.set(res);
    }

    protected Response getResponse() {
        return responseThreadLocal.get();
    }
}