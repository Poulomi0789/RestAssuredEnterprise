package hooks;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import io.restassured.RestAssured;
import io.qameta.allure.Allure;
import utils.ConfigManager;
import utils.RetryFilter;

public class Hooks {

    @Before
    public void setup(Scenario scenario) {
        // 1. Initialize Base URI once per scenario
        RestAssured.baseURI = ConfigManager.getProperty("base.url");

        System.out.println("----------------------------------------------");
        System.out.println("Starting Scenario: " + scenario.getName());
        System.out.println("----------------------------------------------");
        // Add the retry filter to the global RestAssured configuration
        RestAssured.filters(new RetryFilter());
    }

    @After
    public void tearDown(Scenario scenario) {
        // 2. Capture evidence if the scenario fails
        if (scenario.isFailed()) {
            // This is a great place to attach logs or environment details to Allure
            Allure.addAttachment("Failed Scenario Status", "Status: " + scenario.getStatus());
            System.err.println("SCENARIO FAILED: " + scenario.getName());
        }

        // 3. Reset RestAssured for the next scenario to prevent 'state leakage'
        RestAssured.reset();
    }
}