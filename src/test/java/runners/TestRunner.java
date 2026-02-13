package runners;

import io.cucumber.junit.platform.engine.Constants;
import org.junit.platform.suite.api.*;

@Suite
@IncludeEngines("cucumber")
@SelectClasspathResource("features")
@ConfigurationParameter(key = "cucumber.glue", value = "stepdefinitions, hooks")
@ConfigurationParameter(key = "cucumber.plugin",
        value = "pretty, html:target/cucumber.html, io.qameta.allure.cucumber7jvm.AllureCucumber7Jvm")
@ConfigurationParameter(key = "cucumber.execution.parallel.enabled", value = "true")
public class TestRunner {
}
